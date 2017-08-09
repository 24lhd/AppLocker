package com.lhd.main;

import android.content.Context;

import com.lhd.module.Config;
import com.lhd.module.MyLog;

/**
 * Created by D on 8/8/2017.
 */

public class MainModelImpl implements MainModel {
    @Override
    public boolean isStartWellcomeFragment(Context context) {
        return MyLog.getBooleanValueByName(context, Config.LOG_APP, Config.IS_WELLCOME);
    }

    @Override
    public void setStartWellcomeFragment(Context context, boolean value) {
        MyLog.putBooleanValueByName(context, Config.LOG_APP, Config.IS_WELLCOME,value);
    }
}
