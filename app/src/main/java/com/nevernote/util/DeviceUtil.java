package com.nevernote.util;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.provider.Settings;

import com.nevernote.NeverNoteApplication;
import com.nevernote.di.component.ApplicationComponent;

import javax.inject.Inject;

/**
 * Created by ali on 28.02.18.
 */

public class DeviceUtil {

    private Context context;

    public DeviceUtil() {
    }

    public DeviceUtil(NeverNoteApplication mApplication) {
        context = mApplication;
    }

    @SuppressLint("HardwareIds")
    public String getDeviceUniqueId(){
        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }


}
