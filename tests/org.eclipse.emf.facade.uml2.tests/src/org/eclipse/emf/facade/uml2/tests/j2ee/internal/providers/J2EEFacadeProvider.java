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
package org.eclipse.emf.facade.uml2.tests.j2ee.internal.providers;

import com.google.common.collect.Sets;

import java.util.Set;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.facade.FacadeObject;
import org.eclipse.emf.facade.IFacadeProvider;
import org.eclipse.emf.facade.uml2.tests.j2ee.internal.adapters.J2EEFacadeFactory;
import org.eclipse.emf.facade.uml2.tests.j2eeprofile.J2EEProfilePackage;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.util.UMLUtil;

/**
 * The façade provider for J2EE in UML.
 *
 * @author Christian W. Damus
 */
public class J2EEFacadeProvider implements IFacadeProvider {
	private final J2EEFacadeFactory facadeFactory = new J2EEFacadeFactory();

	/** J2EE packages encountered by this façade provider. */
	private final Set<org.eclipse.uml2.uml.Package> j2eePackages = Sets.newHashSet();

	/**
	 * Initializes me.
	 */
	public J2EEFacadeProvider() {
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EObject createFacade(EObject underlyingObject) {
		EObject result = facadeFactory.doSwitch(underlyingObject);

		// If the object is an application of a J2EE stereotype, it is in our domain
		// but has no façade of its own. So, block other providers
		if ((result == null) && isStereotypeApplicationInJ2EEPackage(underlyingObject)) {
			result = FacadeObject.NULL;
		} else if (underlyingObject instanceof org.eclipse.uml2.uml.Package) {
			// It's a J2EE package
			j2eePackages.add((org.eclipse.uml2.uml.Package)underlyingObject);
		}

		return result;
	}

	protected boolean isStereotypeApplicationInJ2EEPackage(EObject object) {
		// Easy case when it is a J2EE stereotype application
		boolean result = (object.eClass().getEPackage() == J2EEProfilePackage.eINSTANCE);

		if (!result && !(object instanceof Element)) {
			Element base = UMLUtil.getBaseElement(object);
			result = (base != null) && isInJ2EEPackage(base);
		}

		return result;
	}

	protected boolean isInJ2EEPackage(Element element) {
		org.eclipse.uml2.uml.Package package_ = element.getNearestPackage();

		return (package_ != null) && isJ2EEPackage(package_);
	}

	protected boolean isJ2EEPackage(org.eclipse.uml2.uml.Package package_) {
		boolean result = j2eePackages.contains(package_);

		if (!result) {
			// Maybe a containing package is. Be careful to account for Components
			// in the containment tree
			Element owner = package_.getOwner();
			if (owner != null) {
				org.eclipse.uml2.uml.Package nesting = owner.getNearestPackage();
				result = isJ2EEPackage(nesting) && j2eePackages.add(package_); // Cache result
			}
		}

		return result;
	}

	//
	// Nested types
	//

	public static class Factory extends IFacadeProvider.Factory.AbstractImpl {
		public Factory() {
			super(J2EEFacadeProvider::new);
		}

		@Override
		public boolean isFacadeProviderFactoryFor(ResourceSet resourceSet) {
			return hasJ2EEPackage(resourceSet);
		}

		protected boolean hasJ2EEPackage(Notifier notifier) {
			boolean result = false;

			if (notifier instanceof EObject) {
				result = isJ2EEPackage((EObject)notifier);
			} else if (notifier instanceof Resource) {
				result = ((Resource)notifier).getContents().stream().limit(1L).anyMatch(this::isJ2EEPackage);
			} else if (notifier instanceof ResourceSet) {
				result = ((ResourceSet)notifier).getResources().stream().anyMatch(this::hasJ2EEPackage);
			}

			return result;
		}

		protected boolean isJ2EEPackage(EObject object) {
			return (object instanceof org.eclipse.uml2.uml.Package)
					&& J2EEFacadeFactory.isJ2EEPackage((org.eclipse.uml2.uml.Package)object);
		}
	}
}
