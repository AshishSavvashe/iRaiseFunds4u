package com.appbell.iraisefund4u.resto.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.appbell.iraisefund4u.R;
import com.appbell.iraisefund4u.common.exception.ApplicationException;
import com.appbell.iraisefund4u.common.util.AndroidAppUtil;
import com.appbell.iraisefund4u.common.util.DateUtil;
import com.appbell.iraisefund4u.resto.app.tasks.RestoCommonAsynkTask;
import com.appbell.iraisefund4u.resto.db.DatabaseManager;
import com.appbell.iraisefund4u.resto.util.NavigationUtil;
import com.appbell.iraisefund4u.resto.util.SharedPreferenceUtil;

import java.util.ArrayList;
import java.util.Date;

public class  VoucherBookListActivity extends CommonActionBarActivity {

    RecyclerView rvVoucherBookList;
    ArrayList<VoucherBookData> voucherbooklist;
    Boolean webViewFlag;
    String bookBuyerPhoneNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher_book_list);
        intView();
    }

    private void intView() {

        String title = getIntent().getStringExtra("title");
        webViewFlag = getIntent().getBooleanExtra("webviewFlag",false);
        bookBuyerPhoneNo = getIntent().getStringExtra("bookBuyerPhoneNo");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setToolbarText(title);

        DateUtil.saveBookTimeZone(VoucherBookListActivity.this,null);

        rvVoucherBookList = findViewById(R.id.rvVoucherBookList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvVoucherBookList.setLayoutManager(linearLayoutManager);

        if(webViewFlag){
            linearLayoutManager.setStackFromEnd(true);
            rvVoucherBookList.setLayoutManager(linearLayoutManager);

        }else {
            linearLayoutManager.setStackFromEnd(false);
            rvVoucherBookList.setLayoutManager(linearLayoutManager);
        }

        loadVoucherBookDataList();
        new GetVoucherBookTask(this).execute();

        findViewById(R.id.linearAddCard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NavigationUtil.navigate2AddCard(VoucherBookListActivity.this);
            }
        });

        findViewById(R.id.linearBuyBook).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NavigationUtil.navigate2BuyBookWebView(VoucherBookListActivity.this,false);

            }
        });
    }

    private void loadVoucherBookDataList() {

        voucherbooklist = (ArrayList<VoucherBookData>) DatabaseManager.getInstance(VoucherBookListActivity.this).getVoucherBookDao().getVoucherBookData();

        rvVoucherBookList.setVisibility(View.VISIBLE);
        findViewById(R.id.btnNoData).setVisibility(View.GONE);
        VoucherBookRecyclerAdapter adapter = new VoucherBookRecyclerAdapter(voucherbooklist, VoucherBookListActivity.this,webViewFlag,bookBuyerPhoneNo);
        rvVoucherBookList.setAdapter(adapter);
    }

    private class GetVoucherBookTask extends RestoCommonAsynkTask {
        String errorMsg = null;

        public GetVoucherBookTask(Activity actContext) {
            super(actContext, false);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
            rvVoucherBookList.setVisibility(View.GONE);
        }
        @Override
        protected Void doInBackground(Void... params) {
            try {

                long lastSyncTime = SharedPreferenceUtil.getLong(actContext, "blst");
                voucherbooklist = new VoucherBookService(appContext).getVoucherBookList_sync(lastSyncTime);
                SharedPreferenceUtil.putLong(actContext, "blst", DateUtil.getCurrentTime(actContext, new Date()).getTime());

            } catch (ApplicationException e) {
                errorMsg = e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (actContext == null || actContext.isFinishing())
                return;

            loadVoucherBookDataList();

            findViewById(R.id.progressBar).setVisibility(View.GONE);
            if (voucherbooklist != null && voucherbooklist.size() > 0) {

                VoucherBookData voucherBookData = voucherbooklist.get(0);

            //    DateUtil.saveBookTimeZone(actContext,voucherBookData.getTimeZoneProp());
                loadVoucherBookDataList();

                /*if (voucherbooklist.size() == 1) {
                    VoucherBookData voucherBookData = voucherbooklist.get(0);

                    DateUtil.saveBookTimeZone(actContext,voucherBookData.getTimeZoneProp());

                    NavigationUtil.navigate2VoucherListActivity(voucherBookData.getBookId(), voucherBookData.getCampaignTagLine(), actContext, true, voucherBookData.getVoucherStartTime(), voucherBookData.getVoucherEndTime());
                    finish();
                } else {
                    loadVoucherBookDataList();
                }*/

            } else {
                Button btnNoData = findViewById(R.id.btnNoData);
                btnNoData.setVisibility(View.VISIBLE);
                if (!AndroidAppUtil.isBlank(errorMsg)) {
                    btnNoData.setText(errorMsg);
                    btnNoData.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_error_black, 0, 0);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.application_menu, menu);
        MenuItem more = menu.findItem(R.id.more);
        more.setActionView(R.layout.actionview_logout);
        more.getActionView().findViewById(R.id.btnMore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottmSheetDialog();
            }
        });

        return true;
    }

    private void showBottmSheetDialog() {
        BottomSheetDialog bottomSheetFragment = new BottomSheetDialog();
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
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
