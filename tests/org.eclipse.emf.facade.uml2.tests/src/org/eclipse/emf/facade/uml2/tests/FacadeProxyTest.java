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

import static com.google.common.collect.Iterables.filter;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assume.assumeThat;

import com.google.common.collect.ImmutableList;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.facade.FacadeAdapter;
import org.eclipse.emf.facade.FacadeObject;
import org.eclipse.emf.facade.FacadeProxy;
import org.eclipse.emf.facade.uml2.tests.data.BasicFacadeInputData;
import org.eclipse.emf.facade.uml2.tests.framework.AutoCloseRule;
import org.eclipse.emf.facade.uml2.tests.framework.RegistryRule;
import org.eclipse.emf.facade.uml2.tests.j2ee.Bean;
import org.eclipse.emf.facade.uml2.tests.j2ee.Finder;
import org.eclipse.emf.facade.uml2.tests.j2ee.HomeInterface;
import org.eclipse.emf.facade.uml2.tests.j2ee.J2EEPackage;
import org.eclipse.emf.facade.uml2.tests.j2ee.Package;
import org.hamcrest.Matcher;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * Unit tests for the {@linkplain FacadeProxy dynamic façade proxy} API.
 *
 * @author Christian W. Damus
 */
@SuppressWarnings({"nls", "boxing" })
public class FacadeProxyTest {

	@ClassRule
	public static final RegistryRule registries = new RegistryRule();

	@Rule
	public final AutoCloseRule closer = new AutoCloseRule();

	private BasicFacadeInputData input = closer.add(new BasicFacadeInputData());

	/**
	 * Initializes me.
	 */
	public FacadeProxyTest() {
		super();
	}

	@Test
	public void beanInPackage() {
		Package package_ = requirePackage();
		Bean thing = requireBean(package_, "Thing");

		assertThat(thing, isFacade());
	}

	@Test
	public void homeInterfaceInPackage() {
		Package package_ = requirePackage();
		HomeInterface thingHome = requireHomeInterface(package_, "ThingHome");

		assertThat(thingHome, isFacade());
	}

	@Test
	public void finderInPackage() {
		Package package_ = requirePackage();
		Finder thingByID = requireFinder(package_, "ThingByID");

		assertThat(thingByID, isFacade());
	}

	@Test
	public void beansInPackage() {
		Package package_ = requirePackage();
		List<Bean> beans = package_.getBeans();

		assumeThat(beans, not(empty()));
		assertThat(beans, everyItem(isFacade()));
	}

	@Test
	public void homeInterfacesInPackage() {
		Package package_ = requirePackage();
		List<HomeInterface> homes = package_.getHomeInterfaces();

		assumeThat(homes, not(empty()));
		assertThat(homes, everyItem(isFacade()));
	}

	@Test
	public void findersInPackage() {
		Package package_ = requirePackage();
		List<Finder> finders = package_.getFinders();

		assumeThat(finders, not(empty()));
		assertThat(finders, everyItem(isFacade()));
	}

	@Test
	public void packageInBean() {
		Package model = requirePackage(); // Be sure to load this only once
		Bean thing = requireBean(model, "Thing");
		Package package_ = thing.getPackage();

		assertThat(package_, isFacade());
		assertThat(package_, sameInstance(model));
	}

	@Test
	public void packageInHomeInterface() {
		Package model = requirePackage(); // Be sure to load this only once
		HomeInterface thingHome = requireHomeInterface(model, "ThingHome");
		Package package_ = thingHome.getPackage();

		assertThat(package_, isFacade());
		assertThat(package_, sameInstance(model));
	}

	@Test
	public void packageInFinder() {
		Package model = requirePackage(); // Be sure to load this only once
		Finder thingByID = requireFinder(model, "ThingByID");
		Package package_ = thingByID.getPackage();

		assertThat(package_, isFacade());
		assertThat(package_, sameInstance(model));
	}

	@Test
	public void beanInHomeInterface() {
		Package package_ = requirePackage();
		HomeInterface thingHome = requireHomeInterface(package_, "ThingHome");
		Bean thing = thingHome.getBean();

		assertThat(thing, isFacade());
		assertThat(thing, sameInstance(package_.getBean("Thing")));
	}

