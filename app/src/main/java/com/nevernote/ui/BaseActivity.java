package com.nevernote.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.birbit.android.jobqueue.TagConstraint;
import com.nevernote.NeverNoteApplication;
import com.nevernote.di.component.ActivityComponent;
import com.nevernote.di.component.DaggerActivityComponent;

import java.util.UUID;

/**
 * Created by ali on 01.03.18.
 */

public class BaseActivity extends AppCompatActivity {
    private ActivityComponent mComponent;
    private String mSessionId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mComponent = DaggerActivityComponent.builder()
                .applicationComponent(getApp().getApplicationComponent()).build();
    }
    protected NeverNoteApplication getApp() {
        return (NeverNoteApplication) getApplicationContext();
    }

    protected ActivityComponent getComponent() {
        return mComponent;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mSessionId = UUID.randomUUID().toString();
    }

    @Override
    protected void onStop() {
        super.onStop();
        getComponent().jobManager().cancelJobsInBackground(null, TagConstraint.ALL, mSessionId);
    }

}
