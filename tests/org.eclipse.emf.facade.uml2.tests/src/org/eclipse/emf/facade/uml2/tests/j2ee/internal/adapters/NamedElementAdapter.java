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
package org.eclipse.emf.facade.uml2.tests.j2ee.internal.adapters;

import java.util.Objects;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.facade.uml2.UMLFacadeAdapter;
import org.eclipse.emf.facade.uml2.tests.j2ee.NamedElement;

/**
 * Abstract façade adapter for named elements in a J2EE model.
 *
 * @author Christian W. Damus
 */
public abstract class NamedElementAdapter extends UMLFacadeAdapter {

	/**
	 * Initializes me.
	 * 
	 * @param facade
	 *            the J2EE façade
	 * @param umlElement
	 *            the UML model element
	 */
	public NamedElementAdapter(NamedElement facade, org.eclipse.uml2.uml.NamedElement umlElement) {
		super(facade, umlElement);
	}

	/**
	 * Initializes me with a stereotype application.
	 * 
	 * @param facade
	 *            the J2EE façade
	 * @param umlElement
	 *            the UML model element
	 * @param stereotype
	 *            the J2EE stereotype application
	 */
	public NamedElementAdapter(NamedElement facade, org.eclipse.uml2.uml.NamedElement umlElement,
			EObject stereotype) {

		super(facade, umlElement, stereotype);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAdapterForType(Object type) {
		return (type == NamedElement.class) || (type == NamedElementAdapter.class) //
				|| super.isAdapterForType(type);
	}

	@Override
	public NamedElement getFacade() {
		return (NamedElement)super.getFacade();
	}

	@Override
	public org.eclipse.uml2.uml.NamedElement getUnderlyingElement() {
		return (org.eclipse.uml2.uml.NamedElement)super.getUnderlyingElement();
	}

	/**
	 * Obtains the adapter instance for some notifier.
	 * 
	 * @param notifier
	 *            a façade or UML model element
	 * @return the adapter, or {@code null}
	 */
	static NamedElementAdapter get(Notifier notifier) {
		return get(notifier, NamedElementAdapter.class);
	}

	/**
	 * Synchronize the element name from the façade to the UML model.
	 * 
	 * @param facade
	 *            the façade
	 * @param model
	 *            the UML element
	 */
	public void syncNameToModel(NamedElement facade, org.eclipse.uml2.uml.NamedElement model) {
		if (!Objects.equals(facade.getName(), model.getName())) {
			if (facade.getName() == null) {
				model.unsetName();
			} else {
				model.setName(facade.getName());
			}
		}
	}

	/**
	 * Synchronize the element name from the UML model to the façade.
	 * 
	 * @param model
	 *            the UML element
	 * @param facade
	 *            the façade
	 */
	public void syncNameToFacade(org.eclipse.uml2.uml.NamedElement model, NamedElement facade) {
		if (!Objects.equals(model.getName(), facade.getName())) {
			facade.setName(model.getName());
		}
	}

}
