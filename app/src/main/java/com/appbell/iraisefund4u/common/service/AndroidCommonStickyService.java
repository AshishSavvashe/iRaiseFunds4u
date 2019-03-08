package com.appbell.iraisefund4u.common.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AndroidCommonStickyService extends Service {
	
	@Override
	public void onCreate() {
		super.onCreate();	
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}
}
