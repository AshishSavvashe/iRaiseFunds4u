package com.appbell.iraisefund4u.common.util;

import android.content.Context;
import android.util.Log;

/**
 * Creating Log entries to show; Error log debug log info log
 * 
 */
public class AppLoggingUtility {
	static final String TAG = "iServe4uPOS ";

    public static void logErrorAndDndPost(Context context, Throwable e, String msg) {
        logError(context, "ER: "+ msg + e.toString(), false);
    }

	public static void logError(Context context, Throwable e, String msg) {
		logError(context, "ER: "+ msg + e.toString(), true);
	}

	public static void logError(Context context, Throwable e) {
		logError(context, "ER: "+ e.toString(), true);
	}

    public static void logError(Context context, String msg) {
        logError(context, msg, true);
    }

	public static void logError(Context context, String msg, boolean post2Cloud) {
		Log.e(TAG, msg);
	}


	public static void logDebug(Context context, String msg) {
        Log.d(TAG, msg);
	}

}
