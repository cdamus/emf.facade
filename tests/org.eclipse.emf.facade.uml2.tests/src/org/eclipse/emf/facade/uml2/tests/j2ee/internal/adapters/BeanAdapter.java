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
import org.eclipse.emf.facade.uml2.tests.j2ee.Bean;
import org.eclipse.emf.facade.uml2.tests.j2ee.BeanKind;
import org.eclipse.uml2.uml.util.UMLUtil;

/**
 * Façade adapter for beans in a J2EE model.
 *
 * @author Christian W. Damus
 */
public class BeanAdapter extends NamedElementAdapter {

	/**
	 * Initializes me with a stereotype application.
	 * 
	 * @param facade
	 *            the bean façade
	 * @param beanClass
	 *            the UML model element
	 * @param stereotype
	 *            the J2EE Bean stereotype application
	 */
	BeanAdapter(Bean facade, org.eclipse.uml2.uml.Class beanClass,
			org.eclipse.emf.facade.uml2.tests.j2eeprofile.Bean stereotype) {

		super(facade, beanClass, stereotype);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAdapterForType(Object type) {
		return (type == Bean.class) || (type == BeanAdapter.class) //
				|| super.isAdapterForType(type);
	}

	@Override
	public Bean getFacade() {
		return (Bean)super.getFacade();
	}

	@Override
	public org.eclipse.uml2.uml.Class getUnderlyingElement() {
		return (org.eclipse.uml2.uml.Class)super.getUnderlyingElement();
	}

	@Override
	public org.eclipse.emf.facade.uml2.tests.j2eeprofile.Bean getStereotype() {
		return (org.eclipse.emf.facade.uml2.tests.j2eeprofile.Bean)super.getStereotype();
	}

	/**
	 * Ensures that the façade and its UML element are connected by an adapter.
	 * 
	 * @param facade
	 *            a bean façade
	 * @param beanClass
	 *            the UML model element
	 * @return the existing or new adapter
	 */
	static BeanAdapter connect(Bean facade, org.eclipse.uml2.uml.Class beanClass) {
		BeanAdapter result = null;

		org.eclipse.emf.facade.uml2.tests.j2eeprofile.Bean stereotype = UMLUtil
				.getStereotypeApplication(beanClass,
						org.eclipse.emf.facade.uml2.tests.j2eeprofile.Bean.class);

		if (stereotype != null) {
			result = connect(facade, beanClass, BeanAdapter.class,
					(f, m) -> new BeanAdapter(f, m, stereotype));
		}

		return result;
	}

	/**
	 * Obtains the adapter instance for some notifier.
	 * 
	 * @param notifier
	 *            a façade or UML model element
	 * @return the adapter, or {@code null}
	 */
	static BeanAdapter get(Notifier notifier) {
		return get(notifier, BeanAdapter.class);
	}

	/**
	 * Queries whether some {@code object} is a UML class that is stereotyped as a bean.
	 * 
	 * @param object
	 *            an object
	 * @return whether it is a bean class in the UML model
	 */
	static boolean isBeanClass(Object object) {
		return (object instanceof org.eclipse.uml2.uml.Class)
				&& (UMLUtil.getStereotypeApplication((org.eclipse.uml2.uml.Class)object,
						org.eclipse.emf.facade.uml2.tests.j2eeprofile.Bean.class) != null);

	}

	/**
	 * Synchronize the bean kind from the façade to the UML model.
	 * 
	 * @param facade
	 *            the façade
	 * @param model
	 *            the UML element
	 */
	public void syncKindToModel(Bean facade,
			org.eclipse.emf.facade.uml2.tests.j2eeprofile.Bean model) {

		if (!Objects.equals(facade.getKind().getLiteral(), model.getKind().getLiteral())) {
			model.setKind(org.eclipse.emf.facade.uml2.tests.j2eeprofile.BeanKind
					.get(facade.getKind().getLiteral()));
		}
	}

	/**
	 * Synchronize the bean kind from the UML model to the façade.
	 * 
	 * @param model
	 *            the UML element
	 * @param facade
	 *            the façade
	 */
	public void syncKindToFacade(org.eclipse.emf.facade.uml2.tests.j2eeprofile.Bean model,
			Bean facade) {

		if (!Objects.equals(facade.getKind().getLiteral(), model.getKind().getLiteral())) {
			facade.setKind(BeanKind.get(model.getKind().getLiteral()));
		}
	}
}
