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
package org.eclipse.emf.facade.uml2;

import java.util.Collection;
import java.util.function.Predicate;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.facade.uml2.internal.UMLFacadePlugin;
import org.eclipse.emf.facade.util.ReflectiveDispatch;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.util.UMLUtil;

/**
 * An adapter on UML resources that handles the creation of stereotype applications to trigger synchronization
 * to the façade.
 *
 * @author Christian W. Damus
 */
public class UMLFacadeResourceAdapter extends AdapterImpl {

	/**
	 * Initializes me.
	 */
	protected UMLFacadeResourceAdapter() {
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTarget(Notifier newTarget) {
		// Only record the first target, which is the resource
		if ((newTarget == null) || getTarget() == null) {
			super.setTarget(newTarget);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAdapterForType(Object type) {
		return type == getClass();
	}

	/**
	 * Gets the existing adapter instance of the given {@code type} attached to a {@coed resource}, or else
	 * creates it.
	 * 
	 * @param resource
	 *            a UML model resource
	 * @param type
	 *            the type of adapter to obtain
	 * @return the adapter, created lazily if necessary
	 * @param <T>
	 *            the adapter type
	 */
	public static <T extends UMLFacadeResourceAdapter> T getInstance(Resource resource, Class<T> type) {
		T result = type.cast(EcoreUtil.getExistingAdapter(resource, type));

		if (result == null) {
			try {
				result = type.newInstance();
				result.addAdapter(resource);
			} catch (IllegalAccessException | InstantiationException e) {
				UMLFacadePlugin.error("Failed to create resource adapter", e); //$NON-NLS-1$
			}
		}

		return result;
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
	 * {@inheritDoc}
	 */
	@Override
	public void notifyChanged(Notification msg) {
		if (msg.isTouch()) {
			return;
		}

		if (msg.getNotifier() == getTarget()) {
			if (msg.getFeatureID(Resource.class) == Resource.RESOURCE__CONTENTS) {
				handleResource(msg);
			}
		} else {
			handleStereotypeApplication(msg);
		}
	}

	/**
	 * Reacts to the addition of a new object to the resource which, if it is a stereotype application, may be
	 * processed now or later when it gets its base element.
	 * 
	 * @param msg
	 *            a notification
	 */
	protected void handleResource(Notification msg) {
		switch (msg.getEventType()) {
			case Notification.ADD:
			case Notification.SET:
				EObject newObject = (EObject)msg.getNewValue();
				if (!(newObject instanceof Element)) {
					discoverStereotypeApplication(newObject);
				}
				break;
			case Notification.ADD_MANY:
				@SuppressWarnings("unchecked")
				Collection<? extends EObject> newObjects = (Collection<? extends EObject>)msg.getNewValue();
				Predicate<EObject> isElement = Element.class::isInstance;
				newObjects.stream().filter(isElement.negate()).forEach(this::discoverStereotypeApplication);
				break;
			default:
				// Pass
				break;
		}
	}

	/**
	 * Attempts to discover a stereotype application, if the given new object is indeed that.
	 * 
	 * @param newObject
	 *            a possible stereotype application
	 */
	private void discoverStereotypeApplication(EObject newObject) {
		Stereotype stereo = UMLUtil.getStereotype(newObject);
		if (stereo != null) {
			Element base = UMLUtil.getBaseElement(newObject);
			if (base != null) {
				processStereotypeApplication(newObject, base);
			} else {
				// Process later
				addAdapter(newObject);
			}
		}
	}

	/**
	 * Reacts to the setting of a stereotype application's base element by processing it.
	 * 
	 * @param msg
	 *            a notification
	 */
	protected void handleStereotypeApplication(Notification msg) {
		switch (msg.getEventType()) {
			case Notification.SET:
				// This is always the kind of notification that signals linkage to the base element
				EObject application = (EObject)msg.getNotifier();
				Element base = UMLUtil.getBaseElement(application);
				if (base != null) {
					// Done with this
					removeAdapter(application);
					processStereotypeApplication(application, base);
				}
				break;
			default:
				// Pass
				break;
		}
	}

	/**
	 * Attempts to create the façade (if there is one) for the given {@code base} element and stereotype
	 * {@code application}.
	 * 
	 * @param application
	 *            a stereotype application that may represent the primary stereotype for a DSML concept
	 * @param base
	 *            the model element to which the stereotype is applied
	 */
	protected void processStereotypeApplication(EObject application, Element base) {
		// Try to discover a façade for this base element
		ReflectiveDispatch.safeInvoke(this, "createFacade", base, application); //$NON-NLS-1$
	}
}
