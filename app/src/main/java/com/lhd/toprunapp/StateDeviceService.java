package com.lhd.toprunapp;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.lhd.MyFingerPrint;
import com.lhd.applock.R;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

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
        if (getTopTask().equals("com.android.music") && isShowLock == false) {
            showWindowLog();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                initKey();
            }
        } else if (!getTopTask().equals("com.android.music") && isShowLock == true) {
            hideWindowLog();
        }
        return START_NOT_STICKY;
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initKey() {
        try {
            mKeyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (KeyStoreException e) {
            throw new RuntimeException("Failed to get an instance of KeyStore", e);
        }
        try {
            mKeyGenerator = KeyGenerator
                    .getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException("Failed to get an instance of KeyGenerator", e);
        }
        Cipher defaultCipher;
        try {
            defaultCipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_CBC + "/"
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get an instance of Cipher", e);
        }
        KeyguardManager keyguardManager = getSystemService(KeyguardManager.class);
        FingerprintManager fingerprintManager = getSystemService(FingerprintManager.class);


        if (!keyguardManager.isKeyguardSecure()) {
            Toast.makeText(this, "Secure lock screen hasn't set up.\n" + "Go to 'Settings -> Security -> Fingerprint' to set up a fingerprint", Toast.LENGTH_LONG).show();
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (!fingerprintManager.hasEnrolledFingerprints()) {
            Toast.makeText(this, "Go to 'Settings -> Security -> Fingerprint' and register at least one fingerprint",
                    Toast.LENGTH_LONG).show();
            return;
        }
        createKey(DEFAULT_KEY_NAME, true);
        createKey(KEY_NAME_NOT_INVALIDATED, false);
        if (initCipher(defaultCipher, DEFAULT_KEY_NAME)) {
            fingerprintManager = (FingerprintManager) getSystemService(Context.FINGERPRINT_SERVICE);
            MyFingerPrint myFingerPrint = new MyFingerPrint(fingerprintManager);
            myFingerPrint.startListening(new FingerprintManager.CryptoObject(defaultCipher));
        }
    }
    private static final String SECRET_MESSAGE = "Very secret message";
    private static final String KEY_NAME_NOT_INVALIDATED = "key_not_invalidated";
    static final String DEFAULT_KEY_NAME = "default_key";
    private KeyStore mKeyStore;
    private KeyGenerator mKeyGenerator;
    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean initCipher(Cipher cipher, String keyName) {
        try {
            mKeyStore.load(null);
            SecretKey key = (SecretKey) mKeyStore.getKey(keyName, null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException
                | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void createKey(String keyName, boolean invalidatedByBiometricEnrollment) {
        try {
            mKeyStore.load(null);
            KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(keyName,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                builder.setInvalidatedByBiometricEnrollment(invalidatedByBiometricEnrollment);
            }
            mKeyGenerator.init(builder.build());
            mKeyGenerator.generateKey();
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException
                | CertificateException | IOException e) {
            throw new RuntimeException(e);
        }
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
