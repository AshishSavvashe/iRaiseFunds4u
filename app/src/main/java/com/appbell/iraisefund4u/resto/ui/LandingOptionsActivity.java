package com.appbell.iraisefund4u.resto.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.appbell.iraisefund4u.R;
import com.appbell.iraisefund4u.common.util.AndroidAppUtil;
import com.appbell.iraisefund4u.resto.util.AndroidAppConstants;
import com.appbell.iraisefund4u.resto.util.AndroidServiceManager;
import com.appbell.iraisefund4u.resto.util.NavigationUtil;

public class LandingOptionsActivity extends AppCompatActivity{
    private static final String CLASS_ID = "LandingOptionsActivity:";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	    setContentView(R.layout.activity_landing_options);

	    if(AndroidAppUtil.isUserLoggedIn(this)){

            findViewById(R.id.btnRegisterCard).setVisibility(View.GONE);
            findViewById(R.id.btnLogin).setVisibility(View.GONE);

            AndroidServiceManager androidServiceManager = new AndroidServiceManager(getApplication());

            if (!androidServiceManager.isAlaramRunning(AndroidAppConstants.ALARM_REQUEST_CODE4WEEK)) {
                new AndroidServiceManager(getApplicationContext()).startWeeklyNotifAlarmManager();
            }

            if(!androidServiceManager.isAlaramRunning(AndroidAppConstants.ALARM_REQUEST_CODE4MONTH)){
                new AndroidServiceManager(getApplicationContext()).startMonthlyNotifAlarmManager();
            }

            launchNextActivity();

        }else{
            findViewById(R.id.btnRegisterCard).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  NavigationUtil.navigate2RegisterCardActivity(LandingOptionsActivity.this);
                }
            });

            findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   NavigationUtil.navigateUserLoginActivity(LandingOptionsActivity.this);
                }
            });
        }

    }

    private void launchNextActivity(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run() {
               // NavigationUtil.navigate2VoucherBookWebView(LandingOptionsActivity.this);
                NavigationUtil.navigate2VoucherBookListActivity(LandingOptionsActivity.this,false,"");
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        }, 1500);
    }

}
