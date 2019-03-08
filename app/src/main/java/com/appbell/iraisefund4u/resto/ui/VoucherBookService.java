package com.appbell.iraisefund4u.resto.ui;

import android.app.PendingIntent;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.appbell.common.codevalues.service.CodeValueConstants;
import com.appbell.common.util.AppUtil;
import com.appbell.common.web.util.WebConstants;
import com.appbell.iraisefund4u.common.exception.ApplicationException;
import com.appbell.iraisefund4u.common.service.ServerCommunicationService;
import com.appbell.iraisefund4u.common.util.AndroidAppUtil;
import com.appbell.iraisefund4u.common.util.AppLoggingUtility;
import com.appbell.iraisefund4u.common.util.DateUtil;
import com.appbell.iraisefund4u.resto.app.tasks.WeeklyNotificationAsynkTask;
import com.appbell.iraisefund4u.resto.db.DatabaseManager;
import com.appbell.iraisefund4u.resto.util.AndroidAppConstants;
import com.appbell.iraisefund4u.resto.util.AndroidServiceManager;
import com.appbell.iraisefund4u.resto.util.RestoAppCache;
import com.appbell.iraisefund4u.resto.vo.ResponseVO;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class VoucherBookService extends ServerCommunicationService {

    public VoucherBookService(Context ctx) {
        super(ctx);
    }

    public ArrayList<VoucherBookData> getVoucherBookList_sync(long lastSyncTime) throws  ApplicationException {

        Map<String, String> requestData = createRequestObject(RestoAppCache.getAppConfig(context));
        requestData.put("commonUserId", String.valueOf(RestoAppCache.getAppConfig(context).getCurrentLoggedInCommonUserId()));
        requestData.put("lastSyncTime",String.valueOf(lastSyncTime));
        ArrayList<VoucherBookData> voucherbookList = new ArrayList<>();

        ResponseVO response = processServerRequestInSyncMode(requestData, WebConstants.ACTION_VoucherBookAction, "s2284",CodeValueConstants.YesNo_Yes);

        if (response != null) {
            JSONObject jsonObj = response.getJsonResponse();

            try {
                String dataList = jsonObj.optString("dataList");
                JSONArray jsonArray = new JSONArray(dataList);

                  JSONObject userDate = jsonObj.getJSONObject("dataMap");
                  String currentServerTime = userDate.getString("currentServerTime");
                  DateUtil.calculateDeltaTimeInMin(context,currentServerTime);

                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<VoucherBookData>>() {}.getType();
                voucherbookList = gson.fromJson(jsonArray.toString(), listType);

                VoucherBookData voucherBookData = null;
                VoucherBookData existingVoucherBookData = null;

                for ( int i = 0 ; i < voucherbookList.size() ; i ++ ){
                    voucherBookData =  voucherbookList.get(i);
                    existingVoucherBookData = DatabaseManager.getInstance(context).getVoucherBookDao().getVoucherDataBy(voucherBookData.getBookId());
                    if(existingVoucherBookData != null){
                        voucherBookData.setAppVoucherBookId(existingVoucherBookData.getAppVoucherBookId());
                        DatabaseManager.getInstance(context).getVoucherBookDao().updateVoucherBookData(voucherbookList.get(i));
                    }else{
                        DatabaseManager.getInstance(context).getVoucherBookDao().createVoucherBookData(voucherbookList.get(i));
                    }
                }
            } catch (JSONException e) {
                AppLoggingUtility.logError(context, e.getMessage());
            }
        }
        return voucherbookList;
    }

    public ArrayList<VoucherData> getVoucherList (int voucherBookID,long lastSyncTime)throws ApplicationException{

        Map<String, String> requestData = createRequestObject(RestoAppCache.getAppConfig(context));
        requestData.put("voucherBookID", String.valueOf(voucherBookID));
        requestData.put("lastSyncTime",String.valueOf(lastSyncTime));

        long lastdeletedVoucherTime4App =  DatabaseManager.getInstance(context).getVoucherBookDao().getLastDeletedVoucherTime(voucherBookID);

        requestData.put("lastSyncTime4DeleteVoucher",String.valueOf(lastdeletedVoucherTime4App));

        ArrayList<VoucherData> voucherList = null;

        ResponseVO response = processServerRequestInSyncMode(requestData, WebConstants.ACTION_VoucherBookAction, "s2285",CodeValueConstants.YesNo_Yes);

        if (response != null ) {
            JSONObject jsonObj = response.getJsonResponse();
            try {
                String dataList = jsonObj.optString("dataList");

                JSONObject userDate = jsonObj.getJSONObject("dataMap");

                String lastDeletedVoucherTime = userDate.getString("lastSyncTime4DeleteVoucher");

                long lastdeleteVoucherTime4Server = AppUtil.parseLong(lastDeletedVoucherTime);

                VoucherData voucherData = null;

                if(dataList.isEmpty()){
                    voucherList = new ArrayList<>();
                }else {
                    JSONArray jsonArray = new JSONArray(dataList);

                    Gson gson = new Gson();
                    Type listType = new TypeToken<ArrayList<VoucherData>>() {}.getType();
                    voucherList = gson.fromJson(jsonArray.toString(), listType);
                }

                if(lastdeleteVoucherTime4Server > lastdeletedVoucherTime4App ) {
                    DatabaseManager.getInstance(context).getVoucherDataDao().deleteVoucher(voucherBookID);
                }

                VoucherData existingVoucherData = null;

                Date cureentDate= DateUtil.getCurrentTime(context, new Date());

                Calendar lastDay = DateUtil.getCalanderObject(context,DateUtil.getCurrentTime(context, new Date()));
                lastDay.set(Calendar.DAY_OF_MONTH, lastDay.getActualMaximum(Calendar.DAY_OF_MONTH));
                lastDay.set(Calendar.HOUR_OF_DAY, 23);
                lastDay.set(Calendar.MINUTE, 59);
                lastDay.set(Calendar.SECOND, 59);

                Date time4LastDayOfMonth = lastDay.getTime();

                for( int i = 0 ; i< voucherList.size() ; i ++) {
                    voucherData = voucherList.get(i);

                    existingVoucherData = DatabaseManager.getInstance(context).getVoucherDataDao().getVoucherData(voucherData.getVoucherId());

                    Date VoucherEndDate = new Date(voucherData.getVoucherEndTime());
                    Date voucherStartTime = new Date(voucherData.getVoucherStartTime());

                    if (AndroidAppUtil.compareOnlyDates(context,voucherStartTime, time4LastDayOfMonth) > 0) {
                        voucherData.setVoucherStatusId(AndroidAppConstants.VOUCHER_COMMINGSOON);
                    } else if (AndroidAppUtil.compareOnlyDates(context,cureentDate, VoucherEndDate) > 0 && !(CodeValueConstants.VOUCHER_STATUS_Redeemed.equals(voucherData.getVoucherStatus()))){
                        voucherData.setVoucherStatusId(AndroidAppConstants.VOUCHER_EXPIRED);
                    } else if (CodeValueConstants.VOUCHER_STATUS_Redeemed.equals(voucherData.getVoucherStatus())) {
                        voucherData.setVoucherStatusId(AndroidAppConstants.VOUCHER_REDEEMED);
                    } else{
                        voucherData.setVoucherStatusId(AndroidAppConstants.VOUCHER_AVIABLE4SALE);
                    }

                    if(existingVoucherData != null ){
                        voucherData.setAppVoucherBookId(existingVoucherData.getAppVoucherBookId());
                        DatabaseManager.getInstance(context).getVoucherDataDao().updateVoucherData(voucherData);
                    }else {
                        DatabaseManager.getInstance(context).getVoucherDataDao().createVoucherData(voucherData);
                    }
                }

              DatabaseManager.getInstance(context).getVoucherBookDao().updateVoucherDeletedTime(lastdeleteVoucherTime4Server,voucherBookID);

            } catch (JSONException e) {
                AppLoggingUtility.logError(context,e.getMessage());
            }
        }
        return voucherList ;
    }


    public void getvoucherReddemDate4Report(int voucherBookID)throws ApplicationException {

        Map<String, String> requestData = createRequestObject(RestoAppCache.getAppConfig(context));
        requestData.put("voucherBookID", String.valueOf(voucherBookID));

        ResponseVO response = processServerRequestInSyncMode(requestData, WebConstants.ACTION_VoucherBookAction, "s2292", CodeValueConstants.YesNo_Yes);

        if (response != null) {

            JSONObject jsonObj = response.getJsonResponse();

            try {
                JSONObject userDate = jsonObj.getJSONObject("dataMap");
                String result = jsonObj.getString("status");

                if (result.equalsIgnoreCase(CodeValueConstants.YesNo_Yes)) {

                    Iterator<String> iter = userDate.keys();
                    String keyVoucherId;
                    String redeemDates;
                    while (iter.hasNext()) {
                        keyVoucherId = iter.next();
                        redeemDates = userDate.optString(keyVoucherId);
                        DatabaseManager.getInstance(context).getVoucherDataDao().updateVoucherRedeemDate(redeemDates,Integer.parseInt(keyVoucherId));
                    }
                }
            } catch (JSONException e) {
                AppLoggingUtility.logError(context, e.getMessage());
            }
        }
    }

    /**
     *
     * @param voucherId
     * @return
     * @throws ApplicationException
     */
    public String[] redeemVoucher_sync(int voucherId)  throws  ApplicationException {

        Map<String, String> requestData = createRequestObject(RestoAppCache.getAppConfig(context));
        requestData.put("voucherId", String.valueOf(voucherId));

        String result[] = {"",""};

        ResponseVO response = processServerRequestInSyncMode(requestData, WebConstants.ACTION_VoucherBookAction, "s2286",CodeValueConstants.YesNo_Yes);
        if (response != null) {
            JSONObject jsonObj = response.getJsonResponse();
            try {

                JSONObject userDate = jsonObj.getJSONObject("dataMap");
                String status = jsonObj.getString("status");

                if (status.equalsIgnoreCase(CodeValueConstants.YesNo_Yes)) {

                    result[0] = userDate.getString("successMsg");
                    result[1] = userDate.getString("usageCountStr");
                }
            } catch (JSONException e) {
               AppLoggingUtility.logError(context,e.getMessage());
            }
        }
        return result;
    }

    public String voucherReddemDate(int facilityId, int restaurantId, int voucherId)throws ApplicationException {

        Map<String, String> requestData = createRequestObject(RestoAppCache.getAppConfig(context));
        requestData.put("facilityId", String.valueOf(facilityId));
        requestData.put("restaurantId", String.valueOf(restaurantId));
        requestData.put("voucherId", String.valueOf(voucherId));

        ResponseVO response = processServerRequestInSyncMode(requestData, WebConstants.ACTION_VoucherBookAction, "s2289", CodeValueConstants.YesNo_Yes);

        String result = "";

        if (response != null) {

            JSONObject jsonObj = response.getJsonResponse();

            try {
                JSONObject userDate = jsonObj.getJSONObject("dataMap");
                if (CodeValueConstants.YesNo_Yes.equalsIgnoreCase(jsonObj.getString("status"))) {
                    result = userDate.getString("strRedeemedDate");
                }
            } catch (JSONException e) {
                AppLoggingUtility.logError(context, e.getMessage());
            }

        }
        return result;
    }

    public Bundle restaurantDetails(int voucherBookID ){

        ArrayList<VoucherData> voucherlist;
        Bundle bundle = new Bundle();

        voucherlist = (ArrayList<VoucherData>) DatabaseManager.getInstance(context).getVoucherDataDao().getAllVoucherList(voucherBookID);

        if (voucherlist != null && voucherlist.size() > 0) {

            VoucherData voucherData;

            for (int i = 0, l = voucherlist.size(); i < l; i++) {
                voucherData = voucherlist.get(i);

                bundle.putParcelable(voucherlist.get(i).getRestaurantName(), new LatLng(voucherData.getRestaurantlat(), voucherData.getRestaurantlong()));
            }
        }
        return bundle ;
    }


    public ArrayList<String> getRestaurantName(int voucherBookID){

        ArrayList<String> restaurantNameList = null ;
        restaurantNameList = (ArrayList<String>) DatabaseManager.getInstance(context).getVoucherDataDao().getRestaurantname(voucherBookID);
        return  restaurantNameList;
    }


    public String getRedeemVoucherRestoName(int voucherBookID,String voucherRedeemDate){

        String redeemVoucherRestoName = null;
        redeemVoucherRestoName = DatabaseManager.getInstance(context).getVoucherDataDao().getRedeemVoucherRestoname(voucherBookID,voucherRedeemDate);
        return redeemVoucherRestoName;
    }

    public String getRedeemVoucherDesc(int voucherBookID,String restaurantName){

        String redeemVoucherDesc = null;
        redeemVoucherDesc = DatabaseManager.getInstance(context).getVoucherDataDao().getRedeemVoucherDesc(voucherBookID,restaurantName);
        return redeemVoucherDesc;
    }

    public ArrayList<VoucherData> getLastMontVoucherList(int voucherBookID ){

        ArrayList<VoucherData> lastMonthVoucherList = null ;

        Calendar curDateCal =DateUtil.getCalanderObject(context,DateUtil.getCurrentTime(context, new Date()));
        curDateCal.setTime(DateUtil.getCurrentTime(context,new Date()));
        curDateCal.set(Calendar.HOUR_OF_DAY, 23);
        curDateCal.set(Calendar.MINUTE, 59);
        curDateCal.set(Calendar.SECOND, 59);
        curDateCal.set(Calendar.MILLISECOND, 0);

        lastMonthVoucherList = (ArrayList<VoucherData>) DatabaseManager.getInstance(context).getVoucherDataDao().getExpiredVoucherList(voucherBookID, curDateCal.getTime().getTime());

        return  lastMonthVoucherList;
    }


    public ArrayList<VoucherData> getAllMontVoucherList(int voucherBookID ){

        ArrayList<VoucherData> allMonthVoucherList = null ;

        Calendar curDateCal = DateUtil.getCalanderObject(context,DateUtil.getCurrentTime(context, new Date()));
        curDateCal.setTime(DateUtil.getCurrentTime(context,new Date()));
        curDateCal.set(Calendar.HOUR_OF_DAY, 0);
        curDateCal.set(Calendar.MINUTE, 0);
        curDateCal.set(Calendar.SECOND, 1);

        allMonthVoucherList = (ArrayList<VoucherData>) DatabaseManager.getInstance(context).getVoucherDataDao().getActiveVoucherList(voucherBookID, curDateCal.getTime().getTime());

        return  allMonthVoucherList;
    }


    public ArrayList<VoucherData> getAllVoucherList(int voucherBookID){

        ArrayList<VoucherData> voucherList = null;
        voucherList = (ArrayList<VoucherData>) DatabaseManager.getInstance(context).getVoucherDataDao().getAllVoucherList(voucherBookID);
        return voucherList;
    }


    public LiveData<List<VoucherData>> getAllLiveVoucherList(int voucherBookID ){

        LiveData<List<VoucherData>> allLiveVoucherList = null ;

        Calendar curDateCal = DateUtil.getCalanderObject(context,DateUtil.getCurrentTime(context, new Date()));
        curDateCal.set(Calendar.HOUR_OF_DAY, 0);
        curDateCal.set(Calendar.MINUTE, 0);
        curDateCal.set(Calendar.SECOND, 1);

        return  allLiveVoucherList;
    }


    public String getTermsandCondition(int voucherBookID ) {

        String termsAndCondition ;
        termsAndCondition = DatabaseManager.getInstance(context).getVoucherBookDao().getTermsAndCondition(voucherBookID);
        return termsAndCondition;
    }

    public String getVoucherRedeemDates(int voucherBookID ){
        String voucherRedeemDate = null;

        voucherRedeemDate = DatabaseManager.getInstance(context).getVoucherDataDao().getVoucherRedeemDates(voucherBookID);
        return voucherRedeemDate;
    }

    public void createVoucherAddedNotification(Context context, int bookId, String notificationMsg) {

        Intent intent = new Intent(context, SplashScreenActivity.class);
        intent.putExtra("notificationMsg",notificationMsg);
        intent.putExtra("bookId",bookId);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AndroidAppUtil.generateNotification(context, pendingIntent,"New coupons added.", true, AndroidAppConstants.NOTIFICATION_ID_NewVoucherAdded, false, null);
    }

    public void createEveryWeekNotification(Context context) {

        if(AndroidAppUtil.isInternetAvailable(context)){
            new WeeklyNotificationAsynkTask(context).execute();
        }else {
            weeklyNotification(context);
        }
    }


    public void weeklyNotification(Context context){

        Intent intent = new Intent(context, SplashScreenActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        ArrayList<VoucherBookData> allBookList;

        allBookList = (ArrayList<VoucherBookData>) DatabaseManager.getInstance(context).getVoucherBookDao().getVoucherBookData();

        int voucherCount = 0;
        int redeemVoucherCount = 0;
        VoucherBookData voucherBookData;

        for (int i = 0, l = allBookList.size(); i < l; i++) {
            voucherBookData = allBookList.get(i);
            voucherCount = voucherCount + DatabaseManager.getInstance(context).getVoucherDataDao().getAllVoucherCount(voucherBookData.getBookId());
        }

        redeemVoucherCount = DatabaseManager.getInstance(context).getVoucherDataDao().getRedemVoucherCount(AndroidAppConstants.RedeemVoucher);

        AndroidAppUtil.generateNotification(context, pendingIntent,"Used " + redeemVoucherCount +  " coupons from "+ voucherCount + " coupons.", false, AndroidAppConstants.NOTIFICATION_ID_NewVoucherAdded, false, null);

    }

    public String getWeeklyNotificationmsg() throws ApplicationException {

        Map<String, String> requestData = new HashMap<>();
        requestData.put("commonUserId", String.valueOf(RestoAppCache.getAppConfig(context).getCurrentLoggedInCommonUserId()));

        ResponseVO response = processServerRequestInSyncMode(requestData, WebConstants.ACTION_VoucherBookAction, "s2312", CodeValueConstants.YesNo_Yes);

        String successMsg = null;

        if (response != null) {

            JSONObject jsonObj = response.getJsonResponse();

            try {
                String status = jsonObj.getString("status");

                if(status.equalsIgnoreCase(CodeValueConstants.YesNo_Yes)){

                    JSONObject jsonDataMap = jsonObj.getJSONObject("dataMap");

                    successMsg = jsonDataMap.getString("successMsg");
                }

            } catch (JSONException e) {
                AppLoggingUtility.logError(context,e.getMessage());
            }
        }
        return successMsg;
    }


    public void createFirstdayofMonthNotification(Context context) {

        Intent intent = new Intent(context, SplashScreenActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AndroidAppUtil.generateNotification(context, pendingIntent,"New coupons are available for your use.", true, AndroidAppConstants.NOTIFICATION_ID_NewVoucherAdded, false, null);

        new AndroidServiceManager(context).startMonthlyNotifAlarmManager();
    }

}
