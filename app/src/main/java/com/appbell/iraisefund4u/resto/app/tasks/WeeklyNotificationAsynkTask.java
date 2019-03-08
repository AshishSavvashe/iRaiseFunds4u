package com.appbell.iraisefund4u.resto.app.tasks;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.appbell.common.util.AppUtil;
import com.appbell.iraisefund4u.common.util.AndroidAppUtil;
import com.appbell.iraisefund4u.resto.ui.SplashScreenActivity;
import com.appbell.iraisefund4u.resto.ui.VoucherBookService;
import com.appbell.iraisefund4u.resto.util.AndroidAppConstants;

public class WeeklyNotificationAsynkTask extends AsyncTask<Void, Void, Void> implements AndroidAppConstants {

    protected Context appContext;
    protected String errorMsg;
    protected String successMsg;

    public WeeklyNotificationAsynkTask(Context appContext) {
        this.appContext = appContext;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            successMsg = new VoucherBookService(appContext).getWeeklyNotificationmsg();
        } catch (Throwable e) {
            errorMsg = e.getMessage();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);

        if (AppUtil.isNotBlank(successMsg)) {
            Intent intent = new Intent(appContext, SplashScreenActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(appContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            AndroidAppUtil.generateNotification(appContext, pendingIntent, successMsg, false, AndroidAppConstants.NOTIFICATION_ID_NewVoucherAdded, false, null);
        } else {
            new VoucherBookService(appContext).weeklyNotification(appContext);
        }
    }

}