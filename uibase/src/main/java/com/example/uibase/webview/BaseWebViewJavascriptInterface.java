package com.example.uibase.webview;

import android.content.Context;
import android.os.Handler;
import android.webkit.JavascriptInterface;

/**
 * 创建时间：2020/3/17
 * 创建人：singleCode
 * 功能描述：
 **/
public class BaseWebViewJavascriptInterface {
    private final Context mContext;
    private final Handler mHandler = new Handler();
    private JavascriptCommand javascriptCommand;

    public BaseWebViewJavascriptInterface(Context context) {
        mContext = context;
    }

    @JavascriptInterface
    public void post(final String cmd, final String param) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    if (javascriptCommand != null) {
                        javascriptCommand.exec(mContext, cmd, param);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setJavascriptCommand(JavascriptCommand javascriptCommand) {
        this.javascriptCommand = javascriptCommand;
    }

    public interface JavascriptCommand {
        void exec(Context context, String cmd, String params);
    }
}
