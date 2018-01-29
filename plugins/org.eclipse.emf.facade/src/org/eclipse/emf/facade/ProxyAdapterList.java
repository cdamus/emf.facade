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

import java.util.List;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.util.DelegatingEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.facade.FacadeProxy.Impl;

/**
 * A wrapper for the list of adapters attached to façade proxies that exposes details of incoming
 * notifications in terms of façade proxies for consistency of object identity.
 *
 * @author Christian W. Damus
 */
final class ProxyAdapterList extends DelegatingEList<Adapter> {

	private static final long serialVersionUID = 1L;

	/** The internal façade proxy implementation that forwards notifications through me. */
	private final Impl owner;

	/** The backing list. */
	private final EList<Adapter> delegate;

	/**
	 * Initializes me with my backing list.
	 * 
	 * @param owner
	 *            the internal façade proxy implementation
	 * @param delegate
	 *            my backing list
	 */
	ProxyAdapterList(Impl owner, EList<Adapter> delegate) {
		super();

		this.owner = owner;
		this.delegate = delegate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected List<Adapter> delegateList() {
		return delegate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected int delegateIndexOf(Object object) {
		if (object instanceof Adapter) {
			List<Adapter> list = delegateList();

			for (int i = 0; i < list.size(); i++) {
				Adapter next = list.get(i);

				if (next.equals(object)) {
					return i;
				}

				// Maybe it's wrapped
				if (next instanceof ProxyAdapter) {
					next = ((ProxyAdapter)next).getDelegate();
				}
				if (next.equals(object)) {
					return i;
				}
			}
		}

		return super.delegateIndexOf(object);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Adapter resolve(int index, Adapter object) {
		if (object instanceof ProxyAdapter) {
			return ((ProxyAdapter)object).getDelegate();
		}
		return object;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Adapter validate(int index, Adapter object) {
		return new ProxyAdapter(owner, object);
	}

}
