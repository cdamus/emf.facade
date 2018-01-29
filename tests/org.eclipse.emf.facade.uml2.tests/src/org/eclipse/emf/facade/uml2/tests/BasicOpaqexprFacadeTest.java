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
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.hasValue;
import static org.hamcrest.Matchers.not;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;

import java.util.Map;
import java.util.Objects;

import org.eclipse.emf.common.util.BasicEMap;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.facade.FacadeAdapter;
import org.eclipse.emf.facade.uml2.tests.data.UMLInputData;
import org.eclipse.emf.facade.uml2.tests.framework.AutoCloseRule;
import org.eclipse.emf.facade.uml2.tests.framework.RegistryRule;
import org.eclipse.emf.facade.uml2.tests.opaqexpr.OpaqexprPackage;
import org.eclipse.emf.facade.uml2.tests.opaqexpr.OpaqueExpression;
import org.eclipse.emf.facade.uml2.tests.opaqexpr.internal.adapters.OpaqexprFacadeFactory;
import org.eclipse.uml2.uml.UMLPackage;
import org.hamcrest.CustomTypeSafeMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * Basic tests of the more complex use cases of the {@link FacadeAdapter} framework exercised by the
 * {@link OpaqexprPackage opaqexpr} façade.
 *
 * @author Christian W. Damus
 */
@SuppressWarnings({"nls", "boxing" })
public class BasicOpaqexprFacadeTest {

	@ClassRule
	public static final RegistryRule registries = new RegistryRule();

	@Rule
	public final AutoCloseRule closer = new AutoCloseRule();

	private UMLInputData input = closer.add(new UMLInputData());

	/**
	 * Initializes me.
	 */
	public BasicOpaqexprFacadeTest() {
		super();
	}

	@Test
	public void basicExpression() {
		org.eclipse.uml2.uml.Package package_ = requirePackage(input.getO1(), "o1");
		OpaqueExpression expr = requireOpaqueExpressionFacade(package_);

		assertThat(expr.getBodies().size(), is(4));
		assertThat(expr.getBodies(), hasEEntry("Smalltalk", "self things notEmpty"));
	}

	@Test
	public void removeBodyInFacade() {
		org.eclipse.uml2.uml.Package package_ = requirePackage(input.getO1(), "o1");
		org.eclipse.uml2.uml.OpaqueExpression uml = requireOpaqueExpression(package_);
		OpaqueExpression expr = getFacade(uml);

		expr.getBodies().removeKey("Smalltalk");

		assertThat(uml.getLanguages().size(), is(3));
		assertThat(uml.getLanguages(), not(hasItem("Smalltalk")));
		assertThat(uml.getBodies().size(), is(3));
		assertThat(uml.getBodies(), not(hasItem("self things notEmpty")));
	}

	@Test
	public void addBodyInFacade() {
		org.eclipse.uml2.uml.Package package_ = requirePackage(input.getO1(), "o1");
		org.eclipse.uml2.uml.OpaqueExpression uml = requireOpaqueExpression(package_);
		OpaqueExpression expr = getFacade(uml);

		expr.getBodies().put("Ruby", "not things.empty?");

		assertThat(uml.getLanguages().size(), is(5));
		assertThat(uml.getLanguages(), hasItemAt("Ruby", 4));
		assertThat(uml.getBodies().size(), is(5));
		assertThat(uml.getBodies(), hasItemAt("not things.empty?", 4));
	}

	@Test
	public void updateBodyInFacade() {
		org.eclipse.uml2.uml.Package package_ = requirePackage(input.getO1(), "o1");
		org.eclipse.uml2.uml.OpaqueExpression uml = requireOpaqueExpression(package_);
		OpaqueExpression expr = getFacade(uml);

		expr.getBodies().put("Smalltalk", "self things size greaterThan: 0");

		// Position is not changed
		assertThat(uml.getLanguages().size(), is(4));
		assertThat(uml.getLanguages(), hasItemAt("Smalltalk", 1));
		assertThat(uml.getBodies().size(), is(4));
		assertThat(uml.getBodies(), hasItemAt("self things size greaterThan: 0", 1));
	}

	@Test
	public void updateLanguageInFacade() {
		org.eclipse.uml2.uml.Package package_ = requirePackage(input.getO1(), "o1");
		org.eclipse.uml2.uml.OpaqueExpression uml = requireOpaqueExpression(package_);
		OpaqueExpression expr = getFacade(uml);

		((BasicEMap.Entry<String, String>)expr.getBodies().get(1)).setKey("Dolphin");

		// Position is not changed
		assertThat(uml.getLanguages().size(), is(4));
		assertThat(uml.getLanguages(), hasItemAt("Dolphin", 1));
		assertThat(uml.getBodies().size(), is(4));
		assertThat(uml.getBodies(), hasItemAt("self things notEmpty", 1));
	}

	@Test
	public void updateBodyInUML() {
		org.eclipse.uml2.uml.Package package_ = requirePackage(input.getO1(), "o1");
		org.eclipse.uml2.uml.OpaqueExpression uml = requireOpaqueExpression(package_);
		OpaqueExpression expr = getFacade(uml);

		uml.getBodies().set(1, "self things size greaterThan: 0");

		// Position is not changed
		assertThat(expr.getBodies().size(), is(4));
		assertThat(expr.getBodies().get(1), isEntry("Smalltalk", "self things size greaterThan: 0"));
	}

