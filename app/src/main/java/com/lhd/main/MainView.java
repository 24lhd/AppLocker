package com.lhd.main;

/**
 * Created by D on 8/8/2017.
 */

public interface MainView {

    public void startWellcomeFragment();
    public void startListAppFragment();
    public void startLogService();

    public void showPermisstionDrawOverApp();

    public void requestPermisstionDrawOverApp();

    public boolean isPermisstionDrawOverApp();

    public void hidePermisstionDrawOverApp();

    public void showPermisstionUsagerAccess();

    public void requestPermisstionUsagerAccess();

    public boolean isPermisstionUsagerAccess();

    public void hidePermisstionUsagerAccess();

    public void showPermisstionAccesiblity();

    public void requestPermisstionAccesiblity();

    public boolean isPermisstionAccesiblity();

    public void hidePermisstionAccesiblity();

    public void showAllPermisstion();

    public void hideAllPermisstion();

    public void startSetPinFragment();

    public void startSettingFragment();

    public void hideBar();

    public void showBar();

}
