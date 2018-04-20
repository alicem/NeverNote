package com.nevernote.event;

import com.nevernote.db.model.NoteModel;

/**
 * Created by ali on 28.02.18.
 */

public class NewNoteEvent {

    private final NoteModel mNote;

    public NewNoteEvent(NoteModel note) {
        mNote = note;
    }

    public NoteModel getNote() {
        return mNote;
    }

}
