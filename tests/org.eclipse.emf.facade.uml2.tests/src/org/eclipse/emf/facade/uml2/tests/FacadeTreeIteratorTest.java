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
package org.eclipse.emf.facade.uml2.tests;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

import java.util.Map;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.facade.IFacadeProvider;
import org.eclipse.emf.facade.uml2.tests.data.UMLInputData;
import org.eclipse.emf.facade.uml2.tests.framework.AutoCloseRule;
import org.eclipse.emf.facade.uml2.tests.j2ee.internal.providers.J2EEFacadeProvider;
import org.eclipse.emf.facade.uml2.tests.opaqexpr.OpaqueExpression;
import org.eclipse.emf.facade.uml2.tests.opaqexpr.internal.providers.OpaqexprFacadeProvider;
import org.eclipse.emf.facade.util.FacadeTreeIterator;
import org.junit.Rule;
import org.junit.Test;

/**
 * Unit tests for the {@code FacadeTreeIterator} class.
 *
 * @author Christian W. Damus
 */
@SuppressWarnings("boxing")
public class FacadeTreeIteratorTest {

	@Rule
	public final AutoCloseRule closer = new AutoCloseRule();

	private UMLInputData input = closer.add(new UMLInputData());

	/**
	 * Initializes me.
	 */
	public FacadeTreeIteratorTest() {
		super();
	}

	@Test
	public void facadeResourceContents() {
		TreeIterator<EObject> iter = iterate(input.getO1());

		// Implicitly asserting hasNext()
		assertThat(iter.next(), instanceOf(org.eclipse.uml2.uml.Package.class));
		assertThat(iter.next(), instanceOf(OpaqueExpression.class));
		for (int i = 0; i < 4; i++) {
			assertThat(iter.next(), instanceOf(Map.Entry.class));
		}

		assertThat(iter.hasNext(), is(false));
	}

	@Test
	public void facadeResourceContents_prune() {
		TreeIterator<EObject> iter = iterate(input.getO1());

		// Implicitly asserting hasNext()
		assertThat(iter.next(), instanceOf(org.eclipse.uml2.uml.Package.class));
		assertThat(iter.next(), instanceOf(OpaqueExpression.class));
		iter.prune();

		assertThat(iter.hasNext(), is(false));
	}

	@Test
	public void facadeObjectContents() {
		TreeIterator<EObject> iter = iterate(input.getO1());

		// Implicitly asserting hasNext()
		assertThat(iter.next(), instanceOf(org.eclipse.uml2.uml.Package.class));
		assertThat(iter.next(), instanceOf(OpaqueExpression.class));
		for (int i = 0; i < 4; i++) {
			assertThat(iter.next(), instanceOf(Map.Entry.class));
		}

		assertThat(iter.hasNext(), is(false));
	}

	@Test
	public void facadeObjectContents_prune() {
		TreeIterator<EObject> iter = iterate(input.getO1());

		// Implicitly asserting hasNext()
		assertThat(iter.next(), instanceOf(org.eclipse.uml2.uml.Package.class));
		assertThat(iter.next(), instanceOf(OpaqueExpression.class));
		iter.prune();

		assertThat(iter.hasNext(), is(false));
	}

	//
	// Test framework
	//

	IFacadeProvider.Factory getFacadeProviderFactory() {
		return new J2EEFacadeProvider.Factory().compose(new OpaqexprFacadeProvider.Factory());
	}

	TreeIterator<EObject> iterate(Resource resource) {
		IFacadeProvider.Factory facadeProviderFactory = getFacadeProviderFactory();
		return new FacadeTreeIterator(facadeProviderFactory, resource);
	}
}
