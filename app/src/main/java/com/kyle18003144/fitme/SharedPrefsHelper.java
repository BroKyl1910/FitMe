package com.kyle18003144.fitme;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class SharedPrefsHelper {
    static String IMPERIAL_PREF_NAME = "ImperialPref";
    static String REMEMBER_PREF_NAME = "RememberPref";
    static String TODAY_FOOTSTEP_PREF_NAME = "TodayFootstepPref";
    static String PREVIOUS_PREF_NAME = "PreviousFootstepPref";
    static String DATE_PREF_NAME = "DatePref";

    //Settings
    public static boolean getImperial(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(IMPERIAL_PREF_NAME, MODE_PRIVATE);
        boolean useImperial = prefs.getBoolean("imperial", false);
        return useImperial;
    }
    public static void setImperial(Context context, boolean useImperial) {
        SharedPreferences.Editor editor = context.getSharedPreferences(IMPERIAL_PREF_NAME, MODE_PRIVATE).edit();
        editor.putBoolean("imperial", useImperial);
        editor.apply();
    }
    public static boolean getRememberMe(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(REMEMBER_PREF_NAME, MODE_PRIVATE);
        boolean remember = prefs.getBoolean("remember", true);
        return remember;
    }
    public static void setRememberMe(Context context, boolean remember) {
        SharedPreferences.Editor editor = context.getSharedPreferences(REMEMBER_PREF_NAME, MODE_PRIVATE).edit();
        editor.putBoolean("remember", remember);
        editor.apply();
    }

    //Values
    public static int getTodayFootsteps(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(TODAY_FOOTSTEP_PREF_NAME, MODE_PRIVATE);
        int footsteps = prefs.getInt("todayFootsteps", -1);
        return footsteps;
    }
    public static void setTodayFootsteps(Context context, int footsteps) {
        SharedPreferences.Editor editor = context.getSharedPreferences(TODAY_FOOTSTEP_PREF_NAME, MODE_PRIVATE).edit();
        editor.putInt("todayFootsteps", footsteps);
        editor.apply();
    }
    public static int getPreviousFootsteps(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREVIOUS_PREF_NAME, MODE_PRIVATE);
        return prefs.getInt("prevFootsteps", 0);
    }
    public static void setPreviousFootsteps(Context context, int footsteps) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREVIOUS_PREF_NAME, MODE_PRIVATE).edit();
        editor.putInt("prevFootsteps", footsteps);
        editor.apply();
    }
    public static Date getDate(Context context) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");
        SharedPreferences prefs = context.getSharedPreferences(DATE_PREF_NAME, MODE_PRIVATE);
        String date = prefs.getString("date", new Date().toString());
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }
    public static void setDate(Context context, Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");
        String strDate = dateFormat.format(date);
        SharedPreferences.Editor editor = context.getSharedPreferences(TODAY_FOOTSTEP_PREF_NAME, MODE_PRIVATE).edit();
        editor.putString("date", strDate);
        editor.apply();
    }
}

