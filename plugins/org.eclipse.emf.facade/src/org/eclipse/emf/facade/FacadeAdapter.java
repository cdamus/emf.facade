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

import static java.util.Objects.requireNonNull;
import static org.eclipse.emf.facade.util.ReflectiveDispatch.getMethods;
import static org.eclipse.emf.facade.util.ReflectiveDispatch.lookupMethod;
import static org.eclipse.emf.facade.util.ReflectiveDispatch.safeInvoke;

import com.google.common.base.Strings;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Queues;
import com.google.common.collect.Sets;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.ObjIntConsumer;
import java.util.function.Predicate;
import java.util.stream.StreamSupport;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.facade.util.ReflectiveDispatch;

/**
 * An adapter that links a façade object with its underlying model element, coördination synchronziation of
 * changes between the two.
 *
 * @author Christian W. Damus
 */
public class FacadeAdapter implements Adapter.Internal {
	/** Cache of reflective incremental synchronizers. */
	private static LoadingCache<CacheKey, Synchronizer> synchronizerCache = CacheBuilder.newBuilder()
			.concurrencyLevel(4).build(CacheLoader.from(FacadeAdapter::resolveSynchronizer));

	/** Cache of reflective initial synchronizers. */
	private static LoadingCache<CacheKey, Synchronizer> initializerCache = CacheBuilder.newBuilder()
			.concurrencyLevel(4).build(CacheLoader.from(FacadeAdapter::resolveInitializer));

	/** The façade object that this adapter links to an underlying model element. */
	private final EObject facade;

	/** The façade's underlying model element in the (real) user model. */
	private final EObject model;

	/** A latch to prevent re-entrant synchronization. */
	private boolean synchronizing;

	/**
	 * Initializes me with the façade and underlying user model element that I associate with one another.
	 * 
	 * @param facade
	 *            the façade element
	 * @param model
	 *            the underlying model element
	 * @throws NullPointerException
	 *             if either argument is {@code null}
	 */
	public FacadeAdapter(EObject facade, EObject model) {
		super();

		this.facade = requireNonNull(facade, "facade"); //$NON-NLS-1$
		this.model = requireNonNull(model, "model"); //$NON-NLS-1$

		addAdapter(facade);
		addAdapter(model);
	}

	/**
	 * Detaches me from all model elements that I adapt.
	 */
	public void dispose() {
		removeAdapter(facade);
		removeAdapter(model);
	}

	/**
	 * Ensures that I am attached to a {@code notifier}, but at most once.
	 * 
	 * @param notifier
	 *            a notifier to adapt
	 */
	public final void addAdapter(Notifier notifier) {
		EList<Adapter> adapters = notifier.eAdapters();
		if (!adapters.contains(this)) {
			adapters.add(this);
		}
	}

	/**
	 * Ensures that I am not attached to a {@code notifier}.
	 * 
	 * @param notifier
	 *            a notifier to unadapt
	 */
	public final void removeAdapter(Notifier notifier) {
		EList<Adapter> adapters = notifier.eAdapters();
		adapters.remove(this);
	}

	/**
	 * The canonical target of this adapter is the {@linkplain #getUnderlyingElement() underlying model
	 * element}.
	 * 
	 * @return the underlying model element
	 * @see #getUnderlyingElement()
	 */
	@Override
	public Notifier getTarget() {
		return model;
	}

	/**
	 * Queries the façade model element.
	 * 
	 * @return my façade
	 */
	public EObject getFacade() {
		return facade;
	}

	/**
	 * Queries the model element underlying the façade.
	 * 
	 * @return the underlying model element
	 * @see FacadeObject#getUnderlyingElement()
	 */
	public EObject getUnderlyingElement() {
		return model;
	}

	/**
	 * Obtains elements from the underlying model that are related to the given {@code underlying} element of
	 * this façade, that are also claimed by this façade as part of its realization. This notion is applied
	 * recursively to related elements of related underlying elements. <strong>Note</strong> that a façade
	 * <ul>
	 * <li>may report as its related underlying elements any elements that are also underlying some other
	 * façade. This mapping is, in general, many-to-many</li>
	 * <li>needs not be concerned with cycles in transitive relationships in the underlying model. Clients of
	 * this API are required to account for cycles, or just use the {@link #getAllRelatedUnderlyingElements()}
	 * API
	 * </ul>
	 * 
	 * @param underlying
	 *            an element underlying my façade
	 * @return elements related to it that are integral to my façade, or an empty iterable if none (not
	 *         {@code null})
	 * @see #getAllRelatedUnderlyingElements()
	 */
	public Iterable<? extends EObject> getRelatedUnderlyingElements(EObject underlying) {
		return Collections.emptyList();
	}

