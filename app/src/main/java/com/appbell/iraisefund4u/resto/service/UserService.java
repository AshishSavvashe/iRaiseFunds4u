package com.appbell.iraisefund4u.resto.service;

import android.content.Context;
import android.provider.Settings.Secure;

import com.appbell.common.codevalues.service.CodeValueConstants;
import com.appbell.common.util.AppUtil;
import com.appbell.common.web.util.WebConstants;
import com.appbell.iraisefund4u.common.exception.ApplicationException;
import com.appbell.iraisefund4u.common.service.ServerCommunicationService;
import com.appbell.iraisefund4u.common.util.AppLoggingUtility;
import com.appbell.iraisefund4u.common.util.DateUtil;
import com.appbell.iraisefund4u.resto.db.DatabaseManager;
import com.appbell.iraisefund4u.resto.db.entity.AppConfigData;
import com.appbell.iraisefund4u.resto.ui.VoucherBookData;
import com.appbell.iraisefund4u.resto.util.RestoAppCache;
import com.appbell.iraisefund4u.resto.vo.ResponseVO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserService extends ServerCommunicationService {

    public UserService(Context ctx) {
        super(ctx);
    }


    public void getCurrentServerTime() throws ApplicationException {

        Map<String, String> requestData = new HashMap<>();

        ResponseVO response = processServerRequestInSyncMode(requestData, WebConstants.ACTION_VoucherBookAction, "s2293", CodeValueConstants.YesNo_Yes);

        if (response != null) {

            JSONObject jsonObj = response.getJsonResponse();

            try {

                JSONObject userDate = jsonObj.getJSONObject("dataMap");
                String status = jsonObj.getString("status");

                if (status.equalsIgnoreCase(CodeValueConstants.YesNo_Yes)) {

                    String currentServerTime = userDate.getString("currentServerTime");
                    DateUtil.calculateDeltaTimeInMin(context,currentServerTime);

                }
            } catch (JSONException e) {
                AppLoggingUtility.logError(context,e.getMessage());
            }
        }
    }

    public String authenticateUser(String loginId, String pwd,String deviceToken4Android) throws ApplicationException {

        AppConfigData appConfigData = RestoAppCache.getAppConfig(context);

        String deviceUID = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);

        Map<String, String> requestData = new HashMap<>();
        requestData.put("loginId", loginId);
        requestData.put("password", pwd);
        requestData.put("deviceToken4Android",deviceToken4Android);
        requestData.put("activationStatus4Android",CodeValueConstants.YesNo_Yes);


        ResponseVO response = processServerRequestInSyncMode(requestData, WebConstants.ACTION_VoucherBookAction, "s2280", CodeValueConstants.YesNo_Yes);//TODO: Replace subAction with WebConstant

        String result = null;

        if (response != null) {

            JSONObject jsonObj = response.getJsonResponse();

            try {

                JSONObject userDate = jsonObj.getJSONObject("dataMap");
                result = jsonObj.getString("status");

                if (result.equalsIgnoreCase(CodeValueConstants.YesNo_Yes)) {

                    int commonUserId = AppUtil.parseInt(userDate.getString("commonUserId"));
                    String custName = userDate.getString("custName");
                    String custEmailId = userDate.getString("custEmailId");
                    String custPhone = userDate.getString("custPhone");
                    String voucherBookIds = userDate.getString("voucherBookIds");
                    String currentServerTime = userDate.getString("currentServerTime");

                    DateUtil.calculateDeltaTimeInMin(context,currentServerTime);

                    AppConfigData configData = new AppConfigData();
                    configData.setCurrentLoggedInCommonUserId(commonUserId);
                    configData.setCustomerName(custName);
                    configData.setCustomerPhone(custPhone);
                    configData.setCustomerEmail(custEmailId);
                    configData.setVoucherBookIds(voucherBookIds);

                    DatabaseManager.getInstance(context).getAppConfigDao().createAppConfigData(configData);
                    RestoAppCache.reinitializeAppConfig(context);

                }

            } catch (JSONException e) {
                AppLoggingUtility.logError(context,e.getMessage());
            }

        }
        return result;
    }


    public String logOutUser4Server() throws ApplicationException {

        Map<String, String> requestData = new HashMap<>();
        requestData.put("commonUserId",String.valueOf(RestoAppCache.getAppConfig(context).getCurrentLoggedInCommonUserId()));
        requestData.put("activationStatus4Android",CodeValueConstants.YesNo_No);

        ResponseVO response = processServerRequestInSyncMode(requestData, WebConstants.ACTION_VoucherBookAction, "s1003", CodeValueConstants.YesNo_Yes);

        String successMsg = null;

        if (response != null) {

            JSONObject jsonObj = response.getJsonResponse();

            try {

                JSONObject userDate = jsonObj.getJSONObject("dataMap");
                String status = jsonObj.getString("status");

                if (status.equalsIgnoreCase(CodeValueConstants.YesNo_Yes)) {
                    successMsg = userDate.getString("successMsg");
                }

            } catch (JSONException e) {
                AppLoggingUtility.logError(context,e.getMessage());

            }
        }
        return successMsg;
    }


    public String registerUserWithBook(String customerName, String customerEmail, String customerPhone, String bookCode ,String customerPassword,String deviceToken4Android) throws ApplicationException {

        String deviceUID = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);

        Map<String, String> requestData = new HashMap<>();
        requestData.put("customerName", customerName);
        requestData.put("customerEmail", customerEmail);
        requestData.put("customerPhone", customerPhone);
        requestData.put("bookCode", bookCode);
        requestData.put("customerPassword",customerPassword);
        requestData.put("deviceToken4Android",deviceToken4Android);
        requestData.put("activationStatus4Android",CodeValueConstants.YesNo_Yes);
        requestData.put("deviceUID", deviceUID);


        ResponseVO response = processServerRequestInSyncMode(requestData, WebConstants.ACTION_VoucherBookAction, "s2281", CodeValueConstants.YesNo_Yes);

        String successMsg = null;

        if (response != null) {

            JSONObject jsonObj = response.getJsonResponse();

            try {

                JSONObject userDate = jsonObj.getJSONObject("dataMap");
                String status = jsonObj.getString("status");

                if (status.equalsIgnoreCase(CodeValueConstants.YesNo_Yes)) {

                    AppConfigData configData = new AppConfigData();

                    configData.setCurrentLoggedInCommonUserId(AppUtil.parseInt(userDate.getString("commonUserId")));
                    configData.setVoucherBookIds(userDate.getString("voucherBookIds"));
                    configData.setUserLoginPassword(userDate.getString("pwd"));

                    configData.setCustomerName(customerName);
                    configData.setCustomerPhone(customerPhone);
                    configData.setCustomerEmail(customerEmail);
                    configData.setUserLoginId(customerPhone);

                    successMsg = userDate.getString("successMsg");

                    DatabaseManager.getInstance(context).getAppConfigDao().createAppConfigData(configData);
                    RestoAppCache.reinitializeAppConfig(context);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return successMsg;
    }


    /**
     * Change the password
     *
     * @param oldPassword
     * @param newPassword
     * @return
     * @throws ApplicationException
     */
    public String changePassword(String oldPassword, String newPassword) throws ApplicationException {
        Map<String, String> requestData = createRequestObject(RestoAppCache.getAppConfig(context));
        requestData.put("phone", String.valueOf(RestoAppCache.getAppConfig(context).getCustomerPhone()));
        requestData.put("oldPassword", oldPassword);
        requestData.put("newPassword", newPassword);

        ResponseVO response = processServerRequestInSyncMode(requestData, WebConstants.ACTION_VoucherBookAction, "s2282", CodeValueConstants.YesNo_Yes);

        String successMsg = null;

        if (response != null) {

            JSONObject jsonObj = response.getJsonResponse();

            try {

                JSONObject userDate = jsonObj.getJSONObject("dataMap");
                String status = jsonObj.getString("status");

                if (status.equalsIgnoreCase(CodeValueConstants.YesNo_Yes)) {

                    successMsg = userDate.getString("successMsg");

                    AppConfigData configData = new AppConfigData();

                    configData.setUserLoginPassword(newPassword);

                    DatabaseManager.getInstance(context).getAppConfigDao().updateUserPassword(newPassword);

                }
            } catch (JSONException e) {
                AppLoggingUtility.logError(context,e.getMessage());
            }
        }
        return successMsg;
    }

    public String forgotPassword_sync(String mobNoOrEmailId) throws ApplicationException {

        Map<String, String> requestData = new HashMap<>();
        requestData.put("loginId", mobNoOrEmailId);

        ResponseVO response = processServerRequestInSyncMode(requestData, WebConstants.ACTION_VoucherBookAction, "s2283", CodeValueConstants.YesNo_Yes);

        String successMsg = null;

        if (response != null) {

            JSONObject jsonObj = response.getJsonResponse();

            try {

                JSONObject userDate = jsonObj.getJSONObject("dataMap");
                String status = jsonObj.getString("status");

                if (status.equalsIgnoreCase(CodeValueConstants.YesNo_Yes)) {
                    successMsg = userDate.getString("successMsg");
                }

            } catch (JSONException e) {
                AppLoggingUtility.logError(context,e.getMessage());

            }
        }
        return successMsg;
    }

    public String registerNewBook(String bookCode) throws ApplicationException {

        Map<String, String> requestData = new HashMap<>();
        requestData.put("bookCode", bookCode);
        requestData.put("commonUserId", String.valueOf(RestoAppCache.getAppConfig(context).getCurrentLoggedInCommonUserId()));

        ResponseVO response = processServerRequestInSyncMode(requestData, WebConstants.ACTION_VoucherBookAction, "s2305", CodeValueConstants.YesNo_Yes);

        String successMsg = null;

        if (response != null) {

            JSONObject jsonObj = response.getJsonResponse();

            try {
                String status = jsonObj.getString("status");

                if(status.equalsIgnoreCase(CodeValueConstants.YesNo_Yes)){

                    JSONObject jsonDataMap = jsonObj.getJSONObject("dataMap");

                    ArrayList<VoucherBookData> voucherbookList =  new Gson().fromJson(jsonObj.optString("dataList"), new TypeToken<ArrayList<VoucherBookData>>() {}.getType());

                    DatabaseManager.getInstance(context).getVoucherBookDao().createVoucherBookData(voucherbookList.get(0));

                    successMsg = jsonDataMap.getString("successMsg");
                }

            } catch (JSONException e) {
                AppLoggingUtility.logError(context,e.getMessage());
            }
        }
        return successMsg;
    }
}