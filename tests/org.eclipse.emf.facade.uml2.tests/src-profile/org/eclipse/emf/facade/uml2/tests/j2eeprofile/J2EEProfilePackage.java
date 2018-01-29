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

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
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
 * @see org.eclipse.emf.facade.uml2.tests.j2eeprofile.J2EEProfileFactory
 * @model kind="package"
 * @generated
 */
public interface J2EEProfilePackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "j2eeprofile"; //$NON-NLS-1$

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.eclipse.org/emf/facade/2017/profile/test/j2ee"; //$NON-NLS-1$

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "j2eeprofile"; //$NON-NLS-1$

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	J2EEProfilePackage eINSTANCE = org.eclipse.emf.facade.uml2.tests.j2eeprofile.internal.impl.J2EEProfilePackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.emf.facade.uml2.tests.j2eeprofile.internal.impl.BeanImpl <em>Bean</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.facade.uml2.tests.j2eeprofile.internal.impl.BeanImpl
	 * @see org.eclipse.emf.facade.uml2.tests.j2eeprofile.internal.impl.J2EEProfilePackageImpl#getBean()
	 * @generated
	 */
	int BEAN = 0;

	/**
	 * The feature id for the '<em><b>Base Class</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEAN__BASE_CLASS = 0;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEAN__KIND = 1;

	/**
	 * The number of structural features of the '<em>Bean</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEAN_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Bean</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEAN_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.facade.uml2.tests.j2eeprofile.internal.impl.HomeInterfaceImpl <em>Home Interface</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.facade.uml2.tests.j2eeprofile.internal.impl.HomeInterfaceImpl
	 * @see org.eclipse.emf.facade.uml2.tests.j2eeprofile.internal.impl.J2EEProfilePackageImpl#getHomeInterface()
	 * @generated
	 */
	int HOME_INTERFACE = 1;

	/**
	 * The feature id for the '<em><b>Base Interface</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HOME_INTERFACE__BASE_INTERFACE = 0;

	/**
	 * The number of structural features of the '<em>Home Interface</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HOME_INTERFACE_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Home Interface</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HOME_INTERFACE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.facade.uml2.tests.j2eeprofile.internal.impl.FinderImpl <em>Finder</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.facade.uml2.tests.j2eeprofile.internal.impl.FinderImpl
	 * @see org.eclipse.emf.facade.uml2.tests.j2eeprofile.internal.impl.J2EEProfilePackageImpl#getFinder()
	 * @generated
	 */
	int FINDER = 2;

	/**
	 * The feature id for the '<em><b>Base Interface</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FINDER__BASE_INTERFACE = 0;

	/**
	 * The number of structural features of the '<em>Finder</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FINDER_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Finder</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FINDER_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.emf.facade.uml2.tests.j2eeprofile.BeanKind <em>Bean Kind</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.facade.uml2.tests.j2eeprofile.BeanKind
	 * @see org.eclipse.emf.facade.uml2.tests.j2eeprofile.internal.impl.J2EEProfilePackageImpl#getBeanKind()
	 * @generated
	 */
	int BEAN_KIND = 3;


	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.facade.uml2.tests.j2eeprofile.Bean <em>Bean</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Bean</em>'.
	 * @see org.eclipse.emf.facade.uml2.tests.j2eeprofile.Bean
	 * @generated
	 */
	EClass getBean();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.facade.uml2.tests.j2eeprofile.Bean#getBase_Class <em>Base Class</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Base Class</em>'.
	 * @see org.eclipse.emf.facade.uml2.tests.j2eeprofile.Bean#getBase_Class()
	 * @see #getBean()
	 * @generated
	 */
	EReference getBean_Base_Class();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.emf.facade.uml2.tests.j2eeprofile.Bean#getKind <em>Kind</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Kind</em>'.
	 * @see org.eclipse.emf.facade.uml2.tests.j2eeprofile.Bean#getKind()
	 * @see #getBean()
	 * @generated
	 */
	EAttribute getBean_Kind();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.facade.uml2.tests.j2eeprofile.HomeInterface <em>Home Interface</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Home Interface</em>'.
	 * @see org.eclipse.emf.facade.uml2.tests.j2eeprofile.HomeInterface
	 * @generated
	 */
	EClass getHomeInterface();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.facade.uml2.tests.j2eeprofile.HomeInterface#getBase_Interface <em>Base Interface</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Base Interface</em>'.
	 * @see org.eclipse.emf.facade.uml2.tests.j2eeprofile.HomeInterface#getBase_Interface()
	 * @see #getHomeInterface()
	 * @generated
	 */
	EReference getHomeInterface_Base_Interface();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.facade.uml2.tests.j2eeprofile.Finder <em>Finder</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Finder</em>'.
	 * @see org.eclipse.emf.facade.uml2.tests.j2eeprofile.Finder
	 * @generated
	 */
	EClass getFinder();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.emf.facade.uml2.tests.j2eeprofile.Finder#getBase_Interface <em>Base Interface</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Base Interface</em>'.
	 * @see org.eclipse.emf.facade.uml2.tests.j2eeprofile.Finder#getBase_Interface()
	 * @see #getFinder()
	 * @generated
	 */
	EReference getFinder_Base_Interface();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.emf.facade.uml2.tests.j2eeprofile.BeanKind <em>Bean Kind</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Bean Kind</em>'.
	 * @see org.eclipse.emf.facade.uml2.tests.j2eeprofile.BeanKind
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
	J2EEProfileFactory getJ2EEProfileFactory();

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
		 * The meta object literal for the '{@link org.eclipse.emf.facade.uml2.tests.j2eeprofile.internal.impl.BeanImpl <em>Bean</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.facade.uml2.tests.j2eeprofile.internal.impl.BeanImpl
		 * @see org.eclipse.emf.facade.uml2.tests.j2eeprofile.internal.impl.J2EEProfilePackageImpl#getBean()
		 * @generated
		 */
		EClass BEAN = eINSTANCE.getBean();

		/**
		 * The meta object literal for the '<em><b>Base Class</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BEAN__BASE_CLASS = eINSTANCE.getBean_Base_Class();

		/**
		 * The meta object literal for the '<em><b>Kind</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BEAN__KIND = eINSTANCE.getBean_Kind();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.facade.uml2.tests.j2eeprofile.internal.impl.HomeInterfaceImpl <em>Home Interface</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.facade.uml2.tests.j2eeprofile.internal.impl.HomeInterfaceImpl
		 * @see org.eclipse.emf.facade.uml2.tests.j2eeprofile.internal.impl.J2EEProfilePackageImpl#getHomeInterface()
		 * @generated
		 */
		EClass HOME_INTERFACE = eINSTANCE.getHomeInterface();

		/**
		 * The meta object literal for the '<em><b>Base Interface</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference HOME_INTERFACE__BASE_INTERFACE = eINSTANCE.getHomeInterface_Base_Interface();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.facade.uml2.tests.j2eeprofile.internal.impl.FinderImpl <em>Finder</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.facade.uml2.tests.j2eeprofile.internal.impl.FinderImpl
		 * @see org.eclipse.emf.facade.uml2.tests.j2eeprofile.internal.impl.J2EEProfilePackageImpl#getFinder()
		 * @generated
		 */
		EClass FINDER = eINSTANCE.getFinder();

		/**
		 * The meta object literal for the '<em><b>Base Interface</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FINDER__BASE_INTERFACE = eINSTANCE.getFinder_Base_Interface();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.facade.uml2.tests.j2eeprofile.BeanKind <em>Bean Kind</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.facade.uml2.tests.j2eeprofile.BeanKind
		 * @see org.eclipse.emf.facade.uml2.tests.j2eeprofile.internal.impl.J2EEProfilePackageImpl#getBeanKind()
		 * @generated
		 */
		EEnum BEAN_KIND = eINSTANCE.getBeanKind();

	}

} //J2EEProfilePackage
