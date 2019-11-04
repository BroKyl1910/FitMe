package com.kyle18003144.fitme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ShutdownReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int currentFootsteps = SharedPrefsHelper.getTodayFootsteps(context);
        SharedPrefsHelper.setPreviousFootsteps(context, currentFootsteps);
    }
}