	/**
	 * Computes the transitive closure of all elements in the underlying model that realize my façade.
	 * 
	 * @return all of my façades underlying elements, starting with and perhaps limited to the
	 *         {@linkplain #getUnderlyingElement() principal underlying element}
	 * @see #getRelatedUnderlyingElements(EObject)
	 * @see #getUnderlyingElement()
	 */
	public Set<? extends EObject> getAllRelatedUnderlyingElements() {
		Set<EObject> result = Sets.newLinkedHashSet();
		Queue<Iterable<? extends EObject>> fifoRelations = Queues.newArrayDeque();

		// Seed the result
		EObject under = getUnderlyingElement();
		result.add(under);
		fifoRelations.offer(getRelatedUnderlyingElements(under));

		// Breadth-first discovery of related elements with accounting for cycles
		for (Iterable<? extends EObject> relations = fifoRelations
				.poll(); relations != null; relations = fifoRelations.poll()) {
			for (EObject next : relations) {
				if (result.add(next)) {
					fifoRelations.offer(getRelatedUnderlyingElements(next));
				}
			}
		}

		return result;
	}

	/**
	 * By default, no additional targets are tracked.
	 * 
	 * @param newTarget
	 *            a notifier to which I have been attached
	 */
	@Override
	public void setTarget(Notifier newTarget) {
		// Don't track any other targets
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void unsetTarget(Notifier oldTarget) {
		if ((oldTarget == model) || (oldTarget == facade)) {
			dispose();
		}
	}

	/**
	 * Reacts to changes in the façade or underlying element to synchronize with the other.
	 * 
	 * @param notification
	 *            description of a change in either the façade or the underlying element
	 * @see #handleNotification(Notification)
	 */
	@Override
	public void notifyChanged(Notification notification) {
		if (notification.isTouch()) {
			return;
		}

		handleNotification(notification);
	}

	/**
	 * Implements the handling of a notification to perform synchronization.
	 * 
	 * @param notification
	 *            description of a change in either the façade or the underlying element. It will not be a
	 *            {@link Notification#isTouch() touch} event
	 * @return whether the notification was completely processed an no further synchronization is required
	 */
	protected boolean handleNotification(Notification notification) {
		boolean result = false;

		if (notification.getNotifier() == model) {
			syncModelToFacade(notification);
			result = true;
		} else if (notification.getNotifier() == facade) {
			syncFacadeToModel(notification);
			result = true;
		}

		return result;
	}

	/**
	 * Synchronizes a feature according to the details of the given {@code notification}.
	 * 
	 * @param from
	 *            the source of the {@code notification}
	 * @param to
	 *            the object to which the change is to be synchronized
	 * @param notification
	 *            the description of the change to be synchronized
	 * @param valueType
	 *            the type of value in the feature to be synchronized
	 * @param onValueAdded
	 *            handler for a value added to the feature, or {@code null} if not needed
	 * @param onValueRemoved
	 *            handler for a value removed from the feature, or {@code null} if not needed
	 * @param <E>
	 *            the type of value in the feature
	 */
	protected <E> void syncFeature(EObject from, EObject to, Notification notification, Class<E> valueType,
			FeatureHandler<? super E> onValueAdded, FeatureHandler<? super E> onValueRemoved) {

		switch (notification.getEventType()) {
			case Notification.ADD:
				E singleAdded = valueType.cast(notification.getNewValue());
				onValueAdded.handleFeatureValue(from, to, singleAdded, notification.getPosition());
				break;
			case Notification.ADD_MANY:
				Iterator<?> manyAdded = ((Collection<?>)notification.getNewValue()).iterator();
				for (int i = notification.getPosition(); manyAdded.hasNext(); i++) {
					E next = valueType.cast(manyAdded.next());
					onValueAdded.handleFeatureValue(from, to, next, i);
				}
				break;
			case Notification.REMOVE:
				E singleRemoved = valueType.cast(notification.getOldValue());
				onValueRemoved.handleFeatureValue(from, to, singleRemoved, notification.getPosition());
				break;
			case Notification.REMOVE_MANY:
				Iterator<?> manyRemoved = ((Collection<?>)notification.getOldValue()).iterator();
				int[] oldPositions = (int[])notification.getNewValue();
				for (int i = 0; manyRemoved.hasNext(); i++) {
					E next = valueType.cast(manyRemoved.next());
					int index;
					if (oldPositions == null) {
						index = Notification.NO_INDEX; // As in clear of the list
					} else {
						index = oldPositions[i];
					}
					onValueRemoved.handleFeatureValue(from, to, next, index);
				}
				break;
			case Notification.SET:
			case Notification.UNSET:
			case Notification.RESOLVE:
				E oldValue = valueType.cast(notification.getOldValue());
				if (oldValue != null) {
					onValueRemoved.handleFeatureValue(from, to, oldValue, notification.getPosition());
				}
				E newValue = valueType.cast(notification.getNewValue());
				if (newValue != null) {
					onValueAdded.handleFeatureValue(from, to, newValue, notification.getPosition());
				}
				break;
			case Notification.MOVE:
				E movedValue = valueType.cast(notification.getNewValue());
				// Cannot use getOldIntValue() because that is for int-valued features
				int oldPosition = ((Integer)notification.getOldValue()).intValue();
				onValueRemoved.handleFeatureValue(from, to, movedValue, oldPosition);
				onValueAdded.handleFeatureValue(from, to, movedValue, notification.getPosition());
				break;
			default:
				// Pass
				break;
		}
	}

	/**
	 * Synchronizes a feature according to the details of the given {@code notification}.
	 * 
	 * @param from
	 *            the source of the {@code notification}
	 * @param to
	 *            the object to which the change is to be synchronized
	 * @param notification
	 *            the description of the change to be synchronized
	 * @param valueType
	 *            the type of value in the feature to be synchronized
	 * @param onValueAdded
	 *            handler for a value added to the feature, or {@code null} if not needed
	 * @param onValueRemoved
	 *            handler for a value removed from the feature, or {@code null} if not needed
	 * @param <E>
	 *            the type of value in the feature
	 */
	protected <E> void syncFeature(EObject from, EObject to, Notification notification, Class<E> valueType,
			ObjIntConsumer<? super E> onValueAdded, ObjIntConsumer<? super E> onValueRemoved) {

		FeatureHandler<? super E> addedHandler;
		if (onValueAdded != null) {
			addedHandler = (fromIgnored, toIgnored, value, position) -> onValueAdded.accept(value, position);
		} else {
			addedHandler = FeatureHandler.PASS;
		}

		FeatureHandler<? super E> removedHandler;
		if (onValueRemoved != null) {
			removedHandler = (fromIgnored, toIgnored, value, position) -> onValueRemoved.accept(value,
					position);
		} else {
			removedHandler = FeatureHandler.PASS;
		}

		syncFeature(from, to, notification, valueType, addedHandler, removedHandler);
	}

	/**
	 * Useful method to reference as a no-op handler for the
	 * {@link #handleNotification(Notification, Class, ObjIntConsumer, ObjIntConsumer)} API.
	 * 
	 * @param value
	 *            ignored
	 * @param position
	 *            ignored
	 * @see #handleNotification(Notification, Class, ObjIntConsumer, ObjIntConsumer)
	 */
	protected final void pass(Object value, int position) {
		// Pass
	}

	/**
	 * Synchronizes the underlying model to the façade, triggered by the given {@code notification}.
	 * 
	 * @param notification
	 *            description of a change in the model
	 */
	protected void syncModelToFacade(Notification notification) {
		synchronize(model, facade, false, notification);
	}

	/**
	 * Synchronizes a {@code source} object to the {@code target}.
	 * 
	 * @param source
	 *            the synchronization source ('from') object
	 * @param target
	 *            the synchronization target ('to') object
	 * @param toModel
	 *            whether synchronization is to the model (as opposed to the façade)
	 * @param notification
	 *            the notification that triggered synchronization
	 */
	protected final void synchronize(EObject source, EObject target, boolean toModel,
			Notification notification) {

		if (this.synchronizing) {
			return;
		}

		Synchronizer synchronizer;
		if (toModel) {
			synchronizer = resolveFacadeToModelSynchronizer(source, target, notification);
		} else {
			synchronizer = resolveModelToFacadeSynchronizer(source, target, notification);
		}

		final boolean wasSynchronizing = this.synchronizing;
		this.synchronizing = true;

		try {
			synchronizer.synchronize(this, source, target, notification);
		} finally {
			this.synchronizing = wasSynchronizing;
		}
	}

	/**
	 * Synchronizes the façade to its underlying model, triggered by the given {@code notification}.
	 * 
	 * @param notification
	 *            description of a change in the façade
	 */
	protected void syncFacadeToModel(Notification notification) {
		synchronize(facade, model, true, notification);
	}

	/**
	 * I am an adapter for either the {@code FacadeObject} type or the {@code FacadeAdapter} type.
	 * 
	 * @param type
	 *            the adapter type to test for
	 * @return whether I am an adapter of the given {@code type}
	 */
	@Override
	public boolean isAdapterForType(Object type) {
		return (type == FacadeObject.class) || (type == FacadeAdapter.class);
	}

	/**
	 * <p>
	 * Resolves the synchronizer call-back for a given {@code notification} from the underlying model element.
	 * The default implementation delegates by reflection to a synchronization method defined by this adapter
	 * with the following name pattern and signature:
	 * </p>
	 * <blockquote> <tt>sync<i>FeatureName</i>ToFacade(<i>ModelType</i>, <i>FacadeType</i>, Notification)</tt>
	 * </blockquote>
	 * <p>
	 * where
	 * </p>
	 * <ul>
	 * <li><i>FeatureName</i> is the name of the feature to synchronize (as indicated by the
	 * {@code notification}) with the initial letter upper-cased</li>
	 * <li><i>ModelType</i> is the Java type of the {@code source} object's {@link EClass}</li>
	 * <li><i>FacadeType</i> is the Java type of the {@code target} object's {@link EClass}</li>
	 * </ul>
	 * <p>
	 * The most specific method signature as determined by the <i>ModelType</i> and <i>FacadeType</i> is
	 * selected that is compatible with the actual types of the {@code source} and {@code target} objects,
	 * respectively. However, the {@link Notification} is optional: the synchronization method is not required
	 * to have this parameter, but any compatible method that accepts a notification trumps any method that
	 * does not, even if the latter is more specific in the other parameters.
	 * </p>
	 * 
	 * @param source
	 *            the synchronization source ('from') object
	 * @param target
	 *            the synchronization target ('to') object
	 * @param notification
	 *            a notification
	 * @return the synchronizer. Must never be {@code null}, but at least the no-op {@link Synchronizer#PASS
	 *         PASS} instance
	 */
	protected Synchronizer resolveModelToFacadeSynchronizer(EObject source, EObject target,
			Notification notification) {
		return resolveReflectiveSynchronizer(source, target, notification, false);
	}

	/**
	 * Resolves the reflective method synchronizer for a given {@code notification} in the specified
	 * direction.
	 * 
	 * @param source
	 *            the synchronization source ('from') object
	 * @param target
	 *            the synchronization target ('to') object
	 * @param notification
	 *            a notification
	 * @param toModel
	 *            whether the synchronization direction is façade-to-model
	 * @return the synchronizer
	 */
	private Synchronizer resolveReflectiveSynchronizer(EObject source, EObject target,
			Notification notification, boolean toModel) {

		Synchronizer result = Synchronizer.PASS;

		Object feature = notification.getFeature();
		if (feature instanceof EStructuralFeature) {
			String featureName = ((EStructuralFeature)feature).getName();

			// Use the EClass instance-classes to get the interface type if these models were
			// generated with interfaces. Try first with a notification for specificity, then without
			Class<?> sourceType = source.eClass().getInstanceClass();
			Class<?> targetType = target.eClass().getInstanceClass();

			CacheKey key = new CacheKey(getClass(), featureName, toModel, sourceType, targetType);
			result = synchronizerCache.getUnchecked(key);
		}

		return result;
	}

	/**
	 * Resolves a synchronizer as specified by its caching {@code key}.
	 * 
	 * @param key
	 *            a caching key
	 * @return the synchronizer to cache. Must not be {@code null}, but instead in that case should be the
	 *         {@link Synchronizer#PASS PASS} instance
	 */
	private static Synchronizer resolveSynchronizer(CacheKey key) {
		String directionName;

		if (key.toModel) {
			directionName = "Model"; //$NON-NLS-1$
		} else {
			directionName = "Facade"; //$NON-NLS-1$
		}

		String syncMethodName = String.format("sync%sTo%s", //$NON-NLS-1$
				toInitialUpperCase(key.featureName), directionName);

		// Try first with a notification for specificity, then without
		Method syncMethod = lookupMethod(key.owner, syncMethodName, key.sourceType, key.targetType,
				Notification.class);

		if (syncMethod != null) {
			return (adapter, from, to, msg) -> safeInvoke(adapter, syncMethod, from, to, msg);
		}

		// Try the variant signature without the notification parameter
		Method syncMethodWoN = lookupMethod(key.owner, syncMethodName, key.sourceType, key.targetType);
		if (syncMethodWoN != null) {
			return (adapter, from, to, msg) -> safeInvoke(adapter, syncMethodWoN, from, to);
		}

		// No synchronization
		return Synchronizer.PASS;
	}

	/**
	 * <p>
	 * Resolves the synchronizer call-back for a given {@code notification} from the façade element. The
	 * default implementation delegates by reflection to a synchronization method defined by this adapter with
	 * the following name pattern and signature:
	 * </p>
	 * <blockquote> <tt>sync<i>FeatureName</i>ToModel(<i>FacadeType</i>, <i>ModelType</i>, Notification)</tt>
	 * </blockquote>
	 * <p>
	 * where
	 * </p>
	 * <ul>
	 * <li><i>FeatureName</i> is the name of the feature to synchronize (as indicated by the
	 * {@code notification}) with the initial letter upper-cased</li>
	 * <li><i>FacadeType</i> is the Java type of the {@code source} object's {@link EClass}</li>
	 * <li><i>ModelType</i> is the Java type of the {@code target} object's {@link EClass}</li>
	 * </ul>
	 * <p>
	 * The most specific method signature as determined by the <i>FcadeType</i> and <i>ModelType</i> is
	 * selected that is compatible with the actual types of the {@code source} and {@code target} objects,
	 * respectively. However, the {@link Notification} is optional: the synchronization method is not required
	 * to have this parameter, but any compatible method that accepts a notification trumps any method that
	 * does not, even if the latter is more specific in the other parameters.
	 * </p>
	 * 
	 * @param source
	 *            the synchronization source ('from') object
	 * @param target
	 *            the synchronization target ('to') object
	 * @param notification
	 *            a notification
	 * @return the synchronizer. Must never be {@code null}, but at least the no-op {@link Synchronizer#PASS
	 *         PASS} instance
	 */
	protected Synchronizer resolveFacadeToModelSynchronizer(EObject source, EObject target,
			Notification notification) {

		return resolveReflectiveSynchronizer(source, target, notification, true);
	}

	/**
	 * Convert a string that may start with a lower case to one that starts with an upper case, if indeed the
	 * first character exists and has case.
	 * 
	 * @param s
	 *            a string or {@code null}
	 * @return {@code s} with the initial character upper case, or else just {@code s}
	 */
	static String toInitialUpperCase(String s) {
		String result = s;

		if (!Strings.isNullOrEmpty(s)) {
			String initial = s.substring(0, 1);
			String upper = initial.toUpperCase();
			if (!initial.equals(upper)) {
				result = upper + s.substring(1);
			}
		}

		return result;
	}

	/**
	 * Initializes synchronization between the model and its façade.
	 * 
	 * @param direction
	 *            the synchronization direction
	 */
	public void initialSync(SyncDirectionKind direction) {
		initialSync(direction, null);
	}

	/**
	 * Performs initial synchronization of a {@code facade} object and its underlying {@code model} element.
	 * 
	 * @param aFacade
	 *            a facade object
	 * @param aModel
	 *            its underlying model element
	 * @param direction
	 *            the synchronization direction
	 */
	protected final void initialSync(EObject aFacade, EObject aModel, SyncDirectionKind direction) {
		initialSync(aFacade, aModel, direction, null);
	}

	/**
	 * Initializes synchronization between the model and its façade.
	 * 
	 * @param direction
	 *            the synchronization direction
	 * @param feature
	 *            the specific feature to synchronize, or {@code null} to synchronize all
	 */
	public void initialSync(SyncDirectionKind direction, EStructuralFeature feature) {
		initialSync(facade, model, direction, feature);
	}

	/**
	 * Performs initial synchronization of a {@code facade} object and its underlying {@code model} element.
	 * 
	 * @param aFacade
	 *            a facade object
	 * @param aModel
	 *            its underlying model element
	 * @param direction
	 *            the synchronization direction
	 * @param feature
	 *            the specific feature to synchronize, or {@code null} to synchronize all
	 */
	protected final void initialSync(EObject aFacade, EObject aModel, SyncDirectionKind direction,
			EStructuralFeature feature) {

		if (this.synchronizing) {
			return;
		}

		final boolean wasSynchronizing = this.synchronizing;
		this.synchronizing = true;

		try {
			String featureName;

			if (feature == null) {
				featureName = null;
			} else {
				featureName = feature.getName();
			}

			Class<?> modelType = aModel.eClass().getInstanceClass();
			Class<?> facadeType = aFacade.eClass().getInstanceClass();

			direction.sync(this, aFacade, aModel,
					() -> initializerCache.getUnchecked(
							new CacheKey(getClass(), featureName, false, modelType, facadeType)),
					() -> initializerCache.getUnchecked(
							new CacheKey(getClass(), featureName, true, facadeType, modelType)));
		} finally {
			this.synchronizing = wasSynchronizing;
		}
	}

	/**
	 * Resolves an initial synchronizer as specified by its caching {@code key}.
	 * 
	 * @param key
	 *            a caching key
	 * @return the initializer to cache. Must not be {@code null}, but instead in that case should be the
	 *         {@link Synchronizer#PASS PASS} instance
	 */
	private static Synchronizer resolveInitializer(CacheKey key) {
		com.google.common.base.Predicate<String> methodNameFilter;

		if (key.toModel) {
			methodNameFilter = syncToModelName(key.featureName);
		} else {
			methodNameFilter = syncToFacadeName(key.featureName);
		}

		Spliterator<Method> methods = getMethods(key.owner, methodNameFilter, key.sourceType, key.targetType)
				.spliterator();
		return StreamSupport.stream(methods, false)
				.<Synchronizer> map(m -> (adapter, from, to, msg) -> safeInvoke(adapter, m, from, to))
				.reduce(Synchronizer::andThen) //
				.orElse(Synchronizer.PASS);
	}

	/**
	 * Matches a sync-to-model method name.
	 * 
	 * @param featureName
	 *            the feature name, or {@code null} to match any feature
	 * @return the to-model synchronization method name filter
	 */
	private static com.google.common.base.Predicate<String> syncToModelName(String featureName) {
		if (featureName == null) {
			return s -> s.startsWith("sync") && s.endsWith("ToModel"); //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			return String.format("sync%sToModel", toInitialUpperCase(featureName))::equals; //$NON-NLS-1$
		}
	}

	/**
	 * Matches a sync-to-façade method name.
	 * 
	 * @param featureName
	 *            the feature name, or {@code null} to match any feature
	 * @return the to-façade synchronization method name filter
	 */
	private static com.google.common.base.Predicate<String> syncToFacadeName(String featureName) {
		if (featureName == null) {
			return s -> s.startsWith("sync") && s.endsWith("ToFacade"); //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			return String.format("sync%sToFacade", toInitialUpperCase(featureName))::equals; //$NON-NLS-1$
		}
	}

	/**
	 * Handles any possible addition/removal of dependencies that need to be tracked.
	 * 
	 * @param notification
	 *            a notification indicating possibly addition/removal of dependencies
	 * @param isDependency
	 *            a predicate matching an object should be or already is a dependency
	 * @return whether the {@code notification} did actually turn out to be about dependencies, which perhaps
	 *         means it needn't be processed for anything else
	 */
	protected boolean handleDependencies(Notification notification,
			BiPredicate<? super EReference, ? super EObject> isDependency) {

		if (!(notification.getFeature() instanceof EReference)) {
			// Obviously not adding/removing dependencies
			return false;
		}

		boolean result = false;

		// Only EObjects can notify on EReferences
		EObject notifier = (EObject)notification.getNotifier();
		EReference reference = (EReference)notification.getFeature();

		EObject newDependency;
		EObject oldDependency;
		switch (notification.getEventType()) {
			case Notification.ADD:
				newDependency = (EObject)notification.getNewValue();
				if (isDependency.test(reference, newDependency)) {
					handleDependencyAdded(notifier, reference, newDependency);
					result = true;
				}
				break;
			case Notification.ADD_MANY:
				result = ((Collection<?>)notification.getNewValue()).stream() //
						.map(EObject.class::cast).filter(applyFirst(reference, isDependency)) //
						.map(d -> {
							handleDependencyAdded(notifier, reference, d);
							return Boolean.TRUE;
						}).reduce(Boolean::logicalOr).orElse(Boolean.FALSE).booleanValue();
				break;
			case Notification.REMOVE:
				oldDependency = (EObject)notification.getOldValue();
				if (isDependency.test(reference, oldDependency)) {
					handleDependencyRemoved(notifier, reference, oldDependency);
					result = true;
				}
				break;
			case Notification.REMOVE_MANY:
				result = ((Collection<?>)notification.getOldValue()).stream() //
						.map(EObject.class::cast).filter(applyFirst(reference, isDependency)) //
						.map(d -> {
							handleDependencyAdded(notifier, reference, d);
							return Boolean.TRUE;
						}).reduce(Boolean::logicalOr).orElse(Boolean.FALSE).booleanValue();
				break;
			case Notification.SET:
			case Notification.UNSET:
			case Notification.RESOLVE:
				oldDependency = (EObject)notification.getOldValue();
				if ((oldDependency != null) && isDependency.test(reference, oldDependency)) {
					handleDependencyRemoved(notifier, reference, oldDependency);
					result = true;
				}
				newDependency = (EObject)notification.getNewValue();
				if ((newDependency != null) && isDependency.test(reference, newDependency)) {
					handleDependencyAdded(notifier, reference, newDependency);
					result = true;
				}
				break;
			default:
				// Pass
				break;
		}

		return result;
	}

	/**
	 * Partially applies a bi-predicate on the {@code first} argument.
	 * 
	 * @param first
	 *            the first argument
	 * @param predicate
	 *            a bi-predicate to partially apply
	 * @return the partial application of the {@code predicate}
	 * @param <T>
	 *            the first argument type
	 * @param <U>
	 *            the second argument type
	 */
	private static <T, U> Predicate<U> applyFirst(T first, BiPredicate<T, U> predicate) {
		return u -> predicate.test(first, u);
	}

	/**
	 * Handles the addition of a new dependency that needs to be tracked. The default implementation
	 * reflectively dispatches to a method of the form <blockquote>
	 * <tt><i>reference</i>Added(<i>ownerType</i>, <i>dependencyType</i>)</tt> </blockquote> where
	 * <ul>
	 * <li><i>reference</i> is the name of the {@code reference}</li>
	 * <li><i>ownerType</i> is the type of the {@code owner}</li>
	 * <li><i>dependencyType</i> is the name of the {@code newDependency}</li>
	 * </ul>
	 * 
	 * @param owner
	 *            the owner of the dependency
	 * @param reference
	 *            the reference implementing the dependency relationship
	 * @param newDependency
	 *            the new dependency in that {@code reference}
	 */
	protected void handleDependencyAdded(EObject owner, EReference reference, EObject newDependency) {
		String methodName = String.format("%sAdded", reference.getName()); //$NON-NLS-1$
		ReflectiveDispatch.safeInvoke(this, methodName, owner, newDependency);
	}

	/**
	 * Handles the addition of an old dependency that no longer needs to be tracked. The default
	 * implementation reflectively dispatches to a method of the form <blockquote>
	 * <tt><i>reference</i>Removed(<i>ownerType</i>, <i>dependencyType</i>)</tt> </blockquote> where
	 * <ul>
	 * <li><i>reference</i> is the name of the {@code reference}</li>
	 * <li><i>ownerType</i> is the type of the {@code owner}</li>
	 * <li><i>dependencyType</i> is the name of the {@code newDependency}</li>
	 * </ul>
	 * 
	 * @param owner
	 *            the owner of the dependency
	 * @param reference
	 *            the reference implementing the dependency relationship
	 * @param oldDependency
	 *            the old dependency in that {@code reference}
	 */
	protected void handleDependencyRemoved(EObject owner, EReference reference, EObject oldDependency) {
		String methodName = String.format("%sRemoved", reference.getName()); //$NON-NLS-1$
		ReflectiveDispatch.safeInvoke(this, methodName, owner, oldDependency);
	}

	/**
	 * Ensures that a {@code facade} is connected to its underlying {@code model}.
	 * 
	 * @param facade
	 *            a facade object
	 * @param model
	 *            the underlying model element
	 * @param type
	 *            the type of adapter
	 * @param adapterCreator
	 *            a function that creates the adapter, if needed
	 * @return the adapter that connects the {@code facade} with its {@code model}
	 * @param <A>
	 *            the adapter type
	 * @param <F>
	 *            the façade type
	 * @param <M>
	 *            the model element type
	 */
	protected static <A extends FacadeAdapter, F extends EObject, M extends EObject> A connect(F facade,
			M model, Class<A> type, BiFunction<? super F, ? super M, ? extends A> adapterCreator) {

		A result = get(model, type);
		if ((result != null) && (result.getFacade() != facade)) {
			result.dispose();
			result = null;
		}

		if (result == null) {
			result = adapterCreator.apply(facade, model);
		}

		return result;
	}

	/**
	 * Obtains the existing facade adapter attached to a {@code notifier}.
	 * 
	 * @param notifier
	 *            an object, either a facade or an underlying model element
	 * @return the existing adapter instance, or {@code null} if none
	 */
	public static FacadeAdapter getInstance(Notifier notifier) {
		return get(notifier, FacadeAdapter.class);
	}

	/**
	 * Obtains the existing facade adapter of the given {@code type} attached to a {@code notifier}.
	 * 
	 * @param notifier
	 *            an object, either a facade or an underlying model element
	 * @param type
	 *            the adapter type
	 * @return the existing adapter instance, or {@code null} if none
	 * @param <A>
	 *            the adapter type
	 */
	protected static <A extends FacadeAdapter> A get(Notifier notifier, Class<A> type) {
		A result = null;

		if (type != FacadeAdapter.class) {
			// We're going for a specific adapter type, so it should be reliable
			result = type.cast(EcoreUtil.getExistingAdapter(notifier, type));
		} else {
			for (Adapter next : notifier.eAdapters()) {
				if (next.isAdapterForType(type) && type.isInstance(next)) {
					// Strong candidate
					result = type.cast(next);

					// But only if it's actually this notifier's façade adapter (the
					// adapters of related elements could also be observing it). Be careful
					// if the 'notifier' is a dynamic proxy façade for the real notifier,
					// which is then what the adapter references
					if ((result.getFacade() == notifier) || (result.getUnderlyingElement() == notifier)
							|| result.getAllRelatedUnderlyingElements().contains(notifier)
							|| (getRealNotifier(notifier) == result.getFacade())) {

						break; // Got it
					} else {
						result = null; // Try again
					}
				}
			}
		}

		return result;
	}

	/**
	 * Queries whether an {@code object} is or appears to be a façade.
	 * 
	 * @param object
	 *            a model element
	 * @return whether it is a façade, has a façade adapter, or is contained in an object that (recursively)
	 *         is or appears to be a façade
	 */
	public static boolean isFacade(EObject object) {
		boolean result = object instanceof FacadeObject; // Easiest

		if (!result && (object != null)) {
			for (EObject next = object; !result && (next != null); next = next.eContainer()) {
				// Look for a façade adapter that has this object as the façade (not
				// the underlying element)
				FacadeAdapter adapter = getInstance(next);
				result = (adapter != null) && (adapter.getFacade() == next);
			}
		}

		return result;
	}

	/**
	 * If a {@code notifier} is a {@linkplain FacadeProxy dynamic proxy} implementation of the
	 * {@link FacadeObject} protocol, then obtain the real façade object that it wraps.
	 * 
	 * @param notifier
	 *            a notifier
	 * @return the real notifier, which may be the {@code notifier} itself, or else a wrapped instance
	 */
	private static Notifier getRealNotifier(Notifier notifier) {
		if (notifier instanceof FacadeObject) {
			return FacadeProxy.unwrap((FacadeObject)notifier);
		} else {
			return notifier;
		}
	}

	/**
	 * Obtains the object underlying the given object assumed to be a {@code facade} object.
	 * 
	 * @param facade
	 *            a presumed façade object
	 * @return the corresponding object from the underlying model, or {@code null} if the input is not an
	 *         object of some façade with a {@link FacadeAdapter} attached to it
	 */
	public static EObject getUnderlyingObject(EObject facade) {
		EObject result = null;

		FacadeAdapter adapter = get(facade, FacadeAdapter.class);
		if ((adapter != null)
				&& ((adapter.getFacade() == facade) || (adapter.getFacade() == getRealNotifier(facade)))) {

			result = adapter.getUnderlyingElement();
		}

		return result;
	}

	/**
	 * Obtains the façade representing the given object assumed to be {@code underlying} a façade.
	 * 
	 * @param underlying
	 *            an object presumed to be underlying a façade
	 * @return the corresponding façade, or {@code null} if the input is not an object underlying some façade
	 *         with a {@link FacadeAdapter} attached to it
	 */
	public static EObject getFacade(EObject underlying) {
		EObject result = null;

		FacadeAdapter adapter = get(underlying, FacadeAdapter.class);
		if (adapter != null) {
			// It could be dynamically proxied, to boot
			result = FacadeProxy.wrap(adapter.getFacade());
		}

		return result;
	}

	/**
	 * A no-op.
	 * 
	 * @param adapter
	 *            ignored
	 * @param from
	 *            ignored
	 * @param to
	 *            ignored
	 * @param msg
	 *            ignored
	 */
	private static void syncNoop(FacadeAdapter adapter, EObject from, EObject to, Notification msg) {
		// Pass
	}

	/**
	 * A no-op.
	 * 
	 * @param from
	 *            ignored
	 * @param to
	 *            ignored
	 * @param value
	 *            ignored
	 * @param position
	 *            ignored
	 */
	private static void featureHandlerNoop(EObject from, EObject to, Object value, int position) {
		// Pass
	}

	//
	// Nested types
	//

	/**
	 * Protocol for call-backs that synchronize the façade with its underlying model element or <i>vice
	 * versa</i>.
	 *
	 * @author Christian W. Damus
	 */
	@FunctionalInterface
	public interface Synchronizer {
		/** A no-op synchronizer, doing nothing. */
		Synchronizer PASS = FacadeAdapter::syncNoop;

		/**
		 * Synchronizes a change {@code from} the changed object {@code to} its counterpart in the façade
		 * association.
		 * 
		 * @param adapter
		 *            the façade adapter in which context the synchronization is performed
		 * @param from
		 *            an object, in the underlying model or the façade, that has changed
		 * @param to
		 *            the counterpart of the changed object, in the façade or the underlying model,
		 *            respectively
		 * @param msg
		 *            the description of the change
		 */
		void synchronize(FacadeAdapter adapter, EObject from, EObject to, Notification msg);

		/**
		 * Chains another synchronizer to be invoked after me with the same arguments.
		 * 
		 * @param next
		 *            a synchronizer to chain
		 * @return the chained synchronizer, myself and then the {@code next}
		 */
		default Synchronizer andThen(Synchronizer next) {
			return (adapter, from, to, msg) -> {
				synchronize(adapter, from, to, msg);
				next.synchronize(adapter, from, to, msg);
			};
		}
	}

	/**
	 * Caching key that uniquely identifies a reflective synchronizer.
	 *
	 * @author Christian W. Damus
	 */
	private static final class CacheKey {
		/** The facade-adapter synchronization method owner. */
		final Class<?> owner;

		/** The name of the feature to synchronize. */
		final String featureName;

		/** Whether this synchronizer performs the to-model direction (as opposed to to-façade). */
		final boolean toModel;

		/** The source type in the synchronization. */
		final Class<?> sourceType;

		/** The target type in the synchronization. */
		final Class<?> targetType;

		/**
		 * Initializes me with my caching parameters.
		 * 
		 * @param owner
		 *            the owner type
		 * @param featureName
		 *            the name of the feature to synchronizer
		 * @param toModel
		 *            whether synchronization is in the to-model direction (as opposed to to-façade)
		 * @param sourceType
		 *            the source type
		 * @param targetType
		 *            the target type
		 */
		CacheKey(Class<? extends FacadeAdapter> owner, String featureName, boolean toModel,
				Class<?> sourceType, Class<?> targetType) {

			super();

			this.owner = owner;
			this.featureName = featureName;
			this.toModel = toModel;
			this.sourceType = sourceType;
			this.targetType = targetType;
		}

		/**
		 * {@inheritDoc}
		 */
		// CHECKSTYLE:OFF (generated)
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((sourceType == null) ? 0 : sourceType.hashCode());
			result = prime * result + ((featureName == null) ? 0 : featureName.hashCode());
			result = prime * result + ((targetType == null) ? 0 : targetType.hashCode());
			result = prime * result + ((owner == null) ? 0 : owner.hashCode());
			result = prime * result + (toModel ? 1231 : 1237);
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
			if (sourceType == null) {
				if (other.sourceType != null) {
					return false;
				}
			} else if (!sourceType.equals(other.sourceType)) {
				return false;
			}
			if (featureName == null) {
				if (other.featureName != null) {
					return false;
				}
			} else if (!featureName.equals(other.featureName)) {
				return false;
			}
			if (targetType == null) {
				if (other.targetType != null) {
					return false;
				}
			} else if (!targetType.equals(other.targetType)) {
				return false;
			}
			if (owner == null) {
				if (other.owner != null) {
					return false;
				}
			} else if (!owner.equals(other.owner)) {
				return false;
			}
			if (toModel != other.toModel) {
				return false;
			}
			return true;
		}
		// CHECKSTYLE:ON

	}

