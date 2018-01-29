/*******************************************************************************
 * Copyright (c) 2017 Christian W. Damus and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Christian W. Damus - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.facade;

import static org.eclipse.emf.facade.FacadeAdapter.getUnderlyingObject;

import com.google.common.base.Predicates;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;
import com.google.common.reflect.TypeToken;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.facade.util.TreeIterators;

/**
 * A factory of dynamic proxies implementing the {@link FacadeObject} protocol to adapt objects of EMF models
 * that do not natively implement this protocol.
 *
 * @author Christian W. Damus
 */
public final class FacadeProxy {
	/** Predicate matching classes that are not EObject. */
	private static final Predicate<Class<?>> NOT_EOBJECT_CLASS;

	/** Cache of interfaces implemented by EClasses. */
	private static final LoadingCache<Class<?>, Class<?>[]> INTERFACE_CACHE = CacheBuilder.newBuilder()
			.weakKeys().build(new CacheLoader<Class<?>, Class<?>[]>() {
				/**
				 * {@inheritDoc}
				 */
				@Override
				public Class<?>[] load(Class<?> key) throws Exception {
					return computeProxiedInterfaces(key);
				}
			});

	static {
		Predicate<Class<?>> isEObjectClass = Predicates.equalTo(EObject.class);
		NOT_EOBJECT_CLASS = isEObjectClass.negate();
	}

	/**
	 * Not instantiable by clients.
	 */
	private FacadeProxy() {
		super();
	}

	/**
	 * Creates or obtains the dynamic façade proxy for an {@code object} that does not natively implement the
	 * {@link FacadeObject} protocol. The resulting proxy implements all of the other interfaces of the
	 * original object in addition to {@code FacadeObject}.
	 * 
	 * @param object
	 *            an EMF object, or {@code null}
	 * @return its dynamic {@link FacadeObject} proxy, which may be the {@code object} if it already
	 *         implements the {@link FacadeObject} protocol or even {@code null} if the {@code object} is
	 *         {@code null}
	 */
	public static FacadeObject createProxy(EObject object) {
		FacadeObject result;

		if (object == null) {
			result = null;
		} else if (object instanceof FacadeObject) {
			result = (FacadeObject)object;
		} else {
			Impl impl = (Impl)EcoreUtil.getExistingAdapter(object, Impl.class);

			if (impl != null) {
				result = impl.getProxy();
			} else {
				impl = new Impl(object);
				object.eAdapters().add(impl); // There can be only one

				Class<?>[] interfaces = getProxiedInterfaces(object.getClass());

				result = (FacadeObject)Proxy.newProxyInstance(object.getClass().getClassLoader(), interfaces,
						impl);
				impl.setProxy(result);
			}
		}

		return result;
	}

	/**
	 * Gets a façade's actual own resource if it's in a resource, otherwise its underlying object's resource.
	 * 
	 * @param facade
	 *            a façade object
	 * @return the best resource available for it
	 */
	static Resource eResource(EObject facade) {
		Resource result = facade.eResource();

		if (result == null) {
			EObject underlying = getUnderlyingObject(facade);
			if (underlying != null) {
				result = underlying.eResource();
			}
		}

		return result;
	}

	/**
	 * Gets a façade's actual direct resource if it's contained in a resource or its own, otherwise its
	 * underlying object's direct resource.
	 * 
	 * @param facade
	 *            a façade object
	 * @return the best resource available for it
	 */
	static Resource.Internal eDirectResource(InternalEObject facade) {
		Resource.Internal result = facade.eDirectResource();

		// Be careful not to return a fake direct resource if the façade is actually
		// indirectly contained in a resource in the proper way
		if ((result == null) && (facade.eResource() == null)) {
			EObject underlying = getUnderlyingObject(facade);
			if (underlying instanceof InternalEObject) {
				result = ((InternalEObject)underlying).eDirectResource();
			}
		}

		return result;
	}

	/**
	 * Obtains all of the interfaces implemented by an Ecore class except for {@link EObject}.
	 * 
	 * @param eClass
	 *            an Ecore class
	 * @return its non-{@link EObject} interfaces
	 */
	private static Class<?>[] getProxiedInterfaces(Class<?> eClass) {
		// Our cache loader cannot fail
		return INTERFACE_CACHE.getUnchecked(eClass);
	}

