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

import static java.util.Collections.singletonList;
import static org.eclipse.emf.facade.FacadeAdapter.getUnderlyingObject;
import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assume.assumeThat;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.facade.FacadeAdapter;
import org.eclipse.emf.facade.uml2.tests.data.BasicFacadeInputData;
import org.eclipse.emf.facade.uml2.tests.framework.AutoCloseRule;
import org.eclipse.emf.facade.uml2.tests.framework.RegistryRule;
import org.eclipse.emf.facade.uml2.tests.j2ee.Bean;
import org.eclipse.emf.facade.uml2.tests.j2ee.BeanKind;
import org.eclipse.emf.facade.uml2.tests.j2ee.Finder;
import org.eclipse.emf.facade.uml2.tests.j2ee.HomeInterface;
import org.eclipse.emf.facade.uml2.tests.j2ee.Package;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Usage;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * Basic tests of the simple use cases of the {@link FacadeAdapter} framework.
 *
 * @author Christian W. Damus
 */
@SuppressWarnings({"nls", "boxing" })
public class BasicFacadeTest {

	@ClassRule
	public static final RegistryRule registries = new RegistryRule();

	@Rule
	public final AutoCloseRule closer = new AutoCloseRule();

	private BasicFacadeInputData input = closer.add(new BasicFacadeInputData());

	/**
	 * Initializes me.
	 */
	public BasicFacadeTest() {
		super();
	}

	@Test
	public void simpleBean() {
		Package package_ = requirePackage(input.getA1(), "a1");
		Bean thing = requireBean(package_, "Thing");

		assertThat(thing.getKind(), is(BeanKind.ENTITY));
	}

	@Test
	public void changeBeanKindFromFacade() {
		Package package_ = requirePackage(input.getA1(), "a1");
		Bean thing = requireBean(package_, "Thing");

		thing.setKind(BeanKind.MESSAGEDRIVEN);
		assertThat(getUnderlyingObject(thing), hasKind(BeanKind.MESSAGEDRIVEN));
	}

	@Test
	public void changeBeanKindFromUML() {
		Package package_ = requirePackage(input.getA1(), "a1");
		Bean thing = requireBean(package_, "Thing");

		setKind(getUnderlyingObject(thing), BeanKind.MESSAGEDRIVEN);
		assertThat(getUnderlyingObject(thing), hasKind(BeanKind.MESSAGEDRIVEN));
	}

	@Test
	public void addBeanInFacade() {
		Package package_ = requirePackage(input.getA1(), "a1");
		Bean newBean = package_.createBean("Doodad");

		org.eclipse.uml2.uml.Class class_ = (org.eclipse.uml2.uml.Class)getUnderlyingObject(newBean);
		assertThat("No UML class", class_, notNullValue());
		assertThat("Wrong class name", class_.getName(), is("Doodad"));
		assertThat("Class not stereotyped as «Bean»", class_, hasStereotype("Bean"));
	}

	@Test
	public void addBeanInUML() {
		Package package_ = requirePackage(input.getA1(), "a1");

		org.eclipse.uml2.uml.Package uml = (org.eclipse.uml2.uml.Package)getUnderlyingObject(package_);
		org.eclipse.uml2.uml.Class class_ = uml.createOwnedClass("Doodad", false);
		applyStereotype(class_, "Bean");

		Bean newBean = requireBean(package_, "Doodad");
		assertThat(getUnderlyingObject(newBean), is(class_));
	}

	@Test
	public void simpleHomeInterface() {
		Package package_ = requirePackage(input.getA2(), "a2");
		HomeInterface thingHome = requireHomeInterface(package_, "ThingHome");

		Bean thing = thingHome.getBean();
		assertThat("Home interface has no bean", thing, notNullValue());
		assertThat("Wrong bean", thing.getName(), is("Thing"));
	}

	@Test
	public void changeHomeInterfaceBeanFromFacade() {
		Package package_ = requirePackage(input.getA2(), "a2");
		HomeInterface thingHome = requireHomeInterface(package_, "ThingHome");

		Bean whatsit = requireBean(package_, "Whatsit");
		thingHome.setBean(whatsit);

		org.eclipse.uml2.uml.Class whatsitClass = (org.eclipse.uml2.uml.Class)getUnderlyingObject(whatsit);
		Interface thingHomeInterface = (Interface)getUnderlyingObject(thingHome);

		List<Usage> usages = thingHomeInterface.getClientDependencies().stream() //
				.filter(Usage.class::isInstance).map(Usage.class::cast).collect(Collectors.toList());
		assertThat("Extra usage created or usage deleted", usages.size(), is(1));
		assertThat("Wrong usage relationship", usages.get(0).getSuppliers(), is(singletonList(whatsitClass)));
	}

