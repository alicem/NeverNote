package com.nevernote.di.component;

import android.content.Context;

import com.birbit.android.jobqueue.JobManager;
import com.nevernote.job.controller.NoteController;
import com.nevernote.db.DbConnector;
import com.nevernote.di.modules.ApplicationModule;
import com.nevernote.job.DeleteNoteJob;
import com.nevernote.job.FetchNotesJob;
import com.nevernote.job.LoadNoteJob;
import com.nevernote.job.NewNoteJob;
import com.nevernote.job.UpdateNoteJob;
import com.nevernote.network.ApiModule;
import com.nevernote.network.ApiService;
import com.nevernote.util.DeviceUtil;

import javax.inject.Singleton;

import dagger.Component;
import de.greenrobot.event.EventBus;

/**
 * Created by ali on 28.02.18.
 */

@Singleton
@Component(modules = {ApplicationModule.class, ApiModule.class})
public interface ApplicationComponent {

    JobManager jobManager();

    DbConnector dbConnector();

    EventBus eventBus();

    ApiService apiService();

    NoteController noteController();

    Context applicationContext();

    DeviceUtil deviceUtil();

    void inject(NoteController noteController);

    void inject(DbConnector dbConnector);

    void inject(FetchNotesJob fetchNotesJob);

    void inject(NewNoteJob newNoteJob);

    void inject(UpdateNoteJob updateNoteJob);

    void inject(DeleteNoteJob deleteNoteJob);

    void inject(LoadNoteJob loadNoteJob);

}
