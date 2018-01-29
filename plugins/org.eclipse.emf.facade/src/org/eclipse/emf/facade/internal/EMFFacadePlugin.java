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

import java.text.ChoiceFormat;

import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.facade.IFacadeProvider;
import org.osgi.framework.BundleContext;

/**
 * The bundle activator (plug-in) class.
 * 
 * @author Christian W. Damus
 */
public class EMFFacadePlugin extends Plugin {

	/** The plug-in ID. */
	public static final String PLUGIN_ID = "org.eclipse.emf.facade"; //$NON-NLS-1$

	/** The id of the façade provider extension point. */
	public static final String FACADE_PROVIDER_PPID = "facadeProvider"; //$NON-NLS-1$

	/** This plug-in's shared instance. */
	private static EMFFacadePlugin plugin;

	/** Whether to use dynamic proxies, which by default is {@code true}. */
	private static boolean useDynamicProxies = true;

	/** The registry that keeps references to façade provider factories. */
	private IFacadeProvider.Factory.Registry facadeProviderRegistry;

	/** A registry reader to watch the façade provider extension point. */
	private FacadeProviderRegistryReader facadeProviderRegistryReader;

	/**
	 * Obtains the shared instance.
	 * 
	 * @return the shared instance
	 */
	public static EMFFacadePlugin getDefault() {
		return plugin;
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		IExtensionRegistry registry = Platform.getExtensionRegistry();
		createFacadeProviderRegistry(registry);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		IExtensionRegistry registry = Platform.getExtensionRegistry();

		disposeFacadeProviderRegistry(registry);

		plugin = null;

		super.stop(context);
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

	/**
	 * Log a problem.
	 * 
	 * @param severity
	 *            the problem severity
	 * @param message
	 *            a cover message indicating the context and/or consequence of the problem
	 * @param exception
	 *            optionally, an exception that signalled the problem
	 */
	public static void log(int severity, String message, Throwable exception) {
		log(new Status(severity, PLUGIN_ID, message, exception));
	}

	/**
	 * Log an error.
	 * 
	 * @param message
	 *            a cover message indicating the context and/or consequence of the problem
	 * @param exception
	 *            optionally, an exception that signalled the problem
	 */
	public static void error(String message, Throwable exception) {
		log(IStatus.ERROR, message, exception);
	}

	/**
	 * Log a problem.
	 * 
	 * @param status
	 *            the problem description
	 */
	@SuppressWarnings("nls")
	public static void log(IStatus status) {
		EMFFacadePlugin instance = getDefault();
		if (instance != null) {
			instance.getLog().log(status);
		} else {
			ChoiceFormat choice = new ChoiceFormat(
					new double[] {IStatus.OK, IStatus.INFO, IStatus.WARNING, IStatus.ERROR },
					new String[] {"OK  ", "INFO ", "WARN ", "ERROR" });
			System.err.printf("[%s] %s%n", choice.format(status.getSeverity()), status.getMessage());
			if (status.getException() != null) {
				status.getException().printStackTrace(System.err);
			}
		}
	}

	/**
	 * Logs a plastic {@code problem}.
	 * 
	 * @param problem
	 *            some kind of indication or description of a problem
	 */
	public static void log(Object problem) {
		if (problem instanceof IStatus) {
			log((IStatus)problem);
		} else if (problem instanceof Diagnostic) {
			log(BasicDiagnostic.toIStatus((Diagnostic)problem));
		} else if (problem instanceof Throwable) {
			log(IStatus.ERROR, "Uncaught exception.", (Throwable)problem); //$NON-NLS-1$
		} else if (problem != null) {
			log(IStatus.ERROR, String.valueOf(problem), null);
		}
	}

}
