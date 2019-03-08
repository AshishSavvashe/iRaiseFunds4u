package com.appbell.iraisefund4u.resto.ui;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.appbell.common.util.AppUtil;
import com.appbell.iraisefund4u.R;
import com.appbell.iraisefund4u.common.util.AndroidAppUtil;
import com.appbell.iraisefund4u.common.util.AppLoggingUtility;
import com.appbell.iraisefund4u.resto.app.tasks.RestoCommonAsynkTask;
import com.appbell.iraisefund4u.resto.service.UserService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChangePasswordActivity extends CommonActionBarActivity {
    private static final String CLASS_ID = "SplashScreenActivity:";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        intView();
    }

    private void intView() {

        setToolbarText("Change Password");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        findViewById(R.id.btnChangePwd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePwd();
            }

        });

    }

    private void changePwd() {

        String oldPwd = ((EditText)findViewById(R.id.etOldPassword)).getEditableText().toString();
        String newPwd = ((EditText)findViewById(R.id.etnewPassword)).getEditableText().toString();
        String reEnteredNewPwd = ((EditText)findViewById(R.id.etConfimPassword)).getEditableText().toString();
        Pattern pattern = Pattern.compile("[^A-Za-z0-9]");
        Matcher matcher=pattern.matcher(reEnteredNewPwd);


        if(AppUtil.isBlank(oldPwd.trim()) || AppUtil.isBlank(reEnteredNewPwd.trim())){
            new CommonAlertDialog().showOkDialog(ChangePasswordActivity.this,getResources().getString(R.string.requiredFieldValidationString));

            //  showMsg(getResources().getString(R.string.requiredFieldValidationString));
            return;
        }

        if(AppUtil.isBlank(newPwd.trim()) || AppUtil.isBlank(reEnteredNewPwd.trim())){
            new CommonAlertDialog().showOkDialog(ChangePasswordActivity.this,getResources().getString(R.string.requiredFieldValidationString));


          ///  showMsg(getResources().getString(R.string.requiredFieldValidationString));
            return;
        }

        if(matcher.find()){
            new CommonAlertDialog().showOkDialog(ChangePasswordActivity.this,getResources().getString(R.string.lblPasswordOnlyAlphaNumeric));

         //   showMsg(getResources().getString(R.string.lblPasswordOnlyAlphaNumeric));
            return;
        }

        if(!newPwd.equals(reEnteredNewPwd)){

            new CommonAlertDialog().showOkDialog(ChangePasswordActivity.this,getResources().getString(R.string.msgEnterCorrectNewPwd));

        //    showMsg(getResources().getString(R.string.msgEnterCorrectNewPwd));
            return;
        }

        new ChangePwdTask(this, oldPwd,newPwd).execute();
    }


    private void showMsg(String msg) {
        findViewById(R.id.layoutShowMsg).setVisibility(View.VISIBLE);
        TextView txtViewLine2 = (TextView)findViewById(R.id.txtViewMsg);
        txtViewLine2.setTextColor(Color.RED);
        txtViewLine2.setText(msg);
    }


    public class ChangePwdTask extends RestoCommonAsynkTask {
        String newPwd;
        String oldPwd;
        String errorMsg = null;
        String successMsg = null;

        public ChangePwdTask(Activity actContext,String oldPwd, String newPwd){
            super(actContext, true);
            this.oldPwd = oldPwd;
            this.newPwd = newPwd;

        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                successMsg = new UserService(appContext).changePassword(oldPwd, newPwd);
            } catch (Throwable e) {
                errorMsg = e.getMessage();
                AppLoggingUtility.logError(appContext, e, CLASS_ID +  "ChangePwdTask");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if(AppUtil.isNotBlank(successMsg)){
                Toast.makeText(actContext, successMsg, Toast.LENGTH_LONG).show();
                finish();
            }else{
                showMsg(errorMsg!= null ? errorMsg :AndroidAppUtil.getString(actContext,R.string.msgPwdNotChnage));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.application_menu, menu);
        MenuItem more = menu.findItem(R.id.more);
        more.setVisible(false);

        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        menu.findItem(R.id.help).setVisible(false);
        menu.findItem(R.id.more).setVisible(false);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
