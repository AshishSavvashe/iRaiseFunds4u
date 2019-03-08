package com.appbell.iraisefund4u.resto.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appbell.common.codevalues.service.CodeValueConstants;
import com.appbell.common.util.AppUtil;
import com.appbell.iraisefund4u.R;
import com.appbell.iraisefund4u.common.universalimageloader.core.ImageLoader;
import com.appbell.iraisefund4u.common.universalimageloader.core.ImageLoaderConfiguration;
import com.appbell.iraisefund4u.common.util.AndroidAppUtil;
import com.appbell.iraisefund4u.common.util.AppLoggingUtility;
import com.appbell.iraisefund4u.common.util.DateUtil;
import com.appbell.iraisefund4u.resto.app.tasks.RestoCommonAsynkTask;
import com.appbell.iraisefund4u.resto.service.MiscService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RedeemVoucherActivity extends CommonActionBarActivity {

    private static final String CLASS_ID = "RedeemVoucherActivity" ;
    VoucherData voucherData;
    SimpleDateFormat sdf;
    TextView tvVoucherRedeemDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem_voucher);
        initView();
    }


    private void initView() {
        voucherData = getIntent().getExtras().getParcelable("voucherListdata");

        final String title = getIntent().getStringExtra("title");

        setToolbarText(title);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ((TextView)findViewById(R.id.tvVoucherDes)).setText(voucherData.getVoucherDesc());
        ((TextView)findViewById(R.id.tvVoucherUsesCountStr)).setText(voucherData.getUsageCountStr());
        ((TextView)findViewById(R.id.tvUserName)).setText(voucherData.getCustomerName());
        ((TextView)findViewById(R.id.tvUserPhoneNo)).setText(voucherData.getCustomerPhone());
        ((TextView)findViewById(R.id.tvUserEmailId)).setText(voucherData.getCustomerEmail());
        ((TextView)findViewById(R.id.tvRestaurantName)).setText(voucherData.getRestaurantName());
        ((TextView)findViewById(R.id.tvCompainName)).setText(voucherData.getCompaingName());



        tvVoucherRedeemDate = findViewById(R.id.tvVoucherRedeemDate);
        TextView tvRestaurantAddresss = findViewById(R.id.tvRestaurantAddresss);
        TextView tvRestaurantPhone = findViewById(R.id.tvRestaurantPhone);
        Button btnRedeem = findViewById(R.id.btnRedeem);

        LinearLayout linerRedeemed = findViewById(R.id.linerRedeemed);
        ImageView imgRedeemedVoucher = findViewById(R.id.imgRedeemedVoucher);

        LinearLayout linearHeader = findViewById(R.id.linearHeader);

        TextView tvCompainData = findViewById(R.id.tvCompainData);
        tvCompainData.setText(voucherData.getVoucherMsg());

        String restoAddress = voucherData.getRestaurantAddress();
        String address = restoAddress.replaceAll(",", ", ");

        tvRestaurantAddresss.setText(address);
        tvRestaurantPhone.setText(voucherData.getRestaurantPhone());

        ImageView imgVoucher = findViewById(R.id.imgVoucher);
        ImageView imgRestoLogo = findViewById(R.id.imgRestoLogo);
        ImageView imgCompaingLogo = findViewById(R.id.imgCompaingLogo);

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));

        String imgeCompainLogo = new MiscService(this).getBaseUrl() + "FileRendererServlet?fileName=" + voucherData.getCompaingName()+ "_"+voucherData.getCompaingId();
        imageLoader.displayImage(imgeCompainLogo,imgCompaingLogo);


        String imagerestoLogo = new MiscService(this).getBaseUrl() + "FileRendererServlet?fileName=" + voucherData.getFacilityId()+ "_logo.png&imgLoc=receipt_images";
        imageLoader.displayImage(imagerestoLogo,imgRestoLogo);

        String voucherimageUrll = new MiscService(this).getBaseUrl() + "FileRendererServlet?fileName=" + voucherData.getImageName();
        imageLoader.displayImage(voucherimageUrll, imgVoucher);


        String comment = voucherData.getComment();

        if (AndroidAppUtil.isBlank(comment)) {
            ((TextView) findViewById(R.id.tvVoucherComment)).setText(Html.fromHtml(voucherData.getNotes()));
        } else {
            String s = " -";
            comment = s + comment.replaceAll(",", ". \n -");
            ((TextView) findViewById(R.id.tvVoucherComment)).setText(comment);
        }

        sdf = DateUtil.getDateFormatter(RedeemVoucherActivity.this,"MM/dd/yyyy");
        DateUtil.getCalanderObject(RedeemVoucherActivity.this,DateUtil.getCurrentTime(RedeemVoucherActivity.this, new Date())).get(Calendar.MONTH);
        Calendar calStartDate =  DateUtil.getCalanderObject(RedeemVoucherActivity.this,DateUtil.getCurrentTime(RedeemVoucherActivity.this, new Date()));

        Date VoucherStartdate = new Date(voucherData.getVoucherStartTime());
        if( VoucherStartdate!= null){
            calStartDate.setTime(VoucherStartdate);
        }


        if(CodeValueConstants.VOUCHER_STATUS_Redeemed.equals(voucherData.getVoucherStatus())){
            linerRedeemed.setVisibility(View.VISIBLE);
            imgRedeemedVoucher.setVisibility(View.VISIBLE);
            btnRedeem.setVisibility(View.GONE);
        }else {
            linerRedeemed.setVisibility(View.GONE);
            btnRedeem.setVisibility(View.VISIBLE);
            imgRedeemedVoucher.setVisibility(View.GONE);
        }

        if(CodeValueConstants.VOUCHER_USE_Book.equalsIgnoreCase(voucherData.getVoucherUse())){
            linearHeader.setVisibility(View.VISIBLE);

        }else {
            linearHeader.setVisibility(View.GONE);
        }


        ((LinearLayout)findViewById(R.id.linearRestoAddress)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String restaurantAddress = voucherData.getRestaurantAddress();
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + restaurantAddress);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        ((LinearLayout)findViewById(R.id.linearRestoPhone)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!AppUtil.isBlank(voucherData.getRestaurantPhone())) {

                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + voucherData.getRestaurantPhone()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }
            }
        });


        btnRedeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String restaurantName = voucherData.getRestaurantName();
                int voucherId = voucherData.getVoucherId();

            new RedeemVoucherDialog(RedeemVoucherActivity.this,restaurantName,voucherId).showpopView(RedeemVoucherActivity.this);

            }
        });


        new GetVoucherRedeemDateTask(RedeemVoucherActivity.this).execute();

    }


    public class GetVoucherRedeemDateTask extends RestoCommonAsynkTask {
        int facilityId, restaurantId,voucherId;
        String resultStr = "";

        public GetVoucherRedeemDateTask(Activity actContext) {
            super(actContext, false);
            facilityId = voucherData.getFacilityId();
            restaurantId= voucherData.getRestaurantId();
            this.voucherId = voucherData.getVoucherId();
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                resultStr = new VoucherBookService(appContext).voucherReddemDate(facilityId, restaurantId,voucherId);
            } catch (Throwable e) {
                errorMsg = e.getMessage();
              AppLoggingUtility.logError(appContext, e, CLASS_ID +  "RedeemVoucherActivity");
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            try{
                super.onPostExecute(result);

                if(actContext == null || actContext.isFinishing())
                    return;

                if (this.resultStr != null && resultStr.length() > 0) {

                    String seprater = " ";
                    String[] strRedeemdDates = resultStr.split(",");

                    if (strRedeemdDates != null ) {
                         tvVoucherRedeemDate.setText("Redeemed Date:");
                        for (int i = 0, len = strRedeemdDates.length; i < len; i++) {
                            tvVoucherRedeemDate.append(seprater + sdf.format(new Date(AppUtil.getLongValAtIndex(strRedeemdDates, i))));
                            seprater = ", ";
                        }
                    }

                    if(strRedeemdDates != null ){
                        tvVoucherRedeemDate.setVisibility(View.VISIBLE);
                    }else {
                        tvVoucherRedeemDate.setVisibility(View.GONE);
                    }

                } else {
                    tvVoucherRedeemDate.setText("Redeem Date: ....");
                }

            }catch(Throwable t){
               AppLoggingUtility.logError(appContext,t, CLASS_ID +  "RedeemVoucherActivity");
            }
        }
    }


    public void updateRedeemState(String usageCountStr){
        findViewById(R.id.imgRedeemedVoucher).setVisibility(View.VISIBLE);
        ((TextView)findViewById(R.id.tvVoucherUsesCountStr)).setText(usageCountStr);
        findViewById(R.id.linerRedeemed).setVisibility(View.VISIBLE);
        findViewById(R.id.btnRedeem).setVisibility(View.GONE);

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
