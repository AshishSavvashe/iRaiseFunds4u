package com.appbell.iraisefund4u.resto.service;

import android.content.Context;

import com.appbell.iraisefund4u.common.service.ServerCommunicationService;
import com.appbell.iraisefund4u.common.util.AndroidAppUtil;
import com.appbell.iraisefund4u.resto.util.AndroidAppConstants;
import com.appbell.iraisefund4u.resto.util.RestoAppCache;

public class MiscService extends ServerCommunicationService {
	private static String URL;
	
	public MiscService(Context ctx) {
		super(ctx);
	}
	
	/**
	 * 
	 * @param carrierConfig
	 * @return
	 */
	public String getBaseUrl() {

		URL = AndroidAppConstants.SERVER_URL ;


		String baseUrl = (isHTTPS ? "https://" : "http://") + URL + "/" + AndroidAppConstants.SERVER_CONTEXT + "/";
		return baseUrl;
	}

}
