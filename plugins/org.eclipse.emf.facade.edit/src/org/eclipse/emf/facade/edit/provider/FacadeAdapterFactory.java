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
package org.eclipse.emf.facade.edit.provider;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.provider.DecoratorAdapterFactory;
import org.eclipse.emf.edit.provider.IItemProviderDecorator;
import org.eclipse.emf.facade.IFacadeProvider;

/**
 * Decorating adapter factory for façade support in the merge viewers.
 *
 * @author Christian W. Damus
 */
public class FacadeAdapterFactory extends DecoratorAdapterFactory {

	/** My façade provider. */
	private final IFacadeProvider facadeProvider;

	/**
	 * Initializes me with my delegate adapter factory and façade provider.
	 * 
	 * @param decoratedAdapterFactory
	 *            the real adapter factory
	 * @param facadeProvider
	 *            my façade provider
	 */
	public FacadeAdapterFactory(AdapterFactory decoratedAdapterFactory, IFacadeProvider facadeProvider) {
		super(decoratedAdapterFactory);

		this.facadeProvider = facadeProvider;
	}

	@Override
	protected IItemProviderDecorator createItemProviderDecorator(Object target, Object Type) {
		return new FacadeItemProvider(this, facadeProvider);
	}

}
