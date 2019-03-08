package com.appbell.iraisefund4u.resto.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.appbell.iraisefund4u.R;
import com.appbell.iraisefund4u.common.util.AndroidAppUtil;


public class CommonAlertDialog {

    public interface CommonAlertDialogButtonListener{
        void onDialogPositiveButtonClicked(int currentDialogAction);
        void onDialogNegativeButtonClicked(int currentDialogAction);
    }

	private CommonAlertDialogButtonListener listener;
    private  AlertDialog alertDialog;
    private int currentDialogAction;

    public CommonAlertDialog() {}

	public CommonAlertDialog(CommonAlertDialogButtonListener listener, int currentDialogAction) {
		this.listener = listener;
		this.currentDialogAction = currentDialogAction;
	}

    public void showOkDialog(Activity context, String msg){
        showDialog(context, msg, context.getResources().getString(R.string.lblOk), null);
    }

	public void showDialog(Activity context, String msg, String positiveBtnLabel, String negativeBtnLabel){
        View rootView = context.getLayoutInflater().inflate(R.layout.new_common_alert_dialog,null);

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(rootView);
        builder.setCancelable(true);

        ((TextView)rootView.findViewById(R.id.txtViewHeaderTitle)).setText(context.getString(R.string.app_name));

        if(!AndroidAppUtil.isBlank(msg)){
            ((TextView)rootView.findViewById(R.id.txtViewDialogContent)).setText(msg);
        }else{
            rootView.findViewById(R.id.txtViewDialogContent).setVisibility(View.GONE);
        }

        if(!AndroidAppUtil.isBlank(negativeBtnLabel)){
            Button btnPositive = rootView.findViewById(R.id.btnPositive);
            btnPositive.setText(positiveBtnLabel);
            btnPositive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null)
                        listener.onDialogPositiveButtonClicked(currentDialogAction);

                    alertDialog.dismiss();
                }
            });
            Button btnNegative = rootView.findViewById(R.id.btnNegative);
            btnNegative.setText(negativeBtnLabel);
            btnNegative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null)
                        listener.onDialogNegativeButtonClicked(currentDialogAction);

                    alertDialog.dismiss();
                }
            });
        }else{
            rootView.findViewById(R.id.layoutBtn1).setVisibility(View.GONE);
            rootView.findViewById(R.id.layoutBtn2).setVisibility(View.VISIBLE);
            Button btnPositive = rootView.findViewById(R.id.btnPositive2);
            btnPositive.setText(positiveBtnLabel);
            btnPositive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null)
                        listener.onDialogPositiveButtonClicked(currentDialogAction);

                    alertDialog.dismiss();
                }
            });

        }

        alertDialog = builder.create();

        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                if(listener != null)
                    listener.onDialogNegativeButtonClicked(currentDialogAction);
            }
        });

		if(!context.isFinishing())
            alertDialog.show();
	}

}
