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
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * The core protocol for elements of a <em>Façade Model</em>.
 *
 * @author Christian W. Damus
 */
public interface FacadeObject extends EObject {

	/**
	 * <p>
	 * A literal null façade object, to be returned by a {@linkplain IFacadeProvider provider} that needs to
	 * assert that an object is in its domain but is not represented directly by a façade object. This
	 * prevents other façade providers from getting a chance to provide a façade for the object or even
	 * provide it as is.
	 * </p>
	 * <p>
	 * <strong>Note</strong> that this object is not a proper façade object: it has no adapter nor underlying
	 * object, so attempts to interact with it must account for this.
	 * </p>
	 */
	FacadeObject NULL = FacadeProxy.createProxy(EcoreFactory.eINSTANCE.createEObject());

	/**
	 * Obtains the model element underlying this façade, with which it is synchronized.
	 * 
	 * @return the underlying model element (never {@code null})
	 */
	EObject getUnderlyingElement();

	/**
	 * Obtains my associated façade adapter, if any.
	 * 
	 * @return my façade adapter or {@code null}
	 */
	default FacadeAdapter getFacadeAdapter() {
		return (FacadeAdapter)EcoreUtil.getExistingAdapter(this, FacadeObject.class);
	}

	/**
	 * <p>
	 * Obtains the resource in which the receiver is contained (directly or indirectly) or, if it is not in a
	 * resourcesas is commonly expected with façades, the resource in which its
	 * {@linkplain #getUnderlyingElement() underlying element} is contained. This ensures that the EMF Facade
	 * framework can trace the façade object to the appropriate resource in the comparison input for the
	 * purposes of grouping by resource and other resource-based calculations.
	 * </p>
	 * <p>
	 * Similar delegation to the underlying model is expected for implementation of the
	 * {@link InternalEObject#eDirectResource()} API. In this case it is even more important because this is
	 * used throughout the EMF Facade framework to determine whether an object is logically the root of a
	 * resource.
	 * </p>
	 * 
	 * @return the receiver's own actual containing resource or else the resource containing its
	 *         {@linkplain #getUnderlyingElement() underlying element}
	 * @see #getUnderlyingElement()
	 */
	@Override
	Resource eResource();
}
