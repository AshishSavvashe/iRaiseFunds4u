package com.appbell.iraisefund4u.resto.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.appbell.common.util.AppUtil;
import com.appbell.iraisefund4u.R;
import com.appbell.iraisefund4u.common.exception.ApplicationException;
import com.appbell.iraisefund4u.common.util.AppLoggingUtility;
import com.appbell.iraisefund4u.resto.app.tasks.RestoCommonAsynkTask;

public class RedeemVoucherDialog {

    View parentView;
    Activity context;
    AlertDialog dialog;
    String restaurantname;
    int voucherId;

    public RedeemVoucherDialog(Activity context,String restaurantname,int voucherId) {
        this.context = context;
        this.restaurantname = restaurantname;
        this.voucherId = voucherId;
    }

    public void showpopView(final Activity context) {

        LayoutInflater inflater = LayoutInflater.from(context);
        parentView = inflater.inflate(R.layout.redeemvoucher_dialog, null, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        dialog = builder.create();
        dialog.setView(parentView);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView tvrestaurantName=parentView.findViewById(R.id.tvrestaurantName);
        tvrestaurantName.setText(restaurantname);

        parentView.findViewById(R.id.imgCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        parentView.findViewById(R.id.btnCancelDialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        parentView.findViewById(R.id.btnRedeemVoucher).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new RedeemVoucherTask(context).execute();
            }
        });
        dialog.show();
    }


    private class RedeemVoucherTask extends RestoCommonAsynkTask {
        String errorMsg = null;
        String response[] = null;//[0]- Success Message, [1]- Voucher Usage Count String

        public RedeemVoucherTask(Activity actContext) {
            super(actContext, true);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                response = new VoucherBookService(context).redeemVoucher_sync(voucherId);
            } catch (ApplicationException e) {
                errorMsg = e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try{
                super.onPostExecute(result);

                if(context == null || context.isFinishing())
                    return;

                if (this.response != null) {
                    dialog.dismiss();
                    new CommonAlertDialog().showOkDialog(context, AppUtil.getValAtIndex(this.response, 0));
                    ((RedeemVoucherActivity)context).updateRedeemState(AppUtil.getValAtIndex(this.response, 1));
                } else {
                    dialog.dismiss();
                    new CommonAlertDialog().showOkDialog(context, errorMsg != null ? errorMsg : context.getString(R.string.redeemfailmsg));
                }

            }catch(Throwable t){
                AppLoggingUtility.logError(appContext,t, "RedemVoucher" +  "RedeemVoucherTask");
            }
        }
    }
}