	@Test
	public void changeHomeInterfaceBeanFromUML1() {
		Package package_ = requirePackage(input.getA2(), "a2");
		HomeInterface thingHome = requireHomeInterface(package_, "ThingHome");

		Bean whatsit = requireBean(package_, "Whatsit");
		org.eclipse.uml2.uml.Class whatsitClass = (org.eclipse.uml2.uml.Class)getUnderlyingObject(whatsit);
		Interface thingHomeInterface = (Interface)getUnderlyingObject(thingHome);

		List<Usage> usages = thingHomeInterface.getClientDependencies().stream() //
				.filter(Usage.class::isInstance).map(Usage.class::cast).collect(Collectors.toList());
		assertThat("Should have an unique usage", usages.size(), is(1));

		// The optimal way to do it
		assumeThat("Invalid usage", usages.get(0).getSuppliers().size(), is(1));
		usages.get(0).getSuppliers().set(0, whatsitClass);

		assertThat("Bean not updated", thingHome.getBean(), is(whatsit));
	}

	@Test
	public void changeHomeInterfaceBeanFromUML2() {
		Package package_ = requirePackage(input.getA2(), "a2");
		HomeInterface thingHome = requireHomeInterface(package_, "ThingHome");

		Bean whatsit = requireBean(package_, "Whatsit");
		org.eclipse.uml2.uml.Class whatsitClass = (org.eclipse.uml2.uml.Class)getUnderlyingObject(whatsit);
		Interface thingHomeInterface = (Interface)getUnderlyingObject(thingHome);

		List<Usage> usages = thingHomeInterface.getClientDependencies().stream() //
				.filter(Usage.class::isInstance).map(Usage.class::cast).collect(Collectors.toList());
		assertThat("Should have an unique usage", usages.size(), is(1));

		// A different way to do it
		usages.get(0).getSuppliers().clear();
		usages.get(0).getSuppliers().add(whatsitClass);

		assertThat("Bean not updated", thingHome.getBean(), is(whatsit));
	}

	@Test
	public void simpleFinders() {
		Package package_ = requirePackage(input.getA3(), "a3");
		Finder thingByID = requireFinder(package_, "ThingByID");
		Finder thingByName = requireFinder(package_, "ThingByName");

		Bean thing = requireBean(package_, "Thing");

		assertThat("Missing finder(s)", thing.getFinders(), hasItems(thingByID, thingByName));
	}

	@Test
	public void changeFinderBeanFromFacade() {
		Package package_ = requirePackage(input.getA3(), "a3");
		Finder thingByName = requireFinder(package_, "ThingByName");

		Bean whatsit = requireBean(package_, "Whatsit");
		whatsit.getFinders().add(thingByName); // Tricky! Work from the opposite end

		org.eclipse.uml2.uml.Class whatsitClass = (org.eclipse.uml2.uml.Class)getUnderlyingObject(whatsit);
		Interface thingByNameInterface = (Interface)getUnderlyingObject(thingByName);

		List<Usage> usages = thingByNameInterface.getClientDependencies().stream() //
				.filter(Usage.class::isInstance).map(Usage.class::cast).collect(Collectors.toList());
		assertThat("Extra usage created or usage deleted", usages.size(), is(1));
		assertThat("Wrong usage relationship", usages.get(0).getSuppliers(), is(singletonList(whatsitClass)));
	}

	@Test
	public void changeFinderBeanFromUML1() {
		Package package_ = requirePackage(input.getA3(), "a3");
		Finder thingByName = requireFinder(package_, "ThingByName");

		Bean whatsit = requireBean(package_, "Whatsit");
		org.eclipse.uml2.uml.Class whatsitClass = (org.eclipse.uml2.uml.Class)getUnderlyingObject(whatsit);
		Interface thingByNameInterface = (Interface)getUnderlyingObject(thingByName);

		List<Usage> usages = thingByNameInterface.getClientDependencies().stream() //
				.filter(Usage.class::isInstance).map(Usage.class::cast).collect(Collectors.toList());
		assertThat("Should have an unique usage", usages.size(), is(1));

		// The optimal way to do it
		assumeThat("Invalid usage", usages.get(0).getSuppliers().size(), is(1));
		usages.get(0).getSuppliers().set(0, whatsitClass);

		assertThat("Bean not updated", whatsit.getFinders(), hasItem(thingByName));
	}

