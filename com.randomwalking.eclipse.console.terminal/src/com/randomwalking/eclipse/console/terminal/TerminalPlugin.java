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
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class TerminalPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.randomwalking.eclipse.console.terminal"; //$NON-NLS-1$

	// Remove console icon borrowed from org.eclipse.ui.console
	public static final String CLOSE_ICON = "icons/full/clcl16/rem_co.gif";

	// Clear console icon borrowed from org.eclipse.ui.console
	public static final String CLEAR_ICON = "icons/full/clcl16/clear_co.gif";

	// Terminal icon, shrunken version of standard gnome-terminal icon
	public static final String TERMINAL_ICON = "icons/full/clcl16/terminal.gif";



	// The shared instance
	private static TerminalPlugin plugin;

	/**
	 * The constructor
	 */
	public TerminalPlugin() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static TerminalPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path.
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
}
