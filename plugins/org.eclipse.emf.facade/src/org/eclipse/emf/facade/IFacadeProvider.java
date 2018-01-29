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

import static org.eclipse.emf.facade.internal.DelegatingFacadeProviderFactory.ensuringFacadeObjects;

import com.google.common.base.Supplier;

import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.facade.internal.CompositeFacadeProvider;
import org.eclipse.emf.facade.internal.CompositeFacadeProviderFactory;

/**
 * A provider of façades for model elements that should be manipulated <em>in lieu of</em> the input model
 * elements. It is expected that façades for semantically comparable model elements will, themselves, be
 * likewise comparable.
 *
 * @author Christian W. Damus
 */
@FunctionalInterface
public interface IFacadeProvider {

	/**
	 * A façade provider that provides no façade for any input.
	 */
	IFacadeProvider NULL_PROVIDER = ignored -> null;

	/**
	 * Creates the façade object for the given underlying object in the model.
	 * 
	 * @param underlyingObject
	 *            an object in the underlying (actual) representation of the model
	 * @return its façade, or {@code null} if the underlying object does not have a representation in the
	 *         façade (in which case it is expected to be managed by some other façade object)
	 */
	EObject createFacade(EObject underlyingObject);

	/**
	 * Composes me with another façade provider to which I delegate when I cannot provider a façade for some
	 * underlying object. <strong>Note</strong> that if any provider in such a composite chain returns the
	 * {@linkplain FacadeObject#NULL null façade}, then that is taken as the result and subsequent providers
	 * in the chain are not consulted for a façade.
	 * 
	 * @param elseProvider
	 *            the provider to delegate to when I provide no façade
	 * @return the composed façade provider
	 */
	default IFacadeProvider compose(IFacadeProvider elseProvider) {
		return CompositeFacadeProvider.compose(this, elseProvider);
	}

	//
	// Nested types
	//

	/**
	 * A (registered) factory that creates a façade provider appropriate to some model that is an input to a
	 * comparison.
	 *
	 * @author Christian W. Damus
	 */
	interface Factory {

		/**
		 * A factory that always returns the {@linkplain IFacadeProvider#NULL_PROVIDER null} façade provider.
		 * 
		 * @see IFacadeProvider#NULL_PROVIDER
		 */
		IFacadeProvider.Factory NULL_FACTORY = new AbstractImpl(Integer.MIN_VALUE,
				() -> IFacadeProvider.NULL_PROVIDER) {

			@Override
			public boolean isFacadeProviderFactoryFor(ResourceSet resourceSet) {
				return true;
			}

			@Override
			public void setRanking(int ranking) {
				// Pass
			}
		};

		/**
		 * Obtains the façade provider.
		 * 
		 * @return the façade provider
		 */
		IFacadeProvider getFacadeProvider();

		/**
		 * Queries the ranking of this façade provider factory.
		 * 
		 * @return my ranking
		 */
		int getRanking();

		/**
		 * Sets the ranking of this façade provider factory.
		 * 
		 * @param ranking
		 *            my ranking
		 */
		void setRanking(int ranking);

		/**
		 * Queries whether the façade provider can create suitable façades for the given resource set. This
		 * resource set can be expected to have loaded resources that can be inspected for the kind of model
		 * that is being edited.
		 * 
		 * @param resourceSet
		 *            the resourceSet containing a model to be edited
		 * @return whether I can create façades for the model elements within the resource set
		 */
		boolean isFacadeProviderFactoryFor(ResourceSet resourceSet);

		/**
		 * Queries whether the façade provider can create suitable façades for the given {@code notifier} in a
		 * comparison.
		 * 
		 * @param notifier
		 *            one of the two or three sides in a comparison
		 * @return whether I can create façades for the model elements within the {@code notifier}
		 */
		default boolean isFacadeProviderFactoryFor(Notifier notifier) {
			boolean result = false;

			ResourceSet context = null;
			if (notifier instanceof EObject) {
				Resource resource = ((EObject)notifier).eResource();
				if (resource != null) {
					context = resource.getResourceSet();
				}
			} else if (notifier instanceof Resource) {
				context = ((Resource)notifier).getResourceSet();
			} else if (notifier instanceof ResourceSet) {
				context = (ResourceSet)notifier;
			}

			if (context != null) {
				result = isFacadeProviderFactoryFor(context);
			}

			return result;
		}

		/**
		 * Composes myself with another factory in ranking order (higher-ranked factory first).
		 * 
		 * @param otherFactory
		 *            another factory
		 * @return the composite factory
		 */
		default IFacadeProvider.Factory compose(IFacadeProvider.Factory otherFactory) {
			return CompositeFacadeProviderFactory.compose(this, otherFactory);
		}

		//
		// Nested types
		//

		/**
		 * An abstract superclass for façade provider factories.
		 *
		 * @author Christian W. Damus
		 */
		abstract class AbstractImpl implements IFacadeProvider.Factory {
			/** An optional supplier of the façade provider to be created. */
			private final Supplier<? extends IFacadeProvider> facadeProviderSupplier;

			/** My ranking. */
			private int ranking;

			/** The created façade provider. */
			private IFacadeProvider provider;

