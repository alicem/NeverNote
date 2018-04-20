package com.nevernote.ui;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.nevernote.R;
import com.nevernote.job.controller.NoteController;
import com.nevernote.db.DbConnector;
import com.nevernote.event.DeleteNoteEvent;
import com.nevernote.event.LoadNoteEvent;
import com.nevernote.event.SubscriberPriority;
import com.nevernote.event.UpdatedNoteEvent;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * An activity representing a single Note detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link NoteListActivity}.
 */
public class NoteDetailActivity extends BaseActivity {

    @BindView(R.id.detailImage) ImageView detailImage;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.noteDetailTextView) TextView noteDetailTextView;
    @BindView(R.id.toolbar_layout) CollapsingToolbarLayout appBarLayout;

    @Inject
    EventBus mEventBus;

    @Inject
    DbConnector mDbConnector;

    @Inject
    NoteController mNoteController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent().inject(this);

        setContentView(R.layout.activity_note_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (appBarLayout != null) {
            appBarLayout.setTitle(" ");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mEventBus.register(this, SubscriberPriority.HIGH);
        mNoteController.loadNoteAsync(getIntent().getLongExtra("id", 0));
    }

    @Override
    protected void onStop() {
        super.onStop();
        mEventBus.unregister(this);
    }


    public void onEventMainThread(UpdatedNoteEvent event) {
        if (getIntent().getLongExtra("id", 0) == event.getNote().getId()){
            NoteDetailActivity.this.noteDetailTextView.setText(event.getNote().getText());
            if (event.getNote().getImage() != null && event.getNote().getImage().getBlob() != null){
                NoteDetailActivity.this.detailImage.setImageBitmap(BitmapFactory.decodeByteArray(event.getNote().getImage().getBlob(), 0, event.getNote().getImage().getBlob().length));
            }
        }
    }

    public void onEventMainThread(LoadNoteEvent event) {
        if (getIntent().getLongExtra("id", 0) == event.getNote().getId()){
            NoteDetailActivity.this.noteDetailTextView.setText(event.getNote().getText());
            if (event.getNote().getImage() != null && event.getNote().getImage().getBlob() != null){
                NoteDetailActivity.this.detailImage.setImageBitmap(BitmapFactory.decodeByteArray(event.getNote().getImage().getBlob(), 0, event.getNote().getImage().getBlob().length));
            }
        }
    }

    public void onEventMainThread(DeleteNoteEvent event) {
        if (getIntent().getLongExtra("id", 0) == event.getmNoteId()){
            onBackPressed();
        }
    }


    @OnClick(R.id.fab)
    public void onFabClicked() {
        Intent intent = new Intent(this, NoteAddEditActivity.class);
        intent.putExtra("id", getIntent().getLongExtra("id", 0));
        startActivity(intent);
    }

    @OnClick(R.id.fabDelete)
    public void onfabDeleteClicked() {
        mNoteController.deleteNoteAsync(getIntent().getLongExtra("id", 0));
    }

    @OnClick(R.id.fabShare)
    public void onfabShareClicked() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, noteDetailTextView.getText().toString());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            navigateUpTo(new Intent(this, NoteListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
