/*******************************************************************************
 * Copyright (c) 2017 Christian W. Damus and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Christian W. Damus - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.facade.internal.expressions;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.facade.FacadeAdapter;

/**
 * Core expressions property tester for façade-related properties.
 *
 * @author Christian W. Damus
 */
public class FacadePropertyTester extends PropertyTester {

	/** The 'isFacade' property. */
	public static final String IS_FACADE = "isFacade"; //$NON-NLS-1$

	/**
	 * Initializes me.
	 */
	public FacadePropertyTester() {
		super();
	}

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		boolean result = false;

		switch (property) {
			case IS_FACADE:
				result = isFacade(receiver) == asBoolean(expectedValue);
				break;
			default:
				throw new IllegalArgumentException(property);
		}

		return result;
	}

	/**
	 * Obtains a value from the core expression as a boolean.
	 * 
	 * @param value
	 *            a value in the core expression
	 * @return its boolean value, or {@code false} if it has none
	 */
	protected static final boolean asBoolean(Object value) {
		if (value == null) {
			return true; // Default in a test expression is true
		} else if (value instanceof Boolean) {
			return ((Boolean)value).booleanValue();
		} else if (value instanceof String) {
			return Boolean.parseBoolean((String)value);
		} else {
			return false;
		}
	}

	/**
	 * Is the {@code receiver} a façade?
	 * 
	 * @param receiver
	 *            an object
	 * @return whether it is a façade
	 */
	protected boolean isFacade(Object receiver) {
		return (receiver instanceof EObject) && FacadeAdapter.isFacade((EObject)receiver);
	}

}
