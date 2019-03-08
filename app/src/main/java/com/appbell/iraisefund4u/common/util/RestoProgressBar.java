package com.appbell.iraisefund4u.common.util;

import android.app.ProgressDialog;
import android.content.Context;

public class RestoProgressBar extends ProgressDialog {
	Context context;
	
	public RestoProgressBar(Context context, String msg){
		super(context);
		this.context = context;
		start(msg);
	} 
	
	public RestoProgressBar(Context context){
		this(context, "Request is in progress...");
	} 
	
	public void start(String msg){
		setMessage(msg);
		setIndeterminate(true);
		show();
	}
	
	public void stop(){
		if(isShowing())
			dismiss();
	}
}
