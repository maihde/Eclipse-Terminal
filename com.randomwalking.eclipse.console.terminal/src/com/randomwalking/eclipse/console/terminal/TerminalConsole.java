/*******************************************************************************
 * Copyright (c) 2010 Michael Ihde
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Michael Ihde - initial API and implementation
 *******************************************************************************/
package com.randomwalking.eclipse.console.terminal;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.console.AbstractConsole;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.part.IPageBookViewPage;

public class TerminalConsole extends AbstractConsole {

	public TerminalConsole(String name, ImageDescriptor imageDescriptor) {
		this(name, imageDescriptor, true);
	}

	public TerminalConsole(String name, ImageDescriptor imageDescriptor, boolean autoLifecycle) {
		this(name, null, imageDescriptor, autoLifecycle);
	}

	public TerminalConsole(String name, String type, ImageDescriptor imageDescriptor, boolean autoLifecycle) {
		super(name, type, imageDescriptor, autoLifecycle);
	}

	@Override
	public IPageBookViewPage createPage(IConsoleView view) {
		return new TerminalConsolePage(this, view);
	}
}
