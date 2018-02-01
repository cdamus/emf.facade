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

import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.facade.IFacadeProvider;
import org.osgi.framework.BundleContext;

/**
 * The bundle activator (plug-in) class.
 * 
 * @author Christian W. Damus
 */
public class EMFFacadePlugin extends EMFPlugin {

	/** The plug-in ID. */
	public static final String PLUGIN_ID = "org.eclipse.emf.facade"; //$NON-NLS-1$

	/** The id of the façade provider extension point. */
	public static final String FACADE_PROVIDER_PPID = "facadeProvider"; //$NON-NLS-1$

	/**
	 * The singleton instance of the plugin.
	 */
	public static final EMFFacadePlugin INSTANCE = new EMFFacadePlugin();

	/** This plug-in's shared instance. */
	private static Implementation plugin;

	/** Whether to use dynamic proxies, which by default is {@code true}. */
	private static boolean useDynamicProxies = true;

	/** The registry that keeps references to façade provider factories. */
	private IFacadeProvider.Factory.Registry facadeProviderRegistry;

	/** A registry reader to watch the façade provider extension point. */
	private FacadeProviderRegistryReader facadeProviderRegistryReader;

	/**
	 * Creates the singleton instance.
	 */
	private EMFFacadePlugin() {
		super(new ResourceLocator[] {});
	}

	@Override
	public ResourceLocator getPluginResourceLocator() {
		return plugin;
	}

	/**
	 * Obtains the Eclipse bundle activator.
	 * 
	 * @return the implementation
	 */
	public static Implementation getPlugin() {
		return plugin;
	}

	public IFacadeProvider.Factory.Registry getFacadeProviderRegistry() {
		return facadeProviderRegistry;
	}

	/**
	 * Initialize the extension-based façade provider registry.
	 * 
	 * @param registry
	 *            {@link IExtensionRegistry} to listen to in order to fill the registry
	 */
	private void createFacadeProviderRegistry(IExtensionRegistry registry) {
		facadeProviderRegistry = new FacadeProviderRegistryImpl();
		facadeProviderRegistryReader = new FacadeProviderRegistryReader(registry, PLUGIN_ID,
				FACADE_PROVIDER_PPID, facadeProviderRegistry);
		facadeProviderRegistryReader.readRegistry();
	}

	/**
	 * Discard the extension-based façade provider registry.
	 * 
	 * @param registry
	 *            IExtensionRegistry to remove listener(s) from
	 */
	private void disposeFacadeProviderRegistry(final IExtensionRegistry registry) {
		facadeProviderRegistryReader.dispose();
		facadeProviderRegistryReader = null;
		facadeProviderRegistry = null;
	}

	/**
	 * Queries whether dynamic proxies are supplied for façade models that do not implement the
	 * {@code FacadeObject} interface.
	 * 
	 * @return whether dynamic proxies are provided on behalf of {@link IFacadeProvider}s
	 */
	public static boolean isUseDynamicProxies() {
		return useDynamicProxies;
	}

	/**
	 * Sets whether dynamic proxies are supplied for façade models that do not implement the
	 * {@code FacadeObject} interface.
	 * 
	 * @param useDynamicProxies
	 *            whether dynamic proxies are provided on behalf of {@link IFacadeProvider}s
	 */
	public static void setUseDynamicProxies(boolean useDynamicProxies) {
		EMFFacadePlugin.useDynamicProxies = useDynamicProxies;
	}

	//
	// Nested types
	//

	/**
	 * The bundle activator implementation of the plug-in.
	 * 
	 * @author Christian W. Damus
	 */
	public static final class Implementation extends EclipsePlugin {

		public Implementation() {
			super();
		}

		@Override
		public void start(BundleContext context) throws Exception {
			super.start(context);
			plugin = this;

			IExtensionRegistry registry = Platform.getExtensionRegistry();
			INSTANCE.createFacadeProviderRegistry(registry);
		}

		@Override
		public void stop(BundleContext context) throws Exception {
			IExtensionRegistry registry = Platform.getExtensionRegistry();

			INSTANCE.disposeFacadeProviderRegistry(registry);

			plugin = null;

			super.stop(context);
		}
	}
}
