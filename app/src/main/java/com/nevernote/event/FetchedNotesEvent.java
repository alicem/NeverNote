package com.nevernote.event;

import com.nevernote.db.model.NoteModel;

import java.util.List;

/**
 * Created by ali on 28.02.18.
 */

public class FetchedNotesEvent {


    private final boolean mSuccess;

    private final List<NoteModel> mNotes;

    public FetchedNotesEvent(boolean success, List<NoteModel> notes) {
        mSuccess = success;
        mNotes = notes;
    }

    public boolean isSuccess() {
        return mSuccess;
    }

    public List<NoteModel> getNotes() {
        return mNotes;
    }
}
