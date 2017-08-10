package com.lhd.listapp;

import com.lhd.module.ItemApp;

import java.util.ArrayList;

/**
 * Created by D on 8/10/2017.
 */

public interface ListAppView {
    public void loadListAppToList(ArrayList<ItemApp> itemApps);


    public void refeshList();


    public void startSetting();

    public void sreachApp();

//    public void loadList();
}
