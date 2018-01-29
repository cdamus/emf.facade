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
package org.eclipse.emf.facade.uml2.tests.data;

import java.net.URL;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.facade.uml2.tests.framework.UMLResourceSet;

/**
 * This is the {@code AbstractUMLInputData} type. Enjoy.
 *
 * @author Christian W. Damus
 */
public class AbstractUMLInputData implements AutoCloseable {

	private final Set<ResourceSet> resourceSets = new LinkedHashSet<ResourceSet>();

	/**
	 * Initializes me.
	 */
	public AbstractUMLInputData() {
		super();
	}

	/**
	 * Loads the resource containing a UML model identified by a relative {@code path}.
	 * 
	 * @param path
	 *            the relative path to the UML model
	 * @return the UML model resource
	 */
	protected Resource loadFromClassLoader(String path) {
		URL url = getClass().getResource(path);
		URI uri = URI.createURI(url.toExternalForm(), true);

		return createResourceSet().getResource(uri, true);
	}

	protected final ResourceSet createResourceSet() {
		ResourceSet result = doCreateResourceSet();
		resourceSets.add(result);
		return result;
	}

	protected ResourceSet doCreateResourceSet() {
		return new UMLResourceSet();
	}

	/**
	 * Obtains the already-loaded resource in any of my resource sets that is loaded from the given
	 * {@code path}.
	 * 
	 * @param path
	 *            the resource's relative path
	 * @return the loaded resource
	 * @throws AssertionError
	 *             to fail the test if the resource is not found to have been loaded
	 */
	protected Resource getLoadedResource(String path) {
		return getSets().stream().map(ResourceSet::getResources).flatMap(Collection::stream)
				.filter(r -> r.getURI().toString().endsWith(path)).findAny()
				.orElseThrow(() -> new AssertionError("No such resource: " + path)); //$NON-NLS-1$
	}

	public Set<ResourceSet> getSets() {
		return resourceSets;
	}

	@Override
	public void close() {
		resourceSets.stream().filter(UMLResourceSet.class::isInstance).map(UMLResourceSet.class::cast)
				.forEach(UMLResourceSet::close);
		resourceSets.clear();
	}
}
