package com.lhd.toprunapp;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.lhd.applock.R;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import static android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN;
import static android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;

/**
 * Created by D on 8/9/2017.
 */

public class StateDeviceService extends Service {
    private static final String TAG = "StateDeviceService";
    private StateScreen stateScreen;
    private ActivityManager mActivityManager;
    private WindowManager windowManager;
    private View viewLock;
    private boolean isShowLock;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;

    }

    public static boolean isRunning(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager
                .getRunningServices(Integer.MAX_VALUE)) {
            if (StateDeviceService.class.getName().equals(
                    service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private static PendingIntent running_intent;

    private static final PendingIntent getRunIntent(Context context) {
        if (running_intent == null) {
            Intent intent = new Intent(context, StateDeviceService.class);
            running_intent = PendingIntent.getService(context, 2500, intent, 0);
        }
        return running_intent;
    }

    private static final void startAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = getRunIntent(context);
        long startTime = SystemClock.elapsedRealtime();
//        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, startTime, 250, pendingIntent);
//        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, startTime, 1000, pendingIntent);
//
//        alarmManager.set(AlarmManager.RTC_WAKEUP,startTime,pendingIntent);
        if (Build.VERSION.SDK_INT < 23) {
            if (Build.VERSION.SDK_INT >= 19) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, startTime, pendingIntent);
            } else {
                alarmManager.set(AlarmManager.ELAPSED_REALTIME, startTime, pendingIntent);
            }
        } else {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME, startTime, pendingIntent);
        }
    }

    private static final void stopAlarm(Context c) {
        AlarmManager am = (AlarmManager) c.getSystemService(ALARM_SERVICE);
        am.cancel(getRunIntent(c));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        listenOnOffSceen();
        unListenOnOffSceen();
        startAlarm(this);
        mActivityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        Log.e(StateDeviceService.TAG, "" + getTopTask());
        if (getTopTask().equals("com.cleanmaster.mguard") && isShowLock == false) {
            showWindowLog();
        } else if (!getTopTask().equals("com.cleanmaster.mguard") && isShowLock == true) {
            hideWindowLog();
        }
        return START_NOT_STICKY;
    }

    private void hideWindowLog() {
        isShowLock = false;
        if (viewLock != null) windowManager.removeViewImmediate(viewLock);
    }

    private void showWindowLog() {
        isShowLock = true;
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                +FLAG_NOT_TOUCH_MODAL
                        + WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                        + FLAG_FULLSCREEN + WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.RIGHT | Gravity.TOP;
        params.setTitle("Load Average");
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
//        this.getwsetSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        viewLock = View.inflate(this, R.layout.window_lock_pin, null);
//        SlidingPaneLayout slidingPaneLayout = viewLock.findViewById(R.id.sldp);
//        slidingPaneLayout.setPanelSlideListener(new SlidingPaneLayout.PanelSlideListener() {
//            @Override
//            public void onPanelSlide(View panel, float slideOffset) {
//
//            }
//
//            @Override
//            public void onPanelOpened(View panel) {
//                hideWindowLog();
//            }
//
//            @Override
//            public void onPanelClosed(View panel) {
//
//            }
//        });
        windowManager.addView(viewLock, params);
    }

    private void unListenOnOffSceen() {
        if (stateScreen != null)
            unregisterReceiver(stateScreen);
    }

    private void listenOnOffSceen() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        stateScreen = new StateScreen();
        registerReceiver(stateScreen, intentFilter);
    }

    private String getTopTask() {

        String topPackageName = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            UsageStatsManager mUsageStatsManager = (UsageStatsManager) getSystemService(USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            // We get usage stats for the last 10 seconds
            List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 10, time);
            // Sort the stats by the last time used
            if (stats != null) {
                SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
                for (UsageStats usageStats : stats) {
                    mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                }
                if (mySortedMap != null && !mySortedMap.isEmpty()) {
                    topPackageName = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
//                    Log.e("topPackageName", topPackageName);
                }
            }

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            topPackageName = mActivityManager.getRunningAppProcesses().get(0).processName;
//            Log.e("topPackageName", topPackageName);
        } else {
            topPackageName = (mActivityManager.getRunningTasks(1).get(0)).topActivity.getPackageName();
//            Log.e("topPackageName", topPackageName);
        }
        return topPackageName;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unListenOnOffSceen();
    }
}
