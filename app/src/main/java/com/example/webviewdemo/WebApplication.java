package com.example.webviewdemo;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.base.service.ServiceManager;
import com.example.uibase.BaseApplication;
import com.example.webviewdemo.login.ILoginService;
import com.example.webviewdemo.login.LoginServiceImpl;

/**
 * 创建时间：2020/3/19
 * 创建人：singleCode
 * 功能描述：
 **/
public class WebApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
        setIsDebug(BuildConfig.DEBUG);
        if (BuildConfig.DEBUG) {           // These two lines must be written before init, otherwise these configurations will be invalid in the init process
            ARouter.openLog();     // Print log
            ARouter.openDebug();   // Turn on debugging mode (If you are running in InstantRun mode, you must turn on debug mode! Online version needs to be closed, otherwise there is a security risk)
        }
        ARouter.init(this); // As early as possible, it is recommended to initialize in the Application

        //初始化manager
        ServiceManager.init(this);
        ServiceManager.registerService(ILoginService.LOGIN_SERVICE_NAME, LoginServiceImpl.class.getName());
    }

}
