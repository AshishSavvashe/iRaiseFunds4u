package com.appbell.iraisefund4u.resto.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.appbell.common.codevalues.service.CodeValueConstants;
import com.appbell.common.util.AppUtil;
import com.appbell.iraisefund4u.R;
import com.appbell.iraisefund4u.resto.ui.AddCardActivity;
import com.appbell.iraisefund4u.resto.ui.ChangePasswordActivity;
import com.appbell.iraisefund4u.resto.ui.LandingOptionsActivity;
import com.appbell.iraisefund4u.resto.ui.MapViewFragment;
import com.appbell.iraisefund4u.resto.ui.RegisterCardActivity;
import com.appbell.iraisefund4u.resto.ui.UserLoginActivity;
import com.appbell.iraisefund4u.resto.ui.VoucherBookListActivity;
import com.appbell.iraisefund4u.resto.ui.VoucherListActivity;
import com.appbell.iraisefund4u.resto.ui.WebViewActivity;
import com.appbell.iraisefund4u.resto.ui.WebViewFragment;


public class NavigationUtil {

    public static void navigate2VoucherBookWebView(Activity activity){

        String serverUrl = (AndroidAppConstants.isHTTPS ? "https://" : "http://") + AndroidAppConstants.SERVER_URL + "/" + AndroidAppConstants.SERVER_CONTEXT + "/";

        String bookIds[] = RestoAppCache.getAppConfig(activity).getVoucherBookIds().split(",");
        String urlMap;

        if(bookIds != null && bookIds.length > 1){
            StringBuilder sbEncryptedBookIds = new StringBuilder();
            String seperator="";
            for (int i = 0; i < bookIds.length; i++) {
                sbEncryptedBookIds.append(seperator).append(AppUtil.encryptVoucherBookId(AppUtil.parseInt(bookIds[i])));
                seperator = "@";
            }
            urlMap = serverUrl + "hmpg/default/blst.jsp?bid=" + sbEncryptedBookIds.toString();
        }else{
            urlMap = serverUrl + "hmpg/default/imbpage.jsp?bid=" + AppUtil.encryptVoucherBookId(AppUtil.parseInt(bookIds[0]));
        }

        urlMap = urlMap + "&_source=" + CodeValueConstants.REQUEST_SOURCE_AndroidApp;

        Intent intent = new Intent(activity, WebViewFragment.class);
        intent.putExtra("url", urlMap);
        intent.putExtra("title", activity.getResources().getString(R.string.app_name));
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    public static void navigateUserLoginActivity(Activity activity){
        Intent intent = new Intent(activity, UserLoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }

    public static void navigate2LandingOptionsActivity(Activity activity){
        Intent intent = new Intent(activity, LandingOptionsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    public static void navigate2RegisterCardActivity(Activity activity){
        Intent intent = new Intent(activity, RegisterCardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }


    public static  void navigate2VoucherBookListActivity(Activity activity,boolean boolen,String bookBuyerPhoneNo){
        Intent intent = new Intent(activity,VoucherBookListActivity.class);
        intent.putExtra("title", activity.getResources().getString(R.string.app_name));
        intent.putExtra("webviewFlag",boolen);
        intent.putExtra("bookBuyerPhoneNo",bookBuyerPhoneNo);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }


    public static void navigate2VoucherListActivity(int voucherBookID, String campaignTagLine, Activity activity, boolean isSingalBook,long startDate, long endDate) {

        Intent intent = new Intent(activity,VoucherListActivity.class);
        intent.putExtra("voucherBookID",voucherBookID);
        intent.putExtra("campaignTagLine",campaignTagLine);
        intent.putExtra("title", activity.getResources().getString(R.string.app_name));
        intent.putExtra("isSinagleBook",isSingalBook);
        intent.putExtra("startDate",startDate);
        intent.putExtra("endDate",endDate);
        activity.startActivity(intent);

    }

    public static void navigate2ChangePassword(FragmentActivity activity) {
        Intent intent = new Intent(activity,ChangePasswordActivity.class);
        activity.startActivity(intent);
    }

    public static void navigate2AddCard(Activity activity) {
        Intent intent = new Intent(activity,AddCardActivity.class);
        activity.startActivity(intent);
    }

    public static void navigate2MapView(Activity activity, Bundle bundle) {
        Intent intent = new Intent(activity,MapViewFragment.class);
        intent.putExtra("restaurantLatLog", bundle);
        activity.startActivity(intent);
    }




    public static void navigate2BuyBookWebView(Activity activity,boolean isHandelBrrowserBack){

        String serverUrl = (AndroidAppConstants.isHTTPS ? "https://" : "http://") + AndroidAppConstants.SERVER_URL + "/" + AndroidAppConstants.SERVER_CONTEXT + "/";

        String urlMap = serverUrl + "EXT/BuyVoucherBook.jsp";

       // urlMap = urlMap + "&_source=" + CodeValueConstants.REQUEST_SOURCE_AndroidApp;

        Intent intent = new Intent(activity, WebViewActivity.class);
        intent.putExtra("url", urlMap);
        intent.putExtra("title", activity.getResources().getString(R.string.app_name));
        intent.putExtra("isHandelBrrowserBack",isHandelBrrowserBack);
        activity.startActivity(intent);

    }
}
