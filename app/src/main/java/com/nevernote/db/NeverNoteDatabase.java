package com.nevernote.db;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by ali on 28.02.18.
 */

@Database(name = NeverNoteDatabase.NAME, version = NeverNoteDatabase.VERSION)
public class NeverNoteDatabase {
    public static final String NAME = "nevernote";
    public static final int VERSION = 1;
}
