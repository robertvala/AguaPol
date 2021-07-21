package com.espol.aguapol;

import android.app.Application;
import android.content.Context;


import androidx.multidex.MultiDex;

import com.google.firebase.database.FirebaseDatabase;

public class AguaPol extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
