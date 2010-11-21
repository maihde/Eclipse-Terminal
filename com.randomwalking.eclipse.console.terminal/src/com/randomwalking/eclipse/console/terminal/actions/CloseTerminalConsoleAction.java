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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;

import com.randomwalking.eclipse.console.terminal.TerminalConsole;
import com.randomwalking.eclipse.console.terminal.TerminalPlugin;


public class CloseTerminalConsoleAction extends Action {
	private final TerminalConsole console;

	public CloseTerminalConsoleAction(TerminalConsole console, String text, String tooltip) {
		this.console = console;

		setText(text);
		setToolTipText(tooltip);
	}

	@Override
	public void run() {
		final Job job = new Job("Close Terminal") {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				ConsolePlugin.getDefault().getConsoleManager().removeConsoles(new IConsole[] { console });
				return Status.OK_STATUS;
			}

		};
		job.setUser(true);
		job.schedule();
	}

	public void update() {
		setEnabled(true);
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return TerminalPlugin.getImageDescriptor(TerminalPlugin.CLOSE_ICON);
	}
}