	@Test
	public void changeFinderfaceBeanFromUML2() {
		Package package_ = requirePackage(input.getA3(), "a3");
		Finder thingByName = requireFinder(package_, "ThingByName");

		Bean whatsit = requireBean(package_, "Whatsit");
		org.eclipse.uml2.uml.Class whatsitClass = (org.eclipse.uml2.uml.Class)getUnderlyingObject(whatsit);
		Interface thingByNameInterface = (Interface)getUnderlyingObject(thingByName);

		List<Usage> usages = thingByNameInterface.getClientDependencies().stream() //
				.filter(Usage.class::isInstance).map(Usage.class::cast).collect(Collectors.toList());
		assertThat("Should have an unique usage", usages.size(), is(1));

		// A different way to do it
		usages.get(0).getSuppliers().clear();
		usages.get(0).getSuppliers().add(whatsitClass);

		assertThat("Bean not updated", whatsit.getFinders(), hasItem(thingByName));
	}

	@Test
	public void addHomeInterfaceInFacade() {
		Package package_ = requirePackage(input.getA2(), "a2");
		Bean whatsit = requireBean(package_, "Whatsit");

		HomeInterface newHome = package_.createHomeInterface("WhatsitHome");
		newHome.setBean(whatsit);

		Interface interface_ = (Interface)getUnderlyingObject(newHome);
		assertThat("No UML interface", interface_, notNullValue());
		assertThat("Wrong interface name", interface_.getName(), is("WhatsitHome"));
		assertThat("Interface not stereotyped as «HomeInterface»", interface_,
				hasStereotype("HomeInterface"));

		assertThat("Home interface should have exactly one dependency",
				interface_.getClientDependencies().size(), is(1));
		assertThat(interface_.getClientDependencies(),
				everyItem(suppliedBy(is(getUnderlyingObject(whatsit)))));
	}

	@Test
	public void traceSecondaryUnderlyingElementsOfHomeInterface() {
		Package package_ = requirePackage(input.getA2(), "a2");
		Bean whatsit = requireBean(package_, "Whatsit");

		HomeInterface newHome = package_.createHomeInterface("WhatsitHome");
		newHome.setBean(whatsit);

		Interface interface_ = (Interface)getUnderlyingObject(newHome);
		assumeThat("No UML interface", interface_, notNullValue());

		assumeThat("Home interface should have exactly one dependency",
				interface_.getClientDependencies().size(), is(1));
		assertThat(interface_.getClientDependencies().stream().map(FacadeAdapter::getFacade)
				.collect(Collectors.toList()), everyItem(is(newHome)));
	}

	@Test
	public void addHomeInterfaceInUML() {
		Package package_ = requirePackage(input.getA2(), "a2");

		org.eclipse.uml2.uml.Package uml = (org.eclipse.uml2.uml.Package)getUnderlyingObject(package_);
		Interface interface_ = uml.createOwnedInterface("WhatsitHome");
		applyStereotype(interface_, "HomeInterface");

		interface_.createUsage(uml.getOwnedMember("Whatsit"));
		HomeInterface newHome = requireHomeInterface(package_, "WhatsitHome");
		assertThat(getUnderlyingObject(newHome), is(interface_));
		assertThat(newHome.getBean(), is(requireBean(package_, "Whatsit")));
	}

	@Test
	public void addFinderInFacade() {
		Package package_ = requirePackage(input.getA3(), "a3");
		Bean thing = requireBean(package_, "Thing");

		Finder newFinder = package_.createFinder("ThingByRandom");
		newFinder.setBean(thing);

		Interface interface_ = (Interface)getUnderlyingObject(newFinder);
		assertThat("No UML interface", interface_, notNullValue());
		assertThat("Wrong interface name", interface_.getName(), is("ThingByRandom"));
		assertThat("Interface not stereotyped as «Finder»", interface_, hasStereotype("Finder"));

		assertThat("Finder interface should have exactly one dependency",
				interface_.getClientDependencies().size(), is(1));
		assertThat(interface_.getClientDependencies(), everyItem(hasStereotype("Create")));
		assertThat(interface_.getClientDependencies(), everyItem(suppliedBy(is(getUnderlyingObject(thing)))));
	}

	@Test
	public void traceSecondaryUnderlyingElementsOfFinder() {
		Package package_ = requirePackage(input.getA3(), "a3");
		Bean thing = requireBean(package_, "Thing");

		Finder newFinder = package_.createFinder("ThingByRandom");
		newFinder.setBean(thing);

		Interface interface_ = (Interface)getUnderlyingObject(newFinder);
		assumeThat("No UML interface", interface_, notNullValue());

		assumeThat("Finder interface should have exactly one dependency",
				interface_.getClientDependencies().size(), is(1));
		assertThat(interface_.getClientDependencies().stream().map(FacadeAdapter::getFacade)
				.collect(Collectors.toList()), everyItem(is(newFinder)));
	}

