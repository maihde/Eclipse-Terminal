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
package com.randomwalking.swt.terminal;

import java.io.OutputStream;

public interface ITerminal {
	/**
	 * Start the terminal providing a basic command prompt.
	 */
	public void start();

	/**
	 * Start the terminal using the provided command.
	 * 
	 * @param command
	 * @param argv
	 * @param envv
	 * @param directory
	 */
	public void start(String command, String argv[], String[] envv, String directory);

	/**
	 * Get the output stream that can be used to write content to the terminal as if the user
	 * typed it on the keyboard
	 * 
	 * @return null if the terminal does not support output streams
	 */
	public OutputStream getOutputStream();

	/**
	 * Clear the contents of the terminal.
	 */
	public void clear();

	/**
	 * Set the number of scroll back lines to maintain.
	 * 
	 * @param lines the number of scrool back lines.  negative for unlimited
	 */
	public void setScrollBackLines(int lines);
}
