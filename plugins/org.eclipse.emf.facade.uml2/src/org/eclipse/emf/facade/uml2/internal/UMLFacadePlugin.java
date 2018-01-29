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
package org.eclipse.emf.facade.uml2.internal;

import java.text.ChoiceFormat;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.osgi.framework.BundleContext;

/**
 * The bundle activator (plug-in) class.
 * 
 * @author Christian W. Damus
 */
public class UMLFacadePlugin extends Plugin {

	/** The plug-in ID. */
	public static final String PLUGIN_ID = "org.eclipse.emf.uml2.facade"; //$NON-NLS-1$

	/** This plug-in's shared instance. */
	private static UMLFacadePlugin plugin;

	/**
	 * Returns the shared instance.
	 * 
	 * @return the shared instance
	 */
	public static UMLFacadePlugin getDefault() {
		return plugin;
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
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
		UMLFacadePlugin instance = getDefault();
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