	@Test
	public void beanInFinder() {
		Package package_ = requirePackage();
		Finder thingByID = requireFinder(package_, "ThingByID");
		Bean thing = thingByID.getBean();

		assertThat(thing, isFacade());
		assertThat(thing, sameInstance(requireBean(package_, "Thing")));
	}

	@Test
	public void homeInterfaceInBean() {
		Package package_ = requirePackage();
		Bean thing = requireBean(package_, "Thing");
		HomeInterface thingHome = thing.getHomeInterface();

		assertThat(thingHome, isFacade());
		assertThat(thingHome, sameInstance(requireHomeInterface(package_, "ThingHome")));
	}

	@Test
	public void finderInBean() {
		Package package_ = requirePackage();
		Bean thing = requireBean(package_, "Thing");
		Finder thingByName = thing.getFinder("ThingByName");

		assertThat(thingByName, isFacade());
		assertThat(thingByName, sameInstance(requireFinder(package_, "ThingByName")));
	}

	@Test
	public void findersInBean() {
		Package package_ = requirePackage();
		Bean thing = requireBean(package_, "Thing");
		List<Finder> finders = thing.getFinders();

		assumeThat(finders, not(empty()));
		assertThat(finders, everyItem(isFacade()));
	}

	@Test
	public void createBean() {
		Package package_ = requirePackage();
		Bean newBean = package_.createBean("NewBean");

		assertThat(newBean, isFacade());
	}

	@Test
	public void eContainer() {
		Package package_ = requirePackage();
		Bean thing = requireBean(package_, "Thing");

		EObject container = thing.eContainer();
		assertThat(container, isFacade());
		assertThat(container, sameInstance(package_));
	}

	@Test
	public void eContents() {
		Package package_ = requirePackage();

		EList<EObject> contents = package_.eContents();

		assertThat(contents, everyItem(isFacade()));

		assertThat(contents, hasItem(requireBean(package_, "Thing")));
		assertThat(contents, hasItem(requireHomeInterface(package_, "ThingHome")));
		assertThat(contents, hasItem(requireFinder(package_, "ThingByID")));
	}

	@Test
	public void eAllContents() {
		Package package_ = requirePackage();

		List<EObject> allContents = ImmutableList.copyOf(package_.eAllContents());

		assertThat(allContents, everyItem(isFacade()));

		assertThat(allContents, hasItem(requireBean(package_, "Thing")));
		assertThat(allContents, hasItem(requireHomeInterface(package_, "ThingHome")));
		assertThat(allContents, hasItem(requireFinder(package_, "ThingByID")));
	}

	@Test
	public void proxyListIterator() {
		Package package_ = requirePackage();

		List<Bean> beans = ImmutableList.copyOf(package_.getBeans().iterator());

		assertThat(beans, everyItem(isFacade()));

		assertThat(beans, hasItem(requireBean(package_, "Thing")));
		assertThat(beans, hasItem(requireBean(package_, "Whatsit")));
	}

	@Test
	public void proxyListListIterator() {
		Package package_ = requirePackage();

		List<Bean> beans = ImmutableList.copyOf(package_.getBeans().listIterator());

		assertThat(beans, everyItem(isFacade()));

		assertThat(beans, hasItem(requireBean(package_, "Thing")));
		assertThat(beans, hasItem(requireBean(package_, "Whatsit")));
	}

	@Test
	public void proxyListToArray() {
		Package package_ = requirePackage();

		Object[] beansArray = package_.getBeans().toArray();
		List<EObject> beans = ImmutableList.copyOf(filter(Arrays.asList(beansArray), Bean.class));

		assertThat(beans, everyItem(isFacade()));

		assertThat(beans, hasItem((EObject)requireBean(package_, "Thing")));
		assertThat(beans, hasItem((EObject)requireBean(package_, "Whatsit")));
	}

