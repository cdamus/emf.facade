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

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.facade.FacadeProxy.Impl;

/**
 * A wrapper for adapters attached to façade proxies that exposes details of incoming notifications in terms
 * of façade proxies for consistency of object identity.
 *
 * @author Christian W. Damus
 */
final class ProxyAdapter implements Adapter {
	/** The internal façade proxy implementation that forwards notifications to me. */
	private final Impl impl;

	/** The adapter to which to forward notifications. */
	private final Adapter delegate;

	/**
	 * Initializes me.
	 * 
	 * @param impl
	 *            the internal façade proxy implementation
	 * @param delegate
	 *            the adapter to which to forward notifications
	 */
	ProxyAdapter(Impl impl, Adapter delegate) {
		super();

		this.impl = impl;
		this.delegate = delegate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Notifier getTarget() {
		return impl.getProxy();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTarget(Notifier newTarget) {
		delegate.setTarget(FacadeProxy.unwrap((EObject)newTarget));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAdapterForType(Object type) {
		return delegate.isAdapterForType(type);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void notifyChanged(Notification notification) {
		delegate.notifyChanged(ProxyNotification.wrap(impl, notification));
	}

	/**
	 * Acceses the adapter to which I forward notifications.
	 * 
	 * @return my delegate
	 */
	Adapter getDelegate() {
		return delegate;
	}
}
