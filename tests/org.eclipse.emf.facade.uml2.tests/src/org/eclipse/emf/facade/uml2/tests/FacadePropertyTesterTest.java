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

import static org.eclipse.emf.facade.internal.expressions.FacadePropertyTester.IS_FACADE;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.facade.FacadeAdapter;
import org.eclipse.emf.facade.FacadeProxy;
import org.eclipse.emf.facade.internal.expressions.FacadePropertyTester;
import org.eclipse.emf.facade.uml2.tests.data.BasicFacadeInputData;
import org.eclipse.emf.facade.uml2.tests.framework.AutoCloseRule;
import org.eclipse.emf.facade.uml2.tests.framework.RegistryRule;
import org.eclipse.emf.facade.uml2.tests.j2ee.Bean;
import org.eclipse.emf.facade.uml2.tests.j2ee.Finder;
import org.eclipse.emf.facade.uml2.tests.j2ee.HomeInterface;
import org.eclipse.emf.facade.uml2.tests.j2ee.Package;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * Unit tests for the {@link FacadePropertyTester} class.
 *
 * @author Christian W. Damus
 */
@SuppressWarnings({"nls", "boxing" })
public class FacadePropertyTesterTest {

	@ClassRule
	public static final RegistryRule registries = new RegistryRule();

	@Rule
	public final AutoCloseRule closer = new AutoCloseRule();

	private BasicFacadeInputData input = closer.add(new BasicFacadeInputData());

	private static FacadePropertyTester fixture = new FacadePropertyTester();

	/**
	 * Initializes me.
	 */
	public FacadePropertyTesterTest() {
		super();
	}

	@Test
	public void isFacade_true() {
		Package package_ = requirePackage(input.getA1(), "a1");
		Bean thing = requireBean(package_, "Thing");

		assertThat(test(thing, IS_FACADE, true), is(true));
	}

	@Test
	public void isFacade_false() {
		Package package_ = requirePackage(input.getA1(), "a1");
		Bean thing = requireBean(package_, "Thing");

		assertThat(test(thing, IS_FACADE, false), is(false));
	}

	@Test
	public void isFacade_default() {
		Package package_ = requirePackage(input.getA1(), "a1");
		Bean thing = requireBean(package_, "Thing");

		assertThat(test(thing, IS_FACADE, null), is(true));
	}

	@Test
	public void isFacade_true_dynamicProxy() {
		Package package_ = requirePackage(input.getA1(), "a1");
		EObject thing = FacadeProxy.createProxy(requireBean(package_, "Thing"));

		assertThat(test(thing, IS_FACADE, true), is(true));
	}

	@Test
	public void isFacade_false_dynamicProxy() {
		Package package_ = requirePackage(input.getA1(), "a1");
		EObject thing = FacadeProxy.createProxy(requireBean(package_, "Thing"));

		assertThat(test(thing, IS_FACADE, false), is(false));
	}

	@Test
	public void isFacade_default_dynamicProxy() {
		Package package_ = requirePackage(input.getA1(), "a1");
		EObject thing = FacadeProxy.createProxy(requireBean(package_, "Thing"));

		assertThat(test(thing, IS_FACADE, null), is(true));
	}

	@Test
	public void isFacade_true_notFacade() {
		Package package_ = requirePackage(input.getA1(), "a1");
		Bean thing = requireBean(package_, "Thing");
		org.eclipse.uml2.uml.Class thingClass = (org.eclipse.uml2.uml.Class)FacadeAdapter
				.getUnderlyingObject(thing);

		assertThat(test(thingClass, IS_FACADE, true), is(false));
	}

	@Test
	public void isFacade_false_notFacade() {
		Package package_ = requirePackage(input.getA1(), "a1");
		Bean thing = requireBean(package_, "Thing");
		org.eclipse.uml2.uml.Class thingClass = (org.eclipse.uml2.uml.Class)FacadeAdapter
				.getUnderlyingObject(thing);

		assertThat(test(thingClass, IS_FACADE, false), is(true));
	}

	@Test
	public void isFacade_default_notFacade() {
		Package package_ = requirePackage(input.getA1(), "a1");
		Bean thing = requireBean(package_, "Thing");
		org.eclipse.uml2.uml.Class thingClass = (org.eclipse.uml2.uml.Class)FacadeAdapter
				.getUnderlyingObject(thing);

		assertThat(test(thingClass, IS_FACADE, null), is(false));
	}

	//
	// Test framework
	//

	boolean test(Object receiver, String property, Object expectedValue, Object... arg) {
		return fixture.test(receiver, property, arg, expectedValue);
	}

	Package requirePackage(Resource resource, String name) {
		Package result = input.getPackage(resource);

		assertThat(String.format("No package '%s'", name), result, notNullValue());
		assertThat("Wrong package name", result.getName(), is(name));

		return result;
	}

	Bean requireBean(Package package_, String name) {
		Bean result = package_.getBean(name);

		assertThat(String.format("No bean '%s'", name), result, notNullValue());

		return result;
	}

	HomeInterface requireHomeInterface(Package package_, String name) {
		HomeInterface result = package_.getHomeInterface(name);

		assertThat(String.format("No home-interface '%s'", name), result, notNullValue());

		return result;
	}

	Finder requireFinder(Package package_, String name) {
		Finder result = package_.getFinder(name);

		assertThat(String.format("No finder '%s'", name), result, notNullValue());

		return result;
	}
}
