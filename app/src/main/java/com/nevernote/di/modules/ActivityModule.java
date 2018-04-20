package com.nevernote.di.modules;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ali on 28.02.18.
 */

@Module
public class ActivityModule {

    private final Activity mActivity;

    public ActivityModule(Activity activity) {
        mActivity = activity;
    }

    @Provides
    Context activityContext() {
        return mActivity;
    }

}
