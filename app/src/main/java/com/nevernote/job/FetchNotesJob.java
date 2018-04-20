package com.nevernote.job;

import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.nevernote.Config;
import com.nevernote.db.DbConnector;
import com.nevernote.db.model.NoteModel;
import com.nevernote.di.component.ApplicationComponent;
import com.nevernote.event.FetchedNotesEvent;
import com.nevernote.network.ApiService;
import com.nevernote.util.DeviceUtil;

import java.util.List;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ali on 28.02.18.
 */

public class FetchNotesJob extends BaseJob {

    private static final String GROUP = "FetchNotesJob";

    @Inject
    transient EventBus mEventBus;
    @Inject
    transient DbConnector mDbConnector;
    @Inject
    transient ApiService mApiService;
    @Inject
    DeviceUtil mDeviceUtil;


    public FetchNotesJob(@Priority int priority) {
        super(new Params(priority).addTags(GROUP).requireNetwork());
    }

    @Override
    public void inject(ApplicationComponent applicationComponent) {
        super.inject(applicationComponent);
        applicationComponent.inject(this);
    }

    @Override
    public void onAdded() {
        mEventBus.post(new FetchedNotesEvent(true, mDbConnector.loadActiveNotes()));
    }

    @Override
    public void onRun() throws Throwable {
        List<NoteModel>  allNotes = mDbConnector.loadAllNotes();

        if (allNotes.size() == 0) {
            mApiService.getNotes(mDeviceUtil.getDeviceUniqueId()).enqueue(new Callback<List<NoteModel>>() {
                @Override
                public void onResponse(Call<List<NoteModel>> call, Response<List<NoteModel>> response) {
                    if (response.isSuccessful()) {
                        handleResponseAndSaveAll(response.body());
                        mEventBus.post(new FetchedNotesEvent(true, response.body()));
                    } else {
                        throw new NetworkException(response.code());
                    }
                }

                @Override
                public void onFailure(Call<List<NoteModel>> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        } else {
            if (mDbConnector.loadPendingNotes().size() == 0){
                return;
            }

            List<NoteModel> allActiveNotes = mDbConnector.loadActiveNotes();
            for (NoteModel note : allActiveNotes){
                note.setPending(false);
            }

            mApiService.putNotes(allActiveNotes, mDeviceUtil.getDeviceUniqueId()).enqueue(new Callback<List<NoteModel>>() {
                @Override
                public void onResponse(Call<List<NoteModel>> call, Response<List<NoteModel>> response) {
                    if (response.isSuccessful()) {
                        handleResponseAndSaveAll(response.body());
                        mEventBus.post(new FetchedNotesEvent(true, response.body()));
                    } else {
                        throw new NetworkException(response.code());
                    }
                }

                @Override
                public void onFailure(Call<List<NoteModel>> call, Throwable t) {
                    t.printStackTrace();
                }
            });

        }

    }

    private void handleResponseAndSaveAll(List<NoteModel> notes) {
        List<NoteModel> allDeletdNotes = mDbConnector.loadDeletedNotes();
        for (NoteModel note : allDeletdNotes){
            note.delete();
        }

        if (notes != null) {
            for (NoteModel noteModel : notes) {
                noteModel.setPending(false);
                mDbConnector.save(noteModel);
            }
        }
    }

    @Override
    public RetryConstraint shouldReRunOnThrowable(Throwable throwable, int runCount,
                                                  int maxRunCount) {
        if (shouldRetry(throwable)) {
            return RetryConstraint.createExponentialBackoff(runCount, 1000);
        }
        return RetryConstraint.CANCEL;
    }

    @Override
    protected int getRetryLimit() {
        return Config.fetchNotesRetryLimit;
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
        mEventBus.post(new FetchedNotesEvent(false, null));
    }




}
