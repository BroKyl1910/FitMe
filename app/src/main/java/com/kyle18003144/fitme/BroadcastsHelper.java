package com.kyle18003144.fitme;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static android.content.Context.ALARM_SERVICE;

public class BroadcastsHelper {

    public static void scheduleFirstFootstepUpload(Context context) {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(System.currentTimeMillis());//set the current time and date for this calendar
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 30);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        Intent intent = new Intent(context, FootstepsBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 234324243, intent, 0);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),pendingIntent);
    }
    public static void scheduleRepeatingFootstepUpload(Context context) {
        long timeInMilliseconds = System.currentTimeMillis() + 86400000;
        Intent intent = new Intent(context, FootstepsBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 234324243, intent, 0);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMilliseconds,pendingIntent);
    }

    public static void addShutdownBroadcastListener(Context context){
//        context.registerReceiver(ShutdownReceiver.class, new IntentFilter(Intent.ACTION_SHUTDOWN));
    }

//    public static void scheduleFirstFootstepUpload(Context context) {
//        Calendar cal = new GregorianCalendar();
//        cal.setTimeInMillis(System.currentTimeMillis());//set the current time and date for this calendar
//        cal.set(Calendar.HOUR_OF_DAY, 13);
//        cal.set(Calendar.MINUTE, 53);
//        cal.set(Calendar.SECOND, 0);
//        cal.set(Calendar.MILLISECOND, 0);
//        long timeInMilliseconds = System.currentTimeMillis() + 5000;
//        Intent intent = new Intent(context, FootstepsBroadcastReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 234324243, intent, 0);
//        AlarmManager alarmManager = (AlarmManager)context.getSystemService(ALARM_SERVICE);
//        alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMilliseconds,pendingIntent);
//
//        Toast.makeText(context, "Alarm Set", Toast.LENGTH_SHORT).show();
//    }
//
//    public static void scheduleRepeatingFootstepUpload(Context context) {
//        long timeInMilliseconds = System.currentTimeMillis() + 10000;
//        Intent intent = new Intent(context, FootstepsBroadcastReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 234324243, intent, 0);
//        AlarmManager alarmManager = (AlarmManager)context.getSystemService(ALARM_SERVICE);
//        alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMilliseconds,pendingIntent);
//
//        Toast.makeText(context, "Alarm Set", Toast.LENGTH_SHORT).show();
//    }
}
