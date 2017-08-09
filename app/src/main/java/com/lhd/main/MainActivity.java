package com.lhd.main;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.lhd.applock.R;
import com.lhd.setpin.SetPinFragment;
import com.lhd.toprunapp.StateDeviceService;
import com.lhd.wellcome.WellcomeFragment;

/**
 * Created by D on 8/8/2017.
 */

public class MainActivity extends AppCompatActivity implements MainView {
    private static final String TAG = "MainActivity";
    private MainPresenter mainPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        mainPresenter = new MainPresenterImpl(this);
        initView();
    }

    private LinearLayout itemNotiDrawOverApp;
    private LinearLayout itemNotiDrawUsagerAccess;
    private LinearLayout itemNotiDrawAccessibility;

    private void initView() {
        itemNotiDrawOverApp = (LinearLayout) findViewById(R.id.main_id_permisstion_draw_over);
        itemNotiDrawUsagerAccess = (LinearLayout) findViewById(R.id.main_id_permisstion_usager_access);
        itemNotiDrawAccessibility = (LinearLayout) findViewById(R.id.main_id_permisstion_accesssibility);
        mainPresenter.checkStartWellcomeFragment();
    }

    @Override
    public void startWellcomeFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_view, new WellcomeFragment()).commit();
    }

    @Override
    public void startLogService() {
        Log.e(TAG, "Servide đang chạy " + StateDeviceService.isRunning(this));
        if (!StateDeviceService.isRunning(this)) {
            startService(new Intent(this, StateDeviceService.class));
        }
    }

    @Override
    public void showPermisstionDrawOverApp() {
        itemNotiDrawOverApp.setVisibility(View.VISIBLE);
    }

    @Override
    public void requestPermisstionDrawOverApp() {

    }

    @Override
    public boolean isPermisstionDrawOverApp() {
        return false;
    }

    @Override
    public void hidePermisstionDrawOverApp() {
        itemNotiDrawOverApp.setVisibility(View.GONE);
    }

    @Override
    public void showPermisstionUsagerAccess() {
        itemNotiDrawOverApp.setVisibility(View.VISIBLE);
    }



    @Override
    public void requestPermisstionUsagerAccess() {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
    }

    @Override
    public boolean isPermisstionUsagerAccess() {
        return false;
    }

    @Override
    public void hidePermisstionUsagerAccess() {
        itemNotiDrawUsagerAccess.setVisibility(View.GONE);
    }

    @Override
    public void showPermisstionAccesiblity() {
        itemNotiDrawAccessibility.setVisibility(View.VISIBLE);
    }

    @Override
    public void requestPermisstionAccesiblity() {

    }

    @Override
    public boolean isPermisstionAccesiblity() {
        return false;
    }

    @Override
    public void hidePermisstionAccesiblity() {
        itemNotiDrawAccessibility.setVisibility(View.GONE);
    }

    @Override
    public void showAllPermisstion() {
        itemNotiDrawAccessibility.setVisibility(View.VISIBLE);
        itemNotiDrawUsagerAccess.setVisibility(View.VISIBLE);
        itemNotiDrawOverApp.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideAllPermisstion() {
        itemNotiDrawAccessibility.setVisibility(View.GONE);
        itemNotiDrawUsagerAccess.setVisibility(View.GONE);
        itemNotiDrawOverApp.setVisibility(View.GONE);
    }

    @Override
    public void startSetPinFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_view, new SetPinFragment()).commit();
    }

    @Override
    public void startSettingFragment() {
        Toast.makeText(this, "startSettingFragment", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideBar() {
        getSupportActionBar().hide();
    }

    @Override
    public void showBar() {
        getSupportActionBar().show();
    }
}
