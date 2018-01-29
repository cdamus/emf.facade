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
package org.eclipse.emf.facade.util;

import static com.google.common.base.Predicates.and;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.primitives.Primitives.wrap;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Ordering;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;

import org.eclipse.emf.facade.internal.EMFFacadePlugin;

/**
 * Convenient dynamic (reflective) access to methods of objects where those methods are not required to exist.
 * Suitable for patterns of <em>call-backs</em> or <em>hooks</em> that the framework will attempt to invoke
 * but that objects may optionally not declare. Because of this degree of flexibility in method dispatch,
 * run-time failures in the form of exceptions are suppressed (but logged). Virtual machine errors are
 * propagated.
 *
 * @author Christian W. Damus
 * @since 3.6
 */
public final class ReflectiveDispatch {

	/** Cache of resolved methods. */
	private static LoadingCache<CacheKey, Optional<Method>> methodCache = CacheBuilder.newBuilder()
			.concurrencyLevel(4).build(CacheLoader.from(new Function<CacheKey, Optional<Method>>() {
				@Override
				public Optional<Method> apply(CacheKey input) {
					return resolveMethod(input);
				}
			}));

	/**
	 * Not instantiable by clients.
	 */
	private ReflectiveDispatch() {
		super();
	}

	/**
	 * Safely invokes a method by reflection.
	 * 
	 * @param target
	 *            the object on which to invoke the method
	 * @param name
	 *            the name of the method to invoke on it
	 * @param arg
	 *            the arguments to pass to it
	 * @return the result of the method invocation, or {@code null} if it failed
	 */
	public static Object safeInvoke(Object target, String name, Object... arg) {
		Method method = lookupMethod(target, name, arg);

		if (method == null) {
			return null;
		} else {
			return safeInvoke(target, method, arg);
		}
	}

	/**
	 * Safely invokes a method by reflection, optionally allowing the given exception type to be thrown.
	 * 
	 * @param target
	 *            the object on which to invoke the method
	 * @param name
	 *            the name of the method to invoke on it
	 * @param throwableType
	 *            the throwable type to propagate, or {@code null} if none
	 * @param arg
	 *            the arguments to pass to it
	 * @return the result of the method invocation, or {@code null} if it failed
	 * @throws X
	 *             an exception of the expected type, if thrown by the method execution
	 * @param <X>
	 *            the throwable type to propagate
	 */
	public static <X extends Throwable> Object safeInvoke(Object target, String name, Class<X> throwableType,
			Object... arg) throws X {

		Method method = lookupMethod(target, name, arg);

		if (method == null) {
			return null;
		} else {
			return safeInvoke(target, method, throwableType, arg);
		}
	}

	/**
	 * Resolves the most specific overload of the {@code name}d method that is applicable to the given
	 * arguments.
	 * 
	 * @param owner
	 *            the owner of the method to invoke
	 * @param name
	 *            the method name
	 * @param arg
	 *            the arguments to be dispatched to the method
	 * @return the resolved method, or {@code null} if there is no method that can accept the arguments
	 */
	public static Method lookupMethod(Object owner, String name, Object... arg) {
		Class<?>[] argTypes = new Class<?>[arg.length];
		for (int i = 0; i < arg.length; i++) {
			if (arg[i] == null) {
				argTypes[i] = Void.class;
			} else {
				argTypes[i] = arg[i].getClass();
			}
		}

		return lookupMethod(owner.getClass(), name, argTypes);
	}

	/**
	 * Resolves the most specific overload of the {@code name}d method that is applicable to the given
	 * argument types.
	 * 
	 * @param owner
	 *            the owner type of the method to resolve
	 * @param name
	 *            the method name
	 * @param argType
	 *            the types of arguments to be dispatched to the method
	 * @return the resolved method, or {@code null} if there is no method that can accept the arguments
	 */
	public static Method lookupMethod(Class<?> owner, String name, Class<?>... argType) {
		return methodCache.getUnchecked(new CacheKey(owner, name, argType)).orNull();
	}

