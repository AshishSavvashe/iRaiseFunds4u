package com.appbell.iraisefund4u.resto.ui;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.appbell.common.codevalues.service.CodeValueConstants;
import com.appbell.common.util.AppUtil;
import com.appbell.iraisefund4u.R;
import com.appbell.iraisefund4u.common.util.AndroidAppUtil;
import com.appbell.iraisefund4u.common.util.DateUtil;
import com.appbell.iraisefund4u.resto.util.AndroidAppConstants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class VoucherListActivity extends CommonActionBarActivity {
    public static final String FRAGMENT_TAG_VoucherList = "VCLIST";

    int voucherBookID;
    String campaignTagLine;
    String currentAppliedFilter;
    Fragment currentFragment;
    BottomNavigationView navigation;
    long startdate;
    long endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher_list);

        startdate = getIntent().getLongExtra("startDate",0);
        endDate = getIntent().getLongExtra("endDate",0);

        voucherBookID = getIntent().getIntExtra("voucherBookID", 0);
        campaignTagLine = getIntent().getStringExtra("campaignTagLine");
        String title = getIntent().getStringExtra("title");
        boolean isSingalBook = getIntent().getBooleanExtra("isSinagleBook", false);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setToolbarText(title);

        if (isSingalBook) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        initView();
        loadVoucherListFragment();

    }


    private void initView() {
        initMonthFilter();
        initRestNameFilter();
        applyFilter();
    }

    public void hideRestonameAndMonthSpineer(){
        findViewById(R.id.linearSpineer).setVisibility(View.GONE);
    }

    public void showRestoNameAndMonthSpineer(){
        findViewById(R.id.linearSpineer).setVisibility(View.VISIBLE);
    }

    public void hideNavigation(){
        findViewById(R.id.navigation).setVisibility(View.GONE);
    }

    public void showNavigation(){
        findViewById(R.id.navigation).setVisibility(View.VISIBLE);
    }

    public void initMonthFilter() {

        ArrayList<String> list = AndroidAppUtil.getVoucherBookMonthList(VoucherListActivity.this,startdate, endDate);
        list.add(0, AndroidAppConstants.ALLMonth);

        Spinner spinnerMonth = findViewById(R.id.spinnerMonth);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(VoucherListActivity.this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerMonth.setAdapter(dataAdapter);

        SimpleDateFormat sdf = DateUtil.getDateFormatter(VoucherListActivity.this,"MMM-yy");

        String currentMonth = sdf.format(DateUtil.getCurrentTime(VoucherListActivity.this,new Date()));
        spinnerMonth.setTag(true);

        spinnerMonth.setOnItemSelectedListener(onItemSelectedListener);

        if (currentMonth != null) {
            int currentMontPosition = dataAdapter.getPosition(currentMonth);
            if (currentMontPosition == -1) {
                spinnerMonth.setSelection(0);
            } else {
                spinnerMonth.setSelection(currentMontPosition);
            }
        }
    }

    public void initRestNameFilter() {

        ArrayList<String> listRest = new VoucherBookService(VoucherListActivity.this).getRestaurantName(voucherBookID);
        listRest.add(0, AndroidAppConstants.ALLRestaurant);

        Spinner spinner = findViewById(R.id.restaurantSpinner);
        spinner.setTag(true);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(VoucherListActivity.this, android.R.layout.simple_spinner_item, listRest);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(onItemSelectedListener);

    }

    AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            boolean firstTimeLaunch = (boolean) parent.getTag();

            if (firstTimeLaunch) {
                parent.setTag(false);
                return;
            }
            applyFilter();

        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

   public void applyFilteronVoucherList(String selectedRestoName ){

       Spinner spinnerMonth = findViewById(R.id.spinnerMonth);
       String month = spinnerMonth.getSelectedItem().toString();
       String selectRestName = selectedRestoName;

       String selectedItem;
       if (selectRestName.equalsIgnoreCase(AndroidAppConstants.ALLRestaurant) && month.equalsIgnoreCase(AndroidAppConstants.ALLMonth)) {
           selectedItem = AndroidAppConstants.ALLRestaurant + "~" + AndroidAppConstants.ALLMonth;
       } else if (selectRestName.equalsIgnoreCase(AndroidAppConstants.ALLRestaurant)) {
           selectedItem = AndroidAppConstants.ALLRestaurant + "~" + month;
       } else if (month.equalsIgnoreCase(AndroidAppConstants.ALLMonth)) {
           selectedItem = selectRestName + "~" + AndroidAppConstants.ALLMonth;
       } else {
           selectedItem = selectRestName + "~" + month;
       }

       currentAppliedFilter = selectedItem;
       navigation.setSelectedItemId(R.id.voucherList);

   }

    public void applyFilter() {

        Spinner spinnerRestNames = findViewById(R.id.restaurantSpinner);
        Spinner spinnerMonth = findViewById(R.id.spinnerMonth);

        String month = spinnerMonth.getSelectedItem().toString();
        String selectRestName = spinnerRestNames.getSelectedItem().toString();

        String selectedItem;
        if (selectRestName.equalsIgnoreCase(AndroidAppConstants.ALLRestaurant) && month.equalsIgnoreCase(AndroidAppConstants.ALLMonth)) {
            selectedItem = AndroidAppConstants.ALLRestaurant + "~" + AndroidAppConstants.ALLMonth;
        } else if (selectRestName.equalsIgnoreCase(AndroidAppConstants.ALLRestaurant)) {
            selectedItem = AndroidAppConstants.ALLRestaurant + "~" + month;
        } else if (month.equalsIgnoreCase(AndroidAppConstants.ALLMonth)) {
            selectedItem = selectRestName + "~" + AndroidAppConstants.ALLMonth;
        } else {
            selectedItem = selectRestName + "~" + month;
        }

        currentAppliedFilter = selectedItem;

        if(currentFragment instanceof VoucherListFragment){
            ((VoucherListFragment)currentFragment).applyFilter(currentAppliedFilter);
        }else if(currentFragment instanceof MapViewFragment){
            ((MapViewFragment)currentFragment).applyFilter(currentAppliedFilter);
        }

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.voucherList:
                    if (!(currentFragment instanceof VoucherListFragment)) {
                        loadVoucherListFragment();
                    }
                    return true;
                case R.id.voucherReport:
                    if (!(currentFragment instanceof WebViewFragment)) {
                        loadVoucherReportFragment();
                    }
                    return true;
                case R.id.mapview:
                    if (!(currentFragment instanceof MapViewFragment)) {
                        loadMapViewFrafment();
                    }
                    return true;
            }
            return false;
        }
    };

    public void loadVoucherListFragment() {

        VoucherListFragment voucherListFragment = VoucherListFragment.getInstance(voucherBookID,startdate,endDate, currentAppliedFilter);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_container, voucherListFragment, FRAGMENT_TAG_VoucherList)
                .commit();

        currentFragment = voucherListFragment;
    }



    private void loadVoucherReportFragment() {

       /* VoucherReportFragment voucherReportFragment = VoucherReportFragment.getInstance(voucherBookID);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_container,voucherReportFragment,FRAGMENT_TAG_VoucherList)
                .commit();

        currentFragment = voucherReportFragment;*/


        String serverUrl = (AndroidAppConstants.isHTTPS ? "https://" : "http://") + AndroidAppConstants.SERVER_URL + "/" + AndroidAppConstants.SERVER_CONTEXT + "/";
        String urlMap = serverUrl + "hmpg/default/bvr.jsp?bid=" + AppUtil.encryptVoucherBookId(AppUtil.parseInt(String.valueOf(voucherBookID)));
        urlMap = urlMap + "&_source=" + CodeValueConstants.REQUEST_SOURCE_AndroidApp;

        WebViewFragment webViewFragment = WebViewFragment.getInstance(urlMap);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_container, webViewFragment, FRAGMENT_TAG_VoucherList)
                .commit();

        currentFragment = webViewFragment;
    }

    private void loadMapViewFrafment() {

     MapViewFragment mapViewFragment = MapViewFragment.getInstance(voucherBookID,startdate,endDate, currentAppliedFilter);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_container, mapViewFragment, FRAGMENT_TAG_VoucherList)
                .commit();

        currentFragment = mapViewFragment;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.application_menu, menu);
        MenuItem more = menu.findItem(R.id.more);
        more.setActionView(R.layout.actionview_logout);
        more.getActionView().findViewById(R.id.btnMore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BottomSheetDialog bottomSheetFragment = new BottomSheetDialog();
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
            }
        });

        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                if (currentFragment instanceof VoucherListFragment) {
                    finish();
                } else {
                    navigation.setSelectedItemId(R.id.voucherList);
                }

                return true;
            case R.id.help:
                showHelpPopup();
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void showHelpPopup() {

        View parentView;
        final AlertDialog dialog;

        LayoutInflater inflater = LayoutInflater.from(VoucherListActivity.this);
        parentView = inflater.inflate(R.layout.help_dialog, null, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(VoucherListActivity.this);
        dialog = builder.create();
        dialog.setView(parentView);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        String termsAndCondition = new VoucherBookService(VoucherListActivity.this).getTermsandCondition(voucherBookID);
        TextView termsCondition = parentView.findViewById(R.id.tvHelp);

        if (AndroidAppUtil.isBlank(termsAndCondition)) {
            termsCondition.setText("");
        } else {
            String s = " -";
            termsAndCondition = s + termsAndCondition.replaceAll(",", ". \n -");
            termsCondition.setText(termsAndCondition);
        }


        parentView.findViewById(R.id.imgCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {

        if (currentFragment instanceof VoucherListFragment) {
            super.onBackPressed();
        } else {
            navigation.setSelectedItemId(R.id.voucherList);
        }
    }
}
