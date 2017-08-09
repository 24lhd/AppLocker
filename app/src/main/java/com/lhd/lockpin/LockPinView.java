package com.lhd.lockpin;

import android.content.Context;

/**
 * Created by D on 8/9/2017.
 */

public interface LockPinView {
    public void initView(Context context);

    public void pinError();

    public void passPin();

    public void showLock();
}
