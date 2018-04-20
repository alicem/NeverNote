package com.nevernote.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.nevernote.R;
import com.nevernote.job.controller.NoteController;
import com.nevernote.db.model.NoteModel;
import com.nevernote.event.FetchedNotesEvent;
import com.nevernote.event.SubscriberPriority;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * An activity representing a list of Notes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link NoteDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class NoteListActivity extends BaseActivity {

    @BindView(R.id.emptyView) TextView emptyView;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.note_list) RecyclerView recyclerView;

    @Inject
    EventBus mEventBus;

    @Inject
    NoteController mNoteController;

    private List<NoteModel> notes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent().inject(this);

        setContentView(R.layout.activity_note_list);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        setupRecyclerView(recyclerView);
    }



    @Override
    protected void onStart() {
        super.onStart();
        mEventBus.register(this, SubscriberPriority.HIGH);
        mNoteController.fetchNotesAsync(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mEventBus.unregister(this);
    }

    public void onEventMainThread(FetchedNotesEvent event) {
        if(event.isSuccess()){
            if (event.getNotes() == null){
                return;
            }
            this.notes = event.getNotes();
            Collections.reverse(this.notes);
            this.recyclerView.getAdapter().notifyDataSetChanged();
            if (notes.size() > 0) {
                emptyView.setVisibility(View.GONE);
            } else {
                emptyView.setVisibility(View.VISIBLE);
            }
        }
    }

    @OnClick(R.id.fab)
    public void onFabClicked() {
        Intent intent = new Intent(NoteListActivity.this, NoteAddEditActivity.class);
        NoteListActivity.this.startActivity(intent);
    }



    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter());
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, NoteDetailActivity.class);
                intent.putExtra("id", NoteListActivity.this.notes.get((Integer) view.getTag()).getId());
                context.startActivity(intent);
            }
        };

        SimpleItemRecyclerViewAdapter() {
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.note_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            NoteModel currentNoteModel = NoteListActivity.this.notes.get(position);
            if (currentNoteModel.isPending()){
                holder.mLoading.setVisibility(View.VISIBLE);
            } else {
                holder.mLoading.setVisibility(View.GONE);
            }

            if (currentNoteModel.getImage() != null && currentNoteModel.getImage().getBlob() != null){
                holder.mImage.setImageBitmap(BitmapFactory.decodeByteArray(currentNoteModel.getImage().getBlob(), 0, currentNoteModel.getImage().getBlob().length));
            } else {
                holder.mImage.setImageBitmap(null);
            }
            holder.mContentView.setText(currentNoteModel.getText());

            holder.itemView.setTag(position);
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return NoteListActivity.this.notes.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final ImageView mImage;
            final TextView mContentView;
            final ProgressBar mLoading;

            ViewHolder(View view) {
                super(view);
                mImage = view.findViewById(R.id.image);
                mContentView = view.findViewById(R.id.content);
                mLoading = view.findViewById(R.id.loading);
            }
        }
    }
}
