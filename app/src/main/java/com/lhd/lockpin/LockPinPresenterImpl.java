package com.lhd.lockpin;

import android.content.Context;

/**
 * Created by D on 8/9/2017.
 */

public class LockPinPresenterImpl implements LockPinPresenter {
    private Context context;

    public LockPinPresenterImpl(Context context) {

        this.context = context;
    }


    @Override
    public void checkPinCode(String pinCode) {

    }
}
