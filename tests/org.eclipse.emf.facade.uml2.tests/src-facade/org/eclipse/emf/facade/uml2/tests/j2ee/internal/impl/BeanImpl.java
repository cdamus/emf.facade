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
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.emf.facade.uml2.tests.j2ee.Bean;
import org.eclipse.emf.facade.uml2.tests.j2ee.BeanKind;
import org.eclipse.emf.facade.uml2.tests.j2ee.Finder;
import org.eclipse.emf.facade.uml2.tests.j2ee.HomeInterface;
import org.eclipse.emf.facade.uml2.tests.j2ee.J2EEPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Bean</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.emf.facade.uml2.tests.j2ee.internal.impl.BeanImpl#getKind <em>Kind</em>}</li>
 *   <li>{@link org.eclipse.emf.facade.uml2.tests.j2ee.internal.impl.BeanImpl#isAbstract <em>Is Abstract</em>}</li>
 *   <li>{@link org.eclipse.emf.facade.uml2.tests.j2ee.internal.impl.BeanImpl#getSuperclass <em>Superclass</em>}</li>
 *   <li>{@link org.eclipse.emf.facade.uml2.tests.j2ee.internal.impl.BeanImpl#getHomeInterface <em>Home Interface</em>}</li>
 *   <li>{@link org.eclipse.emf.facade.uml2.tests.j2ee.internal.impl.BeanImpl#getFinders <em>Finder</em>}</li>
 * </ul>
 *
 * @generated
 */
public class BeanImpl extends NamedElementImpl implements Bean {
	/**
	 * The default value of the '{@link #getKind() <em>Kind</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKind()
	 * @generated
	 * @ordered
	 */
	protected static final BeanKind KIND_EDEFAULT = BeanKind.SESSION;

	/**
	 * The cached value of the '{@link #getKind() <em>Kind</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKind()
	 * @generated
	 * @ordered
	 */
	protected BeanKind kind = KIND_EDEFAULT;

	/**
	 * The default value of the '{@link #isAbstract() <em>Is Abstract</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isAbstract()
	 * @generated
	 * @ordered
	 */
	protected static final boolean IS_ABSTRACT_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isAbstract() <em>Is Abstract</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isAbstract()
	 * @generated
	 * @ordered
	 */
	protected boolean isAbstract = IS_ABSTRACT_EDEFAULT;

	/**
	 * The cached value of the '{@link #getSuperclass() <em>Superclass</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSuperclass()
	 * @generated
	 * @ordered
	 */
	protected Bean superclass;

	/**
	 * The cached value of the '{@link #getHomeInterface() <em>Home Interface</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHomeInterface()
	 * @generated
	 * @ordered
	 */
	protected HomeInterface homeInterface;

	/**
	 * The cached value of the '{@link #getFinders() <em>Finder</em>}' reference list.
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
	protected BeanImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return J2EEPackage.Literals.BEAN;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BeanKind getKind() {
		return kind;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setKind(BeanKind newKind) {
		BeanKind oldKind = kind;
		kind = newKind == null ? KIND_EDEFAULT : newKind;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, J2EEPackage.BEAN__KIND, oldKind, kind));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isAbstract() {
		return isAbstract;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setIsAbstract(boolean newIsAbstract) {
		boolean oldIsAbstract = isAbstract;
		isAbstract = newIsAbstract;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, J2EEPackage.BEAN__IS_ABSTRACT,
					oldIsAbstract, isAbstract));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Bean getSuperclass() {
		if (superclass != null && superclass.eIsProxy()) {
			InternalEObject oldSuperclass = (InternalEObject)superclass;
			superclass = (Bean)eResolveProxy(oldSuperclass);
			if (superclass != oldSuperclass) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, J2EEPackage.BEAN__SUPERCLASS,
							oldSuperclass, superclass));
			}
		}
		return superclass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Bean basicGetSuperclass() {
		return superclass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSuperclass(Bean newSuperclass) {
		Bean oldSuperclass = superclass;
		superclass = newSuperclass;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, J2EEPackage.BEAN__SUPERCLASS, oldSuperclass,
					superclass));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public HomeInterface getHomeInterface() {
		if (homeInterface != null && homeInterface.eIsProxy()) {
			InternalEObject oldHomeInterface = (InternalEObject)homeInterface;
			homeInterface = (HomeInterface)eResolveProxy(oldHomeInterface);
			if (homeInterface != oldHomeInterface) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							J2EEPackage.BEAN__HOME_INTERFACE, oldHomeInterface, homeInterface));
			}
		}
		return homeInterface;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public HomeInterface basicGetHomeInterface() {
		return homeInterface;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetHomeInterface(HomeInterface newHomeInterface, NotificationChain msgs) {
		HomeInterface oldHomeInterface = homeInterface;
		homeInterface = newHomeInterface;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					J2EEPackage.BEAN__HOME_INTERFACE, oldHomeInterface, newHomeInterface);
			if (msgs == null)
				msgs = notification;
			else
				msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setHomeInterface(HomeInterface newHomeInterface) {
		if (newHomeInterface != homeInterface) {
			NotificationChain msgs = null;
			if (homeInterface != null)
				msgs = ((InternalEObject)homeInterface).eInverseRemove(this, J2EEPackage.HOME_INTERFACE__BEAN,
						HomeInterface.class, msgs);
			if (newHomeInterface != null)
				msgs = ((InternalEObject)newHomeInterface).eInverseAdd(this, J2EEPackage.HOME_INTERFACE__BEAN,
						HomeInterface.class, msgs);
			msgs = basicSetHomeInterface(newHomeInterface, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, J2EEPackage.BEAN__HOME_INTERFACE,
					newHomeInterface, newHomeInterface));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<Finder> getFinders() {
		if (finders == null) {
			finders = new EObjectWithInverseResolvingEList<Finder>(Finder.class, this,
					J2EEPackage.BEAN__FINDER, J2EEPackage.FINDER__BEAN);
		}
		return finders;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Finder getFinder(String name) {
		return getFinder(name, false);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Finder getFinder(String name, boolean ignoreCase) {
		finderLoop: for (Finder finder : getFinders()) {
			if (name != null && !(ignoreCase ? name.equalsIgnoreCase(finder.getName())
					: name.equals(finder.getName())))
				continue finderLoop;
			return finder;
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case J2EEPackage.BEAN__HOME_INTERFACE:
				if (homeInterface != null)
					msgs = ((InternalEObject)homeInterface).eInverseRemove(this,
							J2EEPackage.HOME_INTERFACE__BEAN, HomeInterface.class, msgs);
				return basicSetHomeInterface((HomeInterface)otherEnd, msgs);
			case J2EEPackage.BEAN__FINDER:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getFinders()).basicAdd(otherEnd,
						msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case J2EEPackage.BEAN__HOME_INTERFACE:
				return basicSetHomeInterface(null, msgs);
			case J2EEPackage.BEAN__FINDER:
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
			case J2EEPackage.BEAN__KIND:
				return getKind();
			case J2EEPackage.BEAN__IS_ABSTRACT:
				return isAbstract();
			case J2EEPackage.BEAN__SUPERCLASS:
				if (resolve)
					return getSuperclass();
				return basicGetSuperclass();
			case J2EEPackage.BEAN__HOME_INTERFACE:
				if (resolve)
					return getHomeInterface();
				return basicGetHomeInterface();
			case J2EEPackage.BEAN__FINDER:
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
			case J2EEPackage.BEAN__KIND:
				setKind((BeanKind)newValue);
				return;
			case J2EEPackage.BEAN__IS_ABSTRACT:
				setIsAbstract((Boolean)newValue);
				return;
			case J2EEPackage.BEAN__SUPERCLASS:
				setSuperclass((Bean)newValue);
				return;
			case J2EEPackage.BEAN__HOME_INTERFACE:
				setHomeInterface((HomeInterface)newValue);
				return;
			case J2EEPackage.BEAN__FINDER:
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
			case J2EEPackage.BEAN__KIND:
				setKind(KIND_EDEFAULT);
				return;
			case J2EEPackage.BEAN__IS_ABSTRACT:
				setIsAbstract(IS_ABSTRACT_EDEFAULT);
				return;
			case J2EEPackage.BEAN__SUPERCLASS:
				setSuperclass((Bean)null);
				return;
			case J2EEPackage.BEAN__HOME_INTERFACE:
				setHomeInterface((HomeInterface)null);
				return;
			case J2EEPackage.BEAN__FINDER:
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
			case J2EEPackage.BEAN__KIND:
				return kind != KIND_EDEFAULT;
			case J2EEPackage.BEAN__IS_ABSTRACT:
				return isAbstract != IS_ABSTRACT_EDEFAULT;
			case J2EEPackage.BEAN__SUPERCLASS:
				return superclass != null;
			case J2EEPackage.BEAN__HOME_INTERFACE:
				return homeInterface != null;
			case J2EEPackage.BEAN__FINDER:
				return finders != null && !finders.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy())
			return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (kind: "); //$NON-NLS-1$
		result.append(kind);
		result.append(", isAbstract: "); //$NON-NLS-1$
		result.append(isAbstract);
		result.append(')');
		return result.toString();
	}

} //BeanImpl
