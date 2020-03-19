package com.example.uibase;

import android.app.Application;

/**
 * 创建时间：2020/3/19
 * 创建人：singleCode
 * 功能描述：
 **/
public class BaseApplication extends Application {
    public static BaseApplication sApplication;

    public static boolean isIsDebug() {
        return sIsDebug;
    }

    public static void setIsDebug(boolean sIsDebug) {
        BaseApplication.sIsDebug = sIsDebug;
    }
    private static boolean sIsDebug;

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
    }
}