	/**
	 * Obtains all of the interfaces implemented by a Java class except for {@link EObject}.
	 * 
	 * @param clazz
	 *            a Java class
	 * @return its non-{@link EObject} interfaces
	 */
	private static Class<?>[] computeProxiedInterfaces(Class<?> clazz) {
		Set<Class<?>> result = Sets.newHashSet();

		collectAllInterfacesExceptEObject(clazz, result);

		// This one is instead of EObject
		result.add(FacadeObject.class);

		return result.toArray(new Class<?>[result.size()]);
	}

	/**
	 * Recursively collects all of the interfaces implemented by a Java class except for {@link EObject}.
	 * 
	 * @param clazz
	 *            a Java class
	 * @param result
	 *            accumulator of its non-{@link EObject} interfaces
	 */
	private static void collectAllInterfacesExceptEObject(Class<?> clazz, Set<Class<?>> result) {
		if (clazz.isInterface()) {
			if (NOT_EOBJECT_CLASS.test(clazz)) {
				result.add(clazz);
			}
			return;
		}

		// It's not an interface but a class
		Stream.of(clazz.getInterfaces()).filter(NOT_EOBJECT_CLASS).forEach(result::add);

		// Look up the hierarchy for inherited interfaces
		Class<?> superclass = clazz.getSuperclass();
		if (superclass != null) {
			collectAllInterfacesExceptEObject(superclass, result);
		}
	}

	/**
	 * Unwraps a dynamic façade proxy to get at the core object.
	 * 
	 * @param possibleProxy
	 *            a possible façade proxy
	 * @return the unwrapped proxy, or itself if not a proxy
	 * @param <T>
	 *            the model interface to unwrap
	 */
	@SuppressWarnings("unchecked")
	static <T extends EObject> T unwrap(T possibleProxy) {
		T result = possibleProxy;

		if ((result instanceof FacadeObject) && (result instanceof Proxy)) {
			// Unwrap it
			result = (T)((Impl)Proxy.getInvocationHandler(result)).object;
		}

		return result;
	}

	/**
	 * Obtains the dynamic proxy, if any, that wraps a given {@code façade}.
	 * 
	 * @param facade
	 *            a façade implementation
	 * @return the dynamic proxy, or itself if none
	 * @param <T>
	 *            the model interface to wrap
	 */
	@SuppressWarnings("unchecked")
	static <T extends EObject> T wrap(T facade) {
		T result = facade;

		if (!(result instanceof FacadeObject)) {
			// Wrap it
			Impl impl = (Impl)EcoreUtil.getExistingAdapter(result, Impl.class);
			if (impl != null) {
				result = (T)impl.facadeProxy;
			}
		}

		return result;
	}

	/**
	 * Unwraps a collection of dynamic façade proxy to get at the core objects.
	 * 
	 * @param possibleProxies
	 *            possible façade proxies
	 * @return the unwrapped proxies, or the originals if not proxies
	 * @param <T>
	 *            the model interface to unwrap
	 */
	static <T extends EObject> Collection<T> unwrap(Collection<T> possibleProxies) {
		return Collections2.transform(possibleProxies, FacadeProxy::unwrap);
	}

	//
	// Nested types
	//

	/**
	 * Invocation handler for the dynamic façade proxy that is attached to the façade object as an adapter to
	 * ensure at most one proxy exists.
	 *
	 * @author Christian W. Damus
	 */
	static final class Impl extends AdapterImpl implements InvocationHandler {
		/** Generic type parameter of the {@link List} class that represents the element type. */
		@SuppressWarnings("rawtypes")
		private static final TypeVariable<Class<List>> LIST_ELEMENT_TYPE = List.class.getTypeParameters()[0];

		/** The façade object that I adapt to the {@link FacadeObject} protocol. */
		private final EObject object;

		/** The dynamic implementation of the {@link FacadeObject} protocol. */
		private FacadeObject facadeProxy;

