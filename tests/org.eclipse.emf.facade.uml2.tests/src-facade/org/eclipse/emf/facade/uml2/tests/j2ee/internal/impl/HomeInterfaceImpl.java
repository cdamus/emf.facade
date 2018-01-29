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

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.facade.uml2.tests.j2ee.Bean;
import org.eclipse.emf.facade.uml2.tests.j2ee.HomeInterface;
import org.eclipse.emf.facade.uml2.tests.j2ee.J2EEPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Home Interface</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.emf.facade.uml2.tests.j2ee.internal.impl.HomeInterfaceImpl#getBean <em>Bean</em>}</li>
 * </ul>
 *
 * @generated
 */
public class HomeInterfaceImpl extends NamedElementImpl implements HomeInterface {
	/**
	 * The cached value of the '{@link #getBean() <em>Bean</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBean()
	 * @generated
	 * @ordered
	 */
	protected Bean bean;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected HomeInterfaceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return J2EEPackage.Literals.HOME_INTERFACE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Bean getBean() {
		if (bean != null && bean.eIsProxy()) {
			InternalEObject oldBean = (InternalEObject)bean;
			bean = (Bean)eResolveProxy(oldBean);
			if (bean != oldBean) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							J2EEPackage.HOME_INTERFACE__BEAN, oldBean, bean));
			}
		}
		return bean;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Bean basicGetBean() {
		return bean;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetBean(Bean newBean, NotificationChain msgs) {
		Bean oldBean = bean;
		bean = newBean;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					J2EEPackage.HOME_INTERFACE__BEAN, oldBean, newBean);
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
	public void setBean(Bean newBean) {
		if (newBean != bean) {
			NotificationChain msgs = null;
			if (bean != null)
				msgs = ((InternalEObject)bean).eInverseRemove(this, J2EEPackage.BEAN__HOME_INTERFACE,
						Bean.class, msgs);
			if (newBean != null)
				msgs = ((InternalEObject)newBean).eInverseAdd(this, J2EEPackage.BEAN__HOME_INTERFACE,
						Bean.class, msgs);
			msgs = basicSetBean(newBean, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, J2EEPackage.HOME_INTERFACE__BEAN, newBean,
					newBean));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case J2EEPackage.HOME_INTERFACE__BEAN:
				if (bean != null)
					msgs = ((InternalEObject)bean).eInverseRemove(this, J2EEPackage.BEAN__HOME_INTERFACE,
							Bean.class, msgs);
				return basicSetBean((Bean)otherEnd, msgs);
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
			case J2EEPackage.HOME_INTERFACE__BEAN:
				return basicSetBean(null, msgs);
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
			case J2EEPackage.HOME_INTERFACE__BEAN:
				if (resolve)
					return getBean();
				return basicGetBean();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case J2EEPackage.HOME_INTERFACE__BEAN:
				setBean((Bean)newValue);
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
			case J2EEPackage.HOME_INTERFACE__BEAN:
				setBean((Bean)null);
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
			case J2EEPackage.HOME_INTERFACE__BEAN:
				return bean != null;
		}
		return super.eIsSet(featureID);
	}

} //HomeInterfaceImpl
