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
package com.randomwalking.eclipse.console.terminal.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.randomwalking.eclipse.console.terminal.TerminalPlugin;

/**
 * This class represents a preference page that
 * is contributed to the Preferences dialog. By
 * subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows
 * us to create a page that is small and knows how to
 * save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They
 * are stored in the preference store that belongs to
 * the main plug-in class. That way, preferences can
 * be accessed directly via the preference store.
 */

public class TerminalConsolePreferencePage
extends FieldEditorPreferencePage
implements IWorkbenchPreferencePage {

	public TerminalConsolePreferencePage() {
		super(GRID);
		setPreferenceStore(TerminalPlugin.getDefault().getPreferenceStore());
		setDescription("A demonstration of a preference page implementation");
	}

	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
	@Override
	public void createFieldEditors() {
		addField(
				new ColorFieldEditor(
						PreferenceConstants.P_BACKGROUND_COLOR,
						"&Terminal Background Color",
						getFieldEditorParent()));

		addField(
				new ColorFieldEditor(
						PreferenceConstants.P_FOREGROUND_COLOR,
						"&Terminal Foreground Color",
						getFieldEditorParent()));

		addField(
				new BooleanFieldEditor(
						PreferenceConstants.P_LIMIT_CONSOLE_OUTPUT,
						"Limit terminal output",
						getFieldEditorParent()));

		addField(
				new IntegerFieldEditor(
						PreferenceConstants.P_CONSOLE_OUTPUT_MAX_LINES,
						"Maximum terminal lines",
						getFieldEditorParent()));

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(TerminalPlugin.getDefault().getPreferenceStore());
	}

}