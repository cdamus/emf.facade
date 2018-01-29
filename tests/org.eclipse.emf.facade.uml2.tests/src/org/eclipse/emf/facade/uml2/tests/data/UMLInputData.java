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
package org.eclipse.emf.facade.uml2.tests.data;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.UMLPackage;

/**
 * This is the {@code UMLInputData} type. Enjoy.
 *
 * @author Christian W. Damus
 */
@SuppressWarnings("nls")
public class UMLInputData extends AbstractUMLInputData {

	/**
	 * Initializes me.
	 */
	public UMLInputData() {
		super();
	}

	/**
	 * Obtains the UML package contained in a {@code resource}.
	 * 
	 * @param resource
	 *            a resource
	 * @return its UML package, or {@code null} if none
	 */
	public org.eclipse.uml2.uml.Package getPackage(Resource resource) {
		return (org.eclipse.uml2.uml.Package)EcoreUtil.getObjectByType(resource.getContents(),
				UMLPackage.Literals.PACKAGE);
	}

	public Resource getA1() {
		return loadFromClassLoader("a1.uml");
	}

	public Resource getm1() {
		return loadFromClassLoader("m1.uml");
	}

	public Resource getM2() {
		return loadFromClassLoader("m2.uml");
	}

	public Resource getO1() {
		return loadFromClassLoader("o1.uml");
	}

	public Resource getO2() {
		return loadFromClassLoader("o2.uml");
	}

	public Resource getO3() {
		return loadFromClassLoader("o3.uml");
	}

	public Resource getO4() {
		return loadFromClassLoader("o4.uml");
	}

	public Resource getU1() {
		return loadFromClassLoader("u1.uml");
	}
}
