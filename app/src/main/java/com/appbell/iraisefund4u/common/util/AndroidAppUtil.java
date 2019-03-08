package com.appbell.iraisefund4u.common.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.text.Layout;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Patterns;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.EditText;
import android.widget.Toast;

import com.appbell.iraisefund4u.R;
import com.appbell.iraisefund4u.resto.util.AndroidAppConstants;
import com.appbell.iraisefund4u.resto.util.RestoAppCache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Utility class for common utility methods
 *
 */
public class AndroidAppUtil {
	private static final String CLASS_ID = "AndroidAppUtil:";


	public static void slideDownAnimation(View view){
		view.setVisibility(View.VISIBLE);
		view.setAlpha(0.0f);
		view.animate().translationY(view.getHeight()).alpha(1.0f);
	}

	public static void slideBackAnimation(final View view){
		view.animate().translationY(0).alpha(0.0f).setListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				super.onAnimationEnd(animation);
				view.setVisibility(View.GONE);
			}
		});
	}

	public static int[] getScreenSize(Activity context){
		int[] size = new int[2];
		try{
			DisplayMetrics displaymetrics = new DisplayMetrics();
			context.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
			size[0] = displaymetrics.heightPixels == 0 ? 800 : displaymetrics.heightPixels;
			size[1] = displaymetrics.widthPixels == 0 ? 600 : displaymetrics.widthPixels;
		}catch (Throwable t){
			size[0] = 800;
			size[1] = 600;
		}
		return size;
	}
	public static AlphaAnimation getClickAnimation(){
		return new AlphaAnimation(1F, 0.3F);
	}

	public static AlphaAnimation getHighlightAnimation(){
		AlphaAnimation anim = new AlphaAnimation(1F, 0.4F);
		anim.setDuration(1000);
		anim.setRepeatCount(1);
		return anim;
	}

	public static ScaleAnimation getZoomAnimation(int zoomEffect){
		ScaleAnimation scal = null;
		if(zoomEffect == AndroidAppConstants.ANIMATION_EFFECT_ZOOM_IN){
			scal = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f, Animation.RELATIVE_TO_SELF, (float)0.5, Animation.RELATIVE_TO_SELF, (float)0.5);
		}else{
			scal = new ScaleAnimation(1.1f, 1.0f, 1.1f, 1.0f, Animation.RELATIVE_TO_SELF, (float)0.5, Animation.RELATIVE_TO_SELF, (float)0.5);
		}
		scal.setDuration(2000);
		scal.setRepeatCount(5);
		scal.setRepeatMode(2);
		scal.setFillAfter(true);
		return scal;
	}

	public static ScaleAnimation getZoomInOutAnimation(){
		ScaleAnimation scal = new ScaleAnimation(1.0f, 1.05f, 1.0f, 1.05f, Animation.RELATIVE_TO_SELF, (float)0.5, Animation.RELATIVE_TO_SELF, (float)0.5);
		scal.setDuration(1000);
		scal.setRepeatCount(1);
		scal.setRepeatMode(2);
		scal.setFillAfter(true);
		return scal;
	}


	public static void hideKeyboard(Activity activity){
		if(activity == null)
			return;
		try{
			InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
		}catch(Exception e){}
	}

	public static AnimationSet getAnimation(int transitionEffect, int zoomEffect){
		TranslateAnimation animation = null;
		ScaleAnimation scal = null;

		if(transitionEffect== AndroidAppConstants.ANIMATION_EFFECT_LEFT_TO_RIGHT){
			animation = new TranslateAnimation(20,-20, 0, 0);
		}else if(transitionEffect==AndroidAppConstants.ANIMATION_EFFECT_RIGHT_TO_LEFT){
			animation = new TranslateAnimation(-20, 20, 0, 0);
		}else if(transitionEffect==AndroidAppConstants.ANIMATION_EFFECT_TOP_TO_DOWN){
			animation = new TranslateAnimation(0, 0, -20, 20);
		}else{
			animation = new TranslateAnimation(0, 0, 20, -20);
		}

		if(zoomEffect == AndroidAppConstants.ANIMATION_EFFECT_ZOOM_IN){
			scal = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f, Animation.RELATIVE_TO_SELF, (float)0.5, Animation.RELATIVE_TO_SELF, (float)0.5);
		}else{
			scal = new ScaleAnimation(1.2f, 1.1f, 1.2f, 1.1f, Animation.RELATIVE_TO_SELF, (float)0.5, Animation.RELATIVE_TO_SELF, (float)0.5);
		}

		animation.setDuration(5000);
	    animation.setRepeatCount(5);
	    animation.setRepeatMode(2);
	    animation.setFillAfter(true);

	    scal.setDuration(5000);
	    scal.setRepeatCount(5);
	    scal.setRepeatMode(2);
	    scal.setFillAfter(true);

	    AnimationSet anmSet  = new AnimationSet(true);
	    //anmSet.addAnimation(animation);
	    anmSet.addAnimation(scal);

	    return anmSet;
	}

	/**
	 *
	 * @param context
	 * @return
	 */
	public static boolean isGpsProviderAvailable(Context context){
		LocationManager manager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
		return manager.getProvider(LocationManager.GPS_PROVIDER) != null;
	}

	public static boolean isGpsProviderEnabled(Context context){
		LocationManager manager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
		return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}

	public static boolean isNetworkProviderEnabled(Context context){
		LocationManager manager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
		return manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	}

  /*  public static void hideKeyboard(Context context, AlertDialog dialog){
        if(context ==null || dialog == null)
            return;
        try{
            InputMethodManager inputMethodManager = (InputMethodManager)  context.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(dialog.getCurrentFocus().getWindowToken(), 0);
        }catch(Exception e){}
    }*/

   /* public static void hideKeyboard(Context context, Dialog dialog){
        if(context ==null || dialog == null)
            return;
        try{
            InputMethodManager inputMethodManager = (InputMethodManager)  context.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(dialog.getCurrentFocus().getWindowToken(), 0);
        }catch(Exception e){}
    }
*/



	public static void hideEditTextKeyboard(Context context, EditText editText){
		try {
			InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
		} catch (Exception e) {}

	}

    public static void showKeyboardForcefully(Context context){
        try {
            InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        } catch (Exception e) {}

    }

    public static void showKeyboardForEditText(Context context, EditText editText){
        try {
            InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);

        } catch (Exception e) {}

    }

	/**
	 * Method to detect if phone is tablet
	 *
	 * @param context
	 * @return
	 */
	public static boolean isTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout
				& Configuration.SCREENLAYOUT_SIZE_MASK)
				>= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}


    public static boolean isTabletWithLandscapeMode(Context context) {
        return isTablet(context) && context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

	public static Boolean isNWAvailable(Context context) {
		TelephonyManager tel = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return ((tel.getNetworkOperator() == null || tel.getNetworkOperator().equals("")) ? false : true);
	}

	/**
	 * Method to check if network available
	 *
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) {
		boolean isNetworkAvailable = false;
		try {
			ConnectivityManager cManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (cManager != null) {
				NetworkInfo[] info = cManager.getAllNetworkInfo();
				if (info != null) {
					isNetworkAvailable = true;
				}
			}
		} catch (Exception e) {
            AppLoggingUtility.logError(context,e, CLASS_ID + "Error in isNetworkAvailable");
		}
		return isNetworkAvailable;
	}

	/**
	 * Method to check if internet is available on android device
	 *
	 * @return
	 */
	public static boolean isInternetAvailable(Context context) {
		boolean isInternetAvailable = false;

		try {
			ConnectivityManager cManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = cManager.getActiveNetworkInfo();
			if (netInfo != null && netInfo.isConnectedOrConnecting()) {
				isInternetAvailable = true;
			}
		} catch (Exception e) {
			AppLoggingUtility.logError(context,e, CLASS_ID + "Error in isInternetAvailable");
		}
		return isInternetAvailable;
	}

	public static boolean isInternetAvailableWithMsg(Context context) {
		boolean isIntAvlbl = isInternetAvailable(context);
		if(!isIntAvlbl){
			Toast.makeText(context, "Internet is not available.", Toast.LENGTH_LONG).show();
		}
		return isIntAvlbl;
	}

	public static String getString(Context ctx, int id) {
		return ctx.getResources().getString(id);

	}


	/**
	 * Method to get text from editText view
	 *
	 * @param activity
	 * @param id
	 * @return
	 */
	public static String parseEditText(Activity activity, int id) {
		String value = "";
		EditText editText = (EditText) activity.findViewById(id);
		if (editText != null) {
			value = editText.getText().toString().trim();
		}
		return value;
	}


	public static boolean isSDCardPresent(){
		return  Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}

	public static boolean isDateChanging(Date date1, Date date2){
		boolean isDateChanging = false;
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);

		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);

		isDateChanging = cal1.get(Calendar.DATE) != cal2.get(Calendar.DATE);

		return isDateChanging;
	}

	/*public static boolean isDateSame(Date firstDate, Date secondDate){
		SimpleDateFormat compareFormat = DateUtil.getDateFormatter("yyyyMMdd");
		return compareFormat.format(firstDate).equals(compareFormat.format(secondDate));
	}*/

	public static long getFolderSize(File dir) {
		long size = 0;

		if(!dir.exists())
			return 0;

		for (File file : dir.listFiles()) {
			if (file.isFile())
				size += file.length();
			else
				size += getFolderSize(file);
		}
		return size;
	}

	public static String getBaseFolderPath(){
		String path = Environment.getExternalStorageDirectory().getPath() + "/iRaiseFund4u";
		return path;
	}

	public static String getClientFolderPath(){
		String path = getFacilityFolderPath() + "/client";
		return path;
	}

	public static String getServerReceiptFolderPath(){
		String path = getServerFolderPath() + "/receipts";
		return path;
	}

	public static String getServerFolderPath(){
		String path = getFacilityFolderPath() + "/server";
		return path;
	}

    public static String getClientMiscFileFolderPath(){
        String path = getClientFolderPath() + "/miscfiles";
        return path;
    }

	public static String getFacilityFolderPath(){
		String path = getBaseFolderPath() + "/facility";
		return path;
	}

	public static String getDatabaseFolderPath() {
		String path;

		if(isSDCardPresent()){
			path = getBaseFolderPath() + "/database/";
		}else{
			path = "";
		}

		return path;
	}

	public static String getClientFilePath(String fileName){
		String path = getClientFolderPath() + "/" + fileName;
		return path;
	}

	public static File getClientFile(String fileName){
		String path = getClientFilePath(fileName);
		return new File(path);
	}

	public static String getServerFilePath(String fileName){
		String path = getServerFolderPath() + "/" + fileName;
		return path;
	}

	public static File getServerFile(String fileName){
		String path = getServerFilePath(fileName);
		return new File(path);
	}

	public static String getReceiptFilePath(String fileName){
		String path = getServerReceiptFolderPath() + "/" + fileName;
		return path;
	}

	public static File getReceiptFile(String fileName){
		String path = getReceiptFilePath(fileName);
		return new File(path);
	}

	public static boolean isDatabasePresentOnSdCard() {
		File file = new File(getDatabaseFolderPath() + "/pmldb");
		return file.exists();
	}

	public static String[] getAppVersionInfo(Context context){
		String result [] = new String[2];
		try{
			PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			result[0] = pInfo.versionName;
			result[1] = String.valueOf(pInfo.versionCode);
		}catch(Throwable t){
			result[0] = "";
			result[1] = "";
            AppLoggingUtility.logError(context,CLASS_ID + t.getMessage());
		}
		return result;
	}

	public static String getAppVersionName(Context context){
        String result = "";
		try{
			PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			result = pInfo.versionName;
		}catch(NameNotFoundException nnf){
            AppLoggingUtility.logError(context,CLASS_ID + nnf.getMessage());
        }

		return result;
	}


	public static void copyStream(InputStream is, OutputStream os){
		final int buffer_size=1024;
		try{
			byte[] bytes=new byte[buffer_size];
			for(;;){
				int count=is.read(bytes, 0, buffer_size);
				if(count==-1)
					break;
				os.write(bytes, 0, count);
			}
		}
		catch(Exception ex){}
	}

	public static boolean isvalidEmailAddress(String emailId){
		Pattern pattern = Patterns.EMAIL_ADDRESS;
		return pattern.matcher(emailId).matches();
	}

	public static String trim(String msg){
		return msg == null ? "" : msg.trim();
	}

	public static boolean isBlank(String str){
		return str == null || str.trim().length() <= 0 || str.equalsIgnoreCase("null") ;
	}


    public static boolean isUserLoggedIn(Context context){
        return RestoAppCache.getAppConfig(context) == null ?  false : RestoAppCache.getAppConfig(context).getCurrentLoggedInCommonUserId() > 0;
    }


	public static String getDateDifferenceString(long currentTime, long msgTime) {
		long differenceInMilliseconds  = currentTime - msgTime;
		DecimalFormat df = new DecimalFormat("#");
		int minutes  = (int)(differenceInMilliseconds/1000/60) % 60;
		int hours	= (int)(differenceInMilliseconds/1000/60/60) % 24;
		int days 	= (int)differenceInMilliseconds/1000/60/60/24;
		String output = "";
		String timeunit = "";
		if(days > 0){
			timeunit = (days==1) ? " day ago" : " days ago";
			output = "("+days+timeunit+")";
		}else{
			if(hours > 0){
				timeunit = (hours==1) ? " hr ago" : " hrs ago";
				output = "("+hours +timeunit+")";
			}else if(minutes > 0 || minutes == 0){
				minutes = minutes == 0 ? 1 :minutes;
				timeunit = (minutes == 1) ? " minute ago" : " minutes ago";
				output = "("+df.format(minutes) + timeunit+")";
			}
		}
		return output;
	}

	public static int getDateDifferenceInDays(long currentDate, long futureDate) {

	    if(futureDate < currentDate)
	        return -1;

        long differenceInMilliseconds = futureDate - currentDate;
        int days = (int) (differenceInMilliseconds / 1000 / 60 / 60 / 24);
        return days;
    }

    public static String getDifferenceInDaysStr(long currentDate, long futureDate) {
	    int  days = getDateDifferenceInDays(currentDate, futureDate);
        String result="";

        if(days >= 0 && days < 1)
            result = "Today";
        else if(days == 1)
            result = days + " day remaining";
        else if(days > 1)
            result = days + " days remaining";

        return result;
    }

    public static double getTimeDifferenceInMin(long currentDate, long time) {
        long differenceInMilliseconds = currentDate - time;
        int minutes  = (int)(differenceInMilliseconds/1000/60);
        return minutes;
    }

    public static String replaceSpecialChars(String msg){
		if(isBlank(msg)){
			return msg;
		}
		msg = msg.replace("'", "\\'");
		return msg;
	}


    //decodes image and scales it to reduce memory consumption
    public static Bitmap decodeFile(File f) {
        try {
            //decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            FileInputStream stream1=new FileInputStream(f);
            BitmapFactory.decodeStream(stream1,null,o);
            stream1.close();

            //Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE=70;
            int width_tmp=o.outWidth, height_tmp=o.outHeight;
            int scale=1;
            while(true){
                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
                    break;
                width_tmp/=2;
                height_tmp/=2;
                scale*=2;
            }

            //decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            FileInputStream stream2=new FileInputStream(f);
            Bitmap bitmap= BitmapFactory.decodeStream(stream2, null, o2);
            stream2.close();
            return bitmap;
        } catch (FileNotFoundException e) {
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void moveCursorToLast(EditText et){
    	et.setSelection(et.getText().length());
    }

	public static boolean isValidMobile(String number) {
		return !AndroidAppUtil.isBlank(number) && number.length() > 9 && number.length() < 14 && PhoneNumberUtils.isGlobalPhoneNumber(number);
	}


    public static boolean isKitkatAndAbove(){
        return android.os.Build.VERSION.SDK_INT >= 19;
    }




    public static String validatePhoneNumber(String phoneNo){
        phoneNo = normalizePhoneNumber(phoneNo);
        if(phoneNo.length() != 10){
            return "Please enter correct phone number";
        }
        return null;
    }

    public static String normalizePhoneNumber(String phoneNumber) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            phoneNumber = PhoneNumberUtils.normalizeNumber(phoneNumber);
        }else{
            phoneNumber = normalizeNumber(phoneNumber);
        }
        return phoneNumber;
    }




    public static boolean isTextViewEllipsis(Layout layout) {
        if(layout == null)
            return false;

        boolean result = false;
        try {
            int lines = layout.getLineCount();
            if(lines > 0) {
                int ellipsisCount = layout.getEllipsisCount(lines-1);
                if ( ellipsisCount > 0) {
                    result = true;
                }
            }
        }catch (Throwable tt){

        }
       return result;
    }


    public static void clearAllCookies(Context context) {
        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                CookieManager.getInstance().removeAllCookies(null);
                CookieManager.getInstance().flush();
            } else
            {
                CookieSyncManager cookieSyncMngr= CookieSyncManager.createInstance(context);
                cookieSyncMngr.startSync();
                CookieManager cookieManager= CookieManager.getInstance();
                cookieManager.removeAllCookie();
                cookieManager.removeSessionCookie();
                cookieSyncMngr.stopSync();
                cookieSyncMngr.sync();
            }
        }catch (Throwable tt){
            AppLoggingUtility.logError(context, tt, " clearAllCookies: ");
        }
    }


    /**
     * method to get webserver url
     *
     * @return
     */
    public static String getAppRequestProcessorURL(){
        String serverUrl = (AndroidAppConstants.isHTTPS ? "https://" : "http://") + AndroidAppConstants.SERVER_URL + "/" + AndroidAppConstants.SERVER_CONTEXT + "/";
        serverUrl = serverUrl + AndroidAppConstants.URLPART_RequestProcessor;
        return serverUrl;
    }

    /**
     * Normalize a phone number by removing the characters other than digits. If
     * the given number has keypad letters, the letters will be converted to
     * digits first.
     *
     * @param phoneNumber the number to be normalized.
     * @return the normalized number.
     */
    public static String normalizeNumber(String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber)) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        int len = phoneNumber.length();
        for (int i = 0; i < len; i++) {
            char c = phoneNumber.charAt(i);
            // Character.digit() supports ASCII and Unicode digits (fullwidth, Arabic-Indic, etc.)
            int digit = Character.digit(c, 10);
            if (digit != -1) {
                sb.append(digit);
            } else if (sb.length() == 0 && c == '+') {
                sb.append(c);
            }
        }
        return sb.toString();
    }

	public static ArrayList<String> getVoucherBookMonthList(Context context,long startDate, long endDate){
		Calendar startCal = DateUtil.getCalanderObject(context,new Date(startDate));

		ArrayList<String> monthList = new ArrayList<String>();
		SimpleDateFormat sdf = DateUtil.getDateFormatter(context,"MMM-yy");
		while(compareOnlyDates(context,startCal.getTime(), new Date(endDate)) <= 0){
			monthList.add(sdf.format(startCal.getTime()));
			startCal.add(Calendar.MONTH, 1);
		}
		return monthList;
	}


	public static int compareOnlyDates(Context context,Date date1, Date date2){
		Calendar cal1 = DateUtil.getCalanderObject(context,date1);
		cal1.set(Calendar.HOUR_OF_DAY, 0);
		cal1.set(Calendar.MINUTE, 0);
		cal1.set(Calendar.SECOND, 0);
		cal1.set(Calendar.MILLISECOND, 0);

		Calendar cal2 = DateUtil.getCalanderObject(context,date2);
		cal2.set(Calendar.HOUR_OF_DAY, 0);
		cal2.set(Calendar.MINUTE, 0);
		cal2.set(Calendar.SECOND, 0);
		cal2.set(Calendar.MILLISECOND, 0);


		if(cal1.getTimeInMillis() == cal2.getTimeInMillis()){
			return 0;
		}else if(cal1.getTimeInMillis() < cal2.getTimeInMillis()){
			return -1;
		}else /*if(cal1.getTimeInMillis() > cal2.getTimeInMillis())*/{
			return 1;
		}
	}


    public static void generateNotification(Context context, PendingIntent pendingIntent, String message, boolean autoCancel, int id, boolean enableSound, Uri uri) {

		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
		notificationBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
		notificationBuilder.setContentTitle(AndroidAppUtil.getString(context, R.string.app_name));
		notificationBuilder.setContentText(message);
		notificationBuilder.setTicker(AndroidAppUtil.getString(context, R.string.app_name));
		notificationBuilder.setWhen(System.currentTimeMillis());
		notificationBuilder.setAutoCancel(autoCancel);
		notificationBuilder.setPriority(100);

		if(enableSound) {
			uri = uri==null ? RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION) : uri;
			notificationBuilder.setSound(uri);
		}

		notificationBuilder.setContentIntent(pendingIntent);
		Notification notification = notificationBuilder.build();
		NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		int mNotificationId = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
		notificationManager.notify(mNotificationId, notification);

    }
}