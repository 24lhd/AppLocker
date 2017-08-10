package com.lhd.listapp;

import com.lhd.module.ItemApp;

import java.util.ArrayList;

/**
 * Created by D on 8/10/2017.
 */

public interface ListAppModel {
    public ArrayList<ItemApp> getListApp();
    public void saveState();
}
