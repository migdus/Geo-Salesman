package com.diplomadoUNAL.geosalesman;


import android.webkit.WebView;
import android.webkit.WebViewClient;


class MyWebViewClient extends WebViewClient { 
    @Override 
    //show the web page in webview but not in web browser
    public boolean shouldOverrideUrlLoading(WebView view, String url) { 
        view.loadUrl (url); 
        return true;
    } 
}
