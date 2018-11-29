package com.mluansing.phonecalltimer;

import android.app.Application;
import android.content.IntentFilter;
import android.util.Log;

public class MainApplication extends Application {

    private static final String TAG = MainApplication.class.getSimpleName();

    private static KillCallReceiver killCallReceiver;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "Registering KillCallReceiver");
        killCallReceiver = new KillCallReceiver();
        getApplicationContext().registerReceiver(killCallReceiver, new IntentFilter(KillCallReceiver.INTENT_KILL_CALL));

    }
}
