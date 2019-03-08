package com.appbell.iraisefund4u.resto.ui;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout.LayoutParams;

import com.appbell.iraisefund4u.R;
import com.appbell.iraisefund4u.common.util.AndroidAppUtil;
import com.appbell.iraisefund4u.common.util.AppLoggingUtility;

public class WebViewFragment extends Fragment {

	private static final String USER_AGENT = "Mozilla/5.0 (Linux; U; Android 2.3.6; en-gb; GT-S5360 Build/GINGERBREAD) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1";
	private static final String INTERNET_NOT_AVAILABLE = "Request can not be processed as Internet is not available. Please try again later !!! ";

	WebView browser = null;
	String url = null;
	View parentView;


	public static WebViewFragment getInstance(String urlMap) {
		WebViewFragment fragment = new WebViewFragment();
		Bundle bundle = new Bundle();
		bundle.putString("urlMap",urlMap);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}


	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		parentView = inflater.inflate(R.layout.fragment_webview, null);
		return parentView ;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
	}

	private void initView() {

		boolean isInternetAvailable = AndroidAppUtil.isInternetAvailable(getActivity());
		url = getArguments().getString("urlMap");

		if(browser != null){
			if (Build.VERSION.SDK_INT >= 19) {
				browser.setLayerType(View.LAYER_TYPE_HARDWARE, null);
			}
			else {
				browser.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
			}
		}


		((VoucherListActivity)getActivity()).hideRestonameAndMonthSpineer();

		browser = parentView.findViewById(R.id.browser);
		browser.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		browser.getSettings().setUserAgentString(USER_AGENT);



		try {
			browser.clearCache(true);
		}catch (Throwable tt){
			AppLoggingUtility.logError(getActivity(), tt, " WebViewFragment: clearCache: ");
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


		final ProgressBar progressBar = parentView.findViewById(R.id.loadingIndicator);
		progressBar.setVisibility(View.VISIBLE);
		browser.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				if (progress == 100){
					//getSupportActionBar().setTitle(title);
					progressBar.setVisibility(View.GONE);
				}
			}
		});

		browser.setWebViewClient(new WebViewClient(){
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
			case android.R.id.home:
				if (browser.canGoBack()) {
					browser.goBack();

				}else{
					getActivity().finish();
				}
				return true;
		}

		return super.onOptionsItemSelected(item);
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

}
