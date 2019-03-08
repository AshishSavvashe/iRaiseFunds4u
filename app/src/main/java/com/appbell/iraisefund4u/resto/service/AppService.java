package com.appbell.iraisefund4u.resto.service;

import android.content.Context;
import android.content.SharedPreferences;

import com.appbell.iraisefund4u.common.service.ServerCommunicationService;
import com.appbell.iraisefund4u.resto.db.DatabaseManager;
import com.appbell.iraisefund4u.resto.db.entity.AppConfigData;
import com.appbell.iraisefund4u.resto.util.RestoAppCache;
import com.appbell.iraisefund4u.resto.util.SharedPreferenceUtil;
import com.appbell.iraisefund4u.resto.vo.AppStateData;

public class AppService extends ServerCommunicationService {

    public AppService(Context ctx) {
        super(ctx);
    }

    public AppStateData getAppState() {
       /* AppStateData data = DatabaseManager.getInstance(context).getAppStateDBHandler().getAppState();
        if (data == null) {
            data = new AppStateData();
            DatabaseManager.getInstance(context).getAppStateDBHandler().createAppState(data);
        }
*/
        return null;
    }

    public AppConfigData getAppConfigs() {
        AppConfigData data = DatabaseManager.getInstance(context).getAppConfigDao().getAppConfigData();
        return data;
    }

   /* public VoucherBookData getVoucherBookData(){
        VoucherBookData data = DatabaseManager.getInstance(context).getVoucherBookDao().getVoucherBookData();
        return data;
    }*/

    public void logoutUser() {

        DatabaseManager.getInstance(context).getAppConfigDao().deleteAppConfigData();
        DatabaseManager.getInstance(context).getVoucherBookDao().deleteAllVoucherBook();
        DatabaseManager.getInstance(context).getVoucherDataDao().deleteAllVoucher();

        SharedPreferenceUtil.putLong(context,"blst",0);

        RestoAppCache.reinitializeAppConfig(context);
    }


    public void saveEnterPhOrEmail4ForgotPassword(String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("iRaiseFunds4u", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("phOrEmail4ForgotPwd" , value);
        editor.commit();
    }

    public String getPhOrEmail4ForgotPassword() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("iRaiseFunds4u", Context.MODE_PRIVATE);
        return sharedPreferences.getString("phOrEmail4ForgotPwd", null);
    }

    public void saveCurrentDeltaTimeInMin(int deltaTimeInMin) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("iRaiseFunds4u", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("deltaTimeInMin", deltaTimeInMin);
        editor.commit();
    }


    public int getCurrentDeltaTimeInMin() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("iRaiseFunds4u", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("deltaTimeInMin", 0);
    }




}