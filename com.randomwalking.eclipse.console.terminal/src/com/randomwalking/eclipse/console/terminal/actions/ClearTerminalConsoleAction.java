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
package com.randomwalking.eclipse.console.terminal.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.PlatformUI;

import com.randomwalking.eclipse.console.terminal.TerminalPlugin;
import com.randomwalking.swt.terminal.Terminal;


public class ClearTerminalConsoleAction extends Action {
	private final Terminal terminal;

	public ClearTerminalConsoleAction(Terminal console, String text, String tooltip) {
		this.terminal = console;

		setText(text);
		setToolTipText(tooltip);
	}

	@Override
	public void run() {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				terminal.clear();
			}

		});
	}

	public void update() {
		setEnabled(true);
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return TerminalPlugin.getImageDescriptor(TerminalPlugin.CLEAR_ICON);
	}
}
