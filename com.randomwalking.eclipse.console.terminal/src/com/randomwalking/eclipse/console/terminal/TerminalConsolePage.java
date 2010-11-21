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

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.part.IPageBookViewPage;
import org.eclipse.ui.part.IPageSite;

import com.randomwalking.eclipse.console.terminal.actions.ClearTerminalConsoleAction;
import com.randomwalking.eclipse.console.terminal.actions.CloseTerminalConsoleAction;
import com.randomwalking.eclipse.console.terminal.preferences.PreferenceConstants;
import com.randomwalking.swt.terminal.Terminal;

public class TerminalConsolePage implements IPageBookViewPage, IAdaptable, IPropertyChangeListener {

	public static final String SCRIPT_GROUP = "scriptGroup";

	public Terminal terminal;

	private IPageSite site;

	private final TerminalConsole console;

	private final IConsoleView consoleView;

	private final String cmd;

	private final String argv[];

	private CloseTerminalConsoleAction closeConsoleAction;

	private ClearTerminalConsoleAction clearConsoleAction;

	private Color backgroundColor;

	private Color foregroundColor;

	public TerminalConsolePage(TerminalConsole console, IConsoleView view) {
		this(console, view, null, null);
	}

	public TerminalConsolePage(TerminalConsole console, IConsoleView view, String cmd, String argv[]) {
		this.console = console;
		this.consoleView = view;
		this.cmd = cmd;
		this.argv = argv;
	}

	@Override
	public void createControl(Composite parent) {
		terminal = new Terminal(parent, SWT.V_SCROLL | SWT.H_SCROLL);

		final RGB background = PreferenceConverter.getColor(TerminalPlugin.getDefault().getPreferenceStore(), PreferenceConstants.P_BACKGROUND_COLOR);
		backgroundColor = new Color(terminal.getDisplay(), background);
		terminal.setBackground(backgroundColor);

		final RGB foreground = PreferenceConverter.getColor(TerminalPlugin.getDefault().getPreferenceStore(), PreferenceConstants.P_FOREGROUND_COLOR);
		foregroundColor = new Color(terminal.getDisplay(), foreground);
		terminal.setForeground(foregroundColor);

		int maxLines = TerminalPlugin.getDefault().getPreferenceStore().getInt(PreferenceConstants.P_CONSOLE_OUTPUT_MAX_LINES);
		final boolean limitOutput = TerminalPlugin.getDefault().getPreferenceStore().getBoolean(PreferenceConstants.P_LIMIT_CONSOLE_OUTPUT);
		if (!limitOutput) {
			maxLines = -maxLines;
		}
		terminal.setScrollBackLines(maxLines);

		TerminalPlugin.getDefault().getPreferenceStore().addPropertyChangeListener(this);

		terminal.start(cmd, argv, null, null);


		createActions();
		configureToolBar(getSite().getActionBars().getToolBarManager());
	}

	protected void configureToolBar(IToolBarManager mgr) {

	}

	@Override
	public void dispose() {
		backgroundColor.dispose();
		foregroundColor.dispose();
		terminal.dispose();
	}

	@Override
	public Control getControl() {
		return terminal;
	}

	@Override
	public void setActionBars(IActionBars actionBars) {
	}

	@Override
	public void setFocus() {
		if (terminal != null) {
			terminal.setFocus();
		}
	}

	@Override
	public Object getAdapter(Class adapter) {
		return null;
	}

	@Override
	public IPageSite getSite() {
		return site;
	}

	@Override
	public void init(IPageSite site) throws PartInitException {
		this.site = site;
	}

	public IConsole getConsole() {
		return console;
	}

	protected void createActions() {
		final IActionBars actionBars= getSite().getActionBars();

		clearConsoleAction = new ClearTerminalConsoleAction(terminal, "Clear", "Clear Console");

		closeConsoleAction = new CloseTerminalConsoleAction((TerminalConsole)getConsole(), "Close", "Close Terminal");
		final IActionBars bars = getSite().getActionBars();
		final IToolBarManager toolbarManager = bars.getToolBarManager();

		toolbarManager.prependToGroup(IConsoleConstants.LAUNCH_GROUP, new GroupMarker(SCRIPT_GROUP));
		toolbarManager.appendToGroup(SCRIPT_GROUP, new Separator());
		toolbarManager.appendToGroup(SCRIPT_GROUP, closeConsoleAction);

		toolbarManager.appendToGroup(SCRIPT_GROUP, new Separator());
		toolbarManager.appendToGroup(SCRIPT_GROUP, clearConsoleAction);


		actionBars.updateActionBars();
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getProperty().equals(PreferenceConstants.P_BACKGROUND_COLOR)) {
			backgroundColor.dispose();
			final RGB background = PreferenceConverter.getColor(TerminalPlugin.getDefault().getPreferenceStore(), PreferenceConstants.P_BACKGROUND_COLOR);
			backgroundColor = new Color(terminal.getDisplay(), background);
			terminal.setBackground(backgroundColor);
		} else if (event.getProperty().equals(PreferenceConstants.P_FOREGROUND_COLOR)) {
			foregroundColor.dispose();
			final RGB foreground = PreferenceConverter.getColor(TerminalPlugin.getDefault().getPreferenceStore(), PreferenceConstants.P_FOREGROUND_COLOR);
			foregroundColor = new Color(terminal.getDisplay(), foreground);
			terminal.setForeground(foregroundColor);
		}else if (event.getProperty().equals(PreferenceConstants.P_LIMIT_CONSOLE_OUTPUT) || event.getProperty().equals(PreferenceConstants.P_CONSOLE_OUTPUT_MAX_LINES)) {
			int maxLines = TerminalPlugin.getDefault().getPreferenceStore().getInt(PreferenceConstants.P_CONSOLE_OUTPUT_MAX_LINES);
			final boolean limitOutput = TerminalPlugin.getDefault().getPreferenceStore().getBoolean(PreferenceConstants.P_LIMIT_CONSOLE_OUTPUT);
			if (!limitOutput) {
				maxLines = -maxLines;
			}
			terminal.setScrollBackLines(maxLines);
		}
	}

}
