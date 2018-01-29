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

import com.google.common.base.Supplier;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.facade.FacadeObject;
import org.eclipse.emf.facade.IFacadeProvider;

/**
 * A factory that delegates to another for creation of façade providers.
 *
 * @author Christian W. Damus
 */
public class DelegatingFacadeProviderFactory extends IFacadeProvider.Factory.AbstractImpl {
	/** The factory that I delegate to for creation of the provider. */
	private final IFacadeProvider.Factory delegate;

	/**
	 * Initializes me with my delegate and a supplier that can create the façade provider when it is required.
	 * 
	 * @param delegate
	 *            my delegate
	 * @param facadeProviderSupplier
	 *            the supplier of the façade provider, or {@code null} if the subclass overrides the
	 *            {@link #createProvider()} method
	 */
	protected DelegatingFacadeProviderFactory(IFacadeProvider.Factory delegate,
			Supplier<? extends IFacadeProvider> facadeProviderSupplier) {

		super(delegate.getRanking(), facadeProviderSupplier);

		this.delegate = delegate;
	}

	/**
	 * Initializes me with my delegate.
	 * 
	 * @param delegate
	 *            my delegate
	 */
	protected DelegatingFacadeProviderFactory(IFacadeProvider.Factory delegate) {
		this(delegate, delegate::getFacadeProvider);
	}

	/**
	 * Obtains a façade provider factory, based on the given {@code delegate}, that ensures façade objects
	 * provided will implement the {@link FacadeObject} interface, using dynamic proxies if necessary. This is
	 * optimized for special cases of providers that are known not to provide façades needing this treatment
	 * (for example, because they are known not to provide any façades at all).
	 * 
	 * @param delegate
	 *            the real façade provider factory
	 * @return the {@link FacadeObject}-ensuring delegating factory
	 */
	public static IFacadeProvider.Factory ensuringFacadeObjects(IFacadeProvider.Factory delegate) {
		/**
		 * A delegating factory that creates providers that use dynamic proxies if necessary to ensure
		 * {@link FacadeObject}s.
		 *
		 * @author Christian W. Damus
		 */
		class Proxifier extends DelegatingFacadeProviderFactory {
			/**
			 * Initializes me with a delegate that does not provide proxies.
			 * 
			 * @param noProxies
			 *            the non-proxy-providing delegate
			 */
			Proxifier(IFacadeProvider.Factory noProxies) {
				super(noProxies,
						() -> DelegatingFacadeProvider.ensuringFacadeObjects(noProxies.getFacadeProvider()));
			}
		}

		IFacadeProvider.Factory result;

		if (delegate == null) {
			result = NULL_FACTORY;
		} else if (delegate == NULL_FACTORY) {
			result = delegate; // No need for proxies
		} else if (delegate instanceof Proxifier) {
			result = delegate; // Already ensuring proxies
		} else {
			result = new Proxifier(delegate);
		}

		return result;
	}

	@Override
	public boolean isFacadeProviderFactoryFor(ResourceSet resourceSet) {
		return delegate.isFacadeProviderFactoryFor(resourceSet);
	}

	@Override
	public boolean isFacadeProviderFactoryFor(Notifier notifier) {
		return delegate.isFacadeProviderFactoryFor(notifier);
	}
}
