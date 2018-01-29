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
package org.eclipse.emf.facade.uml2.tests.j2ee.internal.operations;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.facade.uml2.tests.j2ee.NamedElement;
import org.eclipse.uml2.common.util.UML2Util;

/**
 * <!-- begin-user-doc --> A static utility class that provides operations related to '<em><b>Named
 * Element</b></em>' model objects. <!-- end-user-doc -->
 * <p>
 * The following operations are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.emf.facade.uml2.tests.j2ee.NamedElement#getQualifiedName() <em>Get Qualified
 * Name</em>}</li>
 * <li>{@link org.eclipse.emf.facade.uml2.tests.j2ee.NamedElement#getPackage() <em>Get
 * Package</em>}</li>
 * <li>{@link org.eclipse.emf.facade.uml2.tests.j2ee.NamedElement#setPackage(org.eclipse.emf.facade.uml2.tests.j2ee.Package)
 * <em>Set Package</em>}</li>
 * </ul>
 *
 * @generated
 */
public class NamedElementOperations {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected NamedElementOperations() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public static String getQualifiedName(NamedElement namedElement) {
		StringBuilder result = new StringBuilder();

		computeQualifiedName(namedElement, result);

		if (result.length() == 0) {
			return null;
		} else {
			return result.toString();
		}
	}

	/**
	 * Recursively compute the qualified name of an element, with heuristics for substitution of unique
	 * containment- and index-based segments where an element has no name.
	 * 
	 * @param namedElement
	 *            a named element (which may or may not have a name)
	 * @param result
	 *            its qualified name
	 */
	protected static void computeQualifiedName(NamedElement namedElement, StringBuilder result) {
		org.eclipse.emf.facade.uml2.tests.j2ee.Package package_ = namedElement.getPackage();
		if (package_ != null) {
			computeQualifiedName(package_, result);
		}

		if (result.length() > 0) {
			result.append('.');
		}

		String name = namedElement.getName();
		if (!UML2Util.isEmpty(name)) {
			result.append(name);
		} else {
			EReference containment = namedElement.eContainmentFeature();
			if (containment != null) {
				result.append('$').append(containment.getName());
			}

			int index;
			if (containment != null) {
				index = indexOf(namedElement.eContainer(), containment, namedElement);
			} else {
				Resource res = namedElement.eResource();
				if (res != null) {
					index = res.getContents().indexOf(namedElement);
				} else {
					index = -1;
				}
			}
			result.append('$').append(index);
		}
	}

	/**
	 * Determines the index of a {@code value} in some {@code feature} of an {@code owner}, which may or may
	 * not be a multi-valued (list) feature.
	 * 
	 * @param owner
	 *            the owner of a {@code feature}
	 * @param feature
	 *            a feature of the {@code owner} object, which may or may not be multi-valued
	 * @param value
	 *            a value purported to be in the {@code feature} of the {@code owner}
	 * @return the {@code value}'s index in the {@code feature} of the {@code owner}, where by convention the
	 *         sole value (if present) of a single-valued feature is at index zero
	 */
	protected static int indexOf(EObject owner, EStructuralFeature feature, Object value) {
		Object actual = owner.eGet(feature);

		if (actual instanceof EList<?>) {
			return ((EList<?>)actual).indexOf(value);
		} else if (UML2Util.safeEquals(actual, value)) {
			return 0;
		} else {
			return -1;
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public static org.eclipse.emf.facade.uml2.tests.j2ee.Package getPackage(
			NamedElement namedElement) {

		org.eclipse.emf.facade.uml2.tests.j2ee.Package result = null;
		EObject container = namedElement.eContainer();

		if (container instanceof org.eclipse.emf.facade.uml2.tests.j2ee.Package) {
			result = (org.eclipse.emf.facade.uml2.tests.j2ee.Package)container;
		}

		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	@SuppressWarnings("unchecked")
	public static void setPackage(NamedElement namedElement,
			org.eclipse.emf.facade.uml2.tests.j2ee.Package newPackage) {

		if (newPackage == null) {
			org.eclipse.emf.facade.uml2.tests.j2ee.Package current = namedElement.getPackage();
			if (current != null) {
				EcoreUtil.remove(namedElement);
			}
		} else {
			newPackage.eClass().getEAllContainments().stream()
					.filter(ref -> ref.getEReferenceType().isSuperTypeOf(namedElement.eClass())).findAny()
					.ifPresent(ref -> ((EList<NamedElement>)newPackage.eGet(ref)).add(namedElement));
		}
	}

} // NamedElementOperations
