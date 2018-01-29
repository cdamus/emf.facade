/*******************************************************************************
 * Copyright (c) 2018 Christian W. Damus and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Christian W. Damus - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.facade.uml2.tests.framework;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

/**
 * A rule that automatically closes resources.
 *
 * @author Christian W. Damus
 */
@SuppressWarnings("nls")
public final class AutoCloseRule extends TestWatcher {

	private final List<AutoCloseable> resources = new ArrayList<>();

	/**
	 * Initializes me.
	 */
	public AutoCloseRule() {
		super();
	}

	/**
	 * Add a resource to be closed at the conclusion of the test.
	 * 
	 * @param resource
	 *            a resource to auto-close later
	 * @return the {@code resource}, for convenience
	 */
	public <T extends AutoCloseable> T add(T resource) {
		resources.add(resource);
		return resource;
	}

	@Override
	protected void finished(Description description) {
		Exception thrown = null;

		try {
			for (AutoCloseable next : resources) {
				try {
					next.close();
				} catch (Exception e) {
					if (thrown != null) {
						thrown.addSuppressed(e);
					} else {
						thrown = e;
					}
				}
			}
		} finally {
			resources.clear();
		}

		if (thrown != null) {
			thrown.printStackTrace();
			fail("Failed to close resource: " + thrown.getMessage());
		}
	}
}
