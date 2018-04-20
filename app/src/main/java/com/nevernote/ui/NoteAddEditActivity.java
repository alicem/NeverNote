package com.nevernote.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import com.nevernote.R;
import com.nevernote.job.controller.NoteController;
import com.nevernote.event.LoadNoteEvent;
import com.nevernote.event.NewNoteEvent;
import com.nevernote.event.SubscriberPriority;
import com.nevernote.event.UpdatedNoteEvent;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by ali on 01.03.18.
 */

public class NoteAddEditActivity extends BaseActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.noteEditText) EditText noteEditText;
    @BindView(R.id.noteImageView) ImageView noteImageView;
    @BindView(R.id.fab) FloatingActionButton fab;

    @Inject
    EventBus mEventBus;

    @Inject
    NoteController mNoteController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getComponent().inject(this);

        setContentView(R.layout.activity_note_add_edit);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
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
        onBackPressed();
    }

    public void onEventMainThread(NewNoteEvent event) {
        onBackPressed();
    }

    public void onEventMainThread(LoadNoteEvent event) {
        if (event.getNote() != null && getIntent().getLongExtra("id", 0) == event.getNote().getId()){
            if(TextUtils.isEmpty(String.valueOf(NoteAddEditActivity.this.noteEditText.getText()))){
                NoteAddEditActivity.this.noteEditText.setText(event.getNote().getText());
            }
            if( (noteImageView.getDrawable() == null) ){
                if (event.getNote().getImage() != null && event.getNote().getImage().getBlob() != null){
                    NoteAddEditActivity.this.noteImageView.setImageBitmap(BitmapFactory.decodeByteArray(event.getNote().getImage().getBlob(), 0, event.getNote().getImage().getBlob().length));
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 999 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                this.noteImageView.setImageBitmap(null);

                this.noteImageView.setImageBitmap(ThumbnailUtils.extractThumbnail(bitmap, 40, 48));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @OnClick(R.id.fab)
    public void onFabClicked() {
        if ( (TextUtils.isEmpty(noteEditText.getText())) && ( (noteImageView.getDrawable() == null) || ((BitmapDrawable)noteImageView.getDrawable()).getBitmap() == null) ){
            Snackbar.make(fab, "image or note is required", Snackbar.LENGTH_LONG).show();
            return;
        }
        byte[] image = null;
        if( !(noteImageView.getDrawable() == null) && !(((BitmapDrawable)noteImageView.getDrawable()).getBitmap() == null) ){
            Bitmap bitmap = ((BitmapDrawable)noteImageView.getDrawable()).getBitmap();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            image = bos.toByteArray();
        }

        if (getIntent().getLongExtra("id", 0) != 0){
            mNoteController.updateNoteAsync(String.valueOf(noteEditText.getText()), image, getIntent().getLongExtra("id", 0));
        } else {
            mNoteController.newNoteAsync(String.valueOf(noteEditText.getText()), image);
        }

    }

    @OnClick(R.id.noteImageView)
    public void noteImageViewClicked() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 999);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
