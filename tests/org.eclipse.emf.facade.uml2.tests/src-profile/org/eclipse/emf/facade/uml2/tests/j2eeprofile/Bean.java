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
package org.eclipse.emf.facade.uml2.tests.j2eeprofile;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Bean</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.emf.facade.uml2.tests.j2eeprofile.Bean#getBase_Class <em>Base Class</em>}</li>
 *   <li>{@link org.eclipse.emf.facade.uml2.tests.j2eeprofile.Bean#getKind <em>Kind</em>}</li>
 * </ul>
 *
 * @see org.eclipse.emf.facade.uml2.tests.j2eeprofile.J2EEProfilePackage#getBean()
 * @model
 * @generated
 */
public interface Bean extends EObject {
	/**
	 * Returns the value of the '<em><b>Base Class</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Base Class</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Class</em>' reference.
	 * @see #setBase_Class(org.eclipse.uml2.uml.Class)
	 * @see org.eclipse.emf.facade.uml2.tests.j2eeprofile.J2EEProfilePackage#getBean_Base_Class()
	 * @model required="true" ordered="false"
	 *        annotation="http://schema.omg.org/spec/MOF/2.0/emof.xml#Property.oppositeRoleName body='extension_Bean'"
	 * @generated
	 */
	org.eclipse.uml2.uml.Class getBase_Class();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.facade.uml2.tests.j2eeprofile.Bean#getBase_Class <em>Base Class</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Class</em>' reference.
	 * @see #getBase_Class()
	 * @generated
	 */
	void setBase_Class(org.eclipse.uml2.uml.Class value);

	/**
	 * Returns the value of the '<em><b>Kind</b></em>' attribute.
	 * The literals are from the enumeration {@link org.eclipse.emf.facade.uml2.tests.j2eeprofile.BeanKind}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Kind</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Kind</em>' attribute.
	 * @see org.eclipse.emf.facade.uml2.tests.j2eeprofile.BeanKind
	 * @see #setKind(BeanKind)
	 * @see org.eclipse.emf.facade.uml2.tests.j2eeprofile.J2EEProfilePackage#getBean_Kind()
	 * @model required="true" ordered="false"
	 * @generated
	 */
	BeanKind getKind();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.facade.uml2.tests.j2eeprofile.Bean#getKind <em>Kind</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Kind</em>' attribute.
	 * @see org.eclipse.emf.facade.uml2.tests.j2eeprofile.BeanKind
	 * @see #getKind()
	 * @generated
	 */
	void setKind(BeanKind value);

} // Bean
