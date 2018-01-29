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

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.eclipse.emf.common.util.TreeIterator;

/**
 * Static utility methods for working with {@link TreeIterator}s as for {@link Iterator}s in the Guava
 * {@link Iterators} API.
 *
 * @author Christian W. Damus
 * @since 3.6
 */
public final class TreeIterators {

	/** The shared empty iterator instance. */
	private static final TreeIterator<Object> EMPTY_ITERATOR = new Empty<>();

	/**
	 * Not instantiable by clients.
	 */
	private TreeIterators() {
		super();
	}

	/**
	 * Obtains an empty tree iterator.
	 * 
	 * @return an empty tree iterator if either argument is {@code null}
	 * @param <E>
	 *            the iterator element type
	 */
	@SuppressWarnings("unchecked")
	public static <E> TreeIterator<E> emptyIterator() {
		return (TreeIterator<E>)EMPTY_ITERATOR;
	}

	/**
	 * Obtains an immutable filtering view of a tree {@code iterator}.
	 * 
	 * @param iterator
	 *            the iterator to filter
	 * @param filter
	 *            the filter
	 * @return the filtering tree iterator
	 * @throws NullPointerException
	 *             if either argument is {@code null}
	 * @param <E>
	 *            the iterator element type
	 */
	public static <E> TreeIterator<E> filter(TreeIterator<E> iterator, Predicate<? super E> filter) {
		// CHECKSTYLE:OFF
		Preconditions.checkNotNull(iterator, "iterator"); //$NON-NLS-1$
		Preconditions.checkNotNull(filter, "filter"); //$NON-NLS-1$
		// CHECKSTYLE:ON

		return new Filtering<>(iterator, filter);
	}

	/**
	 * Obtains an immutable filtering view of a tree {@code iterator} for elements of the given {@code type}.
	 * 
	 * @param iterator
	 *            the iterator to filter
	 * @param type
	 *            the type of elements to filter out of the tree iterator
	 * @return the filtering tree iterator
	 * @throws NullPointerException
	 *             if either argument is {@code null}
	 * @param <E>
	 *            the iterator element type
	 * @param <T>
	 *            the filtered type
	 */
	public static <E, T extends E> TreeIterator<T> filter(TreeIterator<E> iterator, Class<? extends T> type) {
		// CHECKSTYLE:OFF
		Preconditions.checkNotNull(iterator, "iterator"); //$NON-NLS-1$
		Preconditions.checkNotNull(type, "type"); //$NON-NLS-1$
		// CHECKSTYLE:ON

		return new TypeFiltering<>(iterator, type);
	}

	/**
	 * Obtains an immutable transforming view of a tree {@code iterator}.
	 * 
	 * @param iterator
	 *            the iterator to filter
	 * @param transformation
	 *            the transformation function
	 * @return the transformation tree iterator
	 * @throws NullPointerException
	 *             if either argument is {@code null}
	 * @param <F>
	 *            the iterator element type
	 * @param <R>
	 *            the transformation result type
	 */
	public static <F, R> TreeIterator<R> transform(TreeIterator<F> iterator,
			Function<? super F, ? extends R> transformation) {

		// CHECKSTYLE:OFF
		Preconditions.checkNotNull(iterator, "iterator"); //$NON-NLS-1$
		Preconditions.checkNotNull(transformation, "transformation"); //$NON-NLS-1$
		// CHECKSTYLE:ON

		return new Transforming<>(iterator, transformation);
	}

	//
	// Nested types
	//

	/**
	 * An empty tree iterator.
	 * 
	 * @param <E>
	 *            the iterator element type
	 */
	private static final class Empty<E> implements TreeIterator<E> {
		/**
		 * Initializes me.
		 */
		Empty() {
			super();
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean hasNext() {
			return false;
		}

		/**
		 * {@inheritDoc}
		 */
		public E next() {
			throw new NoSuchElementException();
		}

		/**
		 * {@inheritDoc}
		 */
		public void remove() {
			throw new UnsupportedOperationException();
		}

		/**
		 * {@inheritDoc}
		 */
		public void prune() {
			// Prune is never expected to throw, so this is just a no-op
		}
	}

	/**
	 * An immutable filtering view of a tree {@code iterator}.
	 * 
	 * @param <E>
	 *            the iterator element type
	 */
	private static final class Filtering<E> extends AbstractIterator<E> implements TreeIterator<E> {
		/** The backing iterator. */
		private final TreeIterator<E> delegate;

		/** The filter condition. */
		private final Predicate<? super E> filter;

		/**
		 * Initializes me.
		 * 
		 * @param iterator
		 *            the iterator to filter
		 * @param filter
		 *            the filter
		 */
		Filtering(TreeIterator<E> iterator, Predicate<? super E> filter) {
			super();

			this.delegate = iterator;
			this.filter = filter;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected E computeNext() {
			E result;

			out: if (delegate.hasNext()) {
				do {
					// Be optimistic about this
					result = delegate.next();
					if (filter.apply(result)) {
						break out;
					}
				} while (delegate.hasNext());

				result = endOfData();
			} else {
				result = endOfData();
			}

			return result;
		}

		/**
		 * {@inheritDoc}
		 */
		public void prune() {
			delegate.prune();
		}
	}

	/**
	 * An immutable type-filtering view of a tree {@code iterator}.
	 * 
	 * @param <E>
	 *            the iterator element type
	 * @param <T>
	 *            the type filter
	 */
	private static final class TypeFiltering<E, T extends E> extends AbstractIterator<T> implements TreeIterator<T> {
		/** The backing iterator. */
		private final TreeIterator<E> delegate;

		/** The type filter. */
		private final Class<? extends T> type;

		/**
		 * Initializes me.
		 * 
		 * @param iterator
		 *            the iterator to filter
		 * @param type
		 *            the type filter
		 */
		TypeFiltering(TreeIterator<E> iterator, Class<? extends T> type) {
			super();

			this.delegate = iterator;
			this.type = type;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected T computeNext() {
			T result;

			out: if (delegate.hasNext()) {
				do {
					// Be optimistic about this
					E resultE = delegate.next();
					if (type.isInstance(resultE)) {
						result = type.cast(resultE);
						break out;
					}
				} while (delegate.hasNext());

				result = endOfData();
			} else {
				result = endOfData();
			}

			return result;
		}

		/**
		 * {@inheritDoc}
		 */
		public void prune() {
			delegate.prune();
		}
	}

	/**
	 * An immutable transforming view of a tree {@code iterator}.
	 * 
	 * @param <E>
	 *            the iterator element type
	 * @param <T>
	 *            the transformation result type
	 */
	private static final class Transforming<E, T> extends AbstractIterator<T> implements TreeIterator<T> {
		/** The backing iterator. */
		private final TreeIterator<E> delegate;

		/** The transformation function. */
		private final Function<? super E, ? extends T> transformation;

		/**
		 * Initializes me.
		 * 
		 * @param iterator
		 *            the iterator to transform
		 * @param transformation
		 *            the transformation
		 */
		Transforming(TreeIterator<E> iterator, Function<? super E, ? extends T> transformation) {
			super();

			this.delegate = iterator;
			this.transformation = transformation;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected T computeNext() {
			T result;

			if (delegate.hasNext()) {
				result = transformation.apply(delegate.next());
			} else {
				result = endOfData();
			}

			return result;
		}

		/**
		 * {@inheritDoc}
		 */
		public void prune() {
			delegate.prune();
		}
	}
}
