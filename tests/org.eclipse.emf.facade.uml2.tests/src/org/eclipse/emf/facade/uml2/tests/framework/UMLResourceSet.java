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
package org.eclipse.emf.facade.uml2.tests.framework;

import static com.google.common.base.Predicates.alwaysTrue;
import static com.google.common.collect.ImmutableList.copyOf;

import com.google.common.collect.Iterables;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.util.UMLUtil;

/**
 * This is the {@code UMLResourceSet} type. Enjoy.
 *
 * @author Christian W. Damus
 */
@SuppressWarnings("nls")
public class UMLResourceSet extends ResourceSetImpl implements AutoCloseable {

	/**
	 * Initialies me.
	 */
	public UMLResourceSet() {
		super();
	}

	public Resource getResource(String path) {
		return getResource(URI.createPlatformResourceURI(path, true), true);
	}

	public Iterable<NamedElement> findElements(String qualifiedName) {
		return UMLUtil.findNamedElements(this, qualifiedName);
	}

	public <T extends NamedElement> Iterable<T> findElements(Class<T> type, String qualifiedName) {
		return Iterables.filter(findElements(qualifiedName), type);
	}

	public <T extends NamedElement> T requireElement(Class<T> type, String qualifiedName) {
		return Iterables.tryFind(findElements(type, qualifiedName), alwaysTrue()).toJavaUtil()
				.orElseThrow(() -> new AssertionError("No such element: " + qualifiedName));
	}

	//
	// Auto-closeable protocol
	//

	@Override
	public void close() {
		copyOf(getResources()).forEach(this::close);
		eAdapters().clear();
	}

	/**
	 * Disposes of a {@code resource} utterly.
	 * 
	 * @param resource
	 *            the resource to be closed
	 */
	public void close(Resource resource) {
		resource.unload();
		getResources().remove(resource);
		resource.eAdapters().clear();
	}
}
