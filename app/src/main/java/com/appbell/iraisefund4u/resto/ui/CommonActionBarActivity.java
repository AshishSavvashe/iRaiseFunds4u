package com.appbell.iraisefund4u.resto.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.appbell.common.util.AppUtil;
import com.appbell.iraisefund4u.R;
import com.appbell.iraisefund4u.resto.service.AppService;
import com.appbell.iraisefund4u.resto.util.AndroidAppConstants;
import com.appbell.iraisefund4u.resto.util.NavigationUtil;

public class CommonActionBarActivity extends AppCompatActivity  implements CommonAlertDialog.CommonAlertDialogButtonListener {
    Toolbar toolbar;

    public static Activity commonActivity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        commonActivity = this;

    }

    protected void initToolbar(String title, boolean isSetSupportActionBar){
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(AppUtil.isNotBlank(title) ? title : getString(R.string.app_name));

        if(isSetSupportActionBar){
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        supportInvalidateOptionsMenu();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.application_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onDialogPositiveButtonClicked(int currentDialogAction) {
            if(currentDialogAction == AndroidAppConstants.DIALOG_ACTION_Logout){
                new AppService(this).logoutUser();
                NavigationUtil.navigate2LandingOptionsActivity(this);
                finish();
            }
    }

    @Override
    public void onDialogNegativeButtonClicked(int currentDialogAction) {

    }

    public void setToolbarText(String text) {
        ((TextView) findViewById(R.id.title)).setText(AppUtil.isBlank(text) ? getResources().getString(R.string.app_name) : text);
    }
}
