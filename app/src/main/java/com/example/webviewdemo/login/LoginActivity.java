package com.example.webviewdemo.login;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.common.login.AppLoginRouter;
import com.example.webviewdemo.R;

/**
 * 创建时间：2020/3/19
 * 创建人：singleCode
 * 功能描述：
 **/
@Route(path = AppLoginRouter.APP_LOGIN_ACTIVITY)
public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
