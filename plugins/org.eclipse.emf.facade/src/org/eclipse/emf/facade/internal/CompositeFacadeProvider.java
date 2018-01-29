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
import org.eclipse.emf.facade.IFacadeProvider;

/**
 * A composite façade provider.
 *
 * @author Christian W. Damus
 */
public class CompositeFacadeProvider extends DelegatingFacadeProvider {
	/** The lower-priority provider that I delegate to for provision of façades. */
	private final IFacadeProvider next;

	/**
	 * Initializes me with my delegates.
	 * 
	 * @param delegate
	 *            my higher-priority delegate
	 * @param next
	 *            my lower-priority delegate
	 */
	protected CompositeFacadeProvider(IFacadeProvider delegate, IFacadeProvider next) {
		// Give the superclass constructor a supplier of composed providers
		super(delegate);

		this.next = next;
	}

	/**
	 * Obtains an optimal composition of two façade providers. If either is {@code null} or the
	 * {@link IFacadeProvider#NULL_PROVIDER NULL_PROVIDER}, then the result is the other (with a {@code null}
	 * coerced as the {@link IFacadeProvider#NULL_PROVIDER NULL_PROVIDER}).
	 * 
	 * @param provider1
	 *            a façade provider
	 * @param provider2
	 *            another façade provider
	 * @return the optimal composition of the two
	 */
	public static IFacadeProvider compose(IFacadeProvider provider1, IFacadeProvider provider2) {
		IFacadeProvider result;

		if ((provider1 == null) || (provider1 == NULL_PROVIDER)) {
			if (provider2 == null) {
				result = NULL_PROVIDER;
			} else {
				result = provider2;
			}
		} else if ((provider2 == null) || (provider2 == NULL_PROVIDER)) {
			// We already established that this is neither null nor the null-provider
			result = provider1;
		} else {
			result = new CompositeFacadeProvider(provider1, provider2);
		}

		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EObject createFacade(EObject underlyingObject) {
		EObject result = super.createFacade(underlyingObject);

		if (result == null) {
			result = next.createFacade(underlyingObject);
		}

		return result;
	}
}