		/**
		 * Initializes me with the object that I adapt to the {@link FacadeObject} protocol.
		 * 
		 * @param object
		 *            the object that I proxy
		 */
		Impl(EObject object) {
			super();

			this.object = object;
		}

		/**
		 * Obtain the façade that is the proxy that I handle.
		 * 
		 * @return the façade proxy
		 */
		FacadeObject getProxy() {
			return facadeProxy;
		}

		/**
		 * Set the façade that is the proxy that I handle.
		 * 
		 * @param proxy
		 *            the façade proxy
		 */
		void setProxy(FacadeObject proxy) {
			this.facadeProxy = proxy;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isAdapterForType(Object type) {
			return type == Impl.class;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Exception {
			Object result;

			if (method.getDeclaringClass() == Object.class) {
				result = invokeObject(proxy, method, args);
			} else if (method.getDeclaringClass() == FacadeObject.class) {
				result = invokeFacadeObject(proxy, method, args);
			} else if (method.getDeclaringClass() == InternalEObject.class) {
				result = invokeInternalEObject(proxy, method, args);
			} else if (method.getDeclaringClass() == EObject.class) {
				result = invokeEObject(proxy, method, args);
			} else if (method.getDeclaringClass() == Notifier.class) {
				result = invokeNotifier(proxy, method, args);
			} else {
				// Delegate the rest

				Object[] actualArgs = args;
				if (hasEReferenceTypeParameter(method)) {
					// Unwrap arguments for internal consistency
					actualArgs = unwrapEObjects(args);
				}

				result = method.invoke(object, actualArgs);

				if (isEReferenceType(method)) {
					result = wrapEObjects(result);
				}

				return result;
			}

			return result;
		}

		/**
		 * Invokes an {@link Object} method.
		 * 
		 * @param proxy
		 *            the proxy that is delegating the {@code method}
		 * @param method
		 *            the method to invoke
		 * @param args
		 *            the arguments to the {@code method}
		 * @return the {@code method}'s result, or {@code null} if none
		 */
		private Object invokeObject(Object proxy, Method method, Object[] args) {
			Object result;

			switch (method.getName()) {
				case "equals": //$NON-NLS-1$
					result = Boolean.valueOf(proxy == args[0]);
					break;
				case "hashCode": //$NON-NLS-1$
					result = Integer.valueOf(System.identityHashCode(proxy));
					break;
				case "toString": //$NON-NLS-1$
					String toString = object.toString();
					result = toString.replaceFirst("@[0-9a-f]+", "$0\\$Proxy"); //$NON-NLS-1$//$NON-NLS-2$
					break;
				default:
					// Other Object methods are not proxied
					throw new InternalError("Method unexpectedly proxied: " + method); //$NON-NLS-1$
			}

			return result;
		}

		/**
		 * Invokes a {@link FacadeObject} method.
		 * 
		 * @param proxy
		 *            the proxy that is delegating the {@code method}
		 * @param method
		 *            the method to invoke
		 * @param args
		 *            the arguments to the {@code method}
		 * @return the {@code method}'s result, or {@code null} if none
		 */
		private Object invokeFacadeObject(Object proxy, Method method, Object[] args) {
			Object result;

			switch (method.getName()) {
				case "getUnderlyingElement": //$NON-NLS-1$
					result = getUnderlyingObject(object);
					break;
				case "getFacadeAdapter": //$NON-NLS-1$
					result = FacadeAdapter.get(object, FacadeAdapter.class);
					break;
				case "eResource": //$NON-NLS-1$
					result = eResource(object);
					break;
				default:
					throw new NoSuchMethodError(method.toString());
			}

			return result;
		}

		/**
		 * Invokes an {@link EObject} method.
		 * 
		 * @param proxy
		 *            the proxy that is delegating the {@code method}
		 * @param method
		 *            the method to invoke
		 * @param args
		 *            the arguments to the {@code method}
		 * @return the {@code method}'s result, or {@code null} if none
		 * @throws InvocationTargetException
		 *             on failure of default delegation
		 * @throws IllegalArgumentException
		 *             on problems with arguments
		 * @throws IllegalAccessException
		 *             on failure to access the real method implementation
		 */
		private Object invokeEObject(Object proxy, Method method, Object[] args)
				throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			Object result;

			switch (method.getName()) {
				case "eGet": //$NON-NLS-1$
					result = method.invoke(object, args);
					if (isReflectiveAPIReferenceArgument(args[0])) {
						result = wrapEObjects(result);
					}
					break;
				case "eSet": //$NON-NLS-1$
					Object[] actualArgs = args;
					if (isReflectiveAPIReferenceArgument(args[0])) {
						actualArgs = unwrapEObjects(args);
					}
					result = method.invoke(object, actualArgs);
					break;
				case "eResource": //$NON-NLS-1$
					result = eResource(object);
					break;
				case "eContainer": //$NON-NLS-1$
					result = wrapEObjects(object.eContainer());
					break;
				case "eContents": //$NON-NLS-1$
					// Needs the feature-iterator protocol?
					result = wrapEObjects(object.eContents());
					break;
				case "eAllContents": //$NON-NLS-1$
					result = TreeIterators.transform(object.eAllContents(), this::wrapEObjects);
					break;
				default:
					result = method.invoke(object, args);
					break;
			}

			return result;
		}

		/**
		 * Invokes an {@link InternalEObject} method.
		 * 
		 * @param proxy
		 *            the proxy that is delegating the {@code method}
		 * @param method
		 *            the method to invoke
		 * @param args
		 *            the arguments to the {@code method}
		 * @return the {@code method}'s result, or {@code null} if none
		 * @throws InvocationTargetException
		 *             on failure of default delegation
		 * @throws IllegalArgumentException
		 *             on problems with arguments
		 * @throws IllegalAccessException
		 *             on failure to access the real method implementation
		 */
		private Object invokeInternalEObject(Object proxy, Method method, Object[] args)
				throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			Object result;

			switch (method.getName()) {
				case "eGet": //$NON-NLS-1$
					result = method.invoke(object, args);
					if (isReflectiveAPIReferenceArgument(args[0])) {
						result = wrapEObjects(result);
					}
					break;
				case "eSet": //$NON-NLS-1$
					Object[] actualArgs = args;
					if (isReflectiveAPIReferenceArgument(args[0])) {
						actualArgs = unwrapEObjects(args);
					}
					result = method.invoke(object, actualArgs);
					break;
				case "eInternalContainer": //$NON-NLS-1$
					result = wrapEObjects(((InternalEObject)object).eInternalContainer());
					break;
				case "eDirectResource": //$NON-NLS-1$
					result = eDirectResource((InternalEObject)object);
					break;
				case "eSetting": //$NON-NLS-1$
					result = method.invoke(object, args);
					if (result instanceof EList<?>) {
						// It's a feature setting, so of course it's an EcoreEList
						result = new ProxyEcoreEList<>(facadeProxy, (EcoreEList<?>)result);
					} else {
						// It's a single-valued setting
						result = new ProxySetting(facadeProxy, (EStructuralFeature.Setting)result);
					}
					break;
				default:
					result = method.invoke(object, args);
					break;
			}

			return result;
		}

