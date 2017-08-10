package com.lhd;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 * Created by D on 8/10/2017.
 */

public class Demo extends AppCompatActivity {
    public static final String TAG = "demo";

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Ok men", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private KeyStore mKeyStore;
    private KeyGenerator mKeyGenerator;
    static final String DEFAULT_KEY_NAME = "default_key";

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.USE_FINGERPRINT}, 1);
            FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(Context.FINGERPRINT_SERVICE);
            MyFingerPrint myFingerPrint = new MyFingerPrint(fingerprintManager);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            if (!fingerprintManager.isHardwareDetected()) {
                // Device doesn't support fingerprint authentication
                Log.e(TAG, "Thiết bị k hỗ trợ");
            } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                Log.e(TAG, "chưa thêm dấu vân tay ");
                // User hasn't enrolled any fingerprints to authenticate with
            } else {
                Log.e(TAG, "có thể sử dụng");
                // Everything is ready for fingerprint authentication
            }
            try {
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
                Cipher cipherNotInvalidated;
                try {
                    defaultCipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                            + KeyProperties.BLOCK_MODE_CBC + "/"
                            + KeyProperties.ENCRYPTION_PADDING_PKCS7);
                    cipherNotInvalidated = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                            + KeyProperties.BLOCK_MODE_CBC + "/"
                            + KeyProperties.ENCRYPTION_PADDING_PKCS7);
                } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
                    throw new RuntimeException("Failed to get an instance of Cipher", e);
                }

                initCipher(defaultCipher, DEFAULT_KEY_NAME);
                myFingerPrint.startListening(new FingerprintManager.CryptoObject(cipherNotInvalidated));
//                myFingerPrint.startListening(null);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
