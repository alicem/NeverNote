package com.nevernote;

import android.annotation.SuppressLint;
import android.app.Application;

import com.nevernote.di.component.ApplicationComponent;
import com.nevernote.di.component.DaggerApplicationComponent;
import com.nevernote.di.modules.ApplicationModule;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by ali on 28.02.18.
 */

@SuppressLint("Registered")
public class NeverNoteApplication extends Application {

    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        FlowManager.init(new FlowConfig.Builder(this).build());

        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }

}