		/**
		 * Invokes a {@link Notifier} method.
		 * 
		 * @param proxy
		 *            the proxy that is delegating the {@code method}
		 * @param method
		 *            the method to invoke
		 * @param args
		 *            the arguments to the {@code method}
		 * @return the {@code method}'s result, or {@code null} if none
		 * @throws InvocationTargetException
		 *             on failure of default delegation
		 * @throws IllegalArgumentException
		 *             on problems with arguments
		 * @throws IllegalAccessException
		 *             on failure to access the real method implementation
		 */
		private Object invokeNotifier(Object proxy, Method method, Object[] args)
				throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			Object result;

			switch (method.getName()) {
				case "eAdapters": //$NON-NLS-1$
					result = new ProxyAdapterList(this, object.eAdapters());
					break;
				default:
					result = method.invoke(object, args);
					break;
			}

			return result;
		}

		/**
		 * Queries whether an argument passed to a refective {@code eGet} or {@code eSet} API call indicates
		 * an {@link EReference}, possibly by {@link Integer} ID.
		 * 
		 * @param arg
		 *            the first argument of a reflective feature access/mutator call
		 * @return {@code true} if it indicates a reference; {@code false} otherwise
		 */
		protected boolean isReflectiveAPIReferenceArgument(Object arg) {
			Object feature = arg;

			if (arg instanceof Integer) {
				feature = object.eClass().getEStructuralFeature(((Integer)arg).intValue());
			}

			return feature instanceof EReference;
		}

