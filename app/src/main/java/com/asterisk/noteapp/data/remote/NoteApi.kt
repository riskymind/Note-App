package com.asterisk.noteapp.data.remote

import com.asterisk.noteapp.data.remote.models.RemoteNote
import com.asterisk.noteapp.data.remote.models.SimpleResponse
import com.asterisk.noteapp.data.remote.models.User
import com.asterisk.noteapp.util.Constants.API_VERSION
import retrofit2.http.*

interface NoteApi {


    // Create Account service
    @Headers("Content-Type: application/json")
    @POST("$API_VERSION/users/register")
    suspend fun createAccount(
        @Body user: User,
    ): SimpleResponse

    // Login Account service
    @Headers("Content-Type: application/json")
    @POST("$API_VERSION/users/login")
    suspend fun loginAccount(
        @Body user: User,
    ): SimpleResponse

    // ====================NOTE=======================

    // Create note
    @Headers("Content-Type: application/json")
    @POST("$API_VERSION/notes/create")
    suspend fun createNote(
        @Header("Authorization") token: String,
        @Body note: RemoteNote,
    ): SimpleResponse


    // get all notes from server
    @Headers("Content-Type: application/json")
    @GET("$API_VERSION/notes")
    suspend fun getAllNote(
        @Header("Authorization") token: String,
    ): List<RemoteNote>

    // Update note
    @Headers("Content-Type: application/json")
    @POST("$API_VERSION/notes/update")
    suspend fun updateNote(
        @Header("Authorization") token: String,
        @Body note: RemoteNote,
    ): SimpleResponse

    // Create note
    @Headers("Content-Type: application/json")
    @DELETE("$API_VERSION/notes/delete")
    suspend fun deleteNote(
        @Header("Authorization") token: String,
        @Query("id") noteId: String
    ): SimpleResponse
}