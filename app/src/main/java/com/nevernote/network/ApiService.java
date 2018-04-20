package com.nevernote.network;

import com.nevernote.db.model.NoteModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by ali on 28.02.18.
 */

public interface ApiService {

    @GET("{device_unique}.json")
    Call<List<NoteModel>> getNotes(@Path("device_unique") String device_unique);

    @PUT("{device_unique}.json")
    Call<List<NoteModel>> putNotes(@Body List<NoteModel> notes, @Path("device_unique") String device_unique);

//    @POST("update_note")
//    Call<Void> sendUpdateNote(@Body NoteModel noteModel);
//
//    @POST("new_note")
//    Call<Void> sendNewNote(@Body NoteModel noteModel);
//
//    @POST("pending_notes")
//    Call<Void> sendPendingNote(@Body List<NoteModel> noteModels);

}
