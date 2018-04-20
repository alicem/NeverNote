package com.nevernote.job;

import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.nevernote.db.DbConnector;
import com.nevernote.di.component.ApplicationComponent;
import com.nevernote.event.LoadNoteEvent;
import com.nevernote.network.ApiService;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

/**
 * Created by ali on 02.03.18.
 */

public class LoadNoteJob extends BaseJob {

    private static final String GROUP = "LoadNoteJob";

    private long mNoteId;

    @Inject
    transient EventBus mEventBus;
    @Inject
    transient DbConnector mDbConnector;
    @Inject
    transient ApiService mApiService;

    public LoadNoteJob(long id) {
        super(new Params(BACKGROUND).groupBy(GROUP).requireNetwork().persist());
        this.mNoteId = id;
    }

    @Override
    public void inject(ApplicationComponent applicationComponent) {
        super.inject(applicationComponent);
        applicationComponent.inject(this);
    }

    @Override
    public void onAdded() {
        mEventBus.post(new LoadNoteEvent(mDbConnector.loadNote(mNoteId)));
    }

    @Override
    public void onRun() throws Throwable {
    }

    @Override
    public RetryConstraint shouldReRunOnThrowable(Throwable throwable, int runCount,
                                                  int maxRunCount) {
        return RetryConstraint.CANCEL;
    }

    @Override
    protected int getRetryLimit() {
        return 1;
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
    }




}
