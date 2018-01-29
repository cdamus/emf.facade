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

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Bean</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.emf.facade.uml2.tests.j2ee.Bean#getKind <em>Kind</em>}</li>
 *   <li>{@link org.eclipse.emf.facade.uml2.tests.j2ee.Bean#isAbstract <em>Is Abstract</em>}</li>
 *   <li>{@link org.eclipse.emf.facade.uml2.tests.j2ee.Bean#getSuperclass <em>Superclass</em>}</li>
 *   <li>{@link org.eclipse.emf.facade.uml2.tests.j2ee.Bean#getHomeInterface <em>Home Interface</em>}</li>
 *   <li>{@link org.eclipse.emf.facade.uml2.tests.j2ee.Bean#getFinders <em>Finder</em>}</li>
 * </ul>
 *
 * @see org.eclipse.emf.facade.uml2.tests.j2ee.J2EEPackage#getBean()
 * @model
 * @generated
 */
public interface Bean extends NamedElement {
	/**
	 * Returns the value of the '<em><b>Kind</b></em>' attribute.
	 * The literals are from the enumeration {@link org.eclipse.emf.facade.uml2.tests.j2ee.BeanKind}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Kind</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Kind</em>' attribute.
	 * @see org.eclipse.emf.facade.uml2.tests.j2ee.BeanKind
	 * @see #setKind(BeanKind)
	 * @see org.eclipse.emf.facade.uml2.tests.j2ee.J2EEPackage#getBean_Kind()
	 * @model required="true" ordered="false"
	 * @generated
	 */
	BeanKind getKind();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.facade.uml2.tests.j2ee.Bean#getKind <em>Kind</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Kind</em>' attribute.
	 * @see org.eclipse.emf.facade.uml2.tests.j2ee.BeanKind
	 * @see #getKind()
	 * @generated
	 */
	void setKind(BeanKind value);

	/**
	 * Returns the value of the '<em><b>Is Abstract</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Is Abstract</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Is Abstract</em>' attribute.
	 * @see #setIsAbstract(boolean)
	 * @see org.eclipse.emf.facade.uml2.tests.j2ee.J2EEPackage#getBean_IsAbstract()
	 * @model dataType="org.eclipse.uml2.types.Boolean" required="true" ordered="false"
	 * @generated
	 */
	boolean isAbstract();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.facade.uml2.tests.j2ee.Bean#isAbstract <em>Is Abstract</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Is Abstract</em>' attribute.
	 * @see #isAbstract()
	 * @generated
	 */
	void setIsAbstract(boolean value);

	/**
	 * Returns the value of the '<em><b>Superclass</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Superclass</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Superclass</em>' reference.
	 * @see #setSuperclass(Bean)
	 * @see org.eclipse.emf.facade.uml2.tests.j2ee.J2EEPackage#getBean_Superclass()
	 * @model ordered="false"
	 * @generated
	 */
	Bean getSuperclass();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.facade.uml2.tests.j2ee.Bean#getSuperclass <em>Superclass</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Superclass</em>' reference.
	 * @see #getSuperclass()
	 * @generated
	 */
	void setSuperclass(Bean value);

	/**
	 * Returns the value of the '<em><b>Home Interface</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link org.eclipse.emf.facade.uml2.tests.j2ee.HomeInterface#getBean <em>Bean</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Home Interface</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Home Interface</em>' reference.
	 * @see #setHomeInterface(HomeInterface)
	 * @see org.eclipse.emf.facade.uml2.tests.j2ee.J2EEPackage#getBean_HomeInterface()
	 * @see org.eclipse.emf.facade.uml2.tests.j2ee.HomeInterface#getBean
	 * @model opposite="bean" ordered="false"
	 * @generated
	 */
	HomeInterface getHomeInterface();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.facade.uml2.tests.j2ee.Bean#getHomeInterface <em>Home Interface</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Home Interface</em>' reference.
	 * @see #getHomeInterface()
	 * @generated
	 */
	void setHomeInterface(HomeInterface value);

	/**
	 * Returns the value of the '<em><b>Finder</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.emf.facade.uml2.tests.j2ee.Finder}.
	 * It is bidirectional and its opposite is '{@link org.eclipse.emf.facade.uml2.tests.j2ee.Finder#getBean <em>Bean</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Finder</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Finder</em>' reference list.
	 * @see org.eclipse.emf.facade.uml2.tests.j2ee.J2EEPackage#getBean_Finder()
	 * @see org.eclipse.emf.facade.uml2.tests.j2ee.Finder#getBean
	 * @model opposite="bean" ordered="false"
	 * @generated
	 */
	EList<Finder> getFinders();

	/**
	 * Retrieves the first {@link org.eclipse.emf.facade.uml2.tests.j2ee.Finder} with the specified '<em><b>Name</b></em>' from the '<em><b>Finder</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param name The '<em><b>Name</b></em>' of the {@link org.eclipse.emf.facade.uml2.tests.j2ee.Finder} to retrieve, or <code>null</code>.
	 * @return The first {@link org.eclipse.emf.facade.uml2.tests.j2ee.Finder} with the specified '<em><b>Name</b></em>', or <code>null</code>.
	 * @see #getFinders()
	 * @generated
	 */
	Finder getFinder(String name);

	/**
	 * Retrieves the first {@link org.eclipse.emf.facade.uml2.tests.j2ee.Finder} with the specified '<em><b>Name</b></em>' from the '<em><b>Finder</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param name The '<em><b>Name</b></em>' of the {@link org.eclipse.emf.facade.uml2.tests.j2ee.Finder} to retrieve, or <code>null</code>.
	 * @param ignoreCase Whether to ignore case in {@link java.lang.String} comparisons.
	 * @return The first {@link org.eclipse.emf.facade.uml2.tests.j2ee.Finder} with the specified '<em><b>Name</b></em>', or <code>null</code>.
	 * @see #getFinders()
	 * @generated
	 */
	Finder getFinder(String name, boolean ignoreCase);

} // Bean
