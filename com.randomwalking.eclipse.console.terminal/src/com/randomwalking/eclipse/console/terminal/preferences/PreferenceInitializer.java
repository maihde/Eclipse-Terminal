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

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.SWT;
import org.eclipse.ui.PlatformUI;

import com.randomwalking.eclipse.console.terminal.TerminalPlugin;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	@Override
	public void initializeDefaultPreferences() {
		final IPreferenceStore store = TerminalPlugin.getDefault().getPreferenceStore();
		PreferenceConverter.setDefault(store, PreferenceConstants.P_BACKGROUND_COLOR, PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND).getRGB());
		PreferenceConverter.setDefault(store, PreferenceConstants.P_FOREGROUND_COLOR, PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_LIST_FOREGROUND).getRGB());

		store.setDefault(PreferenceConstants.P_LIMIT_CONSOLE_OUTPUT, true);
		store.setDefault(PreferenceConstants.P_CONSOLE_OUTPUT_MAX_LINES, 500);
	}

}
