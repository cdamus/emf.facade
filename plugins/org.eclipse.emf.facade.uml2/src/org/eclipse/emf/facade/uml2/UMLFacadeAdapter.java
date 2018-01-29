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
package org.eclipse.emf.facade.uml2;

import com.google.common.base.Optional;

import java.util.function.BiFunction;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.facade.FacadeAdapter;
import org.eclipse.emf.facade.SyncDirectionKind;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.util.UMLUtil;

/**
 * A specialized façade adapter for UML that includes support for stereotype applications representing DSML
 * concepts.
 *
 * @author Christian W. Damus
 */
public class UMLFacadeAdapter extends FacadeAdapter {

	/** The application of the primary stereotype, if any, representing the domain concept. */
	private final EObject stereotype;

	/**
	 * Initializes me with the façade and underlying UML model element that I associate with one another, and
	 * optionally a primary stereotype representing the façade's domain concept.
	 * 
	 * @param facade
	 *            the façade element
	 * @param umlElement
	 *            the underlying model element
	 * @param stereotype
	 *            application of the primary UML stereotype of the domain concept, or {@code null} if there is
	 *            no stereotype representing that concept
	 */
	public UMLFacadeAdapter(EObject facade, Element umlElement, EObject stereotype) {
		super(facade, umlElement);

		this.stereotype = stereotype;

		if (stereotype != null) {
			addAdapter(stereotype);
		}
	}

	/**
	 * Initializes with the façade and underlying UML model element that I associate with one another where
	 * the UML element does not have a primary stereotype for its domain concept.
	 * 
	 * @param facade
	 *            the façade element
	 * @param umlElement
	 *            the underlying model element
	 */
	public UMLFacadeAdapter(EObject facade, Element umlElement) {
		this(facade, umlElement, null);
	}

	/**
	 * Detaches me from all model elements that I adapt.
	 */
	@Override
	public void dispose() {
		super.dispose();

		if (stereotype != null) {
			removeAdapter(stereotype);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Element getUnderlyingElement() {
		return (Element)super.getUnderlyingElement();
	}

	/**
	 * Obtains the application of the primary stereotype, if any, representing the underlying UML model
	 * element's domain concept.
	 * 
	 * @return the stereotype application, or {@code null}
	 */
	public EObject getStereotype() {
		return stereotype;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterable<? extends EObject> getRelatedUnderlyingElements(EObject underlying) {
		Iterable<? extends EObject> result;

		if (underlying != getUnderlyingElement()) {
			result = super.getRelatedUnderlyingElements(underlying);
		} else {
			result = Optional.fromNullable(getStereotype()).asSet();
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
			if ((stereotype != null) && (notification.getNotifier() == stereotype)) {
				syncStereotypeToFacade(notification);
				result = true;
			}
		}

		return result;
	}

	/**
	 * Synchronizes the underlying model's stereotype to the façade, triggered by the given
	 * {@code notification}.
	 * 
	 * @param notification
	 *            description of a change in the model
	 */
	protected void syncStereotypeToFacade(Notification notification) {
		synchronize(stereotype, getFacade(), false, notification);
	}

	/**
	 * Synchronizes the façade to its underlying model's stereotype, triggered by the given
	 * {@code notification}.
	 * 
	 * @param notification
	 *            description of a change in the façade
	 */
	protected void syncFacadeToStereotype(Notification notification) {
		synchronize(getFacade(), stereotype, true, notification);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void syncFacadeToModel(Notification notification) {
		super.syncFacadeToModel(notification);

		if (stereotype != null) {
			syncFacadeToStereotype(notification);
		}
	}

	/**
	 * I am an adapter for either the {@code UMLFacadeAdapter} type or whatever the superclass implementation
	 * determines.
	 * 
	 * @param type
	 *            the adapter type to test for
	 * @return whether I am an adapter of the given {@code type}
	 */
	@Override
	public boolean isAdapterForType(Object type) {
		return (type == UMLFacadeAdapter.class) || super.isAdapterForType(type);
	}

	@Override
	public void initialSync(SyncDirectionKind direction, EStructuralFeature feature) {
		super.initialSync(direction, feature);

		if (stereotype != null) {
			initialSync(getFacade(), stereotype, direction, feature);
		}
	}

	/**
	 * Ensures that a {@code facade} is connected to its underlying {@code model}.
	 * 
	 * @param facade
	 *            a facade object
	 * @param model
	 *            the underlying model element
	 * @param type
	 *            the type of adapter
	 * @param adapterCreator
	 *            a function that creates the adapter, if needed
	 * @return the adapter that connects the {@code facade} with its {@code model}
	 * @param <A>
	 *            the adapter type
	 * @param <F>
	 *            the façade type
	 * @param <M>
	 *            the model element type
	 */
	protected static <A extends UMLFacadeAdapter, F extends EObject, M extends Element> A connect(F facade,
			M model, Class<A> type, BiFunction<? super F, ? super M, ? extends A> adapterCreator) {

		A result = get(model, type);
		if ((result != null) && (result.getFacade() != facade)) {
			result.dispose();
			result = null;
		}

		if (result == null) {
			result = adapterCreator.apply(facade, model);
		}

		return result;
	}

	/**
	 * Applies a stereotype to an element, if possible, and returns it.
	 * 
	 * @param element
	 *            an element to stereotype
	 * @param stereotypeEClass
	 *            the Ecore definition of the stereotype to apply
	 * @return the stereotyped {@code element}
	 * @param <E>
	 *            the type of element
	 */
	protected <E extends Element> E applyStereotype(E element, EClass stereotypeEClass) {
		Stereotype stereotypeToApply = PrivateUtil.getStereotype(stereotypeEClass, element);
		if ((stereotypeToApply != null) && !element.isStereotypeApplied(stereotypeToApply)) {
			UMLUtil.safeApplyStereotype(element, stereotypeToApply);
		}

		return element;
	}

	//
	// Nested types
	//

	/**
	 * Private local access to protected UML utility APIs.
	 *
	 * @author Christian W. Damus
	 */
	private static final class PrivateUtil extends UMLUtil {
		/**
		 * Queries the UML representation of an Ecore stereotype.
		 * 
		 * @param stereotypeEClass
		 *            the Ecore definition of a stereotype
		 * @param context
		 *            an element in the context of the relevant profile application
		 * @return the stereotype definition, or {@code null} if not resolvable
		 */
		static Stereotype getStereotype(EClass stereotypeEClass, Element context) {
			NamedElement result = getNamedElement(stereotypeEClass, context);
			if (result instanceof Stereotype) {
				return (Stereotype)result;
			} else {
				return null;
			}
		}
	}
}
