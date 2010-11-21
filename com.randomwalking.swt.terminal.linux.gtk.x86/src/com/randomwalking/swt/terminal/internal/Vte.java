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
package com.randomwalking.swt.terminal.internal;

import org.eclipse.swt.internal.Platform;
import org.eclipse.swt.internal.gtk.GdkColor;

public class Vte {
	static {
		try {
			System.loadLibrary("vte_wrap");
		} catch (final UnsatisfiedLinkError e) {
			System.err.println("Native code library failed to load. \n" + e);
			System.exit(1);
		}
	}

	/**
	 * Creates a new terminal widget.
	 */
	private final static native int _vte_terminal_new();
	public final static int vte_terminal_new() {
		Platform.lock.lock();
		try {
			return _vte_terminal_new();
		} finally {
			Platform.lock.unlock();
		}
	}

	/**
	 * Starts the specified command under a newly-allocated controlling pseudo-terminal.
	 * The argv and envv lists should be NULL-terminated, and argv[0] is expected to be the
	 * name of the file being run, as it would be if execve() were being called. TERM is automatically
	 * set to reflect the terminal widget's emulation setting. If lastlog, utmp, or wtmp are TRUE, logs the
	 * session to the specified system log files.
	 * 
	 * Note that all file descriptors except stdin/stdout/stderr will be closed before calling exec() in the child.
	 */
	private final static native int _vte_terminal_fork_command(
			int handle,
			String command,
			String argv[],
			String envv[],
			String directory,
			boolean lastlog,
			boolean utmp,
			boolean wtmp);
	public final static int vte_terminal_fork_command(
			int handle,
			String command,
			String argv[],
			String envv[],
			String directory,
			boolean lastlog,
			boolean utmp,
			boolean wtmp) {
		Platform.lock.lock();
		try {
			return _vte_terminal_fork_command(handle, command, argv, envv, directory, lastlog, utmp, wtmp);
		} finally {
			Platform.lock.unlock();
		}
	}

	/**
	 * Interprets data as if it were data received from a child process. This can either be used to drive the terminal
	 * without a child process, or just to mess with your users.
	 */
	private final static native void _vte_terminal_feed(int handle, String data);
	public final static void vte_terminal_feed(int handle, String data) {
		Platform.lock.lock();
		try {
			_vte_terminal_feed(handle, data);
		} finally {
			Platform.lock.unlock();
		}
	}

	/**
	 * Sends a block of UTF-8 text to the child as if it were entered by the user at the keyboard.
	 */
	private final static native void _vte_terminal_feed_child(int handle, byte[] data);
	public final static void vte_terminal_feed_child(int handle, byte[] data) {
		Platform.lock.lock();
		try {
			_vte_terminal_feed_child(handle, data);
		} finally {
			Platform.lock.unlock();
		}
	}

	/**
	 * Sets the foreground color used to draw normal text.
	 */
	private final static native void _vte_terminal_set_color_foreground(int handle, GdkColor color);
	public final static void vte_terminal_set_color_foreground(int handle, GdkColor color) {
		Platform.lock.lock();
		try {
			_vte_terminal_set_color_foreground(handle, color);
		} finally {
			Platform.lock.unlock();
		}
	}

	/**
	 * Sets the background color for text which does not have a specific background color assigned.
	 * Only has effect when no background image is set and when the terminal is not transparent.
	 */
	private final static native void _vte_terminal_set_color_background(int handle, GdkColor color);
	public final static void vte_terminal_set_color_background(int handle, GdkColor color) {
		Platform.lock.lock();
		try {
			_vte_terminal_set_color_background(handle, color);
		} finally {
			Platform.lock.unlock();
		}
	}

	/**
	 * Resets as much of the terminal's internal state as possible, discarding any unprocessed input data,
	 * resetting character attributes, cursor state, national character set state,
	 * status line, terminal modes (insert/delete), selection state, and encoding.
	 */
	private final static native void _vte_terminal_reset(int handle, boolean clear_tabstops, boolean clear_history);
	public final static void vte_terminal_reset(int handle, boolean clear_tabstops, boolean clear_history) {
		Platform.lock.lock();
		try {
			_vte_terminal_reset(handle, clear_tabstops, clear_history);
		} finally {
			Platform.lock.unlock();
		}
	}

	/**
	 * Sets the length of the scrollback buffer used by the terminal. The size of the scrollback buffer will
	 * be set to the larger of this value and the number of visible rows the widget can display, so 0 can safely be used to disable scrollback.
	 * A negative value means "infinite scrollback". Note that this setting only affects the normal screen buffer.
	 * For terminal types which have an alternate screen buffer, no scrollback is allowed on the alternate screen buffer.
	 */
	private final static native void _vte_terminal_set_scrollback_lines(int handle, int lines);
	public final static void vte_terminal_set_scrollback_lines(int handle, int lines) {
		Platform.lock.lock();
		try {
			_vte_terminal_set_scrollback_lines(handle, lines);
		} finally {
			Platform.lock.unlock();
		}
	}
}
