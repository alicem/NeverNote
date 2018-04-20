package com.nevernote.event;

import com.nevernote.db.model.NoteModel;

/**
 * Created by ali on 02.03.18.
 */

public class LoadNoteEvent {

    private final NoteModel mNote;

    public LoadNoteEvent(NoteModel note) {
        mNote = note;
    }

    public NoteModel getNote() {
        return mNote;
    }


}
