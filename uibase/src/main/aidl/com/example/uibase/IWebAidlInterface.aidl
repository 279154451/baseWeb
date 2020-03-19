// IWebAidlInterface.aidl
package com.example.uibase;

// Declare any non-default types here with import statements
import com.example.uibase.IWebAidlCallback;
interface IWebAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
//    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
//            double aDouble, String aString);

            void handlerWebAction(int level,String actionName,String json,in IWebAidlCallback callback);
}
