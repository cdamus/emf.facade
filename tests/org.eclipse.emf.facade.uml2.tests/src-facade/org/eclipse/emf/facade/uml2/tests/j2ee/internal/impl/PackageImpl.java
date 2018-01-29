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
package org.eclipse.emf.facade.uml2.tests.j2ee.internal.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.emf.facade.uml2.tests.j2ee.Bean;
import org.eclipse.emf.facade.uml2.tests.j2ee.Finder;
import org.eclipse.emf.facade.uml2.tests.j2ee.HomeInterface;
import org.eclipse.emf.facade.uml2.tests.j2ee.J2EEPackage;
import org.eclipse.emf.facade.uml2.tests.j2ee.NamedElement;
import org.eclipse.uml2.common.util.DerivedUnionEObjectEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Package</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.emf.facade.uml2.tests.j2ee.internal.impl.PackageImpl#getMembers <em>Member</em>}</li>
 *   <li>{@link org.eclipse.emf.facade.uml2.tests.j2ee.internal.impl.PackageImpl#getBeans <em>Bean</em>}</li>
 *   <li>{@link org.eclipse.emf.facade.uml2.tests.j2ee.internal.impl.PackageImpl#getHomeInterfaces <em>Home Interface</em>}</li>
 *   <li>{@link org.eclipse.emf.facade.uml2.tests.j2ee.internal.impl.PackageImpl#getFinders <em>Finder</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PackageImpl extends NamedElementImpl implements org.eclipse.emf.facade.uml2.tests.j2ee.Package {
	/**
	 * The cached value of the '{@link #getBeans() <em>Bean</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBeans()
	 * @generated
	 * @ordered
	 */
	protected EList<Bean> beans;

	/**
	 * The cached value of the '{@link #getHomeInterfaces() <em>Home Interface</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHomeInterfaces()
	 * @generated
	 * @ordered
	 */
	protected EList<HomeInterface> homeInterfaces;

	/**
	 * The cached value of the '{@link #getFinders() <em>Finder</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFinders()
	 * @generated
	 * @ordered
	 */
	protected EList<Finder> finders;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PackageImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return J2EEPackage.Literals.PACKAGE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<NamedElement> getMembers() {
		return new DerivedUnionEObjectEList<NamedElement>(NamedElement.class, this,
				J2EEPackage.PACKAGE__MEMBER, MEMBER_ESUBSETS);
	}

	/**
	 * The array of subset feature identifiers for the '{@link #getMembers() <em>Member</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMembers()
	 * @generated
	 * @ordered
	 */
	protected static final int[] MEMBER_ESUBSETS = new int[] {J2EEPackage.PACKAGE__BEAN,
			J2EEPackage.PACKAGE__HOME_INTERFACE, J2EEPackage.PACKAGE__FINDER };

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NamedElement getMember(String name) {
		return getMember(name, false, null);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NamedElement getMember(String name, boolean ignoreCase, EClass eClass) {
		memberLoop: for (NamedElement member : getMembers()) {
			if (eClass != null && !eClass.isInstance(member))
				continue memberLoop;
			if (name != null && !(ignoreCase ? name.equalsIgnoreCase(member.getName())
					: name.equals(member.getName())))
				continue memberLoop;
			return member;
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<Bean> getBeans() {
		if (beans == null) {
			beans = new EObjectContainmentEList<Bean>(Bean.class, this, J2EEPackage.PACKAGE__BEAN);
		}
		return beans;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Bean createBean(String name) {
		Bean newBean = (Bean)create(J2EEPackage.Literals.BEAN);
		getBeans().add(newBean);
		if (name != null)
			newBean.setName(name);
		return newBean;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Bean getBean(String name) {
		return getBean(name, false, false);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Bean getBean(String name, boolean ignoreCase, boolean createOnDemand) {
		beanLoop: for (Bean bean : getBeans()) {
			if (name != null
					&& !(ignoreCase ? name.equalsIgnoreCase(bean.getName()) : name.equals(bean.getName())))
				continue beanLoop;
			return bean;
		}
		return createOnDemand ? createBean(name) : null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<HomeInterface> getHomeInterfaces() {
		if (homeInterfaces == null) {
			homeInterfaces = new EObjectContainmentEList<HomeInterface>(HomeInterface.class, this,
					J2EEPackage.PACKAGE__HOME_INTERFACE);
		}
		return homeInterfaces;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public HomeInterface createHomeInterface(String name) {
		HomeInterface newHomeInterface = (HomeInterface)create(J2EEPackage.Literals.HOME_INTERFACE);
		getHomeInterfaces().add(newHomeInterface);
		if (name != null)
			newHomeInterface.setName(name);
		return newHomeInterface;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public HomeInterface getHomeInterface(String name) {
		return getHomeInterface(name, false, false);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public HomeInterface getHomeInterface(String name, boolean ignoreCase, boolean createOnDemand) {
		homeInterfaceLoop: for (HomeInterface homeInterface : getHomeInterfaces()) {
			if (name != null && !(ignoreCase ? name.equalsIgnoreCase(homeInterface.getName())
					: name.equals(homeInterface.getName())))
				continue homeInterfaceLoop;
			return homeInterface;
		}
		return createOnDemand ? createHomeInterface(name) : null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<Finder> getFinders() {
		if (finders == null) {
			finders = new EObjectContainmentEList<Finder>(Finder.class, this, J2EEPackage.PACKAGE__FINDER);
		}
		return finders;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Finder createFinder(String name) {
		Finder newFinder = (Finder)create(J2EEPackage.Literals.FINDER);
		getFinders().add(newFinder);
		if (name != null)
			newFinder.setName(name);
		return newFinder;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Finder getFinder(String name) {
		return getFinder(name, false, false);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Finder getFinder(String name, boolean ignoreCase, boolean createOnDemand) {
		finderLoop: for (Finder finder : getFinders()) {
			if (name != null && !(ignoreCase ? name.equalsIgnoreCase(finder.getName())
					: name.equals(finder.getName())))
				continue finderLoop;
			return finder;
		}
		return createOnDemand ? createFinder(name) : null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case J2EEPackage.PACKAGE__BEAN:
				return ((InternalEList<?>)getBeans()).basicRemove(otherEnd, msgs);
			case J2EEPackage.PACKAGE__HOME_INTERFACE:
				return ((InternalEList<?>)getHomeInterfaces()).basicRemove(otherEnd, msgs);
			case J2EEPackage.PACKAGE__FINDER:
				return ((InternalEList<?>)getFinders()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case J2EEPackage.PACKAGE__MEMBER:
				return getMembers();
			case J2EEPackage.PACKAGE__BEAN:
				return getBeans();
			case J2EEPackage.PACKAGE__HOME_INTERFACE:
				return getHomeInterfaces();
			case J2EEPackage.PACKAGE__FINDER:
				return getFinders();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case J2EEPackage.PACKAGE__BEAN:
				getBeans().clear();
				getBeans().addAll((Collection<? extends Bean>)newValue);
				return;
			case J2EEPackage.PACKAGE__HOME_INTERFACE:
				getHomeInterfaces().clear();
				getHomeInterfaces().addAll((Collection<? extends HomeInterface>)newValue);
				return;
			case J2EEPackage.PACKAGE__FINDER:
				getFinders().clear();
				getFinders().addAll((Collection<? extends Finder>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case J2EEPackage.PACKAGE__BEAN:
				getBeans().clear();
				return;
			case J2EEPackage.PACKAGE__HOME_INTERFACE:
				getHomeInterfaces().clear();
				return;
			case J2EEPackage.PACKAGE__FINDER:
				getFinders().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case J2EEPackage.PACKAGE__MEMBER:
				return isSetMembers();
			case J2EEPackage.PACKAGE__BEAN:
				return beans != null && !beans.isEmpty();
			case J2EEPackage.PACKAGE__HOME_INTERFACE:
				return homeInterfaces != null && !homeInterfaces.isEmpty();
			case J2EEPackage.PACKAGE__FINDER:
				return finders != null && !finders.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetMembers() {
		return eIsSet(J2EEPackage.PACKAGE__BEAN) || eIsSet(J2EEPackage.PACKAGE__HOME_INTERFACE)
				|| eIsSet(J2EEPackage.PACKAGE__FINDER);
	}

} //PackageImpl
