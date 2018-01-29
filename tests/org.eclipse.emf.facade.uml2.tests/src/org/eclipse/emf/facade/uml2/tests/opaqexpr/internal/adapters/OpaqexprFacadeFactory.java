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
package org.eclipse.emf.facade.uml2.tests.opaqexpr.internal.adapters;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.facade.SyncDirectionKind;
import org.eclipse.emf.facade.uml2.tests.opaqexpr.OpaqexprFactory;
import org.eclipse.emf.facade.uml2.tests.opaqexpr.OpaqueExpression;
import org.eclipse.uml2.uml.util.UMLSwitch;

/**
 * The factory for initialization of an opaque-expression façade from the UML model.
 *
 * @author Christian W. Damus
 */
public class OpaqexprFacadeFactory extends UMLSwitch<EObject> {

	/**
	 * Initializes me.
	 */
	public OpaqexprFacadeFactory() {
		super();
	}

	/**
	 * Obtains the façade for an opaque expression in the UML model.
	 * 
	 * @param opaqexpr
	 *            an opaque expression in the UML model
	 * @return its façade
	 */
	public static OpaqueExpression create(org.eclipse.uml2.uml.OpaqueExpression opaqexpr) {

		OpaqueExpression result = null;

		OpaqueExpressionAdapter adapter = OpaqueExpressionAdapter.get(opaqexpr);
		if (adapter == null) {
			result = OpaqexprFactory.eINSTANCE.createOpaqueExpression();
			OpaqueExpressionAdapter.connect(result, opaqexpr).initialSync(SyncDirectionKind.TO_FACADE);
		} else {
			result = adapter.getFacade();
		}

		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EObject caseOpaqueExpression(org.eclipse.uml2.uml.OpaqueExpression object) {
		return create(object);
	}

}
