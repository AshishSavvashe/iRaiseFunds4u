package com.appbell.iraisefund4u.resto.ui;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.text.Html;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.appbell.common.util.AppUtil;
import com.appbell.iraisefund4u.R;
import com.appbell.iraisefund4u.common.util.AndroidAppUtil;
import com.appbell.iraisefund4u.common.util.AppLoggingUtility;
import com.appbell.iraisefund4u.resto.app.tasks.RestoCommonAsynkTask;
import com.appbell.iraisefund4u.resto.service.UserService;
import com.appbell.iraisefund4u.resto.util.AndroidAppConstants;
import com.appbell.iraisefund4u.resto.util.AndroidServiceManager;
import com.appbell.iraisefund4u.resto.util.NavigationUtil;
import com.google.firebase.iid.FirebaseInstanceId;

public class RegisterCardActivity extends CommonActionBarActivity{
    private static final String CLASS_ID = "SplashScreenActivity:";
    boolean isShowingPwd = false;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	    setContentView(R.layout.activity_register_card);

	    initToolbar(getString(R.string.lblRegisterYourCard), true);

	    findViewById(R.id.btnRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerCard();
            }
        });

        ((EditText)findViewById(R.id.etCustPhNo)).addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        ((EditText)findViewById(R.id.etCustConfirmPhNo)).addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        findViewById(R.id.nsvRegisterCard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AndroidAppUtil.hideKeyboard(RegisterCardActivity.this);
            }
        });

        final ImageView imgShowPassword = findViewById(R.id.imgShowPassword);

        findViewById(R.id.imgShowPassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((EditText) findViewById(R.id.etCustPassword)).setTransformationMethod(!isShowingPwd ? null : new PasswordTransformationMethod());
                isShowingPwd = !isShowingPwd;

                if(isShowingPwd){
                    imgShowPassword.setImageResource(R.drawable.showpass);
                    imgShowPassword.setColorFilter(getApplicationContext().getResources().getColor(R.color.gray));
                }else {
                    imgShowPassword.setImageResource(R.drawable.hidepass);

                }
                //((TextView) v).setText(isShowingPwd ? "Hide" : "Show");
            }
        });

        final ImageView  imgConfirmPwd = findViewById(R.id.imgConfirmPwd);


        findViewById(R.id.imgConfirmPwd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((EditText) findViewById(R.id.etConfirmCustPassword)).setTransformationMethod(!isShowingPwd ? null : new PasswordTransformationMethod());
                isShowingPwd = !isShowingPwd;

                if(isShowingPwd){
                    imgConfirmPwd.setImageResource(R.drawable.showpass);
                    imgConfirmPwd.setColorFilter(getApplicationContext().getResources().getColor(R.color.gray));
                }else {
                    imgConfirmPwd.setImageResource(R.drawable.hidepass);

                }
               // ((TextView) v).setText(isShowingPwd ? "Hide" : "Show");
            }
        });
	}

    /**
     * Register New Card
     */
	private void registerCard(){

	    String custName = ((EditText)findViewById(R.id.etCustName)).getText().toString().trim();
        String custPhNo = ((EditText)findViewById(R.id.etCustPhNo)).getText().toString().trim();
        String custPasswod = ((EditText)findViewById(R.id.etCustPassword)).getText().toString().trim();
        String custEmailId = ((EditText)findViewById(R.id.etCustEmailId)).getText().toString().trim();
        String bogoMemberId = ((EditText)findViewById(R.id.etBogoMemberId)).getText().toString().trim();

        String custPhNo4Confirm = ((EditText)findViewById(R.id.etCustConfirmPhNo)).getText().toString().trim();
        String custPassword4Confirm = ((EditText)findViewById(R.id.etConfirmCustPassword)).getText().toString().trim();
        String custEmail4Confirm = ((EditText)findViewById(R.id.etConfirmCustEmailId)).getText().toString().trim();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            custPhNo = PhoneNumberUtils.normalizeNumber(custPhNo);
            custPhNo4Confirm = PhoneNumberUtils.normalizeNumber(custPhNo4Confirm);
        }else{
            custPhNo = AndroidAppUtil.normalizeNumber(custPhNo);
            custPhNo4Confirm = AndroidAppUtil.normalizeNumber(custPhNo4Confirm);
        }




        if(AppUtil.isBlankCheckNullStr(custName)){
            new CommonAlertDialog().showOkDialog(this, getString(R.string.valMsgEnterName));
            return;
        }

        if(AppUtil.isBlankCheckNullStr(custPhNo)){
            new CommonAlertDialog().showOkDialog(this, getString(R.string.valMsgEnterPhNo));
            return;
        }

        if(!custPhNo.equalsIgnoreCase(custPhNo4Confirm)){
            new CommonAlertDialog().showOkDialog(this, getString(R.string.valMsgPhNoConfirmPhNoNotMatch));
            return;
        }

        if(AppUtil.isBlankCheckNullStr(custPasswod)){
            new CommonAlertDialog().showOkDialog(this, getString(R.string.valMsgEnterPassword));
            return;
        }

        if(!custPasswod.equalsIgnoreCase(custPassword4Confirm)){
            new CommonAlertDialog().showOkDialog(this, getString(R.string.valMsgPassNotMatch));
            return;
        }

        if(AppUtil.isBlankCheckNullStr(custEmailId)){
            new CommonAlertDialog().showOkDialog(this, getString(R.string.valMsgEnterEmailId));
            return;
        }

        if(!AndroidAppUtil.isvalidEmailAddress(custEmailId)){
            new CommonAlertDialog().showOkDialog(this, getString(R.string.valMsgEnterValidEmailId));
            return;
        }

        if(!custEmailId.equalsIgnoreCase(custEmail4Confirm)){
            new CommonAlertDialog().showOkDialog(this, getString(R.string.valMsgPhNoConfirmEmailNotMatch));
            return;
        }

        if(AppUtil.isBlankCheckNullStr(bogoMemberId)){
            new CommonAlertDialog().showOkDialog(this, getString(R.string.valMsgEnterBOGOMemberId));
            return;
        }

        String deviceToken4Android = FirebaseInstanceId.getInstance().getToken();

        new RegisterCardTask(this, custName, custEmailId, custPhNo, bogoMemberId,custPasswod,deviceToken4Android).execute();

    }


    @Override
    public void onDialogPositiveButtonClicked(int currentDialogAction) {
        super.onDialogPositiveButtonClicked(currentDialogAction);
        if(currentDialogAction == AndroidAppConstants.DIALOG_ACTION_RegisterCardMsg){
            NavigationUtil.navigate2VoucherBookListActivity(this, false,"");
            finish();
        }
    }

    @Override
    public void onDialogNegativeButtonClicked(int currentDialogAction) {
        super.onDialogNegativeButtonClicked(currentDialogAction);
        if(currentDialogAction == AndroidAppConstants.DIALOG_ACTION_RegisterCardMsg){
            NavigationUtil.navigate2VoucherBookListActivity(this, false,"");

            finish();
        }
    }

    public class RegisterCardTask extends RestoCommonAsynkTask {
        String customerName;
        String customerEmail;
        String customerPhone;
        String customerPassword;
        String bookCode;
        String deviceToken4Android;
        String successMsg = null;

        public RegisterCardTask(Activity actContext, String customerName, String customerEmail, String customerPhone, String bookCode,String custPasswod,String deviceToken4Android) {
            super(actContext, true);
            this.customerName = customerName;
            this.customerEmail = customerEmail;
            this.customerPhone = customerPhone;
            this.bookCode = bookCode;
            this.customerPassword = custPasswod;
            this.deviceToken4Android = deviceToken4Android;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                successMsg = new UserService(appContext).registerUserWithBook(customerName, customerEmail, customerPhone, bookCode,customerPassword,deviceToken4Android);
            } catch (Throwable e) {
                errorMsg = e.getMessage();
                AppLoggingUtility.logError(appContext, e, CLASS_ID +  "UserAuthenticationTask");
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            try{
                super.onPostExecute(result);

                if(actContext == null || actContext.isFinishing())
                    return;

                if (AppUtil.isNotBlank(successMsg)) {

                    AndroidServiceManager androidServiceManager = new AndroidServiceManager(RegisterCardActivity.this);

                    if (!androidServiceManager.isAlaramRunning(AndroidAppConstants.ALARM_REQUEST_CODE4WEEK)) {
                        new AndroidServiceManager(RegisterCardActivity.this).startWeeklyNotifAlarmManager();
                    }

                    if(!androidServiceManager.isAlaramRunning(AndroidAppConstants.ALARM_REQUEST_CODE4MONTH)){
                        new AndroidServiceManager(RegisterCardActivity.this).startMonthlyNotifAlarmManager();
                    }
                    new CommonAlertDialog(RegisterCardActivity.this, AndroidAppConstants.DIALOG_ACTION_RegisterCardMsg).showDialog(RegisterCardActivity.this, Html.fromHtml(successMsg).toString(), getResources().getString(R.string.lblOk), null);
                } else {

                   ((EditText)findViewById(R.id.etBogoMemberId)).getText().clear();
                    new CommonAlertDialog().showOkDialog(actContext, errorMsg != null ? errorMsg : getResources().getString(R.string.msgInValidLoginCredles));
                }

            }catch(Throwable t){
                AppLoggingUtility.logError(appContext,t, CLASS_ID +  "UserAuthenticationTask");
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
