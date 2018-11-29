package com.mluansing.phonecalltimer.util;

import android.content.Context;
import android.content.SharedPreferences;

public abstract class SharedPreferenceUtil {

    public static final String TAG = SharedPreferenceUtil.class.getSimpleName();

    private static final String SHARED_PREFERENCES_TIMER_SETTINGS = "SHARED_PREFERENCES_TIMER_SETTINGS";
    private static final String DEFAULT_TIMER = "DEFAULT_TIMER"; // true: timer, false: countdown

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(SHARED_PREFERENCES_TIMER_SETTINGS, Context.MODE_PRIVATE);
    }

    public static boolean isDefaultTimer(Context context) {
        return getSharedPreferences(context).getBoolean(DEFAULT_TIMER, true);
    }

    public static void setDefaultTimer(Context context, boolean isTimer) {
        getSharedPreferences(context).edit().putBoolean(DEFAULT_TIMER, isTimer).apply();
    }

}