			/**
			 * Initializes me with my ranking and a supplier that can create the façade provider when it is
			 * required.
			 * 
			 * @param ranking
			 *            my ranking
			 * @param facadeProviderSupplier
			 *            the supplier of the façade provider, or {@code null} if the subclass overrides the
			 *            {@link #createProvider()} method
			 */
			protected AbstractImpl(int ranking, Supplier<? extends IFacadeProvider> facadeProviderSupplier) {
				super();

				if (facadeProviderSupplier == null) {
					this.facadeProviderSupplier = this::mustOverrideCreateProvider;
				} else {
					this.facadeProviderSupplier = facadeProviderSupplier;
				}

				this.ranking = ranking;
			}

			/**
			 * Initializes me with my ranking. The subclass must override the {@link #createProvider()} method
			 * to create the façade provider.
			 * 
			 * @param ranking
			 *            my ranking
			 */
			protected AbstractImpl(int ranking) {
				this(ranking, null);
			}

			/**
			 * Initializes me a supplier that can create the façade provider when it is required.
			 * 
			 * @param facadeProviderSupplier
			 *            the supplier of the façade provider, or {@code null} if the subclass overrides the
			 *            {@link #createProvider()} method
			 */
			protected AbstractImpl(Supplier<? extends IFacadeProvider> facadeProviderSupplier) {
				this(0, facadeProviderSupplier);
			}

			/**
			 * Initializes me. The subclass must override the {@link #createProvider()} method to create the
			 * façade provider.
			 */
			protected AbstractImpl() {
				this(null);
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public int getRanking() {
				return ranking;
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public void setRanking(int ranking) {
				this.ranking = ranking;
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public IFacadeProvider getFacadeProvider() {
				if (provider == null) {
					provider = createProvider();
				}

				return provider;
			}

			/**
			 * Create a new provider.
			 * 
			 * @return a new façade provider
			 */
			protected IFacadeProvider createProvider() {
				return facadeProviderSupplier.get();
			}

			/**
			 * Throws an exception indicating that the class must override the {@link #createProvider()}
			 * method because it did not provide a façade-provider supplier.
			 * 
			 * @return never
			 * @throws IllegalArgumentException
			 *             always
			 */
			private IFacadeProvider mustOverrideCreateProvider() {
				throw new IllegalArgumentException(String
						.format("class %s must override the createProvider() method", getClass().getName())); //$NON-NLS-1$
			}
		}

		/**
		 * A registry of {@linkplain IFacadeProvider.Factory façade provider factories}.
		 * 
		 * @noimplement This interface is not intended to be implemented by clients.
		 * @noextend This interface is not intended to be extended by clients.
		 */
		interface Registry {

			/**
			 * Obtains a factory that creates a façade provider suitable for the given resource set, which is
			 * a ranked delegation chain over {@linkplain #getFacadeProviderFactories(ResourceSet) all
			 * applicable providers}. <strong>Note</strong> that the delegation chain always stops at the
			 * first non-{@code null} façade that is provided, so a provider can assert ownership over an
			 * object and force it to be ommitted from the comparison (because it is indirectly covered by
			 * some other façade) by returning the {@linkplain FacadeObject#NULL null façade}.
			 * 
			 * @param resourceSet
			 *            a resource set
			 * @return the factory, never {@code null} but possibly the
			 *         {@linkplain IFacadeProvider.Factory#NULL_FACTORY null factory}
			 * @see #getFacadeProviderFactories(ResourceSet)
			 * @see IFacadeProvider#compose(IFacadeProvider)
			 */
			default IFacadeProvider.Factory getFacadeProviderFactory(ResourceSet resourceSet) {
				return ensuringFacadeObjects(getFacadeProviderFactories(resourceSet).stream() //
						.reduce(IFacadeProvider.Factory.NULL_FACTORY, //
								IFacadeProvider.Factory::compose));
			}

			/**
			 * Obtains the façade providers applicable to the given resource set, in rank order from highest
			 * to lowest rank.
			 * 
			 * @param resourceSet
			 *            a resource set
			 * @return applicable façade factories, in rank order, or an empty list if none
			 */
			List<IFacadeProvider.Factory> getFacadeProviderFactories(ResourceSet resourceSet);

			/**
			 * Registers a façade provider.
			 * 
			 * @param facadeProviderFactory
			 *            a façade provider factory to add to the registry
			 * @return the previously registered façade provider of the same class as the new factory, or
			 *         {@code null} if there was previous registration of that class
			 */
			IFacadeProvider.Factory add(IFacadeProvider.Factory facadeProviderFactory);

			/**
			 * Removes the registration of a façade provider of the given class.
			 * 
			 * @param className
			 *            name of a (possibly) registered façade provider factory
			 * @return the façade provider factory that was removed, or {@code null} if no provider of the
			 *         given class was registered
			 */
			IFacadeProvider.Factory remove(String className);

			/**
			 * Removes the registration of a façade provider of the given class.
			 * 
			 * @param factoryClass
			 *            type of a (possibly) registered façade provider factory
			 * @return the façade provider factory that was removed, or {@code null} if no provider of the
			 *         given class was registered
			 */
			default IFacadeProvider.Factory remove(Class<? extends IFacadeProvider.Factory> factoryClass) {
				return remove(factoryClass.getName());
			}

			/**
			 * Removes the registration of a façade provider of the same class as the given {@code factory}.
			 * 
			 * @param factory
			 *            a (possibly) registered façade provider factory
			 * @return the façade provider factory that was removed, or {@code null} if no provider of the
			 *         {@code factory}'s type was registered
			 */
			default IFacadeProvider.Factory remove(IFacadeProvider.Factory factory) {
				return remove(factory.getClass());
			}

			/**
			 * Clear the registry.
			 */
			void clear();
		}
	}

}