	@Test
	public void proxyListToArrayOfT() {
		Package package_ = requirePackage();

		Bean[] beansArray = package_.getBeans().toArray(new Bean[0]);
		List<Bean> beans = Arrays.asList(beansArray);

		assertThat(beans, everyItem(isFacade()));

		assertThat(beans, hasItem(requireBean(package_, "Thing")));
		assertThat(beans, hasItem(requireBean(package_, "Whatsit")));
	}

	@Test
	public void addToProxyList() {
		Package package_ = requirePackage();
		Bean whatsit = requireBean(package_, "Whatsit");
		Finder finder = requireFinder(package_, "ThingByName");

		assumeThat(whatsit, isFacade());
		assumeThat(finder, isFacade());
		assumeThat(finder.getBean(), not(whatsit));
		whatsit.getFinders().add(finder);

		assertThat(finder.getBean(), is(whatsit));
	}

	@Test
	public void removeFromProxyList() {
		Package package_ = requirePackage();
		Bean thing = requireBean(package_, "Thing");
		Finder finder = requireFinder(package_, "ThingByName");

		assumeThat(thing, isFacade());
		assumeThat(finder, isFacade());
		assumeThat(finder.getBean(), is(thing));
		thing.getFinders().remove(finder);

		assertThat(finder.getBean(), nullValue());
	}

	@Test
	public void setInverseReference() {
		Package package_ = requirePackage();
		Bean thing = requireBean(package_, "Thing");
		Bean whatsit = requireBean(package_, "Whatsit");
		Finder finder = requireFinder(package_, "ThingByName");

		assumeThat(thing, isFacade());
		assumeThat(whatsit, isFacade());
		assumeThat(finder, isFacade());
		assumeThat(thing.getFinders(), hasItem(finder));

		finder.setBean(whatsit);

		assertThat(thing.getFinders(), not(hasItem(finder)));
		assertThat(whatsit.getFinders(), hasItem(finder));

		// The EMF implementation is correctly linked
		assertThat(unproxy(finder).getBean(), sameInstance(unproxy(whatsit)));
		assertThat(unproxy(whatsit).getFinders(), hasItem(unproxy(finder)));
	}

	@Test
	public void reflectiveAccess() {
		Package package_ = requirePackage();
		Bean thing = requireBean(package_, "Thing");
		Finder finder = requireFinder(package_, "ThingByName");

		assumeThat(thing, isFacade());
		assumeThat(finder, isFacade());

		EObject value = (EObject)finder.eGet(J2EEPackage.Literals.FINDER__BEAN);

		assertThat(value, isFacade());
		assertThat(value, sameInstance(thing));
	}

	@Test
	public void reflectiveMutation() {
		Package package_ = requirePackage();
		Bean thing = requireBean(package_, "Thing");
		Bean whatsit = requireBean(package_, "Whatsit");
		Finder finder = requireFinder(package_, "ThingByName");

		assumeThat(thing, isFacade());
		assumeThat(whatsit, isFacade());
		assumeThat(finder, isFacade());
		assumeThat(thing.getFinders(), hasItem(finder));

		finder.eSet(J2EEPackage.Literals.FINDER__BEAN, whatsit);

		assertThat(thing.getFinders(), not(hasItem(finder)));
		assertThat(whatsit.getFinders(), hasItem(finder));

		// The EMF implementation is correctly linked
		assertThat(unproxy(finder).getBean(), sameInstance(unproxy(whatsit)));
		assertThat(unproxy(whatsit).getFinders(), hasItem(unproxy(finder)));
	}

	@Test
	public void internalReflectiveAccess() {
		Package package_ = requirePackage();
		Bean thing = requireBean(package_, "Thing");
		Finder finder = requireFinder(package_, "ThingByName");

		assumeThat(thing, isFacade());
		assumeThat(finder, isFacade());

		EObject value = (EObject)((InternalEObject)finder).eGet(J2EEPackage.FINDER__BEAN, true, true);

		assertThat(value, isFacade());
		assertThat(value, sameInstance(thing));
	}

