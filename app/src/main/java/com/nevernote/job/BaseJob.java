package com.nevernote.job;

import android.support.annotation.IntDef;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.nevernote.di.component.ApplicationComponent;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by ali on 28.02.18.
 */

public abstract class BaseJob extends Job {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({UI_HIGH, BACKGROUND})
    @interface Priority {

    }

    public static final int UI_HIGH = 10;
    public static final int BACKGROUND = 1;

    BaseJob(Params params) {
        super(params);
    }

    boolean shouldRetry(Throwable throwable) {
        if (throwable instanceof NetworkException) {
            NetworkException exception = (NetworkException) throwable;
            return exception.shouldRetry();
        }
        return true;
    }

    public void inject(ApplicationComponent applicationComponent) {}

}
