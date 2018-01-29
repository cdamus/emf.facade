/**
 * Copyright (c) 2017 Christian W. Damus and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Christian W. Damus - Initial API and implementation
 * 
 */
package org.eclipse.emf.facade.uml2.tests.j2ee;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Finder</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.emf.facade.uml2.tests.j2ee.Finder#getBean <em>Bean</em>}</li>
 * </ul>
 *
 * @see org.eclipse.emf.facade.uml2.tests.j2ee.J2EEPackage#getFinder()
 * @model
 * @generated
 */
public interface Finder extends NamedElement {
	/**
	 * Returns the value of the '<em><b>Bean</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link org.eclipse.emf.facade.uml2.tests.j2ee.Bean#getFinders <em>Finder</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Bean</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Bean</em>' reference.
	 * @see #setBean(Bean)
	 * @see org.eclipse.emf.facade.uml2.tests.j2ee.J2EEPackage#getFinder_Bean()
	 * @see org.eclipse.emf.facade.uml2.tests.j2ee.Bean#getFinders
	 * @model opposite="finder" required="true" ordered="false"
	 * @generated
	 */
	Bean getBean();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.facade.uml2.tests.j2ee.Finder#getBean <em>Bean</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Bean</em>' reference.
	 * @see #getBean()
	 * @generated
	 */
	void setBean(Bean value);

} // Finder
