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

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.eclipse.emf.facade.uml2.tests.j2ee.J2EEFactory
 * @model kind="package"
 * @generated
 */
public interface J2EEPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "j2ee"; //$NON-NLS-1$

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.eclipse.org/emf/facade/2017/test/j2ee"; //$NON-NLS-1$

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "j2ee"; //$NON-NLS-1$

	/**
	 * The package content type ID.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eCONTENT_TYPE = "org.eclipse.emf.facade.uml2.tests.j2ee"; //$NON-NLS-1$

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	J2EEPackage eINSTANCE = org.eclipse.emf.facade.uml2.tests.j2ee.internal.impl.J2EEPackageImpl
			.init();

	/**
	 * The meta object id for the '{@link org.eclipse.emf.facade.uml2.tests.j2ee.internal.impl.NamedElementImpl <em>Named Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.facade.uml2.tests.j2ee.internal.impl.NamedElementImpl
	 * @see org.eclipse.emf.facade.uml2.tests.j2ee.internal.impl.J2EEPackageImpl#getNamedElement()
	 * @generated
	 */
	int NAMED_ELEMENT = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_ELEMENT__NAME = 0;

	/**
	 * The feature id for the '<em><b>Qualified Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_ELEMENT__QUALIFIED_NAME = 1;

	/**
	 * The feature id for the '<em><b>Package</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_ELEMENT__PACKAGE = 2;

	/**
	 * The number of structural features of the '<em>Named Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_ELEMENT_FEATURE_COUNT = 3;

	/**
	 * The operation id for the '<em>Get Qualified Name</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_ELEMENT___GET_QUALIFIED_NAME = 0;

	/**
	 * The operation id for the '<em>Get Package</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_ELEMENT___GET_PACKAGE = 1;

	/**
	 * The operation id for the '<em>Set Package</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_ELEMENT___SET_PACKAGE__PACKAGE = 2;

	/**
	 * The number of operations of the '<em>Named Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_ELEMENT_OPERATION_COUNT = 3;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.facade.uml2.tests.j2ee.internal.impl.PackageImpl <em>Package</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.facade.uml2.tests.j2ee.internal.impl.PackageImpl
	 * @see org.eclipse.emf.facade.uml2.tests.j2ee.internal.impl.J2EEPackageImpl#getPackage()
	 * @generated
	 */
	int PACKAGE = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PACKAGE__NAME = NAMED_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Qualified Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PACKAGE__QUALIFIED_NAME = NAMED_ELEMENT__QUALIFIED_NAME;

	/**
	 * The feature id for the '<em><b>Package</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PACKAGE__PACKAGE = NAMED_ELEMENT__PACKAGE;

	/**
	 * The feature id for the '<em><b>Member</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PACKAGE__MEMBER = NAMED_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Bean</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PACKAGE__BEAN = NAMED_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Home Interface</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PACKAGE__HOME_INTERFACE = NAMED_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Finder</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PACKAGE__FINDER = NAMED_ELEMENT_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Package</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PACKAGE_FEATURE_COUNT = NAMED_ELEMENT_FEATURE_COUNT + 4;

	/**
	 * The operation id for the '<em>Get Qualified Name</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PACKAGE___GET_QUALIFIED_NAME = NAMED_ELEMENT___GET_QUALIFIED_NAME;

	/**
	 * The operation id for the '<em>Get Package</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PACKAGE___GET_PACKAGE = NAMED_ELEMENT___GET_PACKAGE;

	/**
	 * The operation id for the '<em>Set Package</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PACKAGE___SET_PACKAGE__PACKAGE = NAMED_ELEMENT___SET_PACKAGE__PACKAGE;

	/**
	 * The number of operations of the '<em>Package</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PACKAGE_OPERATION_COUNT = NAMED_ELEMENT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.facade.uml2.tests.j2ee.internal.impl.BeanImpl <em>Bean</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.facade.uml2.tests.j2ee.internal.impl.BeanImpl
	 * @see org.eclipse.emf.facade.uml2.tests.j2ee.internal.impl.J2EEPackageImpl#getBean()
	 * @generated
	 */
	int BEAN = 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEAN__NAME = NAMED_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Qualified Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEAN__QUALIFIED_NAME = NAMED_ELEMENT__QUALIFIED_NAME;

	/**
	 * The feature id for the '<em><b>Package</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEAN__PACKAGE = NAMED_ELEMENT__PACKAGE;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEAN__KIND = NAMED_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Is Abstract</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEAN__IS_ABSTRACT = NAMED_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Superclass</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEAN__SUPERCLASS = NAMED_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Home Interface</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEAN__HOME_INTERFACE = NAMED_ELEMENT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Finder</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEAN__FINDER = NAMED_ELEMENT_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>Bean</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEAN_FEATURE_COUNT = NAMED_ELEMENT_FEATURE_COUNT + 5;

	/**
	 * The operation id for the '<em>Get Qualified Name</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEAN___GET_QUALIFIED_NAME = NAMED_ELEMENT___GET_QUALIFIED_NAME;

	/**
	 * The operation id for the '<em>Get Package</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEAN___GET_PACKAGE = NAMED_ELEMENT___GET_PACKAGE;

	/**
	 * The operation id for the '<em>Set Package</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEAN___SET_PACKAGE__PACKAGE = NAMED_ELEMENT___SET_PACKAGE__PACKAGE;

	/**
	 * The number of operations of the '<em>Bean</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEAN_OPERATION_COUNT = NAMED_ELEMENT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.facade.uml2.tests.j2ee.internal.impl.HomeInterfaceImpl <em>Home Interface</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.facade.uml2.tests.j2ee.internal.impl.HomeInterfaceImpl
	 * @see org.eclipse.emf.facade.uml2.tests.j2ee.internal.impl.J2EEPackageImpl#getHomeInterface()
	 * @generated
	 */
	int HOME_INTERFACE = 3;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HOME_INTERFACE__NAME = NAMED_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Qualified Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HOME_INTERFACE__QUALIFIED_NAME = NAMED_ELEMENT__QUALIFIED_NAME;

	/**
	 * The feature id for the '<em><b>Package</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HOME_INTERFACE__PACKAGE = NAMED_ELEMENT__PACKAGE;

	/**
	 * The feature id for the '<em><b>Bean</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HOME_INTERFACE__BEAN = NAMED_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Home Interface</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HOME_INTERFACE_FEATURE_COUNT = NAMED_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The operation id for the '<em>Get Qualified Name</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HOME_INTERFACE___GET_QUALIFIED_NAME = NAMED_ELEMENT___GET_QUALIFIED_NAME;

	/**
	 * The operation id for the '<em>Get Package</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HOME_INTERFACE___GET_PACKAGE = NAMED_ELEMENT___GET_PACKAGE;

	/**
	 * The operation id for the '<em>Set Package</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HOME_INTERFACE___SET_PACKAGE__PACKAGE = NAMED_ELEMENT___SET_PACKAGE__PACKAGE;

	/**
	 * The number of operations of the '<em>Home Interface</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HOME_INTERFACE_OPERATION_COUNT = NAMED_ELEMENT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.facade.uml2.tests.j2ee.internal.impl.FinderImpl <em>Finder</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.facade.uml2.tests.j2ee.internal.impl.FinderImpl
	 * @see org.eclipse.emf.facade.uml2.tests.j2ee.internal.impl.J2EEPackageImpl#getFinder()
	 * @generated
	 */
	int FINDER = 4;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FINDER__NAME = NAMED_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Qualified Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FINDER__QUALIFIED_NAME = NAMED_ELEMENT__QUALIFIED_NAME;

	/**
	 * The feature id for the '<em><b>Package</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FINDER__PACKAGE = NAMED_ELEMENT__PACKAGE;

	/**
	 * The feature id for the '<em><b>Bean</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FINDER__BEAN = NAMED_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Finder</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FINDER_FEATURE_COUNT = NAMED_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The operation id for the '<em>Get Qualified Name</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FINDER___GET_QUALIFIED_NAME = NAMED_ELEMENT___GET_QUALIFIED_NAME;

	/**
	 * The operation id for the '<em>Get Package</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FINDER___GET_PACKAGE = NAMED_ELEMENT___GET_PACKAGE;

	/**
	 * The operation id for the '<em>Set Package</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FINDER___SET_PACKAGE__PACKAGE = NAMED_ELEMENT___SET_PACKAGE__PACKAGE;

	/**
	 * The number of operations of the '<em>Finder</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FINDER_OPERATION_COUNT = NAMED_ELEMENT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.facade.uml2.tests.j2ee.BeanKind <em>Bean Kind</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.facade.uml2.tests.j2ee.BeanKind
	 * @see org.eclipse.emf.facade.uml2.tests.j2ee.internal.impl.J2EEPackageImpl#getBeanKind()
	 * @generated
	 */
	int BEAN_KIND = 5;

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.facade.uml2.tests.j2ee.Package <em>Package</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Package</em>'.
	 * @see org.eclipse.emf.facade.uml2.tests.j2ee.Package
	 * @generated
	 */
	EClass getPackage();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.emf.facade.uml2.tests.j2ee.Package#getMembers <em>Member</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Member</em>'.
	 * @see org.eclipse.emf.facade.uml2.tests.j2ee.Package#getMembers()
	 * @see #getPackage()
	 * @generated
	 */
	EReference getPackage_Member();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.emf.facade.uml2.tests.j2ee.Package#getBeans <em>Bean</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Bean</em>'.
	 * @see org.eclipse.emf.facade.uml2.tests.j2ee.Package#getBeans()
	 * @see #getPackage()
	 * @generated
	 */
	EReference getPackage_Bean();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.emf.facade.uml2.tests.j2ee.Package#getHomeInterfaces <em>Home Interface</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Home Interface</em>'.
	 * @see org.eclipse.emf.facade.uml2.tests.j2ee.Package#getHomeInterfaces()
	 * @see #getPackage()
	 * @generated
	 */
	EReference getPackage_HomeInterface();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.emf.facade.uml2.tests.j2ee.Package#getFinders <em>Finder</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Finder</em>'.
	 * @see org.eclipse.emf.facade.uml2.tests.j2ee.Package#getFinders()
	 * @see #getPackage()
	 * @generated
	 */
	EReference getPackage_Finder();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.facade.uml2.tests.j2ee.NamedElement <em>Named Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Named Element</em>'.
	 * @see org.eclipse.emf.facade.uml2.tests.j2ee.NamedElement
	 * @generated
	 */
	EClass getNamedElement();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.facade.uml2.tests.j2ee.NamedElement#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.emf.facade.uml2.tests.j2ee.NamedElement#getName()
	 * @see #getNamedElement()
	 * @generated
	 */
	EAttribute getNamedElement_Name();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.facade.uml2.tests.j2ee.NamedElement#getQualifiedName <em>Qualified Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Qualified Name</em>'.
	 * @see org.eclipse.emf.facade.uml2.tests.j2ee.NamedElement#getQualifiedName()
	 * @see #getNamedElement()
	 * @generated
	 */
	EAttribute getNamedElement_QualifiedName();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.facade.uml2.tests.j2ee.NamedElement#getPackage <em>Package</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Package</em>'.
	 * @see org.eclipse.emf.facade.uml2.tests.j2ee.NamedElement#getPackage()
	 * @see #getNamedElement()
	 * @generated
	 */
	EReference getNamedElement_Package();

	/**
	 * Returns the meta object for the '{@link org.eclipse.emf.facade.uml2.tests.j2ee.NamedElement#getQualifiedName() <em>Get Qualified Name</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Qualified Name</em>' operation.
	 * @see org.eclipse.emf.facade.uml2.tests.j2ee.NamedElement#getQualifiedName()
	 * @generated
	 */
	EOperation getNamedElement__GetQualifiedName();

	/**
	 * Returns the meta object for the '{@link org.eclipse.emf.facade.uml2.tests.j2ee.NamedElement#getPackage() <em>Get Package</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Package</em>' operation.
	 * @see org.eclipse.emf.facade.uml2.tests.j2ee.NamedElement#getPackage()
	 * @generated
	 */
	EOperation getNamedElement__GetPackage();

	/**
	 * Returns the meta object for the '{@link org.eclipse.emf.facade.uml2.tests.j2ee.NamedElement#setPackage(org.eclipse.emf.facade.uml2.tests.j2ee.Package) <em>Set Package</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Set Package</em>' operation.
	 * @see org.eclipse.emf.facade.uml2.tests.j2ee.NamedElement#setPackage(org.eclipse.emf.facade.uml2.tests.j2ee.Package)
	 * @generated
	 */
	EOperation getNamedElement__SetPackage__Package();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.facade.uml2.tests.j2ee.Bean <em>Bean</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Bean</em>'.
	 * @see org.eclipse.emf.facade.uml2.tests.j2ee.Bean
	 * @generated
	 */
	EClass getBean();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.facade.uml2.tests.j2ee.Bean#getKind <em>Kind</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Kind</em>'.
	 * @see org.eclipse.emf.facade.uml2.tests.j2ee.Bean#getKind()
	 * @see #getBean()
	 * @generated
	 */
	EAttribute getBean_Kind();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.facade.uml2.tests.j2ee.Bean#isAbstract <em>Is Abstract</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Is Abstract</em>'.
	 * @see org.eclipse.emf.facade.uml2.tests.j2ee.Bean#isAbstract()
	 * @see #getBean()
	 * @generated
	 */
	EAttribute getBean_IsAbstract();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.facade.uml2.tests.j2ee.Bean#getSuperclass <em>Superclass</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Superclass</em>'.
	 * @see org.eclipse.emf.facade.uml2.tests.j2ee.Bean#getSuperclass()
	 * @see #getBean()
	 * @generated
	 */
	EReference getBean_Superclass();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.facade.uml2.tests.j2ee.Bean#getHomeInterface <em>Home Interface</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Home Interface</em>'.
	 * @see org.eclipse.emf.facade.uml2.tests.j2ee.Bean#getHomeInterface()
	 * @see #getBean()
	 * @generated
	 */
	EReference getBean_HomeInterface();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.emf.facade.uml2.tests.j2ee.Bean#getFinders <em>Finder</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Finder</em>'.
	 * @see org.eclipse.emf.facade.uml2.tests.j2ee.Bean#getFinders()
	 * @see #getBean()
	 * @generated
	 */
	EReference getBean_Finder();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.facade.uml2.tests.j2ee.HomeInterface <em>Home Interface</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Home Interface</em>'.
	 * @see org.eclipse.emf.facade.uml2.tests.j2ee.HomeInterface
	 * @generated
	 */
	EClass getHomeInterface();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.facade.uml2.tests.j2ee.HomeInterface#getBean <em>Bean</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Bean</em>'.
	 * @see org.eclipse.emf.facade.uml2.tests.j2ee.HomeInterface#getBean()
	 * @see #getHomeInterface()
	 * @generated
	 */
	EReference getHomeInterface_Bean();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.facade.uml2.tests.j2ee.Finder <em>Finder</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Finder</em>'.
	 * @see org.eclipse.emf.facade.uml2.tests.j2ee.Finder
	 * @generated
	 */
	EClass getFinder();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.facade.uml2.tests.j2ee.Finder#getBean <em>Bean</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Bean</em>'.
	 * @see org.eclipse.emf.facade.uml2.tests.j2ee.Finder#getBean()
	 * @see #getFinder()
	 * @generated
	 */
	EReference getFinder_Bean();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.emf.facade.uml2.tests.j2ee.BeanKind <em>Bean Kind</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Bean Kind</em>'.
	 * @see org.eclipse.emf.facade.uml2.tests.j2ee.BeanKind
	 * @generated
	 */
	EEnum getBeanKind();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	J2EEFactory getJ2EEFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.eclipse.emf.facade.uml2.tests.j2ee.internal.impl.PackageImpl <em>Package</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.facade.uml2.tests.j2ee.internal.impl.PackageImpl
		 * @see org.eclipse.emf.facade.uml2.tests.j2ee.internal.impl.J2EEPackageImpl#getPackage()
		 * @generated
		 */
		EClass PACKAGE = eINSTANCE.getPackage();

		/**
		 * The meta object literal for the '<em><b>Member</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PACKAGE__MEMBER = eINSTANCE.getPackage_Member();

		/**
		 * The meta object literal for the '<em><b>Bean</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PACKAGE__BEAN = eINSTANCE.getPackage_Bean();

		/**
		 * The meta object literal for the '<em><b>Home Interface</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PACKAGE__HOME_INTERFACE = eINSTANCE.getPackage_HomeInterface();

		/**
		 * The meta object literal for the '<em><b>Finder</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PACKAGE__FINDER = eINSTANCE.getPackage_Finder();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.facade.uml2.tests.j2ee.internal.impl.NamedElementImpl <em>Named Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.facade.uml2.tests.j2ee.internal.impl.NamedElementImpl
		 * @see org.eclipse.emf.facade.uml2.tests.j2ee.internal.impl.J2EEPackageImpl#getNamedElement()
		 * @generated
		 */
		EClass NAMED_ELEMENT = eINSTANCE.getNamedElement();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NAMED_ELEMENT__NAME = eINSTANCE.getNamedElement_Name();

		/**
		 * The meta object literal for the '<em><b>Qualified Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NAMED_ELEMENT__QUALIFIED_NAME = eINSTANCE.getNamedElement_QualifiedName();

		/**
		 * The meta object literal for the '<em><b>Package</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference NAMED_ELEMENT__PACKAGE = eINSTANCE.getNamedElement_Package();

		/**
		 * The meta object literal for the '<em><b>Get Qualified Name</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation NAMED_ELEMENT___GET_QUALIFIED_NAME = eINSTANCE.getNamedElement__GetQualifiedName();

		/**
		 * The meta object literal for the '<em><b>Get Package</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation NAMED_ELEMENT___GET_PACKAGE = eINSTANCE.getNamedElement__GetPackage();

		/**
		 * The meta object literal for the '<em><b>Set Package</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation NAMED_ELEMENT___SET_PACKAGE__PACKAGE = eINSTANCE.getNamedElement__SetPackage__Package();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.facade.uml2.tests.j2ee.internal.impl.BeanImpl <em>Bean</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.facade.uml2.tests.j2ee.internal.impl.BeanImpl
		 * @see org.eclipse.emf.facade.uml2.tests.j2ee.internal.impl.J2EEPackageImpl#getBean()
		 * @generated
		 */
		EClass BEAN = eINSTANCE.getBean();

		/**
		 * The meta object literal for the '<em><b>Kind</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BEAN__KIND = eINSTANCE.getBean_Kind();

		/**
		 * The meta object literal for the '<em><b>Is Abstract</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BEAN__IS_ABSTRACT = eINSTANCE.getBean_IsAbstract();

		/**
		 * The meta object literal for the '<em><b>Superclass</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BEAN__SUPERCLASS = eINSTANCE.getBean_Superclass();

		/**
		 * The meta object literal for the '<em><b>Home Interface</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BEAN__HOME_INTERFACE = eINSTANCE.getBean_HomeInterface();

		/**
		 * The meta object literal for the '<em><b>Finder</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BEAN__FINDER = eINSTANCE.getBean_Finder();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.facade.uml2.tests.j2ee.internal.impl.HomeInterfaceImpl <em>Home Interface</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.facade.uml2.tests.j2ee.internal.impl.HomeInterfaceImpl
		 * @see org.eclipse.emf.facade.uml2.tests.j2ee.internal.impl.J2EEPackageImpl#getHomeInterface()
		 * @generated
		 */
		EClass HOME_INTERFACE = eINSTANCE.getHomeInterface();

		/**
		 * The meta object literal for the '<em><b>Bean</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference HOME_INTERFACE__BEAN = eINSTANCE.getHomeInterface_Bean();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.facade.uml2.tests.j2ee.internal.impl.FinderImpl <em>Finder</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.facade.uml2.tests.j2ee.internal.impl.FinderImpl
		 * @see org.eclipse.emf.facade.uml2.tests.j2ee.internal.impl.J2EEPackageImpl#getFinder()
		 * @generated
		 */
		EClass FINDER = eINSTANCE.getFinder();

		/**
		 * The meta object literal for the '<em><b>Bean</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FINDER__BEAN = eINSTANCE.getFinder_Bean();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.facade.uml2.tests.j2ee.BeanKind <em>Bean Kind</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.facade.uml2.tests.j2ee.BeanKind
		 * @see org.eclipse.emf.facade.uml2.tests.j2ee.internal.impl.J2EEPackageImpl#getBeanKind()
		 * @generated
		 */
		EEnum BEAN_KIND = eINSTANCE.getBeanKind();

	}

} //J2EEPackage