	/**
	 * Resolves a method as specified by its caching {@code key}.
	 * 
	 * @param key
	 *            a caching key
	 * @return the optional method to cache, possibly empty but of course not {@code null}
	 */
	private static Optional<Method> resolveMethod(CacheKey key) {
		return resolveMethod(key.owner, key.methodName, key.argTypes);
	}

	/**
	 * Gets a stream over the public methods of the {@code owner} class matching a name filter and argument
	 * types.
	 * 
	 * @param owner
	 *            the owner type of the methods to retrieve
	 * @param nameFilter
	 *            predicate on method names
	 * @param argType
	 *            types of proposed arguments to be passed to the method parameters
	 * @return the applicable methods
	 */
	public static Iterable<Method> getMethods(Class<?> owner, final Predicate<? super String> nameFilter,
			final Class<?>... argType) {

		Predicate<Method> byName = new Predicate<Method>() {
			@Override
			public boolean apply(Method input) {
				return nameFilter.test(input.getName());
			}
		};
		Predicate<Method> byParameterCount = new Predicate<Method>() {
			@Override
			public boolean apply(Method input) {
				// TODO(Java 8): For efficiency, use Method::getParameterCount()
				return input.getParameterTypes().length == argType.length;
			}
		};
		Predicate<Method> bySignature = new Predicate<Method>() {
			@Override
			public boolean apply(Method input) {
				return signatureCompatible(input.getParameterTypes(), argType);
			}
		};

		return filter(ImmutableList.copyOf(owner.getMethods()),
				and(byName, and(byParameterCount, bySignature)));
	}

	/**
	 * Finds the most specific method of my class that has the specified {@code name} and can accept
	 * parameters of the given types.
	 * 
	 * @param owner
	 *            the owner type of the method to resolve
	 * @param name
	 *            the method name to look up
	 * @param argType
	 *            types of anticipated arguments to be passed to the resulting method
	 * @return the matching method
	 */
	private static Optional<Method> resolveMethod(Class<?> owner, String name, Class<?>... argType) {
		Iterable<Method> possibleResults = getMethods(owner, Predicates.equalTo(name), argType);
		if (Iterables.isEmpty(possibleResults)) {
			return Optional.absent();
		} else {
			Function<Method, Class<?>[]> parameterTypes = new Function<Method, Class<?>[]>() {
				@Override
				public Class<?>[] apply(Method input) {
					return input.getParameterTypes();
				}
			};

			return Optional.of(
					Ordering.from(signatureSpecificity()).onResultOf(parameterTypes).max(possibleResults));
		}
	}

	/**
	 * Queries whether the signature of a method, as indicated by the given parameter types, is compatible
	 * with proposed arguments.
	 * 
	 * @param parameterTypes
	 *            method parameter types, in order
	 * @param argumentTypes
	 *            anticipated types of arguments
	 * @return whether the arguments types are all position-wise compatible with the parameter types
	 */
	private static boolean signatureCompatible(Class<?>[] parameterTypes, Class<?>[] argumentTypes) {
		boolean result = parameterTypes.length == argumentTypes.length;

		for (int i = 0; result && (i < parameterTypes.length); i++) {
			if (!wrap(parameterTypes[i]).isAssignableFrom(wrap(argumentTypes[i]))) {
				result = false;
			}
		}

		return result;
	}

	/**
	 * A comparator of method signatures, sorting a more specific signature (position-wise by parameter type)
	 * ahead of a more general signature, in the case of method overloads.
	 * 
	 * @return a method overload signature-specificity comparator
	 */
	private static Comparator<Class<?>[]> signatureSpecificity() {
		return new Comparator<Class<?>[]>() {
			@Override
			public int compare(Class<?>[] a, Class<?>[] b) {
				if (signatureCompatible(a, b)) {
					if (signatureCompatible(b, a)) {
						return 0;
					} else {
						return +1;
					}
				} else {
					return -1;
				}
			}
		};
	}

