/*
 * Copyright (c) The Distance Agency Ltd 2016.
 */

package uk.co.thedistance.components.pocketseodemo;

import android.app.Application;
import android.content.Context;
import android.preference.PreferenceManager;

import uk.co.thedistance.components.pocketseodemo.injection.ApplicationComponent;
import uk.co.thedistance.components.pocketseodemo.injection.ApplicationModule;
import uk.co.thedistance.components.pocketseodemo.injection.DaggerApplicationComponent;
import uk.co.thedistance.components.pocketseodemo.injection.MozscapeModule;


public class PocketSeoApplication extends Application {

    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule())
                .mozscapeModule(new MozscapeModule())
                .build();
    }

    public static ApplicationComponent getApplicationComponent(Context context){
        return ((PocketSeoApplication) context.getApplicationContext()).mApplicationComponent;
    }
}
