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
package org.eclipse.emf.facade.uml2.tests.opaqexpr;

import java.util.Map;

import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Opaque Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.emf.facade.uml2.tests.opaqexpr.OpaqueExpression#getBodies <em>Body</em>}</li>
 *   <li>{@link org.eclipse.emf.facade.uml2.tests.opaqexpr.OpaqueExpression#getName <em>Name</em>}</li>
 * </ul>
 *
 * @see org.eclipse.emf.facade.uml2.tests.opaqexpr.OpaqexprPackage#getOpaqueExpression()
 * @model
 * @generated
 */
public interface OpaqueExpression extends EObject {
	/**
	 * Returns the value of the '<em><b>Body</b></em>' map.
	 * The key is of type {@link java.lang.String},
	 * and the value is of type {@link java.lang.String},
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Body</em>' map isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Body</em>' map.
	 * @see org.eclipse.emf.facade.uml2.tests.opaqexpr.OpaqexprPackage#getOpaqueExpression_Body()
	 * @model mapType="org.eclipse.emf.facade.uml2.tests.opaqexpr.BodyEntry&lt;org.eclipse.uml2.types.String, org.eclipse.uml2.types.String&gt;" ordered="false"
	 * @generated
	 */
	EMap<String, String> getBodies();

	/**
	 * Creates a new {@link java.util.Map.Entry} and appends it to the '<em><b>Body</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return The new {@link java.util.Map.Entry}.
	 * @see #getBodies()
	 * @generated
	 */
	Map.Entry<String, String> createBody();

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.eclipse.emf.facade.uml2.tests.opaqexpr.OpaqexprPackage#getOpaqueExpression_Name()
	 * @model dataType="org.eclipse.uml2.types.String" ordered="false"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.eclipse.emf.facade.uml2.tests.opaqexpr.OpaqueExpression#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

} // OpaqueExpression
