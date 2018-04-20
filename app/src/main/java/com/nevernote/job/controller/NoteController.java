package com.nevernote.job.controller;

import android.content.Context;

import com.birbit.android.jobqueue.JobManager;
import com.nevernote.di.component.ApplicationComponent;
import com.nevernote.event.DeleteNoteEvent;
import com.nevernote.event.FetchedNotesEvent;
import com.nevernote.event.LoadNoteEvent;
import com.nevernote.event.NewNoteEvent;
import com.nevernote.event.SubscriberPriority;
import com.nevernote.event.UpdatedNoteEvent;
import com.nevernote.job.BaseJob;
import com.nevernote.job.DeleteNoteJob;
import com.nevernote.job.FetchNotesJob;
import com.nevernote.job.LoadNoteJob;
import com.nevernote.job.NewNoteJob;
import com.nevernote.job.UpdateNoteJob;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

/**
 * Created by ali on 28.02.18.
 */

public class NoteController {

    @Inject
    JobManager mJobManager;
    @Inject
    EventBus mEventBus;
    @Inject
    Context mApplicationContext;

    public NoteController(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
        mEventBus.register(this, SubscriberPriority.LOW);
    }

    public void onEventMainThread(DeleteNoteEvent event) {}
    public void onEventMainThread(FetchedNotesEvent event) {}
    public void onEventMainThread(NewNoteEvent event) {}
    public void onEventMainThread(UpdatedNoteEvent event) {}
    public void onEventMainThread(LoadNoteEvent event) {}


    public void fetchNotesAsync(boolean fromUI) {
        mJobManager.addJobInBackground(
                new FetchNotesJob(fromUI ? BaseJob.UI_HIGH : BaseJob.BACKGROUND));
    }

    public void newNoteAsync(String text, byte[] image) {
        mJobManager.addJobInBackground(new NewNoteJob(text, image));
    }

    public void updateNoteAsync(String text, byte[] image, long id) {
        mJobManager.addJobInBackground(new UpdateNoteJob(text, image, id));
    }

    public void loadNoteAsync(long id) {
        mJobManager.addJobInBackground(new LoadNoteJob(id));
    }

    public void deleteNoteAsync(long id) {
        mJobManager.addJobInBackground(new DeleteNoteJob(id));
    }
}