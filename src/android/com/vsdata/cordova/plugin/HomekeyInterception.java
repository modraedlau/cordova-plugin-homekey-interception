package com.vsdata.cordova.plugin;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.concurrent.atomic.AtomicBoolean;

public class HomekeyInterception extends CordovaPlugin {
    private static final String TAG = "HomekeyInterceptionScanner";
    public static final int REQUEST_CODE = 1;

    private CallbackContext callbackContext;
    private String m_Broadcastname;

    private static AtomicBoolean registered = new AtomicBoolean(false);

    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        Log.d(TAG, "Initializing HomekeyInterception");
    }

    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
        this.callbackContext = callbackContext;
        if ("register".equals(action)) {
            if (registered.compareAndSet(false, true)) {
                initHookEvent();
            }
            return true;
        }
        return false;
    }

    /**
     * Use to get the current Cordova Activity
     *
     * @return your Cordova activity
     */
    private Activity getActivity() {
        return this.cordova.getActivity();
    }

    /**
     * Use to get the Web View Context
     *
     * @return
     */
    private Context getWebViewContext() {
        return super.webView.getContext();
    }

    /**
     * Initializing Hook Event
     * You ABSOLUTELY need to precise getActivity().getApplicationContext()
     * before registerReceiver() otherwise it won't get the good context.
     */
    public void initHookEvent() {
        IntentFilter filter_hook = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        getActivity().getApplicationContext().registerReceiver(broadcastReceiver_hook, filter_hook);
    }

    /**
     * Is natively created by extending CordovaPlugin
     */
    public BroadcastReceiver broadcastReceiver_hook = new BroadcastReceiver() {

        String SYSTEM_REASON = "reason";
        String SYSTEM_HOME_KEY = "homekey";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // 监听home键
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_REASON);
                if (SYSTEM_HOME_KEY.equals(reason)) {
                    // 重新跳转到应用
                    for(int j = 0; j < 10; j++) {
                        Intent selfIntent = new Intent(context, getActivity().getClass());
                        selfIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, selfIntent, 0);
                        try {
                            pendingIntent.send();
                        } catch (PendingIntent.CanceledException e) {
                            // e.printStackTrace();
                        }
                    }
                }
            }
        }
    };
}