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
import org.eclipse.emf.common.notify.NotificationWrapper;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.facade.FacadeProxy.Impl;

/**
 * A wrapper for notifications sent to proxy adapter that exposes details in terms of the façade proxies.
 *
 * @author Christian W. Damus
 */
final class ProxyNotification extends NotificationWrapper {

	/** The internal façade proxy implementation that forwards me. */
	private final Impl impl;

	/**
	 * Initializes me.
	 * 
	 * @param impl
	 *            the internal façade proxy implementation
	 * @param notification
	 *            the notification to wrap
	 */
	private ProxyNotification(Impl impl, Notification notification) {
		super(impl.getProxy(), notification);

		this.impl = impl;
	}

	/**
	 * Wraps a notification for façade proxies.
	 * 
	 * @param impl
	 *            the internal façade proxy implementation
	 * @param notification
	 *            the notification to wrap
	 * @return the notification wrapper
	 */
	static Notification wrap(Impl impl, Notification notification) {
		return new ProxyNotification(impl, notification);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getNewValue() {
		Object result = notification.getNewValue();
		if (getFeature() instanceof EReference) {
			result = impl.wrapEObjects(result);
		}

		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getOldValue() {
		Object result = notification.getOldValue();
		if (getFeature() instanceof EReference) {
			result = impl.wrapEObjects(result);
		} else if (getEventType() == REMOVING_ADAPTER) {
			// Unwrap the adapter
			if (result instanceof ProxyAdapter) {
				result = ((ProxyAdapter)result).getDelegate();
			}
		}

		return result;
	}
}
