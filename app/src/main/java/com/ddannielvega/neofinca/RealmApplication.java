package com.ddannielvega.neofinca;

import android.app.Application;


import io.realm.Realm;
import io.realm.RealmConfiguration;

public class RealmApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .schemaVersion(1)
                .name("myrealm.realm")
                .build();
        Realm.setDefaultConfiguration(config);
    }
}
