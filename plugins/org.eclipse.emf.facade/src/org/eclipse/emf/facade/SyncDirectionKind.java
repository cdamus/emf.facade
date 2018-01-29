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
package org.eclipse.emf.facade;

import java.util.function.Supplier;

import org.eclipse.emf.ecore.EObject;

/**
 * An enumeration of directions of synchronization between a façade and its underlying model.
 *
 * @author Christian W. Damus
 */
public enum SyncDirectionKind {
	/** Synchronize from the model to the façade. */
	TO_FACADE,
	/** Synchronize from the façade to the model. */
	TO_MODEL;

	/**
	 * Performs synchronization between the {@code facade} and the {@code model}.
	 * 
	 * @param adapter
	 *            the façade-adapter performing synchronization
	 * @param facade
	 *            a façade model element
	 * @param model
	 *            an underlying model element
	 * @param toFacadeSupplier
	 *            supplier of to-façade synchronizers, as needed
	 * @param toModelSupplier
	 *            supplier of to-model synchronizers, as needed
	 * @param <F>
	 *            the façade type
	 * @param <M>
	 *            the model type
	 */
	public <F extends EObject, M extends EObject> void sync(FacadeAdapter adapter, F facade, M model,
			Supplier<FacadeAdapter.Synchronizer> toFacadeSupplier,
			Supplier<FacadeAdapter.Synchronizer> toModelSupplier) {

		switch (this) {
			case TO_FACADE:
				toFacadeSupplier.get().synchronize(adapter, model, facade, null);
				break;
			case TO_MODEL:
				toModelSupplier.get().synchronize(adapter, facade, model, null);
				break;
			default:
				throw new IllegalArgumentException(this.name());
		}
	}
}
