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

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.facade.IFacadeProvider;

/**
 * A factory that delegates to another for creation of façade providers.
 *
 * @author Christian W. Damus
 */
public class CompositeFacadeProviderFactory extends DelegatingFacadeProviderFactory {
	/** The lower-priority factory that I delegate to for creation of the provider. */
	private final IFacadeProvider.Factory next;

	/**
	 * Initializes me with my delegates.
	 * 
	 * @param delegate
	 *            my higher-priority delegate
	 * @param next
	 *            my lower-priority delegate
	 */
	protected CompositeFacadeProviderFactory(IFacadeProvider.Factory delegate, IFacadeProvider.Factory next) {
		// Give the superclass constructor a supplier of composed providers
		super(delegate, () -> delegate.getFacadeProvider().compose(next.getFacadeProvider()));

		this.next = next;
	}

	/**
	 * Obtains an optimal composition of two façade provider factories. If either is {@code null} or the
	 * {@link IFacadeProvider.Factory#NULL_FACTORY NULL_FACTORY}, then the result is the other (with a
	 * {@code null} coerced as the {@link IFacadeProvider.Factory#NULL_FACTORY NULL_FACTORY}).
	 * 
	 * @param factory1
	 *            a factory
	 * @param factory2
	 *            another factory
	 * @return the optimal composition of the two
	 */
	public static IFacadeProvider.Factory compose(IFacadeProvider.Factory factory1,
			IFacadeProvider.Factory factory2) {

		IFacadeProvider.Factory result;

		if ((factory1 == null) || (factory1 == NULL_FACTORY)) {
			if (factory2 == null) {
				result = NULL_FACTORY;
			} else {
				result = factory2;
			}
		} else if ((factory2 == null) || (factory2 == NULL_FACTORY)) {
			// We already established that this is neither null nor the null-factory
			result = factory1;
		} else if (factory2.getRanking() > factory1.getRanking()) {
			result = new CompositeFacadeProviderFactory(factory2, factory1);
		} else {
			result = new CompositeFacadeProviderFactory(factory1, factory2);
		}

		return result;
	}

	@Override
	public boolean isFacadeProviderFactoryFor(ResourceSet resourceSet) {
		return super.isFacadeProviderFactoryFor(resourceSet) || next.isFacadeProviderFactoryFor(resourceSet);
	}

	@Override
	public boolean isFacadeProviderFactoryFor(Notifier notifier) {
		return super.isFacadeProviderFactoryFor(notifier) || next.isFacadeProviderFactoryFor(notifier);
	}
}
