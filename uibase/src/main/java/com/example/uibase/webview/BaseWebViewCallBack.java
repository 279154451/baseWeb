package com.example.uibase.webview;

import android.content.Context;
import android.webkit.WebView;

/**
 * 创建时间：2020/3/17
 * 创建人：singleCode
 * 功能描述：
 **/
public interface BaseWebViewCallBack {
    int getCommandLevel();

    void pageStarted(String url);

    void pageFinished(String url);

    boolean overrideUrlLoading(WebView view, String url);

    void onError();

    void exec(Context context, int commandLevel, String cmd, String params, WebView webView);
}
