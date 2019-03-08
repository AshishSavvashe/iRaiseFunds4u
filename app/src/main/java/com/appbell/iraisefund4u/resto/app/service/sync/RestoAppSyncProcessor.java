package com.appbell.iraisefund4u.resto.app.service.sync;

import android.content.Context;

import com.appbell.common.util.AppUtil;
import com.appbell.common.web.util.WebConstants;
import com.appbell.iraisefund4u.common.util.AppLoggingUtility;
import com.appbell.iraisefund4u.resto.ui.VoucherBookService;

import java.util.HashMap;
import java.util.Map;

public class RestoAppSyncProcessor {
	Context context;

	public RestoAppSyncProcessor(Context ctx){
		this.context = ctx;
	}

	public synchronized void processRequest(Map<String, String> messages){

		try{
			Map<String, String> map = new HashMap<String, String>();

			String tokens [] = messages.get(WebConstants.CARR_APP_QUEUE_MSG).split("#");

			String paramValStr = null;
			String paramVal [] = null;
			for(int i=0,l=tokens.length;i<l;i++){
				paramValStr = tokens[i];
				paramVal = paramValStr.split("="); 
				if(paramVal.length<2)
					continue;
				map.put(paramVal[0], paramVal[1]);
			}

			String subAction = messages.get(WebConstants.SUBACTION);

			String newVoucher = "s2304";

			if(newVoucher.equals(subAction) ){

				String notificationMsg = map.get("notificationMsg");
				int bookId		       = AppUtil.parseInt(map.get("BookId"));

				if(bookId > 0){
					new VoucherBookService(context).createVoucherAddedNotification(context,bookId,notificationMsg);
				}
			}

		}catch(Exception e){
			AppLoggingUtility.logError(context, e.getMessage());
		}
	}

}
