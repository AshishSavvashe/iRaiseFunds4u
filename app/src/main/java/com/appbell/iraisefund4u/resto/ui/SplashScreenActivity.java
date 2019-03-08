package com.appbell.iraisefund4u.resto.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.appbell.iraisefund4u.R;
import com.appbell.iraisefund4u.common.util.AndroidAppUtil;
import com.appbell.iraisefund4u.common.util.AppLoggingUtility;
import com.appbell.iraisefund4u.resto.util.NavigationUtil;

import java.io.File;
import java.io.IOException;

public class SplashScreenActivity extends AppCompatActivity{
    private static final String CLASS_ID = "SplashScreenActivity:";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	    setContentView(R.layout.activity_splash);

        createAppFolder();

        launchNextActivity();

	}
	
	private void launchNextActivity(){
        Handler handler = new Handler();
		handler.postDelayed(new Runnable(){
	        @Override
	        public void run() {

	            if(AndroidAppUtil.isUserLoggedIn(SplashScreenActivity.this)){
	                //NavigationUtil.navigate2VoucherBookWebView(SplashScreenActivity.this);
                      NavigationUtil.navigate2VoucherBookListActivity(SplashScreenActivity.this, false,"");
                      finish();
	            }else{
                    NavigationUtil.navigate2LandingOptionsActivity(SplashScreenActivity.this);
                    finish();
                }

	        	finish();
	        	overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	        }
	    }, 2000);
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void createAppFolder(){
        File rootFolder = new File(AndroidAppUtil.getBaseFolderPath());

        if(!rootFolder.exists()){
            //create facility folder
            rootFolder = new File(AndroidAppUtil.getFacilityFolderPath());
            rootFolder.mkdirs();

            //create server folder under facility
            rootFolder = new File(AndroidAppUtil.getServerFolderPath());
            rootFolder.mkdirs();

            //create client folder for under facility
            rootFolder = new File(AndroidAppUtil.getClientFolderPath());
            rootFolder.mkdirs();

            //create nomedia file under client folder
            rootFolder = new File(AndroidAppUtil.getClientFilePath(".nomedia"));
            try {
                rootFolder.createNewFile();
            } catch (IOException e) {
                AppLoggingUtility.logError(getApplicationContext(), CLASS_ID + e.getMessage());
            }

            //create nomedia file under server folder
            rootFolder = new File(AndroidAppUtil.getServerFilePath(".nomedia"));
            try {
                rootFolder.createNewFile();
            } catch (IOException e) {
                AppLoggingUtility.logError(getApplicationContext(), CLASS_ID + e.getMessage());
            }

            try {
                rootFolder.createNewFile();
            } catch (IOException e) {
                AppLoggingUtility.logError(getApplicationContext(), CLASS_ID + e.getMessage());
            }
        }

        try {
            File file = new File(AndroidAppUtil.getServerReceiptFolderPath());
            if (!file.exists())
                file.mkdirs();
        }catch (Exception e){
            AppLoggingUtility.logError(getApplicationContext(), CLASS_ID + e.getMessage());
        }
    }

}
