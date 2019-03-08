package com.appbell.iraisefund4u.resto.db;

import android.content.Context;

public class DatabaseManager {
	private static AppDatabase appDatabase;
	
	public static AppDatabase getInstance(Context context) {
		if(appDatabase == null) {
			appDatabase = AppDatabase.getAppDatabase(context);
		}
		return appDatabase;
	}
}
