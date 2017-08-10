package com.lhd.listapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lhd.applock.R;
import com.lhd.module.ItemApp;

import java.util.ArrayList;

/**
 * Created by D on 8/10/2017.
 */

public class ListAppFragment extends Fragment implements ListAppView {
    private ArrayList<ItemApp> itemApps;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewContent = inflater.inflate(R.layout.list_app_layout, null);
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public void loadListAppToList(ArrayList<ItemApp> itemApps) {
        this.itemApps = itemApps;
    }

    @Override
    public void refeshList() {

    }

    @Override
    public void startSetting() {

    }

    @Override
    public void sreachApp() {

    }
}
