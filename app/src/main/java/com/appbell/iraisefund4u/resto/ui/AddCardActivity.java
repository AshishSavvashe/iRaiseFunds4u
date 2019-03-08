package com.appbell.iraisefund4u.resto.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.appbell.common.util.AppUtil;
import com.appbell.iraisefund4u.R;
import com.appbell.iraisefund4u.common.util.AndroidAppUtil;
import com.appbell.iraisefund4u.common.util.AppLoggingUtility;
import com.appbell.iraisefund4u.resto.app.tasks.RestoCommonAsynkTask;
import com.appbell.iraisefund4u.resto.service.UserService;
import com.appbell.iraisefund4u.resto.util.NavigationUtil;

public class AddCardActivity extends CommonActionBarActivity {

    private static final String CLASS_ID = "AddCardActivity:";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        intView();
    }

    private void intView() {

        setToolbarText("Add Card");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        findViewById(R.id.btnAddToCard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addBook();

            }
        });
    }

    private void addBook() {

        String bogoMemberId = ((EditText)findViewById(R.id.etBogoMemberId)).getText().toString().trim();

        if(AppUtil.isBlankCheckNullStr(bogoMemberId)){
            new CommonAlertDialog().showOkDialog(this, getString(R.string.valMsgEnterBOGOMemberId));
            return;
        }

        new AddBookTask(this, bogoMemberId).execute();

    }

    public class AddBookTask extends RestoCommonAsynkTask {
        String bookCode;
        String successMsg = null;


        public AddBookTask(Activity actContext,  String bookCode) {
            super(actContext, true);
            this.bookCode = bookCode;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                successMsg = new UserService(appContext).registerNewBook(bookCode);
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

                    Toast.makeText(appContext, successMsg, Toast.LENGTH_SHORT).show();
                    AndroidAppUtil.hideKeyboard(AddCardActivity.this);

                    NavigationUtil.navigate2VoucherBookListActivity(AddCardActivity.this,false,"");
                    finish();

                } else {
                    ((EditText)findViewById(R.id.etBogoMemberId)).getText().clear();
                    new CommonAlertDialog().showOkDialog(actContext, errorMsg != null ? errorMsg : getResources().getString(R.string.buybookFailmsg));
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
