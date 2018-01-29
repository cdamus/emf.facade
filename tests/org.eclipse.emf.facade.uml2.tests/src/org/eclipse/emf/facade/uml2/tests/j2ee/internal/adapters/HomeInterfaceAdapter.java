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
package org.eclipse.emf.facade.uml2.tests.j2ee.internal.adapters;

import com.google.common.collect.ImmutableList;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.facade.internal.util.Optionals;
import org.eclipse.emf.facade.uml2.tests.j2ee.Bean;
import org.eclipse.emf.facade.uml2.tests.j2ee.HomeInterface;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Usage;
import org.eclipse.uml2.uml.util.UMLUtil;

/**
 * Façade adapter for home-interfaces in a J2EE model.
 *
 * @author Christian W. Damus
 */
@SuppressWarnings("restriction")
public class HomeInterfaceAdapter extends NamedElementAdapter {

	private Usage usage;

	/**
	 * Initializes me with a stereotype application.
	 * 
	 * @param facade
	 *            the home-interface façade
	 * @param homeInterface
	 *            the UML model element
	 * @param stereotype
	 *            the J2EE HomeInterface stereotype application
	 */
	HomeInterfaceAdapter(HomeInterface facade, Interface homeInterface,
			org.eclipse.emf.facade.uml2.tests.j2eeprofile.HomeInterface stereotype) {

		super(facade, homeInterface, stereotype);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dispose() {
		super.dispose();

		if (usage != null) {
			removeAdapter(usage);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAdapterForType(Object type) {
		return (type == Bean.class) || (type == HomeInterfaceAdapter.class) //
				|| super.isAdapterForType(type);
	}

	@Override
	public HomeInterface getFacade() {
		return (HomeInterface)super.getFacade();
	}

	@Override
	public Interface getUnderlyingElement() {
		return (Interface)super.getUnderlyingElement();
	}

	@Override
	public org.eclipse.emf.facade.uml2.tests.j2eeprofile.HomeInterface getStereotype() {
		return (org.eclipse.emf.facade.uml2.tests.j2eeprofile.HomeInterface)super.getStereotype();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterable<? extends EObject> getRelatedUnderlyingElements(EObject underlying) {
		Iterable<? extends EObject> result;

		if (underlying == usage) {
			result = Collections.unmodifiableList(usage.getStereotypeApplications());
		} else if (underlying == getUnderlyingElement()) {
			ImmutableList.Builder<EObject> resultBuilder = ImmutableList.builder();
			resultBuilder.add(getStereotype());
			if (usage != null) {
				resultBuilder.add(usage);
			}
			result = resultBuilder.build();
		} else {
			result = super.getRelatedUnderlyingElements(underlying);
		}

		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean handleNotification(Notification notification) {
		boolean result = super.handleNotification(notification);

		if (!result) {
			if ((usage != null) && (notification.getNotifier() == usage)) {
				synchronize(usage, getFacade(), false, notification);
			}
		}

		return result;
	}

	/**
	 * Ensures that the façade and its UML element are connected by an adapter.
	 * 
	 * @param facade
	 *            a home-interface façade
	 * @param homeInterface
	 *            the UML model element
	 * @return the existing or new adapter
	 */
	static HomeInterfaceAdapter connect(HomeInterface facade, Interface homeInterface) {
		HomeInterfaceAdapter result = null;

		org.eclipse.emf.facade.uml2.tests.j2eeprofile.HomeInterface stereotype = UMLUtil
				.getStereotypeApplication(homeInterface,
						org.eclipse.emf.facade.uml2.tests.j2eeprofile.HomeInterface.class);

		if (stereotype != null) {
			result = connect(facade, homeInterface, HomeInterfaceAdapter.class,
					(f, m) -> new HomeInterfaceAdapter(f, m, stereotype));
		}

		return result;
	}

	/**
	 * Obtains the adapter instance for some notifier.
	 * 
	 * @param notifier
	 *            a façade or UML model element
	 * @return the adapter, or {@code null}
	 */
	public static HomeInterfaceAdapter get(Notifier notifier) {
		return get(notifier, HomeInterfaceAdapter.class);
	}

	/**
	 * Queries whether some {@code object} is a UML interface that is stereotyped as a home-interface.
	 * 
	 * @param object
	 *            an object
	 * @return whether it is a home interface in the UML model
	 */
	static boolean isHomeInterface(Object object) {
		return (object instanceof Interface) && (UMLUtil.getStereotypeApplication((Interface)object,
				org.eclipse.emf.facade.uml2.tests.j2eeprofile.HomeInterface.class) != null);

	}

	/**
	 * Synchronize the bean from the façade to the UML model.
	 * 
	 * @param facade
	 *            the façade
	 * @param model
	 *            the UML element
	 */
	public void syncBeanToModel(HomeInterface facade, Interface model) {
		org.eclipse.uml2.uml.Class oldBeanClass = getBean(model);
		Bean bean = facade.getBean();

		org.eclipse.uml2.uml.Class newBeanClass = null;
		if (bean != null) {
			newBeanClass = (org.eclipse.uml2.uml.Class)getUnderlyingObject(bean);
		}

		if (newBeanClass != oldBeanClass) {
			setBean(model, newBeanClass);
		}
	}

	public Optional<Usage> getBeanRelationship(Interface homeInterface) {
		return homeInterface.getClientDependencies().stream() //
				.filter(Usage.class::isInstance).map(Usage.class::cast) //
				.filter(u -> u.getSuppliers().stream().anyMatch(BeanAdapter::isBeanClass)) //
				.peek(this::setUsage) // We need to track changes to this
				.findAny();

	}

	protected org.eclipse.uml2.uml.Class getBean(Interface homeInterface) {
		return getBeanRelationship(homeInterface)//
				.flatMap(r -> r.getTargets().stream().filter(BeanAdapter::isBeanClass)
						.map(org.eclipse.uml2.uml.Class.class::cast).findAny())
				.orElse(null);
	}

	protected void setBean(Interface homeInterface, org.eclipse.uml2.uml.Class beanClass) {
		if (beanClass == null) {
			getBeanRelationship(homeInterface).ifPresent(Element::destroy);
		} else {
			Optionals.ifPresentElse(getBeanRelationship(homeInterface), u -> {
				List<NamedElement> suppliers = u.getSuppliers();
				if (suppliers.size() == 1) {
					suppliers.set(0, beanClass);
				} else {
					suppliers.clear();
					suppliers.add(beanClass);
				}
			}, () -> setUsage(homeInterface.createUsage(beanClass)));
		}
	}

	protected void setUsage(Usage newUsage) {
		if (newUsage == this.usage) {
			return;
		}

		if (this.usage != null) {
			removeAdapter(this.usage);
		}
		this.usage = newUsage;
		if (newUsage != null) {
			addAdapter(newUsage);
		}
	}

	/**
	 * Synchronize the bean from the UML model to the façade.
	 * 
	 * @param model
	 *            the UML element
	 * @param facade
	 *            the façade
	 */
	public void syncBeanToFacade(Interface model, HomeInterface facade) {
		Bean oldBean = facade.getBean();
		org.eclipse.uml2.uml.Class newBeanClass = getBean(model);
		Bean newBean = null;
		if (newBeanClass != null) {
			newBean = J2EEFacadeFactory.create(newBeanClass);
		}

		if (newBean != oldBean) {
			facade.setBean(newBean);
		}
	}

	/**
	 * Synchronize the bean from the UML model to the façade by its usage relationship.
	 * 
	 * @param model
	 *            the UML usage element
	 * @param facade
	 *            the façade
	 */
	public void syncClientToFacade(Usage model, HomeInterface facade) {
		if (!model.getClients().contains(getUnderlyingElement())) {
			// This is no longer our usage
			setUsage(null);

			// If it isn't anyone's usage, it no longer exists
			if (model.getClients().isEmpty()) {
				model.destroy();
			}
		}
	}

	/**
	 * Synchronize the bean from the UML model to the façade by its usage relationship.
	 * 
	 * @param model
	 *            the UML usage element
	 * @param facade
	 *            the façade
	 */
	public void syncSupplierToFacade(Usage model, HomeInterface facade) {
		// Just delegate
		syncBeanToFacade(getUnderlyingElement(), facade);
	}
}
