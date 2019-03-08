package com.appbell.iraisefund4u.resto.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.appbell.common.util.AppUtil;
import com.appbell.iraisefund4u.R;
import com.appbell.iraisefund4u.common.util.AndroidAppUtil;
import com.appbell.iraisefund4u.common.util.AppLoggingUtility;
import com.appbell.iraisefund4u.resto.app.tasks.RestoCommonAsynkTask;
import com.appbell.iraisefund4u.resto.service.AppService;
import com.appbell.iraisefund4u.resto.service.UserService;
import com.appbell.iraisefund4u.resto.util.AndroidAppConstants;

public class ForgotPasswordFragment extends Fragment implements CommonAlertDialog.CommonAlertDialogButtonListener{
    private static final String CLASS_ID = ForgotPasswordFragment.class.getSimpleName() + ": ";
    private View rootView;


    public static ForgotPasswordFragment getInstance(){
        ForgotPasswordFragment loginFragment = new ForgotPasswordFragment();
        return loginFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_forgot_pwd, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        renderUI();
    }

    private void renderUI(){

        View.OnTouchListener touchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                AndroidAppUtil.hideKeyboard(getActivity());
                return false;
            }
        };

        rootView.setOnTouchListener(touchListener);

        View nsvForgotPwd = rootView.findViewById(R.id.nsvForgotPwd);

        if(nsvForgotPwd != null){ // Hide for Phone
            nsvForgotPwd.setOnTouchListener(touchListener);
        }

        rootView.findViewById(R.id.btnSubmit4Forgotpwd).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidAppUtil.hideKeyboard(getActivity());
                String mobNoOrEmailId = ((EditText)rootView.findViewById(R.id.etMobOrEmailId)).getText().toString().trim();

                if(AppUtil.isBlankCheckNullStr(mobNoOrEmailId)){
                    new CommonAlertDialog().showOkDialog(getActivity(), getResources().getString(R.string.lblHintForgotPwd));
                }else{
                    new ForgotPasswordTask(getActivity(), true, mobNoOrEmailId).execute();
                }
            }
        });

    }

    @Override
    public void onDialogPositiveButtonClicked(int currentDialogAction) {
        if(currentDialogAction == AndroidAppConstants.DIALOG_ACTION_BackKeyPress){
            getActivity().onBackPressed();
        }
    }

    @Override
    public void onDialogNegativeButtonClicked(int currentDialogAction) {

    }

    public class ForgotPasswordTask extends RestoCommonAsynkTask {
		String mobNoOrEmailId;
		String successMsg = null;

		public ForgotPasswordTask(Activity actContext, boolean showProgressBar, String mobNoOrEmailId) {
			super(actContext, showProgressBar);
            this.mobNoOrEmailId = mobNoOrEmailId;
		}

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
		protected Void doInBackground(Void... params) {
			try {
                successMsg = new UserService(appContext).forgotPassword_sync(mobNoOrEmailId);
			} catch (Throwable e) {
				errorMsg = e.getMessage();
                AppLoggingUtility.logError(appContext, e, CLASS_ID +  "ForgotPasswordTask");
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
				    new CommonAlertDialog().showOkDialog(actContext, successMsg);
				    new AppService(appContext).saveEnterPhOrEmail4ForgotPassword(mobNoOrEmailId);
				} else {
					new CommonAlertDialog().showOkDialog(actContext, errorMsg != null ? errorMsg : "Something went wrong. Please try again.");
				}
				
			}catch(Throwable t){
                AppLoggingUtility.logError(appContext, t, CLASS_ID +  "ForgotPasswordTask");
            }
		}
	}



}
