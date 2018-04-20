package com.nevernote.job;

import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.nevernote.Config;
import com.nevernote.job.controller.NoteController;
import com.nevernote.db.DbConnector;
import com.nevernote.db.model.NoteModel;
import com.nevernote.di.component.ApplicationComponent;
import com.nevernote.event.DeleteNoteEvent;
import com.nevernote.event.FetchedNotesEvent;
import com.nevernote.network.ApiService;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

/**
 * Created by ali on 02.03.18.
 */

public class DeleteNoteJob extends BaseJob {

    private static final String GROUP = "DeleteNoteJob";

    private long mNoteId;

    @Inject
    transient ApiService mApiService;

    @Inject
    transient EventBus mEventBus;

    @Inject
    transient DbConnector mDbConnector;

    @Inject
    NoteController mNoteController;

    public DeleteNoteJob(long noteId) {
        super(new Params(BACKGROUND).groupBy(GROUP).requireNetwork().persist());
        mNoteId = noteId;
    }

    @Override
    public void inject(ApplicationComponent applicationComponent) {
        super.inject(applicationComponent);
        applicationComponent.inject(this);
    }

    @Override
    public void onAdded() {
        NoteModel note = mDbConnector.loadNote(mNoteId);

        note.setDeleted(true);
        note.setPending(true);
        note.update();
        mEventBus.post(new DeleteNoteEvent(mNoteId));

    }

    @Override
    public void onRun() throws Throwable {
        mNoteController.fetchNotesAsync(false);
    }

    @Override
    protected int getRetryLimit() {
        return Config.fetchNotesRetryLimit;
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(Throwable throwable, int runCount,
                                                     int maxRunCount) {
        if (shouldRetry(throwable)) {
            // back off 1000 ms.
            RetryConstraint constraint = RetryConstraint
                    .createExponentialBackoff(runCount, 1000);
            constraint.setApplyNewDelayToGroup(true);
            return constraint;
        }
        return RetryConstraint.CANCEL;
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
        mEventBus.post(new FetchedNotesEvent(false, null));
    }


}
