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
import org.eclipse.emf.facade.uml2.tests.j2ee.Finder;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Usage;
import org.eclipse.uml2.uml.profile.standard.Create;
import org.eclipse.uml2.uml.profile.standard.StandardPackage;
import org.eclipse.uml2.uml.util.UMLUtil;

/**
 * Façade adapter for finder interfaces in a J2EE model.
 *
 * @author Christian W. Damus
 */
@SuppressWarnings("restriction")
public class FinderAdapter extends NamedElementAdapter {

	private Usage create;

	/**
	 * Initializes me with a stereotype application.
	 * 
	 * @param facade
	 *            the finder façade
	 * @param finder
	 *            the UML model element
	 * @param stereotype
	 *            the J2EE Finder stereotype application
	 */
	FinderAdapter(Finder facade, Interface finder,
			org.eclipse.emf.facade.uml2.tests.j2eeprofile.Finder stereotype) {

		super(facade, finder, stereotype);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dispose() {
		super.dispose();

		if (create != null) {
			removeAdapter(create);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAdapterForType(Object type) {
		return (type == Bean.class) || (type == FinderAdapter.class) //
				|| super.isAdapterForType(type);
	}

	@Override
	public Finder getFacade() {
		return (Finder)super.getFacade();
	}

	@Override
	public Interface getUnderlyingElement() {
		return (Interface)super.getUnderlyingElement();
	}

	@Override
	public org.eclipse.emf.facade.uml2.tests.j2eeprofile.Finder getStereotype() {
		return (org.eclipse.emf.facade.uml2.tests.j2eeprofile.Finder)super.getStereotype();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterable<? extends EObject> getRelatedUnderlyingElements(EObject underlying) {
		Iterable<? extends EObject> result;

		if (underlying == create) {
			result = Collections.unmodifiableList(create.getStereotypeApplications());
		} else if (underlying == getUnderlyingElement()) {
			ImmutableList.Builder<EObject> resultBuilder = ImmutableList.builder();
			resultBuilder.add(getStereotype());
			if (create != null) {
				resultBuilder.add(create);
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
			if ((create != null) && (notification.getNotifier() == create)) {
				synchronize(create, getFacade(), false, notification);
			}
		}

		return result;
	}

	/**
	 * Ensures that the façade and its UML element are connected by an adapter.
	 * 
	 * @param facade
	 *            a finder façade
	 * @param finder
	 *            the UML model element
	 * @return the existing or new adapter
	 */
	static FinderAdapter connect(Finder facade, Interface finder) {
		FinderAdapter result = null;

		org.eclipse.emf.facade.uml2.tests.j2eeprofile.Finder stereotype = UMLUtil
				.getStereotypeApplication(finder,
						org.eclipse.emf.facade.uml2.tests.j2eeprofile.Finder.class);

		if (stereotype != null) {
			result = connect(facade, finder, FinderAdapter.class,
					(f, m) -> new FinderAdapter(f, m, stereotype));
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
	public static FinderAdapter get(Notifier notifier) {
		return get(notifier, FinderAdapter.class);
	}

	/**
	 * Queries whether some {@code object} is a UML interface that is stereotyped as a finder.
	 * 
	 * @param object
	 *            an object
	 * @return whether it is a finder in the UML model
	 */
	static boolean isFinder(Object object) {
		return (object instanceof Interface) && (UMLUtil.getStereotypeApplication((Interface)object,
				org.eclipse.emf.facade.uml2.tests.j2eeprofile.Finder.class) != null);

	}

	/**
	 * Synchronize the bean from the façade to the UML model.
	 * 
	 * @param facade
	 *            the façade
	 * @param model
	 *            the UML element
	 */
	public void syncBeanToModel(Finder facade, Interface model) {
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

	public Optional<Usage> getBeanRelationship(Interface finder) {
		return finder.getClientDependencies().stream() //
				.filter(Usage.class::isInstance).map(Usage.class::cast) //
				.filter(d -> UMLUtil.getStereotypeApplication(d, Create.class) != null) //
				.filter(d -> d.getSuppliers().stream().anyMatch(BeanAdapter::isBeanClass)) //
				.peek(this::setCreate) // We need to track changes to this
				.findAny();

	}

	protected org.eclipse.uml2.uml.Class getBean(Interface finder) {
		return getBeanRelationship(finder)//
				.flatMap(r -> r.getTargets().stream().filter(BeanAdapter::isBeanClass)
						.map(org.eclipse.uml2.uml.Class.class::cast).findAny())
				.orElse(null);
	}

	protected void setBean(Interface finder, org.eclipse.uml2.uml.Class beanClass) {
		if (beanClass == null) {
			getBeanRelationship(finder).ifPresent(Element::destroy);
		} else {
			Optionals.ifPresentElse(getBeanRelationship(finder), u -> {
				List<NamedElement> suppliers = u.getSuppliers();
				if (suppliers.size() == 1) {
					suppliers.set(0, beanClass);
				} else {
					suppliers.clear();
					suppliers.add(beanClass);
				}
			}, () -> setCreate(
					applyStereotype(finder.createUsage(beanClass), StandardPackage.Literals.CREATE)));
		}
	}

	protected void setCreate(Usage newCreate) {
		if (newCreate == this.create) {
			return;
		}

		if (this.create != null) {
			removeAdapter(this.create);
		}
		this.create = newCreate;
		if (newCreate != null) {
			addAdapter(newCreate);
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
	public void syncBeanToFacade(Interface model, Finder facade) {
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
	public void syncClientToFacade(Usage model, Finder facade) {
		if (!model.getClients().contains(getUnderlyingElement())) {
			// This is no longer our create usage
			setCreate(null);

			// If it isn't anyone's create usage, it no longer exists
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
	public void syncSupplierToFacade(Usage model, Finder facade) {
		// Just delegate
		syncBeanToFacade(getUnderlyingElement(), facade);
	}
}
