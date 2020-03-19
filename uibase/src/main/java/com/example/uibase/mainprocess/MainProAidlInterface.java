package com.example.uibase.mainprocess;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;

import com.example.uibase.IWebAidlCallback;
import com.example.uibase.IWebAidlInterface;
import com.example.uibase.command.CommandsManager;
import com.example.uibase.command.ResultBack;
import com.google.gson.Gson;

import java.util.Map;

public class MainProAidlInterface extends IWebAidlInterface.Stub {

    private Context context;

    public MainProAidlInterface(Context context) {
        this.context = context;
    }

    private void handleRemoteAction(int level, final String actionName, Map paramMap, final IWebAidlCallback callback) throws Exception {
        CommandsManager.getInstance().findAndExecRemoteCommand(context, level, actionName, paramMap, new ResultBack() {
            @Override
            public void onResult(int status, String action, Object result) {
                try {
                    if (callback != null) {
                        callback.onResult(status, actionName, new Gson().toJson(result));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void handlerWebAction(int level, String actionName, String json, IWebAidlCallback callback) throws RemoteException {
        int pid = android.os.Process.myPid();
        Log.d("webli" , String.format("MainProAidlInterface: 进程ID（%d）， WebView请求（%s）, 参数 （%s）", pid, actionName, json));
        try {
            handleRemoteAction(level, actionName, new Gson().fromJson(json, Map.class), callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
