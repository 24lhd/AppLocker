package com.lhd.toprunapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


/**
 * Created by D on 8/9/2017.
 */

public class StateScreen extends BroadcastReceiver {

    private static final int REQUEST_CODE = 100;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            Log.e("StateScreen", "Screen ON");

            // Trigger package again
//            mLastPackageName = "";
//            startAlarm(AppLockService.this);
        }
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            Log.e("StateScreen", "Screen OFF");
//            stopAlarm(AppLockService.this);
//            if (mRelockScreenOff) {
//                lockAll();
//            }
        }
    }


}