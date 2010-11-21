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
#include <gtk/gtk.h>
#include <vte/vte.h>
#include <string.h>

#include "vte_wrap.h"

/*
 * Borrowed from SWT os_structs.c
 */
#ifndef NO_GdkColor
typedef struct GdkColor_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID pixel, red, green, blue;
} GdkColor_FID_CACHE;

GdkColor_FID_CACHE GdkColorFc;

void cacheGdkColorFields(JNIEnv *env, jobject lpObject)
{
	if (GdkColorFc.cached) return;
	GdkColorFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GdkColorFc.pixel = (*env)->GetFieldID(env, GdkColorFc.clazz, "pixel", "I");
	GdkColorFc.red = (*env)->GetFieldID(env, GdkColorFc.clazz, "red", "S");
	GdkColorFc.green = (*env)->GetFieldID(env, GdkColorFc.clazz, "green", "S");
	GdkColorFc.blue = (*env)->GetFieldID(env, GdkColorFc.clazz, "blue", "S");
	GdkColorFc.cached = 1;
}

GdkColor *getGdkColorFields(JNIEnv *env, jobject lpObject, GdkColor *lpStruct)
{
	if (!GdkColorFc.cached) cacheGdkColorFields(env, lpObject);
	lpStruct->pixel = (guint32)(*env)->GetIntField(env, lpObject, GdkColorFc.pixel);
	lpStruct->red = (guint16)(*env)->GetShortField(env, lpObject, GdkColorFc.red);
	lpStruct->green = (guint16)(*env)->GetShortField(env, lpObject, GdkColorFc.green);
	lpStruct->blue = (guint16)(*env)->GetShortField(env, lpObject, GdkColorFc.blue);
	return lpStruct;
}

void setGdkColorFields(JNIEnv *env, jobject lpObject, GdkColor *lpStruct)
{
	if (!GdkColorFc.cached) cacheGdkColorFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, GdkColorFc.pixel, (jint)lpStruct->pixel);
	(*env)->SetShortField(env, lpObject, GdkColorFc.red, (jshort)lpStruct->red);
	(*env)->SetShortField(env, lpObject, GdkColorFc.green, (jshort)lpStruct->green);
	(*env)->SetShortField(env, lpObject, GdkColorFc.blue, (jshort)lpStruct->blue);
}
#endif

/*
 * Class:     com_randomwalking_swt_terminal_internal_Vte
 * Method:    vte_terminal_new
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_randomwalking_swt_terminal_internal_Vte__1vte_1terminal_1new
  (JNIEnv * env, jclass obj) {
	return (jint)vte_terminal_new();
}

/*
 * Class:     com_randomwalking_swt_terminal_internal_Vte
 * Method:    vte_terminal_fork_comman
 * Signature: (ILjava/lang/String;[Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;ZZZ)I
 */
JNIEXPORT jint JNICALL Java_com_randomwalking_swt_terminal_internal_Vte__1vte_1terminal_1fork_1command
  (JNIEnv * env, jclass obj, jint terminal, jstring command, jobjectArray argv, jobjectArray envv, jstring directory, jboolean lastlog, jboolean utmp, jboolean wtmp) {
	const char *mcommand = NULL;
	if (command != NULL) {
		mcommand = (*env)->GetStringUTFChars(env, command, NULL);
	}
	//const jchar *mcommand = (*env)->GetStringChars(env, command, NULL);
	//const jchar *mdirectory = (*env)->GetStringChars(env, directory, NULL);
	jint pid = vte_terminal_fork_command(VTE_TERMINAL(terminal), mcommand, NULL, NULL, NULL, TRUE, TRUE, TRUE);

	if (mcommand != NULL) {
		(*env)->ReleaseStringUTFChars(env, command, mcommand);
	}
	//
	//(*env)->ReleaseStringChars(env, directory, mdirectory);

	return pid;
}

/*
 * Class:     com_randomwalking_swt_terminal_internal_Vte
 * Method:    _vte_terminal_feed
 * Signature: (ILjava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_randomwalking_swt_terminal_internal_Vte__1vte_1terminal_1feed
  (JNIEnv * env, jclass obj, jint terminal, jstring data) {
	if (data == NULL) {
		return;
	}
	const char * mdata = (*env)->GetStringUTFChars(env, data, NULL);

	vte_terminal_feed(VTE_TERMINAL(terminal), mdata, strlen(mdata));
	(*env)->ReleaseStringUTFChars(env, data, mdata);
}

/*
 * Class:     com_randomwalking_swt_terminal_internal_Vte
 * Method:    _vte_terminal_feed_child
 * Signature: (ILjava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_randomwalking_swt_terminal_internal_Vte__1vte_1terminal_1feed_1child
  (JNIEnv * env, jclass obj, jint terminal, jbyteArray data) {

	jsize datalen = (*env)->GetArrayLength(env, data);
	jbyte* cdata = (*env)->GetByteArrayElements(env, data, NULL);

	vte_terminal_feed_child(VTE_TERMINAL(terminal), (char *)cdata, datalen);

	(*env)-> ReleaseByteArrayElements(env, data, (jbyte *)cdata, 0);
}

/*
 * Class:     com_randomwalking_swt_terminal_internal_Vte
 * Method:    _vte_terminal_set_color_foreground
 * Signature: (ILorg/eclipse/swt/internal/gtk/GdkColor;)V
 */
JNIEXPORT void JNICALL Java_com_randomwalking_swt_terminal_internal_Vte__1vte_1terminal_1set_1color_1foreground
  (JNIEnv * env, jclass obj, jint terminal, jobject color) {
	GdkColor _color, *lcolor=NULL;
	if (color) if ((lcolor = getGdkColorFields(env, color, &_color)) != NULL) {
		vte_terminal_set_color_foreground(VTE_TERMINAL(terminal), lcolor);
	}
}

/*
 * Class:     com_randomwalking_swt_terminal_internal_Vte
 * Method:    _vte_terminal_set_color_background
 * Signature: (ILorg/eclipse/swt/internal/gtk/GdkColor;)V
 */
JNIEXPORT void JNICALL Java_com_randomwalking_swt_terminal_internal_Vte__1vte_1terminal_1set_1color_1background
  (JNIEnv * env, jclass obj, jint terminal, jobject color) {
	GdkColor _color, *lcolor=NULL;
	if (color) if ((lcolor = getGdkColorFields(env, color, &_color)) != NULL) {
		vte_terminal_set_color_background(VTE_TERMINAL(terminal), lcolor);
	}
}

/*
 * Class:     com_randomwalking_swt_terminal_internal_Vte
 * Method:    _vte_terminal_reset
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_randomwalking_swt_terminal_internal_Vte__1vte_1terminal_1reset
  (JNIEnv * env, jclass obj, jint terminal, jboolean clear_tabstops, jboolean clear_history) {
	vte_terminal_reset(VTE_TERMINAL(terminal), clear_tabstops, clear_history);
}

/*
 * Class:     com_randomwalking_swt_terminal_internal_Vte
 * Method:    _vte_terminal_set_scrollback_lines
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_com_randomwalking_swt_terminal_internal_Vte__1vte_1terminal_1set_1scrollback_1lines
  (JNIEnv * env, jclass obj, jint terminal, jint lines) {
	vte_terminal_set_scrollback_lines(VTE_TERMINAL(terminal), lines);
}