	@Test
	public void updateLanguageInUML() {
		org.eclipse.uml2.uml.Package package_ = requirePackage(input.getO1(), "o1");
		org.eclipse.uml2.uml.OpaqueExpression uml = requireOpaqueExpression(package_);
		OpaqueExpression expr = getFacade(uml);

		uml.getLanguages().set(1, "Dolphin");

		// Position is not changed
		assertThat(expr.getBodies().size(), is(4));
		assertThat(expr.getBodies().get(1), isEntry("Dolphin", "self things notEmpty"));
	}

	@Test
	public void addBodyInUML_languageFirst() {
		addBodyInUML(true);
	}

	void addBodyInUML(boolean languageFirst) {
		org.eclipse.uml2.uml.Package package_ = requirePackage(input.getO1(), "o1");
		org.eclipse.uml2.uml.OpaqueExpression uml = requireOpaqueExpression(package_);
		OpaqueExpression expr = getFacade(uml);

		if (languageFirst) {
			uml.getLanguages().add(2, "Ruby");
			uml.getBodies().add(2, "not things.empty?");
		} else {
			uml.getBodies().add(2, "not things.empty?");
			uml.getLanguages().add(2, "Ruby");
		}

		assertThat(expr.getBodies().size(), is(5));
		assertThat(expr.getBodies().get(2), isEntry("Ruby", "not things.empty?"));
		assertThat(expr.getBodies().get(3), isEntry("Java", "!this.getThings().isEmpty()"));
	}

	@Test
	public void addBodyInUML_bodyFirst() {
		addBodyInUML(false);
	}

	@Test
	public void removeBodyInUML_languageFirst() {
		removeBodyInUML(true);
	}

	void removeBodyInUML(boolean languageFirst) {
		org.eclipse.uml2.uml.Package package_ = requirePackage(input.getO1(), "o1");
		org.eclipse.uml2.uml.OpaqueExpression uml = requireOpaqueExpression(package_);
		OpaqueExpression expr = getFacade(uml);

		if (languageFirst) {
			uml.getLanguages().remove(1);
			uml.getBodies().remove(1);
		} else {
			uml.getBodies().remove(1);
			uml.getLanguages().remove(1);
		}

		assertThat(expr.getBodies().size(), is(3));
		assertThat(expr.getBodies().map(), not(hasKey("Smalltalk")));
		assertThat(expr.getBodies().map(), not(hasValue("self things notEmpty")));
		assertThat(expr.getBodies().get(1), isEntry("Java", "!this.getThings().isEmpty()"));
	}

	@Test
	public void removeBodyInUML_bodyFirst() {
		removeBodyInUML(false);
	}

	//
	// Test framework
	//

	org.eclipse.uml2.uml.Package requirePackage(Resource resource, String name) {
		org.eclipse.uml2.uml.Package result = input.getPackage(resource);

		assertThat(String.format("No package '%s'", name), result, notNullValue());
		assertThat("Wrong package name", result.getName(), is(name));

		return result;
	}

	org.eclipse.uml2.uml.OpaqueExpression requireOpaqueExpression(org.eclipse.uml2.uml.Package package_) {
		org.eclipse.uml2.uml.OpaqueExpression result = (org.eclipse.uml2.uml.OpaqueExpression)EcoreUtil
				.getObjectByType(package_.getPackagedElements(), UMLPackage.Literals.OPAQUE_EXPRESSION);

		assertThat("No opaque expression in package " + package_.getQualifiedName(), result, notNullValue());

		return result;
	}

	OpaqueExpression getFacade(org.eclipse.uml2.uml.OpaqueExpression uml) {
		return OpaqexprFacadeFactory.create(uml);
	}

	OpaqueExpression requireOpaqueExpressionFacade(org.eclipse.uml2.uml.Package package_) {
		return OpaqexprFacadeFactory.create(requireOpaqueExpression(package_));
	}

	static Matcher<EMap<String, String>> hasEEntry(String key, String value) {
		final Matcher<Map<? extends String, ? extends String>> delegate = hasEntry(key, value);

		return new TypeSafeMatcher<EMap<String, String>>() {
			/**
			 * {@inheritDoc}
			 */
			@Override
			public void describeTo(Description description) {
				description.appendDescriptionOf(delegate);
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			protected boolean matchesSafely(EMap<String, String> item) {
				return delegate.matches(item.map());
			}
		};
	}

	static Matcher<Map.Entry<String, String>> isEntry(String key, String value) {
		return new CustomTypeSafeMatcher<Map.Entry<String, String>>(
				String.format("is (%s, %s)", key, value)) {

			/**
			 * {@inheritDoc}
			 */
			@Override
			protected boolean matchesSafely(Map.Entry<String, String> item) {
				return Objects.equals(item.getKey(), key) && Objects.equals(item.getValue(), value);
			}
		};
	}

	static <E> Matcher<Iterable<E>> hasItemAt(E expected, int index) {
		return new CustomTypeSafeMatcher<Iterable<E>>(String.format("has %s at index %d", expected, index)) {
			/**
			 * {@inheritDoc}
			 */
			@Override
			protected boolean matchesSafely(Iterable<E> item) {
				return Iterables.indexOf(item, Predicates.equalTo(expected)) == index;
			}
		};
	}
}