		/**
		 * Queries whether the return result of a {@code method} is of an {@link EReference} type.
		 * 
		 * @param method
		 *            a method
		 * @return whether its result is some kind of {@link EObject} type or a {@link List} of some kind of
		 *         {@link EObject} type
		 */
		protected boolean isEReferenceType(Method method) {
			TypeToken<?> type = TypeToken.of(method.getGenericReturnType());

			boolean result = type.isSubtypeOf(EObject.class);
			if (!result && type.isSubtypeOf(List.class)) {
				result = type.resolveType(LIST_ELEMENT_TYPE).isSubtypeOf(EObject.class);
			}

			return result;
		}

		/**
		 * Wraps an {@link EObject} or {@link List} of {@link EObject}s as façade proxies, if necessary.
		 * 
		 * @param value
		 *            an {@link EObject} value (scalar or vector) to wrap
		 * @return the wrapped {@code value}
		 */
		protected Object wrapEObjects(Object value) {
			Object result = value;

			if (value instanceof EObject) {
				// Easy case
				EObject eObject = (EObject)value;
				if (eObject.eClass().getEPackage() == this.object.eClass().getEPackage()) {
					// Wrap it
					result = FacadeProxy.createProxy(eObject);
				}
			} else if (value instanceof EList<?>) {
				// It's some kind of an EList. Does it implement a feature, requiring
				// notifications?
				if (value instanceof EcoreEList<?>) {
					@SuppressWarnings("unchecked")
					EcoreEList<EObject> eList = (EcoreEList<EObject>)value;
					result = new ProxyEcoreEList<>(facadeProxy, eList);
				} else {
					// It's not a feature but an operation result or parameter
					@SuppressWarnings("unchecked")
					EList<EObject> eList = (EList<EObject>)value;
					result = new ProxyEList<>(eList);
				}
			} else if (value instanceof Collection<?>) {
				// It's some other kind of collection, in a notification
				@SuppressWarnings("unchecked")
				Collection<EObject> collection = (Collection<EObject>)value;
				result = Collections2.transform(collection, (EObject eObject) -> {
					EObject transformed = eObject;
					if (eObject.eClass().getEPackage() == this.object.eClass().getEPackage()) {
						// Wrap it
						transformed = FacadeProxy.createProxy(eObject);
					}
					return transformed;
				});
			}

			return result;
		}

		/**
		 * Queries whether any parameter of a {@code method} is of an {@link EReference} type.
		 * 
		 * @param method
		 *            a method
		 * @return whether any parameter is some kind of {@link EObject} type or a {@link List} of some kind
		 *         of {@link EObject} type
		 */
		protected boolean hasEReferenceTypeParameter(Method method) {
			boolean result = false;

			Type[] paramTypes = method.getGenericParameterTypes();
			for (int i = 0; (i < paramTypes.length) && !result; i++) {
				TypeToken<?> type = TypeToken.of(paramTypes[i]);

				result = type.isSubtypeOf(EObject.class);
				if (!result && type.isSubtypeOf(List.class)) {
					result = type.resolveType(LIST_ELEMENT_TYPE).isSubtypeOf(EObject.class);
				}
			}

			return result;
		}

		/**
		 * Unwraps any {@code objects} that are dynamic façade proxies to get the core objects.
		 * 
		 * @param objects
		 *            objects to unwrap
		 * @return the unwrapped {@code objects}
		 */
		protected Object[] unwrapEObjects(Object[] objects) {
			Object[] result = Arrays.copyOf(objects, objects.length);

			for (int i = 0; i < result.length; i++) {
				Object next = result[i];

				if (next instanceof EObject) {
					// Replace it
					result[i] = unwrap((EObject)next);
				}
			}

			return result;
		}
	}
}
