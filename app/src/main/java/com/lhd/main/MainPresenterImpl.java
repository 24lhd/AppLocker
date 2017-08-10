package com.lhd.main;

import android.util.Log;

import com.lhd.module.RequestPermisstion;

/**
 * Created by D on 8/8/2017.
 */

public class MainPresenterImpl implements MainPresenter {


    private MainActivity mainActivity;
    MainModel mainModel;

    public MainPresenterImpl(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        mainModel = new MainModelImpl();
        mainActivity.startLogService();
        checkPermisstionUsagerAccess();
    }

    @Override
    public void checkStartWellcomeFragment() {
        mainModel.setStartWellcomeFragment(mainActivity, false);
        boolean isStart = mainModel.isStartWellcomeFragment(mainActivity);
        if (isStart) {
            mainActivity.showBar();
            mainActivity.startSetPinFragment();
        } else {
            mainActivity.hideBar();
            mainActivity.startWellcomeFragment();
            mainModel.setStartWellcomeFragment(mainActivity, true);
        }
    }

    @Override
    public void checkPermisstionUsagerAccess() {
        Log.e("duongq", "" + RequestPermisstion.isAccessGranted(mainActivity));
        if (RequestPermisstion.isAccessGranted(mainActivity) == false) {
            mainActivity.requestPermisstionUsagerAccess();
        }
    }
}
