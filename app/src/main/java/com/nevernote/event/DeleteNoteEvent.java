package com.nevernote.event;

/**
 * Created by ali on 28.02.18.
 */

public class DeleteNoteEvent {

    private long mNoteId;

    public DeleteNoteEvent(long noteId) {
        mNoteId = noteId;
    }

    public long getmNoteId() {
        return mNoteId;
    }
}
