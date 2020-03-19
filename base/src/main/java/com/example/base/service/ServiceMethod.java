package com.example.base.service;

/**
 * 创建时间：2020/3/17
 * 创建人：singleCode
 * 功能描述：
 **/
public enum ServiceMethod  {
    REGISTER_SERVICE("register_service"),
    UNREGISTER_SERVICE("unregister_service"),
    QUERY_SERVICE("query_service"),
    CALL_SERVICE("call_service"),
    QUERY_INTERFACE("query_interface"),
    REPORT_BINDER("report_binder");


    private String method;

    ServiceMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }
}
