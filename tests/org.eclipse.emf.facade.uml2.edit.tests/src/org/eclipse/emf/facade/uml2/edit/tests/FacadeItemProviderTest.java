/*******************************************************************************
 * Copyright (c) 2017 Christian W. Damus and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Christian W. Damus - Initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.facade.uml2.edit.tests;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

import java.util.Collection;
import java.util.Map;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.facade.edit.provider.FacadeAdapterFactory;
import org.eclipse.emf.facade.edit.provider.FacadeItemProvider;
import org.eclipse.emf.facade.uml2.tests.data.UMLInputData;
import org.eclipse.emf.facade.uml2.tests.framework.AutoCloseRule;
import org.eclipse.emf.facade.uml2.tests.framework.RegistryRule;
import org.eclipse.emf.facade.uml2.tests.opaqexpr.OpaqexprPackage;
import org.eclipse.emf.facade.uml2.tests.opaqexpr.OpaqueExpression;
import org.eclipse.emf.facade.uml2.tests.opaqexpr.internal.adapters.OpaqexprFacadeFactory;
import org.eclipse.emf.facade.uml2.tests.opaqexpr.internal.providers.OpaqexprFacadeProvider;
import org.eclipse.uml2.uml.UMLPackage;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * Test cases for the {@link FacadeItemProvider} class.
 *
 * @author Christian W. Damus
 */
@SuppressWarnings({"nls", "restriction" })
public class FacadeItemProviderTest {

	@ClassRule
	public static final RegistryRule registries = new RegistryRule();

	@Rule
	public final AutoCloseRule closer = new AutoCloseRule();

	private UMLInputData input = closer.add(new UMLInputData());

	private final AdapterFactory adapterFactory = new FacadeAdapterFactory(
			new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE),
			new OpaqexprFacadeProvider());

	@Test
	public void treeContent_getParent() {
		Resource resource = input.getO2();

		org.eclipse.uml2.uml.Package package_ = requirePackage(resource);
		org.eclipse.uml2.uml.OpaqueExpression umlExpr = (org.eclipse.uml2.uml.OpaqueExpression)package_
				.getPackagedElement("expr");
		assertThat("UML expression not found", umlExpr, notNullValue());

		OpaqueExpression expr = OpaqexprFacadeFactory.create(umlExpr);
		assertThat("No expression bodies found", expr.getBodies(), not(empty()));

		Map.Entry<String, String> body = expr.getBodies().get(0);
		ITreeItemContentProvider tree = requireTree(body);
		assertThat("Wrong parent of façade that has a façade container", tree.getParent(body), is(expr));

		tree = requireTree(expr);
		assertThat("Wrong parent of façade that has a raw container", tree.getParent(expr), is(package_));
	}

	@Test
	public void treeContent_getChildren() {
		Resource resource = input.getO2();

		org.eclipse.uml2.uml.Package package_ = requirePackage(resource);

		ITreeItemContentProvider tree = requireTree(package_);
		Collection<?> children = tree.getChildren(package_);

		assertThat("No children found for package", children, not(empty()));
		OpaqueExpression expr = (OpaqueExpression)EcoreUtil.getObjectByType(children,
				OpaqexprPackage.Literals.OPAQUE_EXPRESSION);

		assertThat("No expression façade in children of package", expr, notNullValue());

		tree = requireTree(expr);
		children = tree.getChildren(expr);

		assertThat("No children found for expression façade", children, not(empty()));
		Map.Entry<?, ?> body = (Map.Entry<?, ?>)EcoreUtil.getObjectByType(children,
				OpaqexprPackage.Literals.BODY_ENTRY);
		assertThat("No bodies found for expression façade", body, notNullValue());
	}

	//
	// Test framework
	//

	ITreeItemContentProvider requireTree(Object object) {
		ITreeItemContentProvider result = (ITreeItemContentProvider)adapterFactory.adapt(object,
				ITreeItemContentProvider.class);

		assertThat("No tree item content provider adapter", result, notNullValue());

		return result;
	}

	org.eclipse.uml2.uml.Package requirePackage(Resource resource) {
		org.eclipse.uml2.uml.Package result = (org.eclipse.uml2.uml.Package)EcoreUtil
				.getObjectByType(resource.getContents(), UMLPackage.Literals.PACKAGE);

		assertThat("No package loaded in resource", result, notNullValue());

		return result;
	}

}
