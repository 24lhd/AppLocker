package com.lhd;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

/**
 * Created by D on 8/10/2017.
 */

@RequiresApi(api = Build.VERSION_CODES.M)
public class MyFingerPrint extends FingerprintManager.AuthenticationCallback {
    public MyFingerPrint(FingerprintManager mFingerprintManager) {
        this.mFingerprintManager = mFingerprintManager;
    }

    private FingerprintManager mFingerprintManager;
    private CancellationSignal mCancellationSignal;
    private boolean mSelfCancelled;

    public boolean isFingerprintAuthAvailable() {
        // The line below prevents the false positive inspection from Android Studio
        // noinspection ResourceType
        return mFingerprintManager.isHardwareDetected()
                && mFingerprintManager.hasEnrolledFingerprints();
    }

    public void startListening(FingerprintManager.CryptoObject cryptoObject) {
        if (!isFingerprintAuthAvailable()) {
            return;
        }
        mCancellationSignal = new CancellationSignal();
        mSelfCancelled = false;
        mFingerprintManager.authenticate(cryptoObject, mCancellationSignal, 0 /* flags */, this, null);
    }

    public void stopListening() {
        if (mCancellationSignal != null) {
            mSelfCancelled = true;
            mCancellationSignal.cancel();
            mCancellationSignal = null;
        }
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        Log.e(Demo.TAG, "onAuthenticationSucceeded");
        super.onAuthenticationSucceeded(result);
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        Log.e(Demo.TAG, "onAuthenticationError");
        super.onAuthenticationError(errorCode, errString);
    }

    @Override
    public void onAuthenticationFailed() {
        Log.e(Demo.TAG, "onAuthenticationFailed");
        super.onAuthenticationFailed();
    }
}
