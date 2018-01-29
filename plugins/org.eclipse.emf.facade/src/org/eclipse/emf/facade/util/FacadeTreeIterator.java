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
package org.eclipse.emf.facade.util;

import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.AbstractTreeIterator;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.facade.FacadeObject;
import org.eclipse.emf.facade.IFacadeProvider;

/**
 * An iterator over {@link EObject} content that substitutes façades where they are available for underlying
 * elements and, when an element is replaced by a façade, iterates the façade's subtree instead of the
 * original element's.
 *
 * @author Christian W. Damus
 */
public class FacadeTreeIterator implements TreeIterator<EObject> {
	/** The façade provider factory that I use to get façade providers for each resource. */
	private final IFacadeProvider.Factory facadeProviderFactory;

	/** The façade provider that I use on resources that have façades. */
	private final IFacadeProvider facadeProvider;

	/** The top sub-tree iterator in the prune stack. */
	private Iterator<? extends EObject> current;

	/** A stack of iterators to resume after completion of a prune substitution. */
	private List<TreeIterator<? extends EObject>> pruneStack = Lists.newArrayListWithExpectedSize(3);

	/** The current state of the iteration. */
	private FacadeTreeIterator.State state = State.INITIAL;

	/** The prepared next object to return from the iteration. */
	private EObject preparedNext;

	public FacadeTreeIterator(IFacadeProvider.Factory facadeProviderFactory, ResourceSet resourceSet) {
		super();

		this.facadeProviderFactory = facadeProviderFactory;
		this.facadeProvider = facadeProviderFactory.getFacadeProvider();

		this.current = new ResourceSetIterator(resourceSet);
	}

	public FacadeTreeIterator(IFacadeProvider.Factory facadeProviderFactory, Resource resource) {
		super();

		this.facadeProviderFactory = facadeProviderFactory;
		this.facadeProvider = facadeProviderFactory.getFacadeProvider();

		this.current = new FacadeObjectIterator(resource.getContents());
	}

	public FacadeTreeIterator(IFacadeProvider.Factory facadeProviderFactory, EObject eObject) {
		super();

		this.facadeProviderFactory = facadeProviderFactory;
		this.facadeProvider = facadeProviderFactory.getFacadeProvider();

		this.current = Iterators.singletonIterator(eObject);
	}

	public FacadeTreeIterator(IFacadeProvider.Factory facadeProviderFactory,
			Iterable<? extends EObject> eObjects) {

		super();

		this.facadeProviderFactory = facadeProviderFactory;
		this.facadeProvider = facadeProviderFactory.getFacadeProvider();

		this.current = eObjects.iterator();
	}

	/**
	 * Initializes me with my delegate.
	 * 
	 * @param delegate
	 *            the underlying (raw) content iterator
	 */
	private FacadeTreeIterator(FacadeTreeIterator parent, Iterator<? extends EObject> delegate) {
		super();

		this.facadeProviderFactory = parent.facadeProviderFactory;
		this.facadeProvider = parent.facadeProvider;

		this.current = delegate;
	}

	protected final Iterator<? extends EObject> getChildren(EObject eObject) {
		Iterator<? extends EObject> result;
		Iterator<? extends EObject> raw = doGetChildren(eObject);

		if (raw instanceof TreeIterator<?>) {
			result = new FacadeTreeIterator(this, raw);
		} else {
			// Any elements that aren't façades are taken as is
			result = Iterators.transform(raw, this::facadeElse);
		}

		return result;
	}

	protected Iterator<? extends EObject> doGetChildren(EObject eObject) {
		return eObject.eContents().iterator();
	}

	/**
	 * Pops the tree-iterator to resume after finishing a façade sub-tree.
	 * 
	 * @return the tree iterator to resume, or {@code null} if iteration is finished
	 */
	protected final TreeIterator<? extends EObject> pop() {
		TreeIterator<? extends EObject> result = null;

		if (!pruneStack.isEmpty()) {
			result = pruneStack.remove(pruneStack.size() - 1);
			current = result;
		}

		return result;
	}

