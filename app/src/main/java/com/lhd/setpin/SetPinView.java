package com.lhd.setpin;

import android.view.View;

/**
 * Created by D on 8/9/2017.
 */

public interface SetPinView {
    public View initView(View view);

    public String getPinInput();

    public void showError(String s);

    public void pass();
}