	@Test
	public void internalReflectiveMutation() {
		Package package_ = requirePackage();
		Bean thing = requireBean(package_, "Thing");
		Bean whatsit = requireBean(package_, "Whatsit");
		Finder finder = requireFinder(package_, "ThingByName");

		assumeThat(thing, isFacade());
		assumeThat(whatsit, isFacade());
		assumeThat(finder, isFacade());
		assumeThat(thing.getFinders(), hasItem(finder));

		((InternalEObject)finder).eSet(J2EEPackage.FINDER__BEAN, whatsit);

		assertThat(thing.getFinders(), not(hasItem(finder)));
		assertThat(whatsit.getFinders(), hasItem(finder));

		// The EMF implementation is correctly linked
		assertThat(unproxy(finder).getBean(), sameInstance(unproxy(whatsit)));
		assertThat(unproxy(whatsit).getFinders(), hasItem(unproxy(finder)));
	}

	@Test
	public void notificationScalar() {
		Package package_ = requirePackage();
		Bean whatsit = requireBean(package_, "Whatsit");
		Finder finder = requireFinder(package_, "ThingByName");

		EObject[] newValue = {null };
		finder.eAdapters().add(new AdapterImpl() {
			@Override
			public void notifyChanged(Notification msg) {
				assertThat((EObject)msg.getNotifier(), isFacade());

				if (msg.getFeature() == J2EEPackage.Literals.FINDER__BEAN) {
					newValue[0] = (EObject)msg.getNewValue();
				}
			}
		});

		whatsit.getFinders().add(finder);

		assertThat(newValue[0], isFacade());
		assertThat(newValue[0], sameInstance(whatsit));
	}

	@Test
	public void notificationVector() {
		Package package_ = requirePackage();
		Bean whatsit = requireBean(package_, "Whatsit");
		Finder finder = requireFinder(package_, "ThingByName");

		EObject[] newValue = {null };
		whatsit.eAdapters().add(new AdapterImpl() {
			@Override
			public void notifyChanged(Notification msg) {
				assertThat((EObject)msg.getNotifier(), isFacade());

				if (msg.getFeature() == J2EEPackage.Literals.BEAN__FINDER) {
					newValue[0] = (EObject)msg.getNewValue();
				}
			}
		});

		finder.setBean(whatsit);

		assertThat(newValue[0], isFacade());
		assertThat(newValue[0], sameInstance(finder));
	}

	@Test
	public void notificationVector_addMany() {
		Package package_ = requirePackage();
		Bean whatsit = requireBean(package_, "Whatsit");
		Finder finder = requireFinder(package_, "ThingByName");
		Finder byChance = package_.createFinder("WhatitByChance");

		AtomicReference<Collection<EObject>> newValue = new AtomicReference<>();
		whatsit.eAdapters().add(new AdapterImpl() {
			@SuppressWarnings("unchecked")
			@Override
			public void notifyChanged(Notification msg) {
				assertThat((EObject)msg.getNotifier(), isFacade());

				if (msg.getFeature() == J2EEPackage.Literals.BEAN__FINDER) {
					newValue.set((Collection<EObject>)msg.getNewValue());
				}
			}
		});

		whatsit.getFinders().addAll(asList(finder, byChance));

		assertThat(newValue.get(), notNullValue());
		assertThat(newValue.get(), hasItem(finder));
		assertThat(newValue.get(), hasItem(byChance));
	}

	@Test
	public void notificationVector_removeMany() {
		Package package_ = requirePackage();
		Bean thing = requireBean(package_, "Thing");
		Finder finder = requireFinder(package_, "ThingByName");
		Finder byID = requireFinder(package_, "ThingByID");

		AtomicReference<Collection<EObject>> oldValue = new AtomicReference<>();
		thing.eAdapters().add(new AdapterImpl() {
			@SuppressWarnings("unchecked")
			@Override
			public void notifyChanged(Notification msg) {
				assertThat((EObject)msg.getNotifier(), isFacade());

				if (msg.getFeature() == J2EEPackage.Literals.BEAN__FINDER) {
					oldValue.set((Collection<EObject>)msg.getOldValue());
				}
			}
		});

		thing.getFinders().clear();

		assertThat(oldValue.get(), notNullValue());
		assertThat(oldValue.get(), hasItem(finder));
		assertThat(oldValue.get(), hasItem(byID));
	}

