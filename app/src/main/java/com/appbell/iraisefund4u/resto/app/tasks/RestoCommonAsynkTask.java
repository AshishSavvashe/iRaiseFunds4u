package com.appbell.iraisefund4u.resto.app.tasks;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.appbell.iraisefund4u.common.util.RestoProgressBar;
import com.appbell.iraisefund4u.resto.util.AndroidAppConstants;

public abstract class RestoCommonAsynkTask extends AsyncTask<Void, Void, Void> implements AndroidAppConstants{

	protected Context appContext;
	protected Activity actContext;
	private boolean showProgressBar;
	protected RestoProgressBar progressBar;
	protected boolean progressBarCancelable = true;
    protected String errorMsg;
	
	public RestoCommonAsynkTask(Activity actContext, boolean showProgressBar){
		this.actContext = actContext;
		appContext = actContext.getApplicationContext();
		this.showProgressBar = showProgressBar;
	}
	
	public RestoCommonAsynkTask(Activity actContext, boolean showProgressBar, boolean progressBarCancelable){
		this.actContext = actContext;
		appContext = actContext.getApplicationContext();
		this.showProgressBar = showProgressBar;
		this.progressBarCancelable = progressBarCancelable;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if(showProgressBar && actContext!=null && !actContext.isFinishing()){
			progressBar = new RestoProgressBar(actContext);
			progressBar.setCancelable(progressBarCancelable);
		}
	}
	
	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		if(actContext!=null && !actContext.isFinishing()){
			dismissProgressBar();	
		}
			
	}
	
	protected void dismissProgressBar(){
		if(progressBar != null){
			progressBar.stop();
			progressBar = null;
		}
	}
}
