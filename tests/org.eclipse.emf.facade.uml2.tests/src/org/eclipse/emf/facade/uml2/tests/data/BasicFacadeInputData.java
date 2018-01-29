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

/**
 * This is the {@code BasicFacadeInputData} type. Enjoy.
 *
 * @author Christian W. Damus
 */
@SuppressWarnings("nls")
public class BasicFacadeInputData extends AbstractFacadeInputData {

	/**
	 * Initializes me.
	 */
	public BasicFacadeInputData() {
		super();
	}

	public Resource getA1() {
		return loadFacadeFromClassLoader("a1.uml");
	}

	public Resource getA2() {
		return loadFacadeFromClassLoader("a2.uml");
	}

	public Resource getA3() {
		return loadFacadeFromClassLoader("a3.uml");
	}

	public Resource getB1() {
		return loadFacadeFromClassLoader("b1.uml");
	}

	public Resource getB2() {
		return loadFacadeFromClassLoader("b2.uml");
	}

	public Resource getF1() {
		return loadFacadeFromClassLoader("f1.uml");
	}
}
