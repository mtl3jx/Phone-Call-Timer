package com.mluansing.phonecalltimer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
import java.util.Date;

public abstract class KillCallManager {

    public static final String TAG = KillCallManager.class.getSimpleName();

    public static void cancelTimer(Context context) {
        Intent intent = new Intent(context, KillCallReceiver.class);
        intent.setAction(KillCallReceiver.INTENT_KILL_CALL);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    public static void setTimer(Context context, int hours, int mins, int seconds) {
        Date now = new Date();
        Calendar timeToKillCall = Calendar.getInstance();
        Calendar currentTime = Calendar.getInstance();

        currentTime.setTime(now);
        timeToKillCall.setTime(now);

        // set the alarm for timer mode in 24 HR time
        timeToKillCall.set(Calendar.HOUR_OF_DAY, hours);
        timeToKillCall.set(Calendar.MINUTE, mins);
        timeToKillCall.set(Calendar.SECOND, seconds);

        // do at next available time today or tomorrow
        if (timeToKillCall.before(currentTime)) {
            timeToKillCall.add(Calendar.DATE, 1);
        }

        Intent intent = new Intent();
        intent.setAction(KillCallReceiver.INTENT_KILL_CALL);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeToKillCall.getTimeInMillis(), pendingIntent);
    }

    public static void setCountdown(Context context, int hours, int mins, int seconds) {
        Date now = new Date();
        Calendar timeToKillCall = Calendar.getInstance();
        Calendar currentTime = Calendar.getInstance();

        currentTime.setTime(now);
        timeToKillCall.setTime(now);

        // set the alarm for timer mode
        timeToKillCall.add(Calendar.HOUR_OF_DAY, hours);
        timeToKillCall.add(Calendar.MINUTE, mins);
        timeToKillCall.add(Calendar.SECOND, seconds);

        Intent intent = new Intent();
        intent.setAction(KillCallReceiver.INTENT_KILL_CALL);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeToKillCall.getTimeInMillis(), pendingIntent);
    }

}
