package com.lhd.lockpin;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.lhd.applock.R;

import static android.content.Context.WINDOW_SERVICE;
import static android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN;
import static android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;

/**
 * Created by D on 8/9/2017.
 */

public class LockPinViewImpl implements LockPinView, View.OnClickListener {
    private WindowManager windowManager;
    private View view;
    WindowManager.LayoutParams params;
    private LockPinPresenter lockPinPresenter;
    @Override
    public void initView(Context context) {
        view = View.inflate(context, R.layout.window_lock_pin, null);
        lockPinPresenter=new LockPinPresenterImpl(context);
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                +FLAG_NOT_TOUCH_MODAL
                        + WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                        + FLAG_FULLSCREEN + WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.CENTER;
        windowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);

    }

    @Override
    public void pinError() {

    }

    @Override
    public void passPin() {

    }

    @Override
    public void showLock() {
        windowManager.addView(view, params);
    }


    @Override
    public void onClick(View view) {
        String pin = (String) view.getTag();
        Log.e("onClick", pin);
    }
}
