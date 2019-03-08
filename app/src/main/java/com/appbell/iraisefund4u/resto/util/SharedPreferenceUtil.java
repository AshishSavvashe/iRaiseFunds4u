package com.appbell.iraisefund4u.resto.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.appbell.iraisefund4u.common.util.AppLoggingUtility;

public class SharedPreferenceUtil {
	
	public static long getLong(Context context, String prefKey){
		long val = 0;
		try{
			SharedPreferences prefs = context.getSharedPreferences(AndroidAppConstants.PACKAGE_NAME, Context.MODE_PRIVATE);
			val = prefs.getLong(prefKey, 0);
		}catch(Exception e){
			AppLoggingUtility.logError(context,"Error in getLong for "+ prefKey);
		}
		return val;
	}
	
	public static void putLong(Context context, String prefKey, long value){
		try{
			SharedPreferences prefs = context.getSharedPreferences(AndroidAppConstants.PACKAGE_NAME, Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = prefs.edit();
			editor.putLong(prefKey, value);
			editor.commit();
		}catch(Exception e){
			AppLoggingUtility.logError(context,"Error in putLong for "+ prefKey);
		}
	}
	
	public static String getString(Context context, String prefKey){
		String val = "";
		try{
			SharedPreferences prefs = context.getSharedPreferences(AndroidAppConstants.PACKAGE_NAME, Context.MODE_PRIVATE);
			val = prefs.getString(prefKey, "");
		}catch(Exception e){
			AppLoggingUtility.logError(context,"Error in getString for "+ prefKey);
		}
		return val;
	}
	
	public static void putString(Context context, String prefKey, String value){
		try{
			SharedPreferences prefs = context.getSharedPreferences(AndroidAppConstants.PACKAGE_NAME, Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = prefs.edit();
			editor.putString(prefKey, value);
			editor.commit();
		}catch(Exception e){
			AppLoggingUtility.logError(context,"Error in putString for "+ prefKey);
		}
	}
	
}
