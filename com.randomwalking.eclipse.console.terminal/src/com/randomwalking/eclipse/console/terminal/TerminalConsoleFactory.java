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
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleFactory;

public class TerminalConsoleFactory implements IConsoleFactory {
	public static int nextId = 0;

	public static final String CONSOLE_NAME = "Terminal";

	public static final String CONSOLE_TYPE = "com.randomwalking.eclipse.console.termainl";


	private static String getNextId() {
		nextId += 1;
		return String.valueOf(nextId);
	}

	@Override
	public void openConsole() {
		final String consoleName = CONSOLE_NAME  + " [" + getNextId() + "]";
		final ImageDescriptor consoleImage = TerminalPlugin.getImageDescriptor(TerminalPlugin.TERMINAL_ICON);
		ConsolePlugin.getDefault().getConsoleManager().addConsoles(
				new IConsole[]{new TerminalConsole(consoleName, CONSOLE_TYPE, consoleImage, true)});
	}

}
