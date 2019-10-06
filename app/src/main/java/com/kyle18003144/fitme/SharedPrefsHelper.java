package com.kyle18003144.fitme;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class SharedPrefsHelper {
    static String IMPERIAL_PREF_NAME = "ImperialPref";

    public static boolean getImperial(Context context){
        SharedPreferences prefs = context.getSharedPreferences(IMPERIAL_PREF_NAME, MODE_PRIVATE);
        boolean useImperial = prefs.getBoolean("imperial", false);
        return useImperial;
    }

    public static void setImperial(Context context, boolean useImperial){
        SharedPreferences.Editor editor = context.getSharedPreferences(IMPERIAL_PREF_NAME, MODE_PRIVATE).edit();
        editor.putBoolean("imperial", useImperial);
        editor.apply();
    }

}
