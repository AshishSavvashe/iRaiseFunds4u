package com.appbell.iraisefund4u.common.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Utility class for common utility methods
 * 
 */

public class AndroidToastUtil {
	private static final String CLASS_ID = "AndroidToastUtil:";

	public static void showToast(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}

	public static void showToastShort(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}
}