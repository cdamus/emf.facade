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
package org.eclipse.emf.facade.uml2.tests.opaqexpr.internal.adapters;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.IntStream;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.BasicEMap;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.facade.uml2.UMLFacadeAdapter;
import org.eclipse.emf.facade.uml2.tests.opaqexpr.OpaqexprPackage;
import org.eclipse.emf.facade.uml2.tests.opaqexpr.OpaqueExpression;

/**
 * The synchronization adapter for the {@link OpaqueExpression} façade.
 *
 * @author Christian W. Damus
 */
public class OpaqueExpressionAdapter extends UMLFacadeAdapter {

	/**
	 * @param facade
	 * @param umlElement
	 */
	OpaqueExpressionAdapter(OpaqueExpression facade, org.eclipse.uml2.uml.OpaqueExpression umlElement) {
		super(facade, umlElement);
	}

	/**
	 * Obtains the adapter instance for some notifier.
	 * 
	 * @param notifier
	 *            a façade or UML model element
	 * @return the adapter, or {@code null}
	 */
	static OpaqueExpressionAdapter get(Notifier notifier) {
		return get(notifier, OpaqueExpressionAdapter.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAdapterForType(Object type) {
		return (type == OpaqueExpression.class) || (type == OpaqueExpressionAdapter.class) //
				|| super.isAdapterForType(type);
	}

	@Override
	public OpaqueExpression getFacade() {
		return (OpaqueExpression)super.getFacade();
	}

	@Override
	public org.eclipse.uml2.uml.OpaqueExpression getUnderlyingElement() {
		return (org.eclipse.uml2.uml.OpaqueExpression)super.getUnderlyingElement();
	}

	/**
	 * Ensures that the façade and its UML element are connected by an adapter.
	 * 
	 * @param facade
	 *            an opaque expression façade
	 * @param uml
	 *            the UML model element
	 * @return the existing or new adapter
	 */
	static OpaqueExpressionAdapter connect(OpaqueExpression facade,
			org.eclipse.uml2.uml.OpaqueExpression uml) {

		OpaqueExpressionAdapter result = connect(facade, uml, OpaqueExpressionAdapter.class,
				(f, m) -> new OpaqueExpressionAdapter(f, m));

		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean handleNotification(Notification notification) {
		// Ensure that we will track changes on any body entries added and stop tracking on removal
		handleDependencies(notification,
				(ref, value) -> ref == OpaqexprPackage.Literals.OPAQUE_EXPRESSION__BODY);

		boolean result = super.handleNotification(notification);

		if (!result && OpaqexprPackage.Literals.BODY_ENTRY.isInstance(notification.getNotifier())) {
			// Handle changes in body-entry objects
			synchronize((EObject)notification.getNotifier(), getUnderlyingElement(), true, notification);
		}

		return result;
	}

	//
	// Dependency handling in the façade
	//

	/**
	 * @param expr
	 *            opaque expression to which a body entry is added
	 * @param body
	 *            the body entry that was added
	 */
	public void bodyAdded(OpaqueExpression expr, Map.Entry<String, String> body) {
		// Follow changes
		addAdapter((Notifier)body);
	}

	/**
	 * @param expr
	 *            opaque expression from which a body entry is removed
	 * @param body
	 *            the body entry that was removed
	 */
	public void bodyRemoved(OpaqueExpression expr, Map.Entry<String, String> body) {
		// Stop following
		removeAdapter((Notifier)body);
	}

	//
	// Façade-to-model synchronization
	//

	public void syncNameToModel(OpaqueExpression facade, org.eclipse.uml2.uml.OpaqueExpression uml) {
		if (facade.eIsSet(OpaqexprPackage.Literals.OPAQUE_EXPRESSION__NAME)) {
			uml.setName(facade.getName());
		} else {
			uml.unsetName();
		}
	}

	public void syncBodyToModel(OpaqueExpression facade, org.eclipse.uml2.uml.OpaqueExpression uml) {
		// This will be called for initial sync only
		uml.unsetLanguages();
		uml.unsetBodies();

		facade.getBodies().forEach(entry -> {
			uml.getLanguages().add(entry.getKey());
			uml.getBodies().add(entry.getValue());
		});
	}

	public void syncBodyToModel(OpaqueExpression facade, org.eclipse.uml2.uml.OpaqueExpression uml,
			Notification msg) {

		syncFeature(facade, uml, msg, Map.Entry.class, this::handleBodyAdded, this::handleBodyRemoved);
	}

	protected void handleBodyAdded(Map.Entry<String, String> body, int position) {
		org.eclipse.uml2.uml.OpaqueExpression uml = getUnderlyingElement();

		if (body.getKey() != null) {
			if ((position == 0) || (uml.isSetLanguages() && (uml.getLanguages().size() >= position))) {
				uml.getLanguages().add(position, body.getKey());
			}
		}

		if (body.getValue() != null) {
			if ((position == 0) || (uml.isSetBodies() && (uml.getBodies().size() >= position))) {
				uml.getBodies().add(position, body.getValue());
			}
		}
	}

	protected void handleBodyRemoved(Map.Entry<String, String> body, int position) {
		removeAdapter((Notifier)body);

		org.eclipse.uml2.uml.OpaqueExpression uml = getUnderlyingElement();

		if (body.getKey() != null) {
			if (position == Notification.NO_INDEX) {
				if (uml.isSetLanguages()) {
					uml.getLanguages().remove(body.getKey());
				}
			} else if (uml.isSetLanguages() && (uml.getLanguages().size() > position)) {
				uml.getLanguages().remove(position);
			}
		}

		if (body.getValue() != null) {
			if (position == Notification.NO_INDEX) {
				if (uml.isSetBodies()) {
					uml.getBodies().remove(body.getValue());
				}
			} else if (uml.isSetBodies() && (uml.getBodies().size() > position)) {
				uml.getBodies().remove(position);
			}
		}
	}

	@SuppressWarnings("unchecked")
	static <K, V> EMap<K, V> getEMap(Map.Entry<K, V> entry) {
		EObject entryObject = (EObject)entry;
		EObject container = entryObject.eContainer();

		EMap<K, V> result;

		if (container == null) {
			result = null;
		} else {
			result = (EMap<K, V>)entryObject.eContainer().eGet(entryObject.eContainmentFeature());
		}

		return result;
	}

	static int indexOf(Map.Entry<?, ?> emapEntry) {
		int result = -1;

		EMap<?, ?> emap = getEMap(emapEntry);
		if (emap != null) {
			result = emap.indexOf(emapEntry);
		}

		return result;
	}

	public void syncKeyToModel(Map.Entry<String, String> facadeEntry,
			org.eclipse.uml2.uml.OpaqueExpression uml) {

		int index = indexOf(facadeEntry);
		uml.getLanguages().set(index, facadeEntry.getKey());
	}

	public void syncValueToModel(Map.Entry<String, String> facadeEntry,
			org.eclipse.uml2.uml.OpaqueExpression uml) {

		int index = indexOf(facadeEntry);
		uml.getBodies().set(index, facadeEntry.getValue());
	}

	//
	// Model-to-façade synchronization
	//

	public void syncNameToFacade(org.eclipse.uml2.uml.OpaqueExpression uml, OpaqueExpression facade) {
		if (uml.isSetName()) {
			facade.setName(uml.getName());
		} else {
			facade.eUnset(OpaqexprPackage.Literals.OPAQUE_EXPRESSION__NAME);
		}
	}

	public void syncBodyToFacade(org.eclipse.uml2.uml.OpaqueExpression uml, OpaqueExpression facade) {
		if (!uml.isSetBodies() || uml.getBodies().isEmpty()) {
			// Bodies are required
			return;
		}

		EList<String> bodies = uml.getBodies();

		// This will be called for initial sync only, so pick up languages here
		BiFunction<String, String, Map.Entry<String, String>> mapper = OpaqueExpressionAdapter::createEntry;

		if (!uml.isSetLanguages() || uml.getLanguages().isEmpty()) {
			// Degenerate case in which only bodies are specified in the UML
			Function<String, Map.Entry<String, String>> languageless = value -> mapper.apply(null, value);

			uml.getBodies().stream().map(languageless).forEach(facade.getBodies()::add);
		} else {
			EList<String> languages = uml.getLanguages();

			int size = Math.min(languages.size(), bodies.size());
			IntStream.range(0, size).mapToObj(i -> mapper.apply(languages.get(i), bodies.get(i)))
					.forEach(facade.getBodies()::add);
		}
	}

	static Map.Entry<String, String> createEntry(String key, String value) {
		@SuppressWarnings("unchecked")
		BasicEMap.Entry<String, String> result = (BasicEMap.Entry<String, String>)EcoreUtil
				.create(OpaqexprPackage.Literals.BODY_ENTRY);
		result.setKey(key);
		result.setValue(value);
		result.setHash(Objects.hashCode(key));
		return result;
	}

	public void syncBodyToFacade(org.eclipse.uml2.uml.OpaqueExpression uml, OpaqueExpression facade,
			Notification msg) {

		int index = msg.getPosition();

		switch (msg.getEventType()) {
			case Notification.SET:
				// Just replacing a body: easy
				if (!uml.isSetLanguages() || (index >= uml.getLanguages().size())) {
					// Or maybe not so easy. There is no language.
					// XXX
				} else {
					String language = uml.getLanguages().get(index);
					facade.getBodies().put(language, (String)msg.getNewValue());
				}
				break;
			case Notification.REMOVE:
			case Notification.REMOVE_MANY:
				// This is handled in the languages, which are our map keys
				break;
			case Notification.ADD:
				if (uml.getLanguages().size() == uml.getBodies().size()) {
					// We have matched pairs. Now we can add the newest pair
					String body = (String)msg.getNewValue();
					facade.getBodies().add(index, createEntry(uml.getLanguages().get(index), body));
				} // else wait until the matching body arrives
				break;
			case Notification.ADD_MANY:
				if (uml.getLanguages().size() == uml.getBodies().size()) {
					// We have matched pairs. Now we can add the newest ones
					@SuppressWarnings("unchecked")
					List<String> newBodies = (List<String>)msg.getNewValue();
					for (int i = 0; i < newBodies.size(); i++) {
						int pos = index + i;
						facade.getBodies().add(pos,
								createEntry(uml.getLanguages().get(pos), newBodies.get(pos)));
					}
				} // else wait until the matching languages arrive
				break;
			case Notification.MOVE:
				// XXX ??
				break;
			default:
				// Not applicable to multi-valued attributes
				break;
		}
	}

	public void syncLanguageToFacade(org.eclipse.uml2.uml.OpaqueExpression uml, OpaqueExpression facade,
			Notification msg) {

		int index = msg.getPosition();

		switch (msg.getEventType()) {
			case Notification.SET:
				// Just replacing a language: easy
				BasicEMap.Entry<String, String> entry = (BasicEMap.Entry<String, String>)facade.getBodies()
						.get(index);
				entry.setKey((String)msg.getNewValue());
				break;
			case Notification.REMOVE:
				// The languages are our keys, so we handle removals primarily on them
				facade.getBodies().removeKey(msg.getOldValue());
				break;
			case Notification.REMOVE_MANY:
				// The languages are our keys, so we handle removals primarily on them
				((Collection<?>)msg.getOldValue()).forEach(facade.getBodies()::removeKey);
				break;
			case Notification.ADD:
				if (uml.getLanguages().size() == uml.getBodies().size()) {
					// We have matched pairs. Now we can add the newest pair
					String language = (String)msg.getNewValue();
					facade.getBodies().add(index, createEntry(language, uml.getBodies().get(index)));
				} // else wait until the matching body arrives
				break;
			case Notification.ADD_MANY:
				if (uml.getLanguages().size() == uml.getBodies().size()) {
					// We have matched pairs. Now we can add the newest ones
					@SuppressWarnings("unchecked")
					List<String> newLanguages = (List<String>)msg.getNewValue();
					for (int i = 0; i < newLanguages.size(); i++) {
						int pos = index + i;
						facade.getBodies().add(pos,
								createEntry(newLanguages.get(pos), uml.getBodies().get(pos)));
					}
				} // else wait until the matching bodies arrive
				break;
			case Notification.MOVE:
				// XXX ??
				break;
			default:
				// Not applicable to multi-valued attributes
				break;
		}
	}
}
