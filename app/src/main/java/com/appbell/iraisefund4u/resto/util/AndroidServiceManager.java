package com.appbell.iraisefund4u.resto.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
import java.util.Date;

public class AndroidServiceManager implements AndroidAppConstants {

    private static final String CLASS_ID = "AndroidServiceManager:";
    Context context = null;

    public AndroidServiceManager(Context ctx) {
        context = ctx;
    }


    public void startWeeklyNotifAlarmManager() {
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        alarmIntent.putExtra("alarmRequestCode", AndroidAppConstants.ALARM_REQUEST_CODE4WEEK);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, AndroidAppConstants.ALARM_REQUEST_CODE4WEEK, alarmIntent, 0);

        long comingSunday = getComingSunday();

        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        long intervaltime = 7 * 24 * 60 * 60 * 1000; // seven days in milisecond

        manager.setRepeating(AlarmManager.RTC_WAKEUP, comingSunday, intervaltime, pendingIntent);
    }

    private long getComingSunday(){

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 00);
        cal.set(Calendar.MINUTE, 00);
        cal.set(Calendar.SECOND, 00);
        cal.add(Calendar.DATE,1);

        while (true) {
            if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
                return cal.getTimeInMillis();
            }else {
                cal.add(Calendar.DATE,1);
            }
        }
    }

    public void startMonthlyNotifAlarmManager() {

        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        alarmIntent.putExtra("alarmRequestCode", AndroidAppConstants.ALARM_REQUEST_CODE4MONTH);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, AndroidAppConstants.ALARM_REQUEST_CODE4MONTH, alarmIntent, 0);

        Calendar lastDay = Calendar.getInstance();
        lastDay.setTime(new Date());


        lastDay.set(Calendar.DAY_OF_MONTH, lastDay.getActualMaximum(Calendar.DAY_OF_MONTH));
        lastDay.set(Calendar.HOUR_OF_DAY, 23);
        lastDay.set(Calendar.MINUTE, 59);
        lastDay.set(Calendar.SECOND, 59);


        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.set(AlarmManager.RTC_WAKEUP, lastDay.getTimeInMillis(), pendingIntent);
       // manager.setRepeating(AlarmManager.RTC_WAKEUP, lastDay.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pendingIntent);

    }


    public void stopNotifAlarmsOnLogout() {

        try {
            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, AndroidAppConstants.ALARM_REQUEST_CODE4WEEK, new Intent(context, AlarmReceiver.class), PendingIntent.FLAG_UPDATE_CURRENT);
            manager.cancel(pendingIntent);
            pendingIntent.cancel();
        }catch (Throwable tt){}

        try {
            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, AndroidAppConstants.ALARM_REQUEST_CODE4MONTH, new Intent(context, AlarmReceiver.class), PendingIntent.FLAG_UPDATE_CURRENT);
            manager.cancel(pendingIntent);
            pendingIntent.cancel();
        }catch (Throwable tt){}

    }

    public boolean isAlaramRunning(int requestCode) {
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        alarmIntent.putExtra("alarmRequestCode",requestCode);
        boolean  alarmUp = (PendingIntent.getBroadcast(context, requestCode, alarmIntent, PendingIntent.FLAG_NO_CREATE) != null);

        return alarmUp;
    }

    public void startAllService(){
        startWeeklyNotifAlarmManager();
        startMonthlyNotifAlarmManager();
    }
}