	/**
	 * If an {@code iterator} is a tree iterator, prunes it and pushes it onto the stack for resumption later
	 * after we have finished with the façade sub-tree.
	 * 
	 * @param iterator
	 *            an iterator to prune and push, if it is a tree iterator
	 * @return {@code true} if the {@code iterator} could be pruned and pushed; {@code false} otherwise, in
	 *         which case it should continue to be the current
	 */
	protected final boolean push(Iterator<? extends EObject> iterator) {
		boolean result = false;

		// We can only push tree iterators for pruning
		if (iterator instanceof TreeIterator<?>) {
			TreeIterator<? extends EObject> subtree = (TreeIterator<? extends EObject>)iterator;
			subtree.prune();
			result = pruneStack.add(subtree);
		}

		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasNext() {
		boolean result;

		if (state.isDone()) {
			result = false;
		} else if (state.isPrepared()) {
			result = true;
		} else {
			prepareNext();
			result = hasNext();
		}

		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final EObject next() {
		EObject result;

		if (!hasNext()) {
			throw new NoSuchElementException();
		}

		result = preparedNext;
		state = state.returned();
		preparedNext = null;

		return result;
	}

	/** Prepares the next value to return. */
	private void prepareNext() {
		preparedNext = computeNext();
	}

	/**
	 * Signals that there is nothing left to iterate.
	 * 
	 * @return a dummy, useful as the return result from {@link #computeNext()}
	 */
	final EObject endOfData() {
		state = State.DONE;
		return null;
	}

	EObject computeNext() {
		EObject result;

		if (current == null) {
			// Pop one off the stack, if available
			if (pop() != null) {
				result = computeNext();
			} else {
				result = endOfData();
			}
		} else {
			if (current.hasNext()) {
				result = current.next();

				EObject facade = facadeElse(result);
				if (facade == result) {
					// Just a normal iteration step
					state = state.prepared();
				} else {
					// Got a facade. Substitute it for the result and the sub-tree, too
					result = facade;

					if (push(current)) {
						current = getChildren(facade);
						state = state.substituted();
					} else {
						// Can't prune? Have to hope that the children of the original map
						// one-for-one to façades, then
					}
				}
			} else {
				current = null;
				result = computeNext();
			}
		}

		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void prune() {
		switch (state) {
			case RETURNED:
				if (current instanceof TreeIterator<?>) {
					((TreeIterator<?>)current).prune();
				}
				break;
			case RETURNED_SUBSTITUTE:
				// The current iterator is that object's contents, so skip it entirely
				current = null;
				break;
			default:
				// Can only prune after a result was returned
				break;

		}
	}

	/**
	 * Obtain the façade for a {@code notifier} or else {@code null}.
	 * 
	 * @param notifier
	 *            a notifier
	 * @return its corresponding façade or {@code null} if none
	 * @param <T>
	 *            the notifier type
	 */
	<T extends Notifier> T facade(T notifier) {
		return facadeElse(notifier, null);
	}

	/**
	 * Obtain the façade for a {@code notifier} or else just the original {@code notifier}, itself.
	 * 
	 * @param notifier
	 *            a notifier
	 * @return its corresponding façade or the {@code notifier} if none
	 * @param <T>
	 *            the notifier type
	 */
	<T extends Notifier> T facadeElse(T notifier) {
		return facadeElse(notifier, notifier);
	}

	/**
	 * Obtain the façade for a {@code notifier} or else a default result.
	 * 
	 * @param notifier
	 *            a notifier
	 * @param defaultResult
	 *            the return result in case the {@code notifier} has no corresponding façade
	 * @return its corresponding façade or the {@code defaultResult} if none
	 * @param <T>
	 *            the notifier type, either {@link ResourceSet}, {@link Resource}, or {@link EObject} but not
	 *            more specific than these because unerlying and façade types are necessarily different
	 */
	<T extends Notifier> T facadeElse(T notifier, T defaultResult) {
		T result = defaultResult;

		if (notifier instanceof EObject) {
			EObject facade = facadeProvider.createFacade((EObject)notifier);
			if ((facade != FacadeObject.NULL) && (facade != null)) {
				@SuppressWarnings("unchecked")
				T facadeAsT = (T)facade;
				result = facadeAsT;
			}
		}

		return result;
	}

	//
	// Nested types
	//

	@SuppressWarnings("serial")
	private class FacadeObjectIterator extends AbstractTreeIterator<EObject> {
		FacadeObjectIterator(Collection<? extends EObject> objects) {
			super(objects, false);
		}

		@Override
		protected Iterator<? extends EObject> getChildren(Object parent) {
			Iterable<EObject> result;

			if (parent instanceof EObject) {
				EObject eObject = (EObject)parent;
				result = Iterables.transform(eObject.eContents(), FacadeTreeIterator.this::facadeElse);
			} else if (parent instanceof Iterable<?>) {
				result = Iterables.filter((Iterable<?>)parent, EObject.class);
			} else {
				result = Collections.emptyList();
			}

			return result.iterator();
		}
	}

	/**
	 * An iterator over {@link ResourceSet} content.
	 *
	 * @author Christian W. Damus
	 */
	private class ResourceSetIterator extends AbstractIterator<EObject> implements TreeIterator<EObject> {
		private final EList<Resource> resources;

		private int resourceIndex = 0;

		/** The current root object's sub-tree. */
		private Iterator<? extends EObject> currentTree;

		/** The iterator to which to delegate a prune() call. */
		private TreeIterator<? extends EObject> pruneIterator;

		ResourceSetIterator(ResourceSet resourceSet) {
			super();

			this.resources = resourceSet.getResources();
		}

		@Override
		protected EObject computeNext() {
			EObject result = null;

			if (currentTree == null) {
				while (currentTree == null) {
					if (resourceIndex < resources.size()) {
						Resource nextResource = resources.get(resourceIndex++);
						Iterator<? extends EObject> thisTree = new FacadeObjectIterator(
								nextResource.getContents());

						if (thisTree.hasNext()) {
							result = thisTree.next();

							// Calling prune() now should remove the entire currentTree, not
							// just some portion of it
							pruneIterator = null;

							currentTree = thisTree;
						}
					} else {
						result = endOfData();
						break;
					}
				}
			} else {
				if (currentTree instanceof TreeIterator<?>) {
					// Now that we have stepped into this iterator, it can be pruned
					pruneIterator = (TreeIterator<? extends EObject>)currentTree;
				}

				if (currentTree.hasNext()) {
					result = currentTree.next();
				} else {
					currentTree = null;
					result = computeNext();
				}
			}

			return result;
		}

		@Override
		public void prune() {
			if (pruneIterator != null) {
				pruneIterator.prune();
			} else {
				// Just prune off the entire current sub-tree
				currentTree = null;
			}
		}
	}

	/**
	 * Enumeration of the possible states of the {@link FacadeTreeIterator}.
	 *
	 * @author Christian W. Damus
	 */
	enum State {
		/** We haven't yet computed anything. */
		INITIAL,
		/** We have prepared the next element to return. */
		PREPARED,
		/** We have returned an element and are ready to prepare another. */
		RETURNED,
		/** We have prepared the next element to return and it was a façade substitution. */
		PREPARED_SUBSTITUTE,
		/** We have returned a façade substitution and are ready to prepare the next element. */
		RETURNED_SUBSTITUTE,
		/** Iteration has finished; we have no more. */
		DONE;

		/**
		 * Queries whether I am the <em>done</em> state.
		 * 
		 * @return whether I am the <em>done</em> state
		 */
		boolean isDone() {
			return this == DONE;
		}

		/**
		 * Queries whether I am a kind of <em>prepared</em> state.
		 * 
		 * @return whether I am a kind of <em>prepared</em> state
		 */
		boolean isPrepared() {
			return (this == PREPARED) || (this == PREPARED_SUBSTITUTE);
		}

		/**
		 * Transitions from a <em>prepared</em> state to the corresponding <em>returned</em> state.
		 * 
		 * @return the <em>returned</em> state
		 * @throws IllegalStateException
		 *             if we cannot transition to a <em>returned</em> state from this
		 */
		FacadeTreeIterator.State returned() {
			switch (this) {
				case PREPARED:
					return RETURNED;
				case PREPARED_SUBSTITUTE:
					return RETURNED_SUBSTITUTE;
				default:
					throw new IllegalStateException("return from " + this); //$NON-NLS-1$
			}
		}

		/**
		 * Transitions to the <em>prepared</em> state.
		 * 
		 * @return the <em>prepared</em> state
		 * @throws IllegalStateException
		 *             if we cannot transition to the <em>prepared</em> state from this
		 */
		FacadeTreeIterator.State prepared() {
			switch (this) {
				case INITIAL:
				case RETURNED:
				case RETURNED_SUBSTITUTE:
					return PREPARED;
				default:
					throw new IllegalStateException("prepare from " + this); //$NON-NLS-1$
			}
		}

		/**
		 * Transitions to the <em>prepared substitution</em> state.
		 * 
		 * @return the <em>prepared substitution</em> state
		 * @throws IllegalStateException
		 *             if we cannot transition to the <em>prepared substitution</em> state from this
		 */
		FacadeTreeIterator.State substituted() {
			switch (this) {
				case INITIAL:
				case RETURNED:
				case RETURNED_SUBSTITUTE:
					return PREPARED_SUBSTITUTE;
				default:
					throw new IllegalStateException("substitute from " + this); //$NON-NLS-1$
			}
		}
	}
}
