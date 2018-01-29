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
package org.eclipse.emf.facade.internal;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.facade.FacadeAdapter;
import org.eclipse.emf.facade.FacadeObject;
import org.eclipse.emf.facade.FacadeProxy;
import org.eclipse.emf.facade.IFacadeProvider;

/**
 * A delegating façade provider.
 *
 * @author Christian W. Damus
 */
class DelegatingFacadeProvider implements IFacadeProvider {

	/** My delegate that provides façades for me. */
	private final IFacadeProvider delegate;

	/**
	 * Initializes me with my delegate.
	 * 
	 * @param delegate
	 *            my delegate
	 */
	DelegatingFacadeProvider(IFacadeProvider delegate) {
		super();

		this.delegate = delegate;
	}

	/**
	 * Obtains a façade provider, based on the given {@code delegate}, that ensures façade objects provided
	 * will implement the {@link FacadeObject} interface, using dynamic proxies if necessary. This is
	 * optimized for special cases of providers that are known not to provide façades needing this treatment
	 * (for example, because they are known not to provide any façades at all).
	 * 
	 * @param delegate
	 *            the real façade provider
	 * @return the {@link FacadeObject}-ensuring delegating provider
	 */
	static IFacadeProvider ensuringFacadeObjects(IFacadeProvider delegate) {
		/**
		 * A delegating provider that uses dynamic proxies if necessary to ensure {@link FacadeObject}s.
		 *
		 * @author Christian W. Damus
		 */
		class Proxifier extends DelegatingFacadeProvider {
			/**
			 * Initializes me with a delegate that does not provide proxies.
			 * 
			 * @param noProxies
			 *            the non-proxy-providing delegate
			 */
			Proxifier(IFacadeProvider noProxies) {
				super(noProxies);
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public EObject createFacade(EObject underlyingObject) {
				EObject result = delegateCreateFacade(underlyingObject);

				// Note that the FacadeObject.NULL is a FacadeObject
				if ((FacadeAdapter.isFacade(result)) && !(result instanceof FacadeObject)) {
					result = FacadeProxy.createProxy(result);
				}

				return result;
			}
		}

		IFacadeProvider result;

		if (delegate == null) {
			result = NULL_PROVIDER;
		} else if (delegate == NULL_PROVIDER) {
			result = delegate; // No need for proxies
		} else if (delegate instanceof Proxifier) {
			result = delegate; // Already ensuring proxies
		} else if (EMFFacadePlugin.isUseDynamicProxies()) {
			result = new Proxifier(delegate);
		} else {
			result = delegate;
		}

		return result;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EObject createFacade(EObject underlyingObject) {
		return delegateCreateFacade(underlyingObject);
	}

	/**
	 * Delegates creation of a façade.
	 * 
	 * @param underlyingObject
	 *            the underlying object for which to create a façade
	 * @return the façade as provided by my delegate
	 */
	protected EObject delegateCreateFacade(EObject underlyingObject) {
		return delegate.createFacade(underlyingObject);
	}
}
