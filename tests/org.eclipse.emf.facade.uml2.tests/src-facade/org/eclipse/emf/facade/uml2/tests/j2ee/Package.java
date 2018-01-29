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
import org.eclipse.emf.ecore.EClass;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Package</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.emf.facade.uml2.tests.j2ee.Package#getMembers <em>Member</em>}</li>
 *   <li>{@link org.eclipse.emf.facade.uml2.tests.j2ee.Package#getBeans <em>Bean</em>}</li>
 *   <li>{@link org.eclipse.emf.facade.uml2.tests.j2ee.Package#getHomeInterfaces <em>Home Interface</em>}</li>
 *   <li>{@link org.eclipse.emf.facade.uml2.tests.j2ee.Package#getFinders <em>Finder</em>}</li>
 * </ul>
 *
 * @see org.eclipse.emf.facade.uml2.tests.j2ee.J2EEPackage#getPackage()
 * @model
 * @generated
 */
public interface Package extends NamedElement {
	/**
	 * Returns the value of the '<em><b>Member</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.emf.facade.uml2.tests.j2ee.NamedElement}.
	 * It is bidirectional and its opposite is '{@link org.eclipse.emf.facade.uml2.tests.j2ee.NamedElement#getPackage <em>Package</em>}'.
	 * This feature is a derived union.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Member</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Member</em>' reference list.
	 * @see org.eclipse.emf.facade.uml2.tests.j2ee.J2EEPackage#getPackage_Member()
	 * @see org.eclipse.emf.facade.uml2.tests.j2ee.NamedElement#getPackage
	 * @model opposite="package" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
	 * @generated
	 */
	EList<NamedElement> getMembers();

	/**
	 * Retrieves the first {@link org.eclipse.emf.facade.uml2.tests.j2ee.NamedElement} with the specified '<em><b>Name</b></em>' from the '<em><b>Member</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param name The '<em><b>Name</b></em>' of the {@link org.eclipse.emf.facade.uml2.tests.j2ee.NamedElement} to retrieve, or <code>null</code>.
	 * @return The first {@link org.eclipse.emf.facade.uml2.tests.j2ee.NamedElement} with the specified '<em><b>Name</b></em>', or <code>null</code>.
	 * @see #getMembers()
	 * @generated
	 */
	NamedElement getMember(String name);

	/**
	 * Retrieves the first {@link org.eclipse.emf.facade.uml2.tests.j2ee.NamedElement} with the specified '<em><b>Name</b></em>' from the '<em><b>Member</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param name The '<em><b>Name</b></em>' of the {@link org.eclipse.emf.facade.uml2.tests.j2ee.NamedElement} to retrieve, or <code>null</code>.
	 * @param ignoreCase Whether to ignore case in {@link java.lang.String} comparisons.
	 * @param eClass The Ecore class of the {@link org.eclipse.emf.facade.uml2.tests.j2ee.NamedElement} to retrieve, or <code>null</code>.
	 * @return The first {@link org.eclipse.emf.facade.uml2.tests.j2ee.NamedElement} with the specified '<em><b>Name</b></em>', or <code>null</code>.
	 * @see #getMembers()
	 * @generated
	 */
	NamedElement getMember(String name, boolean ignoreCase, EClass eClass);

	/**
	 * Returns the value of the '<em><b>Bean</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.facade.uml2.tests.j2ee.Bean}.
	 * <p>
	 * This feature subsets the following features:
	 * </p>
	 * <ul>
	 *   <li>'{@link org.eclipse.emf.facade.uml2.tests.j2ee.Package#getMembers() <em>Member</em>}'</li>
	 * </ul>
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Bean</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Bean</em>' containment reference list.
	 * @see org.eclipse.emf.facade.uml2.tests.j2ee.J2EEPackage#getPackage_Bean()
	 * @model containment="true" ordered="false"
	 * @generated
	 */
	EList<Bean> getBeans();

	/**
	 * Creates a new {@link org.eclipse.emf.facade.uml2.tests.j2ee.Bean}, with the specified '<em><b>Name</b></em>', and appends it to the '<em><b>Bean</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param name The '<em><b>Name</b></em>' for the new {@link org.eclipse.emf.facade.uml2.tests.j2ee.Bean}, or <code>null</code>.
	 * @return The new {@link org.eclipse.emf.facade.uml2.tests.j2ee.Bean}.
	 * @see #getBeans()
	 * @generated
	 */
	Bean createBean(String name);

	/**
	 * Retrieves the first {@link org.eclipse.emf.facade.uml2.tests.j2ee.Bean} with the specified '<em><b>Name</b></em>' from the '<em><b>Bean</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param name The '<em><b>Name</b></em>' of the {@link org.eclipse.emf.facade.uml2.tests.j2ee.Bean} to retrieve, or <code>null</code>.
	 * @return The first {@link org.eclipse.emf.facade.uml2.tests.j2ee.Bean} with the specified '<em><b>Name</b></em>', or <code>null</code>.
	 * @see #getBeans()
	 * @generated
	 */
	Bean getBean(String name);

	/**
	 * Retrieves the first {@link org.eclipse.emf.facade.uml2.tests.j2ee.Bean} with the specified '<em><b>Name</b></em>' from the '<em><b>Bean</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param name The '<em><b>Name</b></em>' of the {@link org.eclipse.emf.facade.uml2.tests.j2ee.Bean} to retrieve, or <code>null</code>.
	 * @param ignoreCase Whether to ignore case in {@link java.lang.String} comparisons.
	 * @param createOnDemand Whether to create a {@link org.eclipse.emf.facade.uml2.tests.j2ee.Bean} on demand if not found.
	 * @return The first {@link org.eclipse.emf.facade.uml2.tests.j2ee.Bean} with the specified '<em><b>Name</b></em>', or <code>null</code>.
	 * @see #getBeans()
	 * @generated
	 */
	Bean getBean(String name, boolean ignoreCase, boolean createOnDemand);

	/**
	 * Returns the value of the '<em><b>Home Interface</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.facade.uml2.tests.j2ee.HomeInterface}.
	 * <p>
	 * This feature subsets the following features:
	 * </p>
	 * <ul>
	 *   <li>'{@link org.eclipse.emf.facade.uml2.tests.j2ee.Package#getMembers() <em>Member</em>}'</li>
	 * </ul>
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Home Interface</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Home Interface</em>' containment reference list.
	 * @see org.eclipse.emf.facade.uml2.tests.j2ee.J2EEPackage#getPackage_HomeInterface()
	 * @model containment="true" ordered="false"
	 * @generated
	 */
	EList<HomeInterface> getHomeInterfaces();

	/**
	 * Creates a new {@link org.eclipse.emf.facade.uml2.tests.j2ee.HomeInterface}, with the specified '<em><b>Name</b></em>', and appends it to the '<em><b>Home Interface</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param name The '<em><b>Name</b></em>' for the new {@link org.eclipse.emf.facade.uml2.tests.j2ee.HomeInterface}, or <code>null</code>.
	 * @return The new {@link org.eclipse.emf.facade.uml2.tests.j2ee.HomeInterface}.
	 * @see #getHomeInterfaces()
	 * @generated
	 */
	HomeInterface createHomeInterface(String name);

	/**
	 * Retrieves the first {@link org.eclipse.emf.facade.uml2.tests.j2ee.HomeInterface} with the specified '<em><b>Name</b></em>' from the '<em><b>Home Interface</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param name The '<em><b>Name</b></em>' of the {@link org.eclipse.emf.facade.uml2.tests.j2ee.HomeInterface} to retrieve, or <code>null</code>.
	 * @return The first {@link org.eclipse.emf.facade.uml2.tests.j2ee.HomeInterface} with the specified '<em><b>Name</b></em>', or <code>null</code>.
	 * @see #getHomeInterfaces()
	 * @generated
	 */
	HomeInterface getHomeInterface(String name);

	/**
	 * Retrieves the first {@link org.eclipse.emf.facade.uml2.tests.j2ee.HomeInterface} with the specified '<em><b>Name</b></em>' from the '<em><b>Home Interface</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param name The '<em><b>Name</b></em>' of the {@link org.eclipse.emf.facade.uml2.tests.j2ee.HomeInterface} to retrieve, or <code>null</code>.
	 * @param ignoreCase Whether to ignore case in {@link java.lang.String} comparisons.
	 * @param createOnDemand Whether to create a {@link org.eclipse.emf.facade.uml2.tests.j2ee.HomeInterface} on demand if not found.
	 * @return The first {@link org.eclipse.emf.facade.uml2.tests.j2ee.HomeInterface} with the specified '<em><b>Name</b></em>', or <code>null</code>.
	 * @see #getHomeInterfaces()
	 * @generated
	 */
	HomeInterface getHomeInterface(String name, boolean ignoreCase, boolean createOnDemand);

	/**
	 * Returns the value of the '<em><b>Finder</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.facade.uml2.tests.j2ee.Finder}.
	 * <p>
	 * This feature subsets the following features:
	 * </p>
	 * <ul>
	 *   <li>'{@link org.eclipse.emf.facade.uml2.tests.j2ee.Package#getMembers() <em>Member</em>}'</li>
	 * </ul>
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Finder</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Finder</em>' containment reference list.
	 * @see org.eclipse.emf.facade.uml2.tests.j2ee.J2EEPackage#getPackage_Finder()
	 * @model containment="true" ordered="false"
	 * @generated
	 */
	EList<Finder> getFinders();

	/**
	 * Creates a new {@link org.eclipse.emf.facade.uml2.tests.j2ee.Finder}, with the specified '<em><b>Name</b></em>', and appends it to the '<em><b>Finder</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param name The '<em><b>Name</b></em>' for the new {@link org.eclipse.emf.facade.uml2.tests.j2ee.Finder}, or <code>null</code>.
	 * @return The new {@link org.eclipse.emf.facade.uml2.tests.j2ee.Finder}.
	 * @see #getFinders()
	 * @generated
	 */
	Finder createFinder(String name);

	/**
	 * Retrieves the first {@link org.eclipse.emf.facade.uml2.tests.j2ee.Finder} with the specified '<em><b>Name</b></em>' from the '<em><b>Finder</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param name The '<em><b>Name</b></em>' of the {@link org.eclipse.emf.facade.uml2.tests.j2ee.Finder} to retrieve, or <code>null</code>.
	 * @return The first {@link org.eclipse.emf.facade.uml2.tests.j2ee.Finder} with the specified '<em><b>Name</b></em>', or <code>null</code>.
	 * @see #getFinders()
	 * @generated
	 */
	Finder getFinder(String name);

	/**
	 * Retrieves the first {@link org.eclipse.emf.facade.uml2.tests.j2ee.Finder} with the specified '<em><b>Name</b></em>' from the '<em><b>Finder</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param name The '<em><b>Name</b></em>' of the {@link org.eclipse.emf.facade.uml2.tests.j2ee.Finder} to retrieve, or <code>null</code>.
	 * @param ignoreCase Whether to ignore case in {@link java.lang.String} comparisons.
	 * @param createOnDemand Whether to create a {@link org.eclipse.emf.facade.uml2.tests.j2ee.Finder} on demand if not found.
	 * @return The first {@link org.eclipse.emf.facade.uml2.tests.j2ee.Finder} with the specified '<em><b>Name</b></em>', or <code>null</code>.
	 * @see #getFinders()
	 * @generated
	 */
	Finder getFinder(String name, boolean ignoreCase, boolean createOnDemand);

} // Package