	@Test
	public void removeAdapter() {
		Package package_ = requirePackage();
		Bean thing = requireBean(package_, "Thing");

		boolean[] removed = {false };

		Adapter adapter = new AdapterImpl() {
			@Override
			public void notifyChanged(Notification msg) {
				assertThat((EObject)msg.getNotifier(), isFacade());

				if (msg.getEventType() == Notification.REMOVING_ADAPTER) {
					removed[0] = msg.getOldValue() == this;
				}
			}
		};
		thing.eAdapters().add(adapter);
		thing.eAdapters().remove(adapter);

		assertThat("adapter not removed", removed[0], is(true));
	}

	@Test
	public void eSetting_vector() {
		Package package_ = requirePackage();
		Bean thing = requireBean(package_, "Thing");

		EStructuralFeature.Setting setting = ((InternalEObject)thing)
				.eSetting(J2EEPackage.Literals.BEAN__FINDER);

		assertThat(setting.getEObject(), sameInstance(thing));
		assertThat(setting.getEStructuralFeature(), is(J2EEPackage.Literals.BEAN__FINDER));
		assertThat(setting.get(true), instanceOf(EList.class));

		EList<?> values = (EList<?>)setting.get(true);
		assertThat(values, not(empty()));
		assertThat(values, everyItem(instanceOf(Finder.class)));
		assertThat(values, everyItem(instanceOf(FacadeObject.class)));
	}

	@Test
	public void eSetting_scalar() {
		Package package_ = requirePackage();
		Bean thing = requireBean(package_, "Thing");

		EStructuralFeature.Setting setting = ((InternalEObject)thing)
				.eSetting(J2EEPackage.Literals.BEAN__HOME_INTERFACE);

		assertThat(setting.getEObject(), sameInstance(thing));
		assertThat(setting.getEStructuralFeature(), is(J2EEPackage.Literals.BEAN__HOME_INTERFACE));
		assertThat(setting.get(true), instanceOf(HomeInterface.class));
		assertThat(setting.get(true), instanceOf(FacadeObject.class));
	}

	@Test
	public void eSetting_setScalar() {
		Package package_ = requirePackage();
		Bean thing = requireBean(package_, "Thing");
		HomeInterface newHome = package_.createHomeInterface("NewHome");
		assumeThat(newHome, instanceOf(FacadeObject.class));

		EStructuralFeature.Setting setting = ((InternalEObject)thing)
				.eSetting(J2EEPackage.Literals.BEAN__HOME_INTERFACE);

		setting.set(newHome);

		assertThat(setting.get(true), sameInstance(newHome));

		Bean realThing = unproxy(thing);
		HomeInterface realHome = realThing.getHomeInterface();
		assertThat(realHome, sameInstance(unproxy(newHome)));
	}

	//
	// Test framework
	//

	Package requirePackage() {
		return requirePackage(input.getF1());
	}

	Package requirePackage(Resource resource) {
		return requirePackage(resource, "f1");
	}

	Package requirePackage(Resource resource, String name) {
		Package result = input.getPackage(resource);

		assertThat(String.format("No package '%s'", name), result, notNullValue());
		assertThat("Wrong package name", result.getName(), is(name));

		return (Package)FacadeProxy.createProxy(result);
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

	/**
	 * If the given object is a façade proxy, get the real EMF object that it wraps.
	 * 
	 * @param facadeProxy
	 *            a façade proxy
	 * @return the real EMF implementation
	 */
	@SuppressWarnings("unchecked")
	<T extends EObject> T unproxy(T facadeProxy) {
		T result = facadeProxy;

		if ((result instanceof FacadeObject) && (result instanceof Proxy)) {
			FacadeAdapter adapter = ((FacadeObject)result).getFacadeAdapter();
			result = (T)adapter.getFacade();
		}

		return result;
	}

	/**
	 * Matcher for is-a-façade assertions.
	 * 
	 * @return a matcher that tests whether an object is a {@link FacadeObject}
	 */
	<T extends EObject> Matcher<T> isFacade() {
		return instanceOf(FacadeObject.class);
	}
}
