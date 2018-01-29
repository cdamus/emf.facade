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
package org.eclipse.emf.facade.internal.util;

import com.google.common.base.Function;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Utility operations on {@link Optional}s that should have been provided by that API.
 *
 * @author Christian W. Damus
 */
public final class Optionals {

	/**
	 * Not instantiable by clients.
	 */
	private Optionals() {
		super();
	}

	/**
	 * Apply an action on the value of an {@code optional} if it is present, else run an alternative action if
	 * it isn't.
	 * 
	 * @param optional
	 *            an optional
	 * @param ifPresent
	 *            consumer of its value if there is one
	 * @param elseNot
	 *            action to run in case the {@code optional} is absent
	 * @param <T>
	 *            the optional type
	 */
	public static <T> void ifPresentElse(Optional<T> optional, Consumer<? super T> ifPresent,
			Runnable elseNot) {

		optional.ifPresent(ifPresent);
		if (!optional.isPresent()) {
			elseNot.run();
		}
	}

	/**
	 * Map an {@code optional} with whichever of two functions first provides a result, in order.
	 * 
	 * @param optional
	 *            an optional
	 * @param mapper1
	 *            the first map function
	 * @param mapper2
	 *            the second map function
	 * @return an optional with the result or the first function or the second, or just empty
	 * @param <T>
	 *            the optional type
	 * @param <R>
	 *            the mapper result type
	 */
	public static <T, R> Optional<R> map(Optional<T> optional, Function<? super T, ? extends R> mapper1,
			Function<? super T, ? extends R> mapper2) {

		Optional<R> result = optional.map(mapper1);
		if (!result.isPresent()) {
			result = optional.map(mapper2);
		}
		return result;
	}

	/**
	 * Perform a side-effect {@code action} if an {@code optional} is absent.
	 * 
	 * @param optional
	 *            an optional
	 * @param action
	 *            a side-effect to run if the {@code optional} is {@linkplain Optional#isPresent() absent}
	 * @return the {@code optional} for fluent call chaining
	 * @param <T>
	 *            the optional type
	 */
	public static <T> Optional<T> ifAbsent(Optional<T> optional, Runnable action) {
		if (!optional.isPresent()) {
			action.run();
		}
		return optional;
	}
}
