package com.appbell.iraisefund4u.resto.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.appbell.iraisefund4u.resto.ui.VoucherBookService;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        AndroidServiceManager manager = new AndroidServiceManager(context);

        int requestCode= intent.getIntExtra("alarmRequestCode", 1);

        if(requestCode == AndroidAppConstants.ALARM_REQUEST_CODE4WEEK){
             new VoucherBookService(context).createEveryWeekNotification(context);
        }else if(requestCode == AndroidAppConstants.ALARM_REQUEST_CODE4MONTH){
            new VoucherBookService(context).createFirstdayofMonthNotification(context);
        }

        // This intent is received when android device restarts

        if (Intent.ACTION_BOOT_COMPLETED.equalsIgnoreCase(intent.getAction()) || Intent.ACTION_REBOOT.equalsIgnoreCase(intent.getAction())) {
            manager.startAllService();

        }

    }
}
