package com.uipeople.schedule;

import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class NateOnClient extends WebViewClient {
	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		view.loadUrl(url);
		return true;
	}
}
