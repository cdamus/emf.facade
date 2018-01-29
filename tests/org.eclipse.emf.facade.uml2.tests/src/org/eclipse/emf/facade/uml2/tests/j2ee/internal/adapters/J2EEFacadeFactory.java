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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.facade.SyncDirectionKind;
import org.eclipse.emf.facade.uml2.UMLFacadeAdapter;
import org.eclipse.emf.facade.uml2.tests.j2ee.Bean;
import org.eclipse.emf.facade.uml2.tests.j2ee.J2EEFactory;
import org.eclipse.emf.facade.uml2.tests.j2ee.NamedElement;
import org.eclipse.emf.facade.uml2.tests.j2eeprofile.J2EEProfilePackage;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.util.UMLSwitch;

/**
 * The factory for initialization of a J2EE façade from the profiled UML model.
 *
 * @author Christian W. Damus
 */
public class J2EEFacadeFactory extends UMLSwitch<EObject> {

	/**
	 * Initializes me.
	 */
	public J2EEFacadeFactory() {
		super();
	}

	/**
	 * Obtains the J2EE façade for a package in the UML model.
	 * 
	 * @param package_
	 *            a package in the UML model
	 * @return its façade, or {@code null} if the {@code package_} does not represent a J2EE package
	 */
	public static org.eclipse.emf.facade.uml2.tests.j2ee.Package create(
			org.eclipse.uml2.uml.Package package_) {

		org.eclipse.emf.facade.uml2.tests.j2ee.Package result = null;

		PackageAdapter adapter = PackageAdapter.get(package_);
		if (adapter == null) {
			if (isJ2EEPackage(package_)) {
				result = J2EEFactory.eINSTANCE.createPackage();
				PackageAdapter.connect(result, package_).initialSync(SyncDirectionKind.TO_FACADE);
			}
		} else {
			result = adapter.getFacade();
		}

		return result;
	}

	/**
	 * Obtains the J2EE bean façade for a class in the UML model.
	 * 
	 * @param bean
	 *            a class in the UML model that purports to be a bean
	 * @return its façade, or {@code null} if the {@code bean} does not represent a J2EE bean
	 */
	public static Bean create(org.eclipse.uml2.uml.Class bean) {
		Bean result = null;

		BeanAdapter adapter = BeanAdapter.get(bean);
		if (adapter == null) {
			// Ensure the contextual package
			org.eclipse.emf.facade.uml2.tests.j2ee.Package package_ = create(bean.getPackage());
			if (package_ != null) {
				PackageAdapter.get(package_).initialSync(SyncDirectionKind.TO_FACADE,
						UMLPackage.Literals.PACKAGE__PACKAGED_ELEMENT);

				// Now there will be an adapter if it's a real bean
				adapter = BeanAdapter.get(bean);
			}
		}

		if (adapter != null) {
			result = adapter.getFacade();
		}

		return result;
	}

	/**
	 * Obtains the J2EE home or finder interface façade for an interface in the UML model.
	 * 
	 * @param interface_
	 *            an interface in the UML model that purports to be a home-interface or a finder
	 * @return its façade, or {@code null} if the {@code interface_} does not represent a J2EE home or finder
	 *         interface
	 */
	public static NamedElement create(Interface interface_) {
		NamedElement result = null;

		UMLFacadeAdapter adapter = HomeInterfaceAdapter.get(interface_);
		if (adapter == null) {
			adapter = FinderAdapter.get(interface_);
		}

		if (adapter == null) {
			// Ensure the contextual package
			org.eclipse.emf.facade.uml2.tests.j2ee.Package package_ = create(interface_.getPackage());
			if (package_ != null) {
				PackageAdapter.get(package_).initialSync(SyncDirectionKind.TO_FACADE,
						UMLPackage.Literals.PACKAGE__PACKAGED_ELEMENT);

				// Now there will be an adapter if it's a real J2EE interface
				adapter = HomeInterfaceAdapter.get(interface_);
				if (adapter == null) {
					adapter = FinderAdapter.get(interface_);
				}
			}
		}

		if (adapter != null) {
			result = (NamedElement)adapter.getFacade();
		}

		return result;
	}

	/**
	 * Queries whether a package is a J2EE package.
	 * 
	 * @param package_
	 *            an UML package
	 * @return {@code true} if the package has the J2EE profile applied (directly or to a containing package);
	 *         {@code false}, otherwise
	 */
	public static boolean isJ2EEPackage(org.eclipse.uml2.uml.Package package_) {
		return package_.getAllAppliedProfiles().stream()
				.anyMatch(p -> J2EEProfilePackage.eNS_URI.equals(p.getURI()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EObject casePackage(Package object) {
		return create(object);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EObject caseClass(Class object) {
		return create(object);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EObject caseInterface(Interface object) {
		return create(object);
	}
}
