package com.nevernote.di.component;

import com.nevernote.di.scope.ActivityScope;
import com.nevernote.ui.NoteAddEditActivity;
import com.nevernote.ui.NoteDetailActivity;
import com.nevernote.ui.NoteListActivity;

import dagger.Component;

/**
 * Created by ali on 28.02.18.
 */

@ActivityScope
@Component(dependencies = ApplicationComponent.class)
public interface ActivityComponent extends ApplicationComponent{

    void inject(NoteListActivity noteListActivity);

    void inject(NoteDetailActivity noteDetailActivity);

    void inject(NoteAddEditActivity noteAddEditActivity);

}
