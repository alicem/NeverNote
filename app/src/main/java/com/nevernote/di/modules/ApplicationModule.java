package com.nevernote.di.modules;

import android.content.Context;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.config.Configuration;
import com.birbit.android.jobqueue.di.DependencyInjector;
import com.nevernote.NeverNoteApplication;
import com.nevernote.job.controller.NoteController;
import com.nevernote.db.DbConnector;
import com.nevernote.db.NeverNoteDatabase;
import com.nevernote.job.BaseJob;
import com.nevernote.util.DeviceUtil;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.greenrobot.event.EventBus;

/**
 * Created by ali on 28.02.18.
 */

@Module
public class ApplicationModule {

    private final NeverNoteApplication mApplication;

    public ApplicationModule(NeverNoteApplication application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    DbConnector postModel(DatabaseWrapper database) {
        return new DbConnector(mApplication, database);
    }

    @Provides
    @Singleton
    Context applicationContext() {
        return mApplication;
    }


    @Provides
    @Singleton
    NoteController noteController() {
        return new NoteController(mApplication.getApplicationComponent());
    }

    @Provides
    @Singleton
    DeviceUtil deviceUtil() {
        return new DeviceUtil(mApplication);
    }

    @Provides
    @Singleton
    EventBus eventBus() {
        return new EventBus();
    }

    @Provides
    @Singleton
    DatabaseWrapper database() {
        return FlowManager.getDatabase(NeverNoteDatabase.NAME).getWritableDatabase();
    }

    @Provides
    @Singleton
    JobManager jobManager() {
        Configuration config = new Configuration.Builder(mApplication)
                .consumerKeepAlive(45)
                .maxConsumerCount(3)
                .minConsumerCount(1)
                .injector(new DependencyInjector() {
                    @Override
                    public void inject(Job job) {
                        if (job instanceof BaseJob) {
                            ((BaseJob) job).inject(mApplication.getApplicationComponent());
                        }
                    }
                })
                .build();
        return new JobManager(config);
    }




}
