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
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.DelegatingEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * A delegating reference list that wraps elements as dynamic façades proxies.
 *
 * @param <E>
 *            the list's element type
 * @author Christian W. Damus
 */
final class ProxyEcoreEList<E> //
		extends DelegatingEList<E> //
		implements InternalEList.Unsettable<E>, EStructuralFeature.Setting {

	private static final long serialVersionUID = 1L;

	/** My owner, as a proxy. */
	private final FacadeObject owner;

	/** The backing list. */
	private final EcoreEList<E> delegate;

	/** Am I the value of a reference feature? */
	private final boolean isReference;

	/**
	 * Initializes me with my backing list.
	 * 
	 * @param owner
	 *            my owner, as a proxy
	 * @param delegate
	 *            my backing list
	 */
	ProxyEcoreEList(FacadeObject owner, EcoreEList<E> delegate) {
		super();

		this.owner = owner;
		this.delegate = delegate;
		this.isReference = delegate.getEStructuralFeature() instanceof EReference;
	}

	/**
	 * Wrap an {@code object} in the list as a dynamic façade proxy.
	 * 
	 * @param object
	 *            an object
	 * @return its dynamic façade proxy wrapper
	 */
	@SuppressWarnings("unchecked")
	E wrap(E object) {
		if (!isReference || !FacadeAdapter.isFacade((EObject)object)) {
			return object;
		} else {
			return (E)FacadeProxy.createProxy((EObject)object);
		}
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
	@Override
	protected E resolve(int index, E object) {
		return wrap(object);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected E validate(int index, E object) {
		E result = super.validate(index, object);

		if (isReference) {
			result = (E)FacadeProxy.unwrap((EObject)result);
		}

		return result;
	}

	//
	// Internal EMF list APIs
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EObject getEObject() {
		return owner;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EStructuralFeature getEStructuralFeature() {
		return delegate.getEStructuralFeature();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object get(boolean resolve) {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void set(Object newValue) {
		clear();
		addAll((List<? extends E>)newValue);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSet() {
		return delegate.isSet();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void unset() {
		delegate.unset();
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
	@Override
	public Object[] basicToArray() {
		return super.toArray();
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T[] basicToArray(T[] array) {
		return super.toArray(array);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int basicIndexOf(Object object) {
		return super.indexOf(object);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int basicLastIndexOf(Object object) {
		return super.lastIndexOf(object);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean basicContains(Object object) {
		return super.contains(object);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean basicContainsAll(Collection<?> collection) {
		return super.containsAll(collection);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<E> basicIterator() {
		return super.basicIterator();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ListIterator<E> basicListIterator() {
		return super.basicListIterator();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ListIterator<E> basicListIterator(int index) {
		return super.basicListIterator(index);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public E basicGet(int index) {
		return super.basicGet(index);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected E primitiveGet(int index) {
		return wrap(super.primitiveGet(index));
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<E> basicList() {
		List<E> result = super.basicList();

		if (isReference) {
			// They need to be wrapped, but not by me
			result = (List<E>)new ProxyEList<>((EList<EObject>)result);
		}

		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NotificationChain basicAdd(E object, NotificationChain notifications) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NotificationChain basicRemove(Object object, NotificationChain notifications) {
		throw new UnsupportedOperationException();
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
		} else if (!isReference) {
			return super.addAllUnique(index, collection);
		} else {
			@SuppressWarnings("unchecked")
			Collection<? extends E> unwrapped = (Collection<? extends E>)FacadeProxy
					.unwrap((Collection<? extends EObject>)collection);
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

}
