package com.appbell.iraisefund4u.resto.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.appbell.common.util.AppUtil;
import com.appbell.iraisefund4u.R;
import com.appbell.iraisefund4u.common.util.AndroidAppUtil;
import com.appbell.iraisefund4u.common.util.AppLoggingUtility;
import com.appbell.iraisefund4u.resto.app.tasks.RestoCommonAsynkTask;
import com.appbell.iraisefund4u.resto.service.AppService;
import com.appbell.iraisefund4u.resto.service.UserService;
import com.appbell.iraisefund4u.resto.util.AndroidAppConstants;
import com.appbell.iraisefund4u.resto.util.AndroidServiceManager;
import com.appbell.iraisefund4u.resto.util.NavigationUtil;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;

public class UserLoginFragment extends Fragment implements CommonAlertDialog.CommonAlertDialogButtonListener{
    private static final String CLASS_ID = UserLoginFragment.class.getSimpleName() + ": ";
    private View rootView;

    boolean isShowingPwd = false;

    public static UserLoginFragment getInstance(){
        UserLoginFragment loginFragment = new UserLoginFragment();
        return loginFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_user_login, container, false);
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

        View scrollviewAdLgn = rootView.findViewById(R.id.scrollviewAdLgn);

        if(scrollviewAdLgn != null){ // Hide for Phone
            scrollviewAdLgn.setOnTouchListener(touchListener);
        }

        String mobOrEmail4ForgotPwd = new AppService(getActivity()).getPhOrEmail4ForgotPassword();
        if(AppUtil.isNotBlank(mobOrEmail4ForgotPwd)){
            ((EditText)rootView.findViewById(R.id.etLoginId)).setText(mobOrEmail4ForgotPwd);
        }

        rootView.findViewById(R.id.btnLgnUsingIdPwd).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidAppUtil.hideKeyboard(getActivity());
                String loginId = ((EditText)rootView.findViewById(R.id.etLoginId)).getText().toString().trim();
                String pwd = ((EditText)rootView.findViewById(R.id.etLoginPwd)).getText().toString().trim();
                String deviceToken4Android = FirebaseInstanceId.getInstance().getToken();

                if(AppUtil.isBlankCheckNullStr(loginId) || AppUtil.isBlankCheckNullStr(pwd)){
                    new CommonAlertDialog().showOkDialog(getActivity(), getResources().getString(R.string.msgEnterLoginIdPWd));
                }else{
                    new UserAuthenticationTask(getActivity(), true, loginId, pwd ,deviceToken4Android).execute();
                }
            }
        });

        final ImageView imgPwd = rootView.findViewById(R.id.imgPwd);


        rootView.findViewById(R.id.imgPwd).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((EditText)rootView.findViewById(R.id.etLoginPwd)).setTransformationMethod(!isShowingPwd ? null : new PasswordTransformationMethod());
                isShowingPwd = !isShowingPwd;

                if(isShowingPwd){
                    imgPwd.setImageResource(R.drawable.showpass);
                    imgPwd.setColorFilter(getActivity().getResources().getColor(R.color.gray));
                }else {
                    imgPwd.setImageResource(R.drawable.hidepass);

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

    public class UserAuthenticationTask extends RestoCommonAsynkTask {
		String loginId, pwd,deviceToken4Android;
		String result = null;

		public UserAuthenticationTask(Activity actContext, boolean showProgressBar, String id, String pwd,String deviceToken4Android) {
			super(actContext, showProgressBar);
			loginId = id;
			this.pwd = pwd;
			this.deviceToken4Android = deviceToken4Android;
		}


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
		protected Void doInBackground(Void... params) {
			try {
                result = new UserService(appContext).authenticateUser(loginId, pwd , deviceToken4Android);
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

				if (this.result != null) {
					 Toast.makeText(actContext, getResources().getString(R.string.msgLogInSuccess), Toast.LENGTH_LONG).show();

                    AndroidServiceManager androidServiceManager = new AndroidServiceManager(getActivity());

                    if (!androidServiceManager.isAlaramRunning(AndroidAppConstants.ALARM_REQUEST_CODE4WEEK)) {
                        new AndroidServiceManager(getActivity()).startWeeklyNotifAlarmManager();
                    }

                    if(!androidServiceManager.isAlaramRunning(AndroidAppConstants.ALARM_REQUEST_CODE4MONTH)){
                        new AndroidServiceManager(getActivity()).startMonthlyNotifAlarmManager();
                    }

					 NavigationUtil.navigate2VoucherBookListActivity(actContext, false,"");
                     getActivity().finish();
				} else {
					new CommonAlertDialog().showOkDialog(actContext, errorMsg != null ? errorMsg : getResources().getString(R.string.msgInValidLoginCredles));
				}
				
			}catch(Throwable t){
                AppLoggingUtility.logError(appContext,t, CLASS_ID +  "UserAuthenticationTask");
            }
		}
	}


    @Override
    public void onDestroy() {
        new AppService(getActivity()).saveEnterPhOrEmail4ForgotPassword(null);
        super.onDestroy();
    }
}
