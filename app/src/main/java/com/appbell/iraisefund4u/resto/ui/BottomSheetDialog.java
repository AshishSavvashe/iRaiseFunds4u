package com.appbell.iraisefund4u.resto.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.appbell.common.util.AppUtil;
import com.appbell.iraisefund4u.R;
import com.appbell.iraisefund4u.common.util.AppLoggingUtility;
import com.appbell.iraisefund4u.resto.app.tasks.RestoCommonAsynkTask;
import com.appbell.iraisefund4u.resto.service.AppService;
import com.appbell.iraisefund4u.resto.service.UserService;
import com.appbell.iraisefund4u.resto.util.AndroidAppConstants;
import com.appbell.iraisefund4u.resto.util.AndroidServiceManager;
import com.appbell.iraisefund4u.resto.util.NavigationUtil;

public class BottomSheetDialog extends BottomSheetDialogFragment implements CommonAlertDialog.CommonAlertDialogButtonListener {

    View parentView;
    private static final String CLASS_ID = BottomSheetDialog.class.getSimpleName() + ": ";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        intView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.bottom_sheet, null);
        return parentView;
    }

    private void intView() {

        parentView.findViewById(R.id.linearBuyBook).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationUtil.navigate2BuyBookWebView(getActivity(),false);
                dismiss();

            }
        });

        parentView.findViewById(R.id.linearChangePassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationUtil.navigate2ChangePassword(getActivity());
                dismiss();
            }
        });


        parentView.findViewById(R.id.linearLogOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CommonAlertDialog(BottomSheetDialog.this,AndroidAppConstants.DIALOG_ACTION_Logout).showDialog(getActivity(), null, getResources().getString(R.string.lblLogout), getResources().getString(R.string.lblCancel));
            }
        });

        parentView.findViewById(R.id.linearContactUS).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               sendEmail();
                dismiss();
            }

        });

    }


    private void sendEmail() {

        String[] TO = {"contact@instafundr.com "};
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setType("plain/text");
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);

        try {
            startActivity(Intent.createChooser(emailIntent, "Contact US"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onDialogPositiveButtonClicked(int currentDialogAction) {

        if(currentDialogAction == AndroidAppConstants.DIALOG_ACTION_Logout){

            new LogoutUserTask(getActivity(), true).execute();

        }

    }


    public class LogoutUserTask extends RestoCommonAsynkTask {
        String successMsg = null;

        public LogoutUserTask(Activity actContext, boolean showProgressBar) {
            super(actContext, showProgressBar);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                successMsg = new UserService(appContext).logOutUser4Server();
            } catch (Throwable e) {
                errorMsg = e.getMessage();
                AppLoggingUtility.logError(appContext, e, CLASS_ID +  "UpdateActivationStatus");
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
                    new AppService(getActivity()).logoutUser();
                    new AndroidServiceManager(getActivity()).stopNotifAlarmsOnLogout();
                    NavigationUtil.navigate2LandingOptionsActivity(getActivity());
                    dismiss();
                } else {
                    new CommonAlertDialog().showOkDialog(actContext, errorMsg != null ? errorMsg : "Something went wrong. Please try again.");
                }

            }catch(Throwable t){
                AppLoggingUtility.logError(appContext, t, CLASS_ID +  "ForgotPasswordTask");
            }
        }
    }


    @Override
    public void onDialogNegativeButtonClicked(int currentDialogAction) {
        dismiss();
    }
}
