package com.nevernote.db;

import android.content.Context;

import com.nevernote.NeverNoteApplication;
import com.nevernote.db.model.NoteModel;
import com.nevernote.db.model.NoteModel_Table;
import com.nevernote.di.component.ApplicationComponent;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by ali on 28.02.18.
 */

public class DbConnector {

    protected final DatabaseWrapper mDatabaseWrapper;
    protected final ApplicationComponent mComponent;

    public DbConnector(NeverNoteApplication application, DatabaseWrapper databaseWrapper) {
        mComponent = application.getApplicationComponent();
        mDatabaseWrapper = databaseWrapper;
        application.getApplicationComponent().inject(this);
    }


    public List<NoteModel> loadAllNotes() {
        return new Select().from(NoteModel.class).queryList();
    }

    public NoteModel loadNote(long id) {
        return new Select().from(NoteModel.class).where(NoteModel_Table.id.eq(id)).querySingle();
    }

    public List<NoteModel> loadPendingNotes() {
        return new Select().from(NoteModel.class).where(NoteModel_Table.pending.eq(true)).queryList();
    }

   public List<NoteModel> loadActiveNotes() {
        return new Select().from(NoteModel.class).where(NoteModel_Table.deleted.eq(false)).queryList();
    }

   public List<NoteModel> loadDeletedNotes() {
        return new Select().from(NoteModel.class).where(NoteModel_Table.deleted.eq(true)).queryList();
    }

    public synchronized NoteModel save(NoteModel noteModel) {
        NoteModel existing = loadNote(noteModel.getId());
        if (existing == null) {
            noteModel.save();
        } else {
            noteModel.setId(existing.getId());
            noteModel.update();
        }
        return noteModel;
    }

    public void delete(NoteModel noteModel) {
        noteModel.delete();
    }



}
