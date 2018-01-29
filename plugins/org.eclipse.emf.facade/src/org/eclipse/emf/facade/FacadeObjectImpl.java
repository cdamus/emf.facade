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

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl.Container;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * An implementation of the {@link FacadeObject} protocol that is suitable to use as the root class for EMF
 * model code generation.
 *
 * @author Christian W. Damus
 */
public class FacadeObjectImpl extends Container implements FacadeObject {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EObject getUnderlyingElement() {
		EObject result = null;

		FacadeAdapter adapter = getFacadeAdapter();
		if (adapter != null) {
			result = adapter.getUnderlyingElement();
		}

		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Resource eResource() {
		Resource result = super.eResource();

		if (result == null) {
			EObject underlying = getUnderlyingElement();
			if (underlying != null) {
				result = underlying.eResource();
			}
		}

		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Resource.Internal eDirectResource() {
		Resource.Internal result = super.eDirectResource();

		// Be careful not to return a fake direct resource if the façade is actually
		// indirectly contained in a resource in the proper way
		if ((result == null) && (super.eResource() == null)) {
			EObject underlying = getUnderlyingElement();
			if (underlying instanceof InternalEObject) {
				result = ((InternalEObject)underlying).eDirectResource();
			}
		}

		return result;
	}

	/**
	 * Frozen access to the basic EMF implementation of {@link #eDirectResource()}.
	 * 
	 * @return the actual EMF-compliant direct resource
	 */
	protected final Resource.Internal eInternalDirectResource() {
		return super.eDirectResource();
	}

	//
	// Copied from EMF and tweaked for direct-resource semantics
	//

	// CHECKSTYLE:OFF (copied from EMF)

	/**
	 * Overrides the superclass implementation to account for the delegation of the {@link #eDirectResource()}
	 * to the underlying model when the façade object is not attached to a resource of its own.
	 */
	@SuppressWarnings("all")
	@Override
	public Resource.Internal eInternalResource() {
		Resource.Internal result = eInternalDirectResource();
		if (result == null) {
			int count = 0;
			for (InternalEObject eContainer = eInternalContainer(); eContainer != null; eContainer = eContainer
					.eInternalContainer()) {
				// Since the cycle is detected by checking if we hit "this" again, after many iterations we'll
				// call this method recursively
				// in case we started with something that wasn't part of a cycle but later traversed up to a
				// cycle.
				//
				if (++count > 100000) {
					return eContainer.eInternalResource();
				}
				if (eContainer instanceof FacadeObjectImpl) {
					result = ((FacadeObjectImpl)eContainer).eInternalDirectResource();
				} else {
					result = eContainer.eDirectResource();
				}
				if (result != null || eContainer == this) {
					break;
				}
			}
		}
		return result;
	}

	/**
	 * Overrides the superclass implementation to account for the delegation of the {@link #eDirectResource()}
	 * to the underlying model when the façade object is not attached to a resource of its own.
	 */
	@SuppressWarnings("all")
	@Override
	public NotificationChain eSetResource(Resource.Internal resource, NotificationChain notifications) {
		Resource.Internal oldResource = eInternalDirectResource();
		// When setting the resource to null we assume that detach has already been called in the resource
		// implementation
		//
		if (oldResource != null && resource != null) {
			notifications = ((InternalEList<?>)oldResource.getContents()).basicRemove(this, notifications);
			oldResource.detached(this);
		}
		InternalEObject oldContainer = eInternalContainer();
		if (oldContainer != null) {
			if (eContainmentFeature().isResolveProxies()) {
				Resource.Internal oldContainerResource = oldContainer.eInternalResource();
				if (oldContainerResource != null) {
					// If we're not setting a new resource, attach it to the old container's resource.
					if (resource == null) {
						oldContainerResource.attached(this);
					}
					// If we didn't detach it from an old resource already, detach it from the old container's
					// resource.
					//
					else if (oldResource == null) {
						oldContainerResource.detached(this);
					}
				}
			} else {
				notifications = eBasicRemoveFromContainer(notifications);
				notifications = eBasicSetContainer(null, -1, notifications);
			}
		}

		eSetDirectResource(resource);

		return notifications;
	}

	/**
	 * Overrides the superclass implementation to account for the delegation of the {@link #eDirectResource()}
	 * to the underlying model when the façade object is not attached to a resource of its own.
	 */
	@SuppressWarnings("all")
	@Override
	public NotificationChain eBasicSetContainer(InternalEObject newContainer, int newContainerFeatureID,
			NotificationChain msgs) {
		InternalEObject oldContainer = eInternalContainer();
		Resource.Internal oldResource = this.eInternalDirectResource();
		Resource.Internal newResource = null;
		if (oldResource != null) {
			if (newContainer != null
					&& !eContainmentFeature(this, newContainer, newContainerFeatureID).isResolveProxies()) {
				msgs = ((InternalEList<?>)oldResource.getContents()).basicRemove(this, msgs);
				eSetDirectResource(null);
				newResource = newContainer.eInternalResource();
			} else {
				oldResource = null;
			}
		} else {
			if (oldContainer != null) {
				oldResource = oldContainer.eInternalResource();
			}
			if (newContainer != null) {
				newResource = newContainer.eInternalResource();
			}
		}

		if (oldResource != newResource && oldResource != null) {
			oldResource.detached(this);
		}

		int oldContainerFeatureID = eContainerFeatureID();
		eBasicSetContainer(newContainer, newContainerFeatureID);

		if (oldResource != newResource && newResource != null) {
			newResource.attached(this);
		}

		if (eNotificationRequired()) {
			if (oldContainer != null && oldContainerFeatureID >= 0
					&& oldContainerFeatureID != newContainerFeatureID) {
				ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
						oldContainerFeatureID, oldContainer, null);
				if (msgs == null) {
					msgs = notification;
				} else {
					msgs.add(notification);
				}
			}
			if (newContainerFeatureID >= 0) {
				ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
						newContainerFeatureID,
						oldContainerFeatureID == newContainerFeatureID ? oldContainer : null, newContainer);
				if (msgs == null) {
					msgs = notification;
				} else {
					msgs.add(notification);
				}
			}
		}
		return msgs;
	}

	// CHECKSTYLE:ON

}
