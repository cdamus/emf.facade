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
package org.eclipse.emf.facade.uml2.tests.j2eeprofile.internal.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;

import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.emf.facade.uml2.tests.j2eeprofile.Bean;
import org.eclipse.emf.facade.uml2.tests.j2eeprofile.BeanKind;
import org.eclipse.emf.facade.uml2.tests.j2eeprofile.Finder;
import org.eclipse.emf.facade.uml2.tests.j2eeprofile.HomeInterface;
import org.eclipse.emf.facade.uml2.tests.j2eeprofile.J2EEProfileFactory;
import org.eclipse.emf.facade.uml2.tests.j2eeprofile.J2EEProfilePackage;
import org.eclipse.uml2.types.TypesPackage;

import org.eclipse.uml2.uml.UMLPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class J2EEProfilePackageImpl extends EPackageImpl implements J2EEProfilePackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass beanEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass homeInterfaceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass finderEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum beanKindEEnum = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.eclipse.emf.facade.uml2.tests.j2eeprofile.J2EEProfilePackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private J2EEProfilePackageImpl() {
		super(eNS_URI, J2EEProfileFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link J2EEProfilePackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static J2EEProfilePackage init() {
		if (isInited) return (J2EEProfilePackage)EPackage.Registry.INSTANCE.getEPackage(J2EEProfilePackage.eNS_URI);

		// Obtain or create and register package
		J2EEProfilePackageImpl theJ2EEProfilePackage = (J2EEProfilePackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof J2EEProfilePackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new J2EEProfilePackageImpl());

		isInited = true;

		// Initialize simple dependencies
		EcorePackage.eINSTANCE.eClass();
		TypesPackage.eINSTANCE.eClass();
		UMLPackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theJ2EEProfilePackage.createPackageContents();

		// Initialize created meta-data
		theJ2EEProfilePackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theJ2EEProfilePackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(J2EEProfilePackage.eNS_URI, theJ2EEProfilePackage);
		return theJ2EEProfilePackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getBean() {
		return beanEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getBean_Base_Class() {
		return (EReference)beanEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getBean_Kind() {
		return (EAttribute)beanEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getHomeInterface() {
		return homeInterfaceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getHomeInterface_Base_Interface() {
		return (EReference)homeInterfaceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getFinder() {
		return finderEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getFinder_Base_Interface() {
		return (EReference)finderEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EEnum getBeanKind() {
		return beanKindEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public J2EEProfileFactory getJ2EEProfileFactory() {
		return (J2EEProfileFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		beanEClass = createEClass(BEAN);
		createEReference(beanEClass, BEAN__BASE_CLASS);
		createEAttribute(beanEClass, BEAN__KIND);

		homeInterfaceEClass = createEClass(HOME_INTERFACE);
		createEReference(homeInterfaceEClass, HOME_INTERFACE__BASE_INTERFACE);

		finderEClass = createEClass(FINDER);
		createEReference(finderEClass, FINDER__BASE_INTERFACE);

		// Create enums
		beanKindEEnum = createEEnum(BEAN_KIND);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		UMLPackage theUMLPackage = (UMLPackage)EPackage.Registry.INSTANCE.getEPackage(UMLPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes

		// Initialize classes, features, and operations; add parameters
		initEClass(beanEClass, Bean.class, "Bean", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(getBean_Base_Class(), theUMLPackage.getClass_(), null, "base_Class", null, 1, 1, Bean.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED); //$NON-NLS-1$
		initEAttribute(getBean_Kind(), this.getBeanKind(), "kind", null, 1, 1, Bean.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED); //$NON-NLS-1$

		initEClass(homeInterfaceEClass, HomeInterface.class, "HomeInterface", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(getHomeInterface_Base_Interface(), theUMLPackage.getInterface(), null, "base_Interface", null, 1, 1, HomeInterface.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED); //$NON-NLS-1$

		initEClass(finderEClass, Finder.class, "Finder", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(getFinder_Base_Interface(), theUMLPackage.getInterface(), null, "base_Interface", null, 1, 1, Finder.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED); //$NON-NLS-1$

		// Initialize enums and add enum literals
		initEEnum(beanKindEEnum, BeanKind.class, "BeanKind"); //$NON-NLS-1$
		addEEnumLiteral(beanKindEEnum, BeanKind.SESSION);
		addEEnumLiteral(beanKindEEnum, BeanKind.ENTITY);
		addEEnumLiteral(beanKindEEnum, BeanKind.MESSAGEDRIVEN);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// http://schema.omg.org/spec/MOF/2.0/emof.xml#Property.oppositeRoleName
		createEmofAnnotations();
	}

	/**
	 * Initializes the annotations for <b>http://schema.omg.org/spec/MOF/2.0/emof.xml#Property.oppositeRoleName</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createEmofAnnotations() {
		String source = "http://schema.omg.org/spec/MOF/2.0/emof.xml#Property.oppositeRoleName"; //$NON-NLS-1$	
		addAnnotation
		  (getBean_Base_Class(), 
		   source, 
		   new String[] {
			 "body", "extension_Bean" //$NON-NLS-1$ //$NON-NLS-2$
		   });	
		addAnnotation
		  (getHomeInterface_Base_Interface(), 
		   source, 
		   new String[] {
			 "body", "extension_HomeInterface" //$NON-NLS-1$ //$NON-NLS-2$
		   });	
		addAnnotation
		  (getFinder_Base_Interface(), 
		   source, 
		   new String[] {
			 "body", "extension_Finder" //$NON-NLS-1$ //$NON-NLS-2$
		   });
	}

} //J2EEProfilePackageImpl
