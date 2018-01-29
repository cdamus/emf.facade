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

import static org.eclipse.emf.ecore.util.EcoreUtil.getAdapterFactory;
import static org.eclipse.emf.ecore.util.EcoreUtil.getExistingAdapter;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.facade.FacadeObject;
import org.eclipse.emf.facade.IFacadeProvider;

/**
 * Miscellaneous utilities for façades.
 *
 * @author Christian W. Damus
 */
public final class FacadeUtil {

	/**
	 * Not instantiable by clients.
	 */
	private FacadeUtil() {
		super();
	}

	/**
	 * Obtains the façade provider that should be used to obtain façade elements in the given resource set.
	 * 
	 * @param resourceSet
	 *            a resource set
	 * @return its façade provider, or the {@code IFacadeProvider#NULL_PROVIDER null} provider if none (never
	 *         actually {@code null})
	 */
	public static IFacadeProvider getFacadeProvider(ResourceSet resourceSet) {
		IFacadeProvider result = null;

		Adapter adapter = getExistingAdapter(resourceSet, IFacadeProvider.class);
		if (adapter == null) {
			AdapterFactory factory = getAdapterFactory(resourceSet.getAdapterFactories(),
					IFacadeProvider.class);
			if (factory != null) {
				adapter = factory.adaptNew(resourceSet, IFacadeProvider.class);
			}
		} // TODO Pluggable support for AdapterFactoryEditingDomain

		if (adapter instanceof IFacadeProvider) {
			result = (IFacadeProvider)adapter;
		}

		if (result == null) {
			result = IFacadeProvider.NULL_PROVIDER;
		}

		return result;
	}

	/**
	 * Obtain the façade for an {@code object} or else {@code null}.
	 * 
	 * @param provider
	 *            the façade provider to use
	 * @param object
	 *            an object
	 * @return its corresponding façade or {@code null} if none
	 */
	public static EObject facade(IFacadeProvider provider, EObject object) {
		return facadeElse(provider, object, null);
	}

	/**
	 * Obtain the façade for an {@code object} or else just the original {@code object}, itself.
	 * 
	 * @param provider
	 *            the façade provider to use
	 * @param object
	 *            an object
	 * @return its corresponding façade or the {@code object} if none
	 */
	public static EObject facadeElse(IFacadeProvider provider, EObject object) {
		return facadeElse(provider, object, object);
	}

	/**
	 * Obtain the façade for an {@code object} or else a default result.
	 * 
	 * @param provider
	 *            the façade provider to use
	 * @param object
	 *            an object
	 * @param defaultResult
	 *            the return result in case the {@code object} has no corresponding façade
	 * @return its corresponding façade or the {@code defaultResult} if none
	 */
	public static EObject facadeElse(IFacadeProvider provider, EObject object, EObject defaultResult) {
		EObject result = defaultResult;

		EObject facade = provider.createFacade(object);
		if ((facade != FacadeObject.NULL) && (facade != null)) {
			result = facade;
		}

		return result;
	}
}
