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
package org.eclipse.emf.facade.uml2.tests.opaqexpr.internal.providers;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.facade.IFacadeProvider;
import org.eclipse.emf.facade.uml2.tests.opaqexpr.OpaqexprPackage;
import org.eclipse.emf.facade.uml2.tests.opaqexpr.internal.adapters.OpaqexprFacadeFactory;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.util.UMLUtil;

/**
 * The façade provider for {@link OpaqexprPackage opaqexpr}, a partial façade for opaque expressions in UML.
 *
 * @author Christian W. Damus
 */
public class OpaqexprFacadeProvider implements IFacadeProvider {

	private final OpaqexprFacadeFactory facadeFactory = new OpaqexprFacadeFactory();

	/**
	 * Initializes me.
	 */
	public OpaqexprFacadeProvider() {
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EObject createFacade(EObject underlyingObject) {
		EObject result = facadeFactory.doSwitch(underlyingObject);

		if (result == null) {
			if (isUMLObject(underlyingObject)) {
				// Return any other UML content as though it were a façade for itself
				result = underlyingObject;
			}
		}

		return result;
	}

	/**
	 * Queries whether an {@code object} is some kind of UML model content.
	 * 
	 * @param object
	 *            an object
	 * @return {@code true} if either is an UML {@link Element} or it is an application of an UML
	 *         {@link Stereotype}; {@code false}, otherwise
	 */
	protected boolean isUMLObject(EObject object) {
		return (object instanceof Element) || (UMLUtil.getStereotype(object) != null);
	}

	//
	// Nested types
	//

	public static class Factory extends IFacadeProvider.Factory.AbstractImpl {
		public Factory() {
			super(OpaqexprFacadeProvider::new);
		}

		@Override
		public boolean isFacadeProviderFactoryFor(ResourceSet resourceSet) {
			return hasUMLPackage(resourceSet);
		}

		protected boolean hasUMLPackage(Notifier notifier) {
			boolean result = false;

			if (notifier instanceof EObject) {
				result = isUMLPackage((EObject)notifier);
			} else if (notifier instanceof Resource) {
				result = ((Resource)notifier).getContents().stream().limit(1L).anyMatch(this::isUMLPackage);
			} else if (notifier instanceof ResourceSet) {
				result = ((ResourceSet)notifier).getResources().stream().anyMatch(this::hasUMLPackage);
			}

			return result;
		}

		protected boolean isUMLPackage(EObject object) {
			return (object instanceof org.eclipse.uml2.uml.Package);
		}
	}
}
