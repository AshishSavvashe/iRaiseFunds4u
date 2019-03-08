package com.appbell.iraisefund4u.resto.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout.LayoutParams;

import com.appbell.iraisefund4u.R;
import com.appbell.iraisefund4u.common.util.AndroidAppUtil;
import com.appbell.iraisefund4u.common.util.AppLoggingUtility;
import com.appbell.iraisefund4u.resto.util.AndroidAppConstants;
import com.appbell.iraisefund4u.resto.util.NavigationUtil;

import java.util.Date;

public class WebViewActivity extends CommonActionBarActivity {

	private static final String USER_AGENT = "Mozilla/5.0 (Linux; U; Android 2.3.6; en-gb; GT-S5360 Build/GINGERBREAD) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1";
	private static final String INTERNET_NOT_AVAILABLE = "Request can not be processed as Internet is not available. Please try again later !!! ";

	WebView browser = null;
	String url = null;
	boolean isHandelBack;


	@SuppressLint("JavascriptInterface")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview);
//		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//		getSupportActionBar().setHomeButtonEnabled(true);

		boolean isInternetAvailable = AndroidAppUtil.isInternetAvailable(this);
		url = getIntent().getStringExtra("url");



		final String title = getIntent().getStringExtra("title");
		isHandelBack = getIntent().getBooleanExtra("isHandelBrrowserBack",false);
		//getSupportActionBar().setTitle(title);

		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		setToolbarText(title);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);

		browser = findViewById(R.id.browser);
		browser.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		browser.getSettings().setUserAgentString(USER_AGENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

		try {
            browser.clearCache(true);
        }catch (Throwable tt){
            AppLoggingUtility.logError(this, tt, " WebViewActivity: clearCache: ");
        }

		final LayoutParams params = new LayoutParams(
		        LayoutParams.MATCH_PARENT,
		        LayoutParams.WRAP_CONTENT
		);
		params.setMargins(0, 0, 0, 60);
		
		WebSettings webSettings = browser.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		webSettings.setSupportZoom(true);
		webSettings.setDatabaseEnabled(true);

		browser.addJavascriptInterface(new JavaScriptInterface(this), "Android");


		final ProgressBar progressBar = findViewById(R.id.loadingIndicator);
		progressBar.setVisibility(View.VISIBLE);
		browser.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				//getSupportActionBar().setTitle("Loading...");
				if (progress == 100){
					//getSupportActionBar().setTitle(title);
					progressBar.setVisibility(View.GONE);
				}
			}
		});

		browser.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

            	long a = new Date().getTime();
				Log.d("msg", String.valueOf(a));
            	super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
				long aa = new Date().getTime();
				Log.d("msg1", String.valueOf(aa));
                super.onPageFinished(view, url);

            }
        });

        if (isInternetAvailable) {
			browser.loadUrl(url);
		} else {
			String html = getHTMLErrorMessage(INTERNET_NOT_AVAILABLE);
			browser.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
		}

	}


	public class JavaScriptInterface {
		Context mContext;

		JavaScriptInterface(Context c) {
			mContext = c;
		}

		@JavascriptInterface
		public void openNative(String bookBuyerPhoneNo) {

			//Toast.makeText(mContext, bookBuyerPhoneNo, Toast.LENGTH_SHORT).show();
			NavigationUtil.navigate2VoucherBookListActivity(WebViewActivity.this,true,bookBuyerPhoneNo);

		}
	}

    @Override
	public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:

				if(isHandelBack){
					if (browser.canGoBack()) {
						browser.goBack();
						// browser.goBackOrForward(0);
					} else {
						finish();
					}
				}else {
					finish();
				}
                
                return true;
        }

		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		menu.findItem(R.id.help).setVisible(false);
		menu.findItem(R.id.more).setVisible(false);

		return super.onPrepareOptionsMenu(menu);
	}

	private String getHTMLErrorMessage(String message) {
		StringBuffer html = new StringBuffer("<html><body>");
		html.append("<br/><br/>");
		html.append(message);
		html.append("<br/><br/>");
		html.append("<a href='").append(url).append("'><center> Click Here to Try Again </center></a>");
		html.append("</body></html>");
		return html.toString();
	}

	@Override
	protected void onStop() {
		super.onStop();
		finish();
	}

    @Override
    public void onBackPressed() {

		if(isHandelBack){
			if (browser.canGoBack()) {
				browser.goBack();
				
			} else {
				super.onBackPressed();
			}
		}else {
			super.onBackPressed();
		}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.application_menu, menu);
        MenuItem menuLogout = menu.findItem(R.id.more);
        menuLogout.setActionView(R.layout.actionview_logout);
        menuLogout.getActionView().findViewById(R.id.more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CommonAlertDialog(WebViewActivity.this, AndroidAppConstants.DIALOG_ACTION_Logout).showDialog(WebViewActivity.this, null, getResources().getString(R.string.lblLogout), getResources().getString(R.string.lblCancel));
            }
        });

        return true;
    }
}
