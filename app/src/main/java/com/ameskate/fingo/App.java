package com.ameskate.fingo;

import android.app.Application;

public class App extends Application{

    private static App mInstance;
    private DataManager mDataManager;

    public static App getInstance(){
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        mDataManager = new DataManager(this);
    }

    public DataManager getDataManager(){
        return mDataManager;
    }
}
