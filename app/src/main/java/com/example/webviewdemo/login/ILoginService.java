package com.example.webviewdemo.login;

/**
 * 创建时间：2020/3/19
 * 创建人：singleCode
 * 功能描述：
 **/
public interface ILoginService {
    public String LOGIN_SERVICE_NAME = "login";

    void login(String name,String key);
}
