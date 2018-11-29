package com.mluansing.phonecalltimer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Method;

public class KillCallReceiver extends BroadcastReceiver {

    public static final String TAG = KillCallReceiver.class.getSimpleName();

    public static final String INTENT_KILL_CALL = "com.mluansing.phonecalltimer.KILL_CALL";

    TelephonyManager telephonyManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().compareTo(INTENT_KILL_CALL) == 0) {

            telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            // at least one call exists that is dialing, active, or on hold, and no calls are ringing or waiting
            if (telephonyManager.getCallState() == TelephonyManager.CALL_STATE_OFFHOOK) {

                if (!killCall()) {
                    Toast.makeText(context, "Attempted to kill phone call", Toast.LENGTH_SHORT).show(); // TODO: remove

                    Log.e(TAG, "Call kill failed");
                } else {
                    Toast.makeText(context, R.string.call_kill_success, Toast.LENGTH_SHORT).show(); // TODO: remove
                }
            } else {
                Log.e(TAG, "No active phone call to kill");

                Toast.makeText(context, R.string.no_active_call, Toast.LENGTH_SHORT).show();
            }

            updateUI();
        }
    }

    public boolean killCall() {
        try {
            // Get the getITelephony() method
            Class classTelephony = Class.forName(telephonyManager.getClass().getName());
            Method methodGetITelephony = classTelephony.getDeclaredMethod("getITelephony");

            // Ignore that the method is supposed to be private
            methodGetITelephony.setAccessible(true);

            // Invoke getITelephony() to get the ITelephony interface
            Object telephonyInterface = methodGetITelephony.invoke(telephonyManager);

            // Get the endCall method from ITelephony
            Class telephonyInterfaceClass =
                    Class.forName(telephonyInterface.getClass().getName());
            Method methodEndCall = telephonyInterfaceClass.getDeclaredMethod("endCall");

            // Invoke endCall()
            methodEndCall.invoke(telephonyInterface);

        } catch (Exception ex) { // Many things can go wrong with reflection calls
            Log.e(TAG, "Unable to kill current phone call: " + ex.toString());
            return false;
        }
        return true;
    }

    public void updateUI() {
        try {
            MainActivity.getInstance().resetClock();
        } catch (Exception e) {
            Log.e(TAG, "Unable to reset UI: " + e.toString());
        }
    }

}