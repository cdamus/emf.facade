/*******************************************************************************
 * Copyright (c) 2018 Christian W. Damus and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Christian W. Damus - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.facade.uml2.tests.framework;

import java.net.URL;
import java.util.Map;

import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.facade.uml2.tests.j2ee.J2EEPackage;
import org.eclipse.emf.facade.uml2.tests.j2ee.util.J2EEResource;
import org.eclipse.emf.facade.uml2.tests.j2ee.util.J2EEResourceFactoryImpl;
import org.eclipse.emf.facade.uml2.tests.j2eeprofile.J2EEProfilePackage;
import org.eclipse.emf.facade.uml2.tests.opaqexpr.OpaqexprPackage;
import org.eclipse.emf.facade.uml2.tests.opaqexpr.util.OpaqexprResource;
import org.eclipse.emf.facade.uml2.tests.opaqexpr.util.OpaqexprResourceFactoryImpl;
import org.eclipse.uml2.uml.UMLPlugin;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

/**
 * A test rule that ensures proper configuration of the EMF and EMF Façade registries for the duration.
 * 
 * @author Christian W. Damus
 */
@SuppressWarnings("nls")
public class RegistryRule extends TestWatcher {

	@Override
	protected void starting(Description description) {
		if (!EMFPlugin.IS_ECLIPSE_RUNNING) {
			EPackage.Registry.INSTANCE.put(J2EEPackage.eNS_URI, J2EEPackage.eINSTANCE);
			EPackage.Registry.INSTANCE.put(J2EEProfilePackage.eNS_URI, J2EEProfilePackage.eINSTANCE);
			EPackage.Registry.INSTANCE.put(OpaqexprPackage.eNS_URI, OpaqexprPackage.eINSTANCE);

			Map<String, Object> factories = Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap();
			factories.put(J2EEResource.FILE_EXTENSION, new J2EEResourceFactoryImpl());
			factories.put(OpaqexprResource.FILE_EXTENSION, new OpaqexprResourceFactoryImpl());

			UMLPlugin.getEPackageNsURIToProfileLocationMap().put(J2EEProfilePackage.eNS_URI,
					URI.createURI("pathmap://UML2_FACADE_TESTS/j2ee.profile.uml#_0"));

			// Map the base resource location
			URI baseURI = getModelsBaseURI();
			URIConverter.URI_MAP.put(URI.createURI("pathmap://UML2_FACADE_TESTS/"), baseURI);
		}
	}

	/**
	 * Determine the URI of the {@code model/} folder in the plug-in bundle, whether it is in source form in
	 * the workspace (accounting for classes being then in a {@code bin/} folder) or in binary form in an
	 * installed JAR.
	 * 
	 * @return the URI of the {@code model/} folder containing profiles and metamodels
	 */
	private static URI getModelsBaseURI() {
		URI result;

		URL classURL = RegistryRule.class.getResource("RegistryRule.class");
		URI classURI = URI.createURI(classURL.toExternalForm());

		int segments = classURI.segmentCount();
		int trim;
		for (trim = 1; trim < segments; trim++) {
			if (classURI.segment(segments - trim).equals("org")) {
				// Are we in a source project in the workspace?
				if ((trim < segments) && classURI.segment(segments - trim - 1).equals("bin")) {
					// Yup.
					trim++;
				}
				break;
			}
		}

		result = classURI.trimSegments(trim).appendSegment("model") //
				.appendSegment(""); // Ensure a trailing separator
		return result;
	}

	@Override
	protected void finished(Description description) {
		if (!EMFPlugin.IS_ECLIPSE_RUNNING) {
			URIConverter.URI_MAP.remove(URI.createURI("pathmap://UML2_FACADE_TESTS/"));

			UMLPlugin.getEPackageNsURIToProfileLocationMap().remove(J2EEProfilePackage.eNS_URI);

			Map<String, Object> factories = Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap();
			factories.remove(J2EEResource.FILE_EXTENSION);
			factories.remove(OpaqexprResource.FILE_EXTENSION);

			EPackage.Registry.INSTANCE.remove(OpaqexprPackage.eNS_URI);
			EPackage.Registry.INSTANCE.remove(J2EEProfilePackage.eNS_URI);
			EPackage.Registry.INSTANCE.remove(J2EEPackage.eNS_URI);
		}
	}

}
