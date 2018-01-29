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

import com.google.common.collect.Iterators;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.util.DelegatingEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * A simple delegating list that wraps elements as dynamic façades proxies.
 *
 * @param <E>
 *            the list's element type
 * @author Christian W. Damus
 */
final class ProxyEList<E extends EObject> extends DelegatingEList<E> {

	private static final long serialVersionUID = 1L;

	/** The backing list. */
	private final EList<E> delegate;

	/**
	 * Initializes me with my backing list.
	 * 
	 * @param delegate
	 *            my backing list
	 */
	ProxyEList(EList<E> delegate) {
		super();

		this.delegate = delegate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected List<E> delegateList() {
		return delegate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected int delegateIndexOf(Object object) {
		if (object instanceof EObject) {
			return super.delegateIndexOf(FacadeProxy.unwrap((EObject)object));
		}
		return super.delegateIndexOf(object);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected E resolve(int index, E object) {
		return (E)FacadeProxy.createProxy(object);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected E validate(int index, E object) {
		return FacadeProxy.unwrap(super.validate(index, object));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addAllUnique(Collection<? extends E> collection) {
		return addAllUnique(size(), collection);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addAllUnique(int index, Collection<? extends E> collection) {
		++modCount;

		if (collection.isEmpty()) {
			return false;
		} else {
			Collection<? extends E> unwrapped = FacadeProxy.unwrap(collection);
			delegateList().addAll(unwrapped);

			int i = index;
			for (E object : unwrapped) {
				didAdd(i, object);
				didChange();
				i++;
			}

			return true;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] toArray() {
		return Iterators.toArray(iterator(), Object.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T[] toArray(T[] array) {
		T[] result = array;

		if (result.length < size()) {
			result = (T[])Array.newInstance(array.getClass().getComponentType(), size());
		}

		int i = 0;
		for (E next : this) {
			result[i++] = (T)next;
		}
		if (i < array.length) {
			result[i] = null; // Terminate the array
		}

		return result;
	}
}
