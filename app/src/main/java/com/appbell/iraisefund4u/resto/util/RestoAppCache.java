package com.appbell.iraisefund4u.resto.util;

import android.content.Context;

import com.appbell.iraisefund4u.resto.service.AppService;
import com.appbell.iraisefund4u.resto.db.entity.AppConfigData;
import com.appbell.iraisefund4u.resto.vo.AppStateData;


public class RestoAppCache {
	private static AppStateData appState;
	private static AppConfigData appConfig;
	
	public static AppStateData getAppState(Context ctx){
		if(appState == null){
			appState = new AppService(ctx).getAppState();
		}
		return appState;
	}
	
	public static void setAppState(AppStateData newState){
		appState = newState;
	}
	
	public static AppStateData reinitializeAppState(Context ctx){
		appState = null;
		return getAppState(ctx);
	}
	
	public static AppConfigData getAppConfig(Context ctx){
		if(appConfig == null){
			appConfig = new AppService(ctx).getAppConfigs();
		}
		return appConfig;
	}
	
	public static void setAppConfig(AppConfigData newAppConfig){
		appConfig = newAppConfig;
	}
	
	public static AppConfigData reinitializeAppConfig(Context ctx){
		appConfig = null;
		return getAppConfig(ctx);
	}
	
	public static void clearTechPropertiesCache(){
		appConfig = null;
	}
	
}
