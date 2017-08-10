package com.lhd.module;

/**
 * Created by D on 8/10/2017.
 */

public class ItemApp {
    public int iconApp;
    public String namePackage;
    public String nameApp;
    public boolean isLock;

    @Override
    public String toString() {
        return "ItemApp{" +
                "iconApp=" + iconApp +
                ", namePackage='" + namePackage + '\'' +
                ", nameApp='" + nameApp + '\'' +
                ", isLock=" + isLock +
                '}';
    }

    public int getIconApp() {
        return iconApp;
    }

    public void setIconApp(int iconApp) {
        this.iconApp = iconApp;
    }

    public String getNamePackage() {
        return namePackage;
    }

    public void setNamePackage(String namePackage) {
        this.namePackage = namePackage;
    }

    public String getNameApp() {
        return nameApp;
    }

    public void setNameApp(String nameApp) {
        this.nameApp = nameApp;
    }

    public boolean isLock() {
        return isLock;
    }

    public void setLock(boolean lock) {
        isLock = lock;
    }

    public ItemApp(int iconApp, String namePackage, String nameApp, boolean isLock) {

        this.iconApp = iconApp;
        this.namePackage = namePackage;
        this.nameApp = nameApp;
        this.isLock = isLock;
    }
}
