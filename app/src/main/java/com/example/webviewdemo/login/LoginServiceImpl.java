package com.example.webviewdemo.login;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.common.login.AppLoginRouter;

/**
 * 创建时间：2020/3/19
 * 创建人：singleCode
 * 功能描述：
 **/
public class LoginServiceImpl implements ILoginService{
    @Override
    public void login(String name, String key) {
        ARouter.getInstance().build(AppLoginRouter.APP_LOGIN_ACTIVITY).navigation();
    }
}
