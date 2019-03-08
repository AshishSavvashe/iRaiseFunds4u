package com.appbell.iraisefund4u.resto.db.handler;

import android.content.Context;


public abstract class CommonDBHandler {
	protected Context context;
	
	public static final int DB_VERSION_1 	= 1;
	public static final int DB_VERSION_2    = 2;
	public static final int DB_VERSION_3    = 3; // version 1.4
	public static final int DB_VERSION_4    = 4; // version 1.6.2


	public CommonDBHandler(Context context){
		this.context = context;
	}
	
}
