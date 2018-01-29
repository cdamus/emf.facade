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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * A delegating scalar feature setting that wraps elements as dynamic façades proxies.
 *
 * @author Christian W. Damus
 */
final class ProxySetting implements EStructuralFeature.Setting {

	/** My owner, as a proxy. */
	private final FacadeObject owner;

	/** My delegate. */
	private final EStructuralFeature.Setting delegate;

	/** Am I a setting for a reference? */
	private final boolean isReference;

	/**
	 * Initializes me with the real setting to delegate to.
	 * 
	 * @param owner
	 *            my owner, as a proxy
	 * @param delegate
	 *            my delegate
	 */
	ProxySetting(FacadeObject owner, EStructuralFeature.Setting delegate) {
		super();

		this.owner = owner;
		this.delegate = delegate;
		this.isReference = delegate.getEStructuralFeature() instanceof EReference;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EObject getEObject() {
		return owner;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EStructuralFeature getEStructuralFeature() {
		return delegate.getEStructuralFeature();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object get(boolean resolve) {
		Object result = delegate.get(resolve);

		if (isReference && (result != null)) {
			result = wrap((EObject)result);
		}

		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void set(Object newValue) {
		if (isReference && (newValue instanceof EObject)) {
			delegate.set(FacadeProxy.unwrap((EObject)newValue));
		} else {
			delegate.set(newValue);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSet() {
		return delegate.isSet();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void unset() {
		delegate.unset();
	}

	/**
	 * Wrap an {@code object} in the setting as a dynamic façade proxy.
	 * 
	 * @param object
	 *            an object
	 * @return its dynamic façade proxy wrapper
	 */
	EObject wrap(EObject object) {
		if (!FacadeAdapter.isFacade(object)) {
			return object;
		} else {
			return FacadeProxy.createProxy(object);
		}
	}

}
