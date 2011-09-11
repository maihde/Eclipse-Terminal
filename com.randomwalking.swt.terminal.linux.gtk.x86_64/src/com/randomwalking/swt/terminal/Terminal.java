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

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Hashtable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.gtk.GdkColor;
import org.eclipse.swt.internal.gtk.GtkRequisition;
import org.eclipse.swt.internal.gtk.OS;
import org.eclipse.swt.widgets.Composite;

import com.randomwalking.swt.terminal.internal.Vte;

public class Terminal extends Composite implements ITerminal {

    private long terminalHandle;

	private long scrolledHandle;

	private OutputStream outputStream;

	static Hashtable terminals = new Hashtable();

	public Terminal(Composite parent, int style) {
		/* Things break if H_SCROLL or V_SCROLL are passed here. */
		super(parent, style & ~SWT.H_SCROLL & ~SWT.V_SCROLL);
		createHandle(style);
		if (terminalHandle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		terminals.put(new Long(terminalHandle), this);

		addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent e) {
				Terminal.this.widgetDisposed(e);
			}

		});

		addControlListener(new ControlListener() {

			@Override
			public void controlResized(ControlEvent e) {
				Terminal.this.controlResized(e);
			}

			@Override
			public void controlMoved(ControlEvent e) {
				Terminal.this.controlMoved(e);
			}
		});
	}

	@Override
	public void start() {
		Vte.vte_terminal_fork_command(terminalHandle, null, null, null, null, false, false, false);
	}

	@Override
	public void start(String command, String[] argv, String[] envv, String directory) {
		Vte.vte_terminal_fork_command(terminalHandle, command, argv, envv, directory, false, false, false);
	}

	@Override
	public OutputStream getOutputStream() {
		if (outputStream == null) {
			outputStream = new OutputStream() {

				@Override
				public void write(byte[] b) throws IOException {
					Vte.vte_terminal_feed_child(terminalHandle, b);
				}



				@Override
				public void write(byte[] b, int off, int len)
				throws IOException {
					Vte.vte_terminal_feed_child(terminalHandle, Arrays.copyOfRange(b, off, off+len));
				}



				@Override
				public void write(int b) throws IOException {
					final byte[] data = new byte[1];
					data[0] = (byte)(b & 0xFF);
					Vte.vte_terminal_feed_child(terminalHandle, data);
				}

			};
		}
		return outputStream;
	}

	@Override
	public void clear() {
		Vte.vte_terminal_reset(terminalHandle, true, true);
		// If we don't push a 'carriage-return' we don't get a prompt after the reset
		Vte.vte_terminal_feed_child(terminalHandle, "\n".getBytes());
	}

	/* Implement widget functions */
	protected void controlResized(ControlEvent e) {
		checkWidget();
		final Rectangle rect = getClientArea ();
		setBounds(rect.x, rect.y, rect.width, rect.height);
		OS.gtk_widget_set_size_request (terminalHandle, rect.width, rect.height);
	}

	protected void controlMoved(ControlEvent e) {
		checkWidget();

	}

	protected void widgetDisposed(DisposeEvent e) {
		terminals.remove(new Long(terminalHandle));
		terminalHandle = 0;
	}

	public void focusGained(FocusEvent e) {
		checkWidget();
	}

	@Override
	public Point computeSize (int wHint, int hHint, boolean changed) {
		checkWidget ();
		if (wHint != SWT.DEFAULT && wHint < 0) wHint = 0;
		if (hHint != SWT.DEFAULT && hHint < 0) hHint = 0;

		final Point size = computeNativeSize(terminalHandle, wHint, hHint, changed);
		final Rectangle trim = computeTrim (0, 0, size.x, size.y);
		size.x = trim.width;
		size.y = trim.height;

		return size;
	}

	@Override
	public void setBackground(Color color) {
		GdkColor gdkColor = null;
		if (color != null) {
			if (color.isDisposed ()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
			gdkColor = color.handle;
		}
		if (gdkColor != null) {
			Vte.vte_terminal_set_color_background(terminalHandle, gdkColor);
		}
	}

	@Override
	public void setForeground(Color color) {
		GdkColor gdkColor = null;
		if (color != null) {
			if (color.isDisposed ()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
			gdkColor = color.handle;
		}
		if (gdkColor != null) {
			Vte.vte_terminal_set_color_foreground(terminalHandle, gdkColor);
		}
	}

	@Override
	public void setScrollBackLines(int lines) {
		Vte.vte_terminal_set_scrollback_lines(terminalHandle, lines);
	}

	// Stolen from Control.java
	protected Point computeNativeSize (long /*int*/ h, int wHint, int hHint, boolean changed) {
		int width = wHint, height = hHint;
		if (wHint == SWT.DEFAULT && hHint == SWT.DEFAULT) {
			final GtkRequisition requisition = new GtkRequisition ();
			OS.gtk_widget_size_request (h, requisition);
			width = OS.GTK_WIDGET_REQUISITION_WIDTH (h);
			height = OS.GTK_WIDGET_REQUISITION_HEIGHT (h);
		} else if (wHint == SWT.DEFAULT || hHint == SWT.DEFAULT) {
			final int [] reqWidth = new int [1], reqHeight = new int [1];
			OS.gtk_widget_get_size_request (h, reqWidth, reqHeight);
			OS.gtk_widget_set_size_request (h, wHint, hHint);
			final GtkRequisition requisition = new GtkRequisition ();
			OS.gtk_widget_size_request (h, requisition);
			OS.gtk_widget_set_size_request (h, reqWidth [0], reqHeight [0]);
			width = wHint == SWT.DEFAULT ? requisition.width : wHint;
			height = hHint == SWT.DEFAULT ? requisition.height : hHint;
		}
		return new Point (width, height);
	}

	protected void createHandle(int style) {
		/* Since Composite doesn't work H_SCROLL or V_SCROLL make our own. */
		scrolledHandle = OS.gtk_scrolled_window_new (0, 0);
		if (scrolledHandle == 0) SWT.error (SWT.ERROR_NO_HANDLES);

		terminalHandle = Vte.vte_terminal_new();

		OS.gtk_container_add (handle, scrolledHandle);
		OS.gtk_container_add (scrolledHandle, terminalHandle);

		final int hsp = (style & SWT.H_SCROLL) != 0 ? OS.GTK_POLICY_AUTOMATIC : OS.GTK_POLICY_NEVER;
		final int vsp = (style & SWT.V_SCROLL) != 0 ? OS.GTK_POLICY_AUTOMATIC : OS.GTK_POLICY_NEVER;
		OS.gtk_scrolled_window_set_policy (scrolledHandle, hsp, vsp);

		OS.gtk_widget_show(scrolledHandle);
		OS.gtk_widget_show(terminalHandle);
	}

}
