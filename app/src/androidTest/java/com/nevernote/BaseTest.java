package com.nevernote;

import android.support.test.InstrumentationRegistry;
import android.test.AndroidTestCase;

import com.nevernote.di.component.ApplicationComponent;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by ali on 15.03.18.
 */

public class BaseTest extends AndroidTestCase {
    private ApplicationComponent mTestApplicationComponent;

    protected ApplicationComponent getTestComponent() {
        return mTestApplicationComponent;
    }

    @Before
    public final void setupBaseTest() throws Exception {
        super.setUp();
        NeverNoteApplication app = (NeverNoteApplication) InstrumentationRegistry.getTargetContext().getApplicationContext();
        mTestApplicationComponent = app.getApplicationComponent();

    }
}

