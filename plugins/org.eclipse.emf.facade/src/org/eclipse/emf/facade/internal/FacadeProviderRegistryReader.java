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
package org.eclipse.emf.facade.internal;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IRegistryChangeListener;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.plugin.RegistryReader;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.facade.IFacadeProvider;
import org.eclipse.osgi.util.NLS;

/**
 * A registry reader that watches the façade provider extension point.
 * 
 * @author Christian W. Damus
 */
class FacadeProviderRegistryReader extends RegistryReader {

	/** Attribute name for the factory implementation class. */
	public static final String CLASS = "class"; //$NON-NLS-1$

	/** Element name for the façade provider registration. */
	private static final String FACADE_PROVIDER_FACTORY = "facadeProviderFactory"; //$NON-NLS-1$

	/** Attribute name for the ranking. */
	private static final String RANKING = "ranking"; //$NON-NLS-1$

	/** Attribute name for the user-presentable label. */
	private static final String LABEL = "label"; //$NON-NLS-1$

	/** Attribute name for the user-presentable description. */
	private static final String DESCRIPTION = "description"; //$NON-NLS-1$

	/** The façade provider registry to which extensions will be registered. */
	private final IFacadeProvider.Factory.Registry facadeProviderRegistry;

	/**
	 * Initializes me with the façade provider registry that I maintain.
	 * 
	 * @param extensionRegistry
	 *            the Eclipse Platform extension registry
	 * @param pluginID
	 *            the plugin ID of the extension point to be monitored
	 * @param extensionPointID
	 *            the extension point ID to be monitored
	 * @param rfacadeProviderRegistry
	 *            the façade provider registry to populate and maintain
	 */
	public FacadeProviderRegistryReader(IExtensionRegistry extensionRegistry, String pluginID,
			String extensionPointID, IFacadeProvider.Factory.Registry facadeProviderRegistry) {

		super(createProxy(extensionRegistry), pluginID, extensionPointID);

		this.facadeProviderRegistry = facadeProviderRegistry;
	}

	/**
	 * Removes me from the Eclipse Platform extension registry.
	 */
	public void dispose() {
		((Disposable)pluginRegistry).dispose();
	}

	@Override
	protected boolean readElement(IConfigurationElement element, boolean add) {
		return add ? addElement(element) : removeElement(element);
	}

	protected boolean addElement(IConfigurationElement element) {
		boolean result = true;

		switch (element.getName()) {
			case FACADE_PROVIDER_FACTORY:
				if (element.getAttribute(CLASS) == null) {
					logMissingAttribute(element, CLASS);
					result = false;
				} else if (element.getAttribute(RANKING) == null) {
					logMissingAttribute(element, RANKING);
					result = false;
				} else {
					String rankingStr = element.getAttribute(RANKING);
					try {
						Integer.parseInt(rankingStr);
					} catch (NumberFormatException e) {
						logError(element, NLS.bind("{0} is not an integer: {1}", RANKING, rankingStr));
						result = false;
					}

					addFacadeProvider(element);
				}
				break;
			default:
				result = false;
				break;
		}

		return result;
	}

	protected void addFacadeProvider(IConfigurationElement element) {
		String label = element.getAttribute(LABEL);
		String description = element.getAttribute(DESCRIPTION);
		int rank = Integer.parseInt(element.getAttribute(RANKING));
		FactoryDescriptor descriptor = new FactoryDescriptor(element, CLASS, label, description, rank);

		IFacadeProvider.Factory previous = facadeProviderRegistry.add(descriptor);
		if (previous != null) {
			EMFFacadePlugin.log(IStatus.WARNING,
					NLS.bind("Duplicate facade provider extension: {0}", element.getAttribute(CLASS)), null);
		}
	}

	protected boolean removeElement(IConfigurationElement element) {
		facadeProviderRegistry.remove(element.getAttribute(CLASS));
		return true;
	}

	/**
	 * Because the EMF {@link RegistryReader} provides no API for stopping listening to the extension
	 * registry, and does not provide access to its listener, we proxy the extension registry to capture the
	 * listener and let it be removed.
	 * 
	 * @param extensionRegistry
	 *            the extension registry to proxy
	 * @return the proxy registry
	 */
	private static IExtensionRegistry createProxy(IExtensionRegistry extensionRegistry) {
		final List<IRegistryChangeListener> listeners = new ArrayList<>(3);

		return (IExtensionRegistry)Proxy.newProxyInstance(FacadeProviderRegistryImpl.class.getClassLoader(),
				new Class<?>[] {IExtensionRegistry.class, Disposable.class }, (proxy, method, args) -> {
					switch (method.getName()) {
						case "addRegistryChangeListener": //$NON-NLS-1$
							if (args != null && ((args.length == 1) || (args.length == 2))) {
								listeners.add((IRegistryChangeListener)args[0]);
								// And delegate, below
							}
							break;
						case "dispose": { //$NON-NLS-1$
							if ((args == null) || (args.length == 0)) {
								listeners.forEach(extensionRegistry::removeRegistryChangeListener);
								return null; // Do not delegate
							}
						}
						default:
							// Pass
							break;
					}

					return method.invoke(extensionRegistry, args);
				});
	}

	//
	// Nested types
	//

	/**
	 * Private interface for the disposable extension registry proxy.
	 */
	private interface Disposable {
		void dispose();
	}

	protected final class FactoryDescriptor extends PluginClassDescriptor implements IFacadeProvider.Factory {

		private final String label;

		private final String description;

		private final int ranking;

		private IFacadeProvider provider;

		private boolean providerFailed;

		/**
		 * Initializes me with my configuration {@code element} and the attribute that indicates to
		 * instantiate from the contributing plug-in class, plus other metadata from the extension point.
		 * 
		 * @param element
		 *            my configuration element
		 * @param attributeName
		 *            the class attribute
		 * @param label
		 *            my user-friendly label
		 * @param description
		 *            my user-friendly description
		 * @param ranking
		 *            my ranking relative to other extensions
		 */
		public FactoryDescriptor(IConfigurationElement element, String attributeName, String label,
				String description, int ranking) {
			super(element, attributeName);

			this.label = label;
			this.description = description;
			this.ranking = ranking;
		}

		@Override
		public IFacadeProvider getFacadeProvider() {
			if (provider == null && !providerFailed) {
				try {
					provider = (IFacadeProvider)createInstance();
				} catch (Exception e) {
					providerFailed = true;
					EMFFacadePlugin.log(e);
					logError(element,
							"Failed to instantiate facade provider extension: " + e.getLocalizedMessage());
				}
			}
			return provider;
		}

		@Override
		public int getRanking() {
			return ranking;
		}

		@Override
		public void setRanking(int ranking) {
			throw new UnsupportedOperationException("immutable extension descriptor"); //$NON-NLS-1$
		}

		@Override
		public boolean isFacadeProviderFactoryFor(ResourceSet resourceSet) {
			// TODO Auto-generated method stub
			return false;
		}

	}
}
