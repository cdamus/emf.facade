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

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.facade.SyncDirectionKind;
import org.eclipse.emf.facade.uml2.UMLFacadeResourceAdapter;
import org.eclipse.emf.facade.uml2.tests.j2ee.Bean;
import org.eclipse.emf.facade.uml2.tests.j2ee.Finder;
import org.eclipse.emf.facade.uml2.tests.j2ee.HomeInterface;
import org.eclipse.emf.facade.uml2.tests.j2ee.J2EEPackage;
import org.eclipse.emf.facade.uml2.tests.j2ee.NamedElement;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.Usage;

/**
 * Stereotype applications adapter on UML resources for the J2EE façade.
 *
 * @author Christian W. Damus
 */
public class J2EEFacadeResourceAdapter extends UMLFacadeResourceAdapter {

	/**
	 * Initialize me.
	 */
	public J2EEFacadeResourceAdapter() {
		super();
	}

	/**
	 * Gets the existing adapter instance of the given {@code type} attached to a {@coed resource}, or else
	 * creates it.
	 * 
	 * @param resource
	 *            a UML model resource
	 * @param type
	 *            the type of adapter to obtain
	 * @return the adapter, created lazily if necessary
	 * @param <T>
	 *            the adapter type
	 */
	public static J2EEFacadeResourceAdapter getInstance(Resource resource) {
		return getInstance(resource, J2EEFacadeResourceAdapter.class);
	}

	/**
	 * Creates the bean façade, if any, for a class.
	 * 
	 * @param class_
	 *            an UML class
	 * @param bean
	 *            a bean stereotype application attached to it
	 * @return the bean façade
	 */
	public Bean createFacade(org.eclipse.uml2.uml.Class class_,
			org.eclipse.emf.facade.uml2.tests.j2eeprofile.Bean bean) {

		return J2EEFacadeFactory.create(class_);
	}

	/**
	 * Creates the home-interface façade, if any, for an interface.
	 * 
	 * @param interface_
	 *            an UML interface
	 * @param homeInterface
	 *            a home-interface stereotype application attached to it
	 * @return the home-interface façade
	 */
	public HomeInterface createFacade(Interface interface_,
			org.eclipse.emf.facade.uml2.tests.j2eeprofile.HomeInterface homeInterface) {

		return (HomeInterface)J2EEFacadeFactory.create(interface_);
	}

	/**
	 * Creates the finder façade, if any, for an interface.
	 * 
	 * @param interface_
	 *            an UML interface
	 * @param finder
	 *            a finder stereotype application attached to it
	 * @return the finder façade
	 */
	public Finder createFacade(Interface interface_,
			org.eclipse.emf.facade.uml2.tests.j2eeprofile.Finder finder) {

		return (Finder)J2EEFacadeFactory.create(interface_);
	}

	/**
	 * Creates the home or interface finder façade, if any, for the client of a usage.
	 * 
	 * @param usage
	 *            an UML usage
	 * @param create
	 *            a create stereotype application attached to it
	 * @return the home or finder façade
	 */
	public NamedElement createFacade(Usage usage, org.eclipse.uml2.uml.profile.standard.Create create) {
		NamedElement result = null;

		// See if it's a finder interface usage
		Interface interface_ = null;
		if (!usage.getClients().isEmpty()) {
			interface_ = (Interface)EcoreUtil.getObjectByType(usage.getClients(),
					UMLPackage.Literals.INTERFACE);
		}

		if (interface_ != null) {
			result = J2EEFacadeFactory.create(interface_);
			if (result != null) {
				// Synchronize its bean-ness
				if (result instanceof Finder) {
					FinderAdapter.get(result).initialSync(SyncDirectionKind.TO_FACADE,
							J2EEPackage.Literals.FINDER__BEAN);
				} else if (result instanceof HomeInterface) {
					HomeInterfaceAdapter.get(result).initialSync(SyncDirectionKind.TO_FACADE,
							J2EEPackage.Literals.HOME_INTERFACE__BEAN);
				}
			}
		}

		return result;
	}
}