	@Test
	public void addFinderInUML() {
		Package package_ = requirePackage(input.getA3(), "a3");

		org.eclipse.uml2.uml.Package uml = (org.eclipse.uml2.uml.Package)getUnderlyingObject(package_);
		Interface interface_ = uml.createOwnedInterface("WhatsitByID");
		applyStereotype(interface_, "Finder");

		Usage usage = interface_.createUsage(uml.getOwnedMember("Whatsit"));
		applyStereotype(usage, "Create");

		Finder newFinder = requireFinder(package_, "WhatsitByID");
		assertThat(getUnderlyingObject(newFinder), is(interface_));
		assertThat(requireBean(package_, "Whatsit").getFinders(), hasItem(newFinder));
	}

	//
	// Test framework
	//

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

	@SuppressWarnings({"unchecked", "rawtypes" })
	void setKind(EObject owner, BeanKind kind) {
		EObject target = resolveBeanKindTarget(owner);
		assertThat("Cannot set bean kind of " + owner, target, notNullValue());
		Optional<EAttribute> kindAttr = resolveKindAttribute(target);
		kindAttr.ifPresent(a -> target.eSet(a,
				Enum.valueOf((Class<Enum>)a.getEAttributeType().getInstanceClass(), kind.name())));
	}

	private Optional<EAttribute> resolveKindAttribute(EObject owner) {
		return owner.eClass().getEAllAttributes().stream() //
				.filter(a -> a.getEAttributeType() instanceof EEnum)
				.filter(a -> "BeanKind".equals(a.getEAttributeType().getName())).findAny();
	}

	private EObject resolveBeanKindTarget(EObject object) {
		EObject result = null;

		Optional<EAttribute> kindAttr = resolveKindAttribute(object);
		if (kindAttr.isPresent()) {
			result = object;
		} else if (object instanceof Element) {
			result = ((Element)object).getStereotypeApplications().stream()
					.filter(st -> resolveKindAttribute(st).isPresent()).findAny().orElse(null);
		}

		return result;
	}

	<E extends EObject> Matcher<E> hasKind(BeanKind kind) {
		return new TypeSafeDiagnosingMatcher<E>() {
			/**
			 * {@inheritDoc}
			 */
			@Override
			public void describeTo(Description description) {
				description.appendText("has Bean kind ").appendValue(kind);
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			protected boolean matchesSafely(EObject item, Description mismatchDescription) {
				EObject target = resolveBeanKindTarget(item);
				if (target == null) {
					mismatchDescription.appendText("has no 'kind' attribute");
					return false;
				}

				Optional<EAttribute> kindAttr = resolveKindAttribute(target);

				return kindAttr.map(a -> {
					Enum<?> actual = (Enum<?>)target.eGet(a);

					boolean result = actual.name().equals(kind.name());

					if (!result) {
						mismatchDescription.appendText("kind was " + actual);
					}

					return result;
				}).orElseGet(() -> {
					mismatchDescription.appendText("has no 'kind' attribute");
					return false;
				});
			}
		};
	}

	<E extends Element> Matcher<E> hasStereotype(String name) {
		return new TypeSafeMatcher<E>() {
			/**
			 * {@inheritDoc}
			 */
			@Override
			public void describeTo(Description description) {
				description.appendText(String.format("stereotyped as «%s»", name));
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			protected boolean matchesSafely(Element item) {
				return item.getAppliedStereotypes().stream().anyMatch(s -> name.equals(s.getName()));
			}
		};
	}

	void applyStereotype(Element element, String name) {
		Optional<EObject> application = element.getApplicableStereotypes().stream() //
				.filter(s -> name.equals(s.getName())).findAny().map(s -> element.applyStereotype(s));

		assertThat("Stereotype not applied: " + name, application.isPresent(), is(true));
	}

	<D extends Dependency> Matcher<D> suppliedBy(Matcher<? super NamedElement> supplier) {
		return new TypeSafeMatcher<D>() {
			/**
			 * {@inheritDoc}
			 */
			@Override
			public void describeTo(Description description) {
				description.appendText("supplied by element that ") //
						.appendDescriptionOf(supplier);
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			protected boolean matchesSafely(Dependency item) {
				return hasItem(supplier).matches(item.getSuppliers());
			}
		};
	}
}