	/**
	 * Protocol for call-backs that handle addition or removal of an object in a feature being synchronized.
	 *
	 * @param <E>
	 *            the type of value(s) in the feature
	 * @author Christian W. Damus
	 */
	@FunctionalInterface
	public interface FeatureHandler<E> {
		/** A no-op handler, doing nothing. */
		FeatureHandler<Object> PASS = FacadeAdapter::featureHandlerNoop;

		/**
		 * Handles addition or removal of a {@code value} at some {@code position} in the feature.
		 * 
		 * @param from
		 *            an object, in the underlying model or the façade, that has changed
		 * @param to
		 *            the counterpart of the changed object, in the façade or the underlying model,
		 *            respectively
		 * @param value
		 *            the value that was added to or removed from the feature
		 * @param position
		 *            the position of the change in the feature, or {@link Notification#NO_INDEX} if the
		 *            change has no position (scalar feature or list cleared)
		 */
		void handleFeatureValue(EObject from, EObject to, E value, int position);

		/**
		 * Chains another feature handler to be invoked after me with the same arguments.
		 * 
		 * @param next
		 *            a handler to chain
		 * @return the chained handler, myself and then the {@code next}
		 */
		default FeatureHandler<E> andThen(FeatureHandler<? super E> next) {
			return (from, to, value, position) -> {
				handleFeatureValue(from, to, value, position);
				next.handleFeatureValue(from, to, value, position);
			};
		}
	}
}
