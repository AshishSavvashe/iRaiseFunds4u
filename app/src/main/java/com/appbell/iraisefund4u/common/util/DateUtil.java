package com.appbell.iraisefund4u.common.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.appbell.common.util.AppUtil;
import com.appbell.iraisefund4u.resto.service.AppService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class DateUtil {



    public static void calculateDeltaTimeInMin(Context context,String currentServerTime ){

        long currentServertime = AppUtil.parseLong(currentServerTime);
        long currentDeviceTime = new Date().getTime();

        long diff = currentDeviceTime - currentServertime;
        int deltaTimeInMin = (int) TimeUnit.MILLISECONDS.toMinutes(diff);



        if(deltaTimeInMin > 0 && deltaTimeInMin <= 1){
            new AppService(context).saveCurrentDeltaTimeInMin(0);
        }else {
            new AppService(context).saveCurrentDeltaTimeInMin(deltaTimeInMin);
        }
    }

    public static Date getCurrentTime(Context context, Date date){

        Calendar calendar = Calendar.getInstance();

        calendar.setTimeZone(TimeZone.getTimeZone(DateUtil.getBookTimeZone(context)));
        calendar.setTime(date);

        calendar.add(Calendar.MINUTE, - new AppService(context).getCurrentDeltaTimeInMin());

        return calendar.getTime();
    }

    public static Calendar getCalanderObject(Context context, Date date){

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(DateUtil.getBookTimeZone(context)), Locale.US);

        calendar.setTime(date);
        return calendar;
    }


    public static void saveBookTimeZone(Context context , String timeZone){
        SharedPreferences sharedPreferences = context.getSharedPreferences("iRaiseFunds4u",context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("TimeZone",timeZone);
        editor.commit();
    }

    public static String getBookTimeZone(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("iRaiseFunds4u",context.MODE_PRIVATE);
        return sharedPreferences.getString("TimeZone","America/Los_Angeles");
    }

    public static SimpleDateFormat getDateFormatter(Context context ,String dateFormat) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(DateUtil.getBookTimeZone(context)));
        return simpleDateFormat;
    }
}
