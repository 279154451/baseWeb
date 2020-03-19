// IWebAidlCallback.aidl
package com.example.uibase;

// Declare any non-default types here with import statements

interface IWebAidlCallback {

    void onResult(int responseCode, String actionName, String response);
}
