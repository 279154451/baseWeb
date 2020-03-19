package com.example.base.service;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.base.binder.MethodRouter;
import com.example.base.binder.ProcessBinder;
import com.example.base.compat.BundleCompat;
import com.example.base.local.ServicePool;

import java.lang.reflect.Proxy;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 创建时间：2020/3/17
 * 创建人：singleCode
 * 功能描述：
 **/
public class ServiceProvider extends ContentProvider {
    public static final String PID = "pid";
    public static final String BINDER = "binder";
    public static final String NAME = "name";
    public static final String INTERFACE = "interface";

    public static final String QUERY_SERVICE_RESULT_IS_IN_PROVIDIDER_PROCESS = "query_service_result_is_in_provider_process";
    public static final String QUERY_SERVICE_RESULT_BINDER = "query_service_result_binder";
    public static final String QUERY_SERVICE_RESULT_DESCRIPTOR = "query_service_result_desciptor";
    public static final String QUERY_INTERFACE_RESULT = "query_interface_result";
    private static Uri CONTENT_URI;

    //服务名：进程ID
    private static ConcurrentHashMap<String, Recorder> allServiceList = new ConcurrentHashMap<>();
    //进程ID：进程Binder
    private static ConcurrentHashMap<Integer, IBinder> processBinder = new ConcurrentHashMap<>();
    public static Uri buildUri() {
        if (CONTENT_URI == null) {
            CONTENT_URI = Uri.parse("content://"+ ServiceManager.sApplication.getPackageName() + ".svcmgr/call");
        }
        return CONTENT_URI;
    }
    public static class Recorder {
        public Integer pid;
        public String interfaceClass;
    }
    @Nullable
    @Override
    public Bundle call(@NonNull String method, @Nullable String arg, @Nullable Bundle extras) {
        switch (ServiceMethod.valueOf(method)){
            case REPORT_BINDER:
                final int pid = extras.getInt(PID);
                IBinder iBinder = BundleCompat.getBinder(extras, BINDER);
                processBinder.put(pid, iBinder);
                try {
                    iBinder.linkToDeath(new IBinder.DeathRecipient() {
                        @Override
                        public void binderDied() {
                            removeAllRecorderForPid(pid);
                        }
                    }, 0);
                } catch (RemoteException e) {
                    e.printStackTrace();
                    processBinder.remove(pid);
                }
                break;
            case REGISTER_SERVICE:
                String serviceName = arg;
                int pid1 = extras.getInt(PID);
                String interfaceClass = extras.getString(INTERFACE);
                IBinder binder =  processBinder.get(pid1);
                if (binder != null && binder.isBinderAlive()) {
                    Recorder recorder = new Recorder();
                    recorder.pid = pid1;
                    recorder.interfaceClass = interfaceClass;
                    allServiceList.put(serviceName, recorder);
                } else {
                    allServiceList.remove(pid1);
                }
                return null;
            case UNREGISTER_SERVICE:
                int pid2 = extras.getInt(PID);
                String name = extras.getString(NAME);
                if (TextUtils.isEmpty(name)) {
                    removeAllRecorderForPid(pid2);
                } else {
                    allServiceList.remove(name);
                    notifyClient(name);
                }
                break;
            case CALL_SERVICE:
                return MethodRouter.routerToInstance(extras);
            case QUERY_INTERFACE:
                Bundle bundle0 = new Bundle();
                Recorder recorder0 = allServiceList.get(arg);
                if (recorder0 != null) {
                    bundle0.putString(QUERY_INTERFACE_RESULT, recorder0.interfaceClass);
                }
                return bundle0;
            case QUERY_SERVICE:
                String serviceName1 = arg;
                if (allServiceList.containsKey(serviceName1)) {

                    Object instance = ServicePool.getService(serviceName1);

                    Bundle bundle = new Bundle();
                    if (instance != null && !Proxy.isProxyClass(instance.getClass())) {
                        bundle.putBoolean(QUERY_SERVICE_RESULT_IS_IN_PROVIDIDER_PROCESS, true);
                        return bundle;
                    } else {
                        Recorder recorder = allServiceList.get(serviceName1);
                        if (recorder != null) {
                            IBinder iBinder1 = processBinder.get(recorder.pid);
                            if (iBinder1 != null && iBinder1.isBinderAlive()) {
                                bundle.putBoolean(QUERY_SERVICE_RESULT_IS_IN_PROVIDIDER_PROCESS, false);
                                bundle.putString(QUERY_SERVICE_RESULT_DESCRIPTOR, ProcessBinder.class.getName() + "_" + recorder.pid);
                                BundleCompat.putBinder(bundle, QUERY_SERVICE_RESULT_BINDER, iBinder1);
                                return bundle;
                            }
                        }
                        return null;
                    }
                }
        }
        return null;
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
    private void removeAllRecorderForPid(int pid) {
        Log.w("ServiceProvider", "remove all service recordor for pid" + pid);

        //服务提供方进程挂了,或者服务提供方进程主动通知清理服务, 则先清理服务注册表, 再通知所有客户端清理自己的本地缓存
        processBinder.remove(pid);
        Iterator<Map.Entry<String, Recorder>> iterator = allServiceList.entrySet().iterator();
        while(iterator.hasNext()) {
            Map.Entry<String, Recorder> entry = iterator.next();
            if (entry.getValue().pid.equals(pid)) {
                iterator.remove();
                notifyClient(entry.getKey());
            }
        }
    }

    private void notifyClient(String name) {
        //通知持有服务的客户端清理缓存
        Intent intent = new Intent(ServiceManager.ACTION_SERVICE_DIE_OR_CLEAR);
        intent.putExtra(NAME, name);
        ServiceManager.sApplication.sendBroadcast(intent);
    }
}