	/**
	 * Safely invokes a method by reflection.
	 * 
	 * @param owner
	 *            the owner of the method to invoke
	 * @param method
	 *            the method to invoke on it
	 * @param arg
	 *            the arguments to pass to it
	 * @return the result of the method invocation, or {@code null} if it failed
	 */
	public static Object safeInvoke(Object owner, Method method, Object... arg) {
		try {
			return method.invoke(owner, arg);
		} catch (IllegalAccessException | IllegalArgumentException e) {
			EMFFacadePlugin.error(String.format("Failed to invoke %s on receiver %s", method, owner), e); //$NON-NLS-1$
			return null;
		} catch (InvocationTargetException e) {
			EMFFacadePlugin.error(String.format("Method %s execution failed on receiver %s", method, owner), //$NON-NLS-1$
					e.getTargetException());
			return null;
		}
	}

	/**
	 * Safely invokes a method by reflection, optionally allowing the given exception type to be thrown.
	 * 
	 * @param owner
	 *            the owner of the method to invoke
	 * @param method
	 *            the method to invoke on it
	 * @param throwableType
	 *            the throwable type to propagate, or {@code null} if none
	 * @param arg
	 *            the arguments to pass to it
	 * @return the result of the method invocation, or {@code null} if it failed
	 * @throws X
	 *             an exception of the expected type, if thrown by the method execution
	 * @param <X>
	 *            the throwable type to propagate
	 */
	public static <X extends Throwable> Object safeInvoke(Object owner, Method method, Class<X> throwableType,
			Object... arg) throws X {

		try {
			return method.invoke(owner, arg);
		} catch (InvocationTargetException e) {
			if (throwableType == null) {
				// It is optional, after all
				EMFFacadePlugin.error(
						String.format("Method %s execution failed on receiver %s", method, owner), //$NON-NLS-1$
						e.getTargetException());
				return null;
			}

			if (throwableType.isInstance(e.getTargetException())) {
				// Propagate
				throw throwableType.cast(e.getTargetException());
			}
			EMFFacadePlugin
					.error(String.format("Unexpected exception in %s on receiver %s: %s", method, owner, //$NON-NLS-1$
							e.getClass().getName()), e);
			return null;
		} catch (IllegalAccessException | IllegalArgumentException e) {
			EMFFacadePlugin.error(String.format("Failed to invoke %s on receiver %s", method, owner), e); //$NON-NLS-1$
			return null;
		}
	}

	//
	// Nested types
	//

	/**
	 * Caching key that uniquely identifies a reflective method dispatch.
	 *
	 * @author Christian W. Damus
	 */
	private static final class CacheKey {
		/** The method owner. */
		final Class<?> owner;

		/** The name of the method. */
		final String methodName;

		/** The argument types to dispatch. */
		final Class<?>[] argTypes;

		/**
		 * Initializes me with my caching parameters.
		 * 
		 * @param owner
		 *            the owner type
		 * @param methodName
		 *            the name of the method to dispatch
		 * @param argTypes
		 *            the argument types to dispatch
		 */
		CacheKey(Class<?> owner, String methodName, Class<?>[] argTypes) {
			super();

			this.owner = owner;
			this.methodName = methodName;
			this.argTypes = argTypes;
		}

		/**
		 * {@inheritDoc}
		 */
		// CHECKSTYLE:OFF (generated)
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((owner == null) ? 0 : owner.hashCode());
			result = prime * result + ((methodName == null) ? 0 : methodName.hashCode());
			result = prime * result + ((argTypes == null) ? 0 : Arrays.hashCode(argTypes));
			return result;
		}
		// CHECKSTYLE:ON

		/**
		 * {@inheritDoc}
		 */
		// CHECKSTYLE:OFF (generated)
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			CacheKey other = (CacheKey)obj;
			if (owner == null) {
				if (other.owner != null) {
					return false;
				}
			} else if (!owner.equals(other.owner)) {
				return false;
			}
			if (methodName == null) {
				if (other.methodName != null) {
					return false;
				}
			} else if (!methodName.equals(other.methodName)) {
				return false;
			}
			if (argTypes == null) {
				if (other.argTypes != null) {
					return false;
				}
			} else if (!Arrays.equals(argTypes, other.argTypes)) {
				return false;
			}
			return true;
		}
		// CHECKSTYLE:ON

	}
}
