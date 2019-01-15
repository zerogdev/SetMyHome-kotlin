package com.zerogdev.setmyhome.util;

import android.util.Log;


import com.zerogdev.setmyhome.BuildConfig;

import java.util.Locale;

public class L {

	final static boolean DEBUG = BuildConfig.DEBUG;

	final static String TAG = "SETMYHOME";

	public static void d(String msg) {
		if (DEBUG) {
			if (msg == null)
				msg = "null";
			Log.d(TAG, buildMessage(msg, null));
		}
	}

	public static void d(String format, Object... args) {
		if (DEBUG) {
			Log.d(TAG, buildMessage(format, args));
		}
	}

	private static String buildMessage(String format, Object... args) {
		String msg = args == null ? format : String.format(Locale.getDefault(), format, args);
		StackTraceElement[] trace = (new Throwable()).fillInStackTrace().getStackTrace();
		String caller = "<unknown>";
		int lineNumber = -1;
		String fileName = "<unknown>";

		for (int i = 2; i < trace.length; ++i) {
			Class clazz = trace[i].getClass();
			if (!clazz.equals(L.class)) {
				String callingClass = trace[i].getClassName();
				callingClass = callingClass.substring(callingClass.lastIndexOf(46) + 1);//.
				callingClass = callingClass.substring(callingClass.lastIndexOf(36) + 1);//$
				caller = callingClass + " # " + trace[i].getMethodName();
				lineNumber = trace[i].getLineNumber();
				fileName = trace[i].getFileName();
				break;
			}
		}

		return String.format(Locale.getDefault(), "[%05d] %s : %s  (%s:%d)", new Object[]{Long.valueOf(Thread.currentThread().getId()), caller, msg, fileName, lineNumber});
	}



}
