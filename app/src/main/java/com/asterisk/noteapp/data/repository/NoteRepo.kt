package com.asterisk.noteapp.data.repository

import com.asterisk.noteapp.data.local.models.LocalNote
import com.asterisk.noteapp.data.remote.models.User
import com.asterisk.noteapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface NoteRepo {
    suspend fun createUser(user: User): Resource<String>
    suspend fun login(user: User): Resource<String>
    suspend fun getUser(): Resource<User>
    suspend fun logout(): Resource<String>


    suspend fun createNote(note: LocalNote): Resource<String>
    suspend fun updateNote(note: LocalNote): Resource<String>

    fun getAllNotes(): Flow<List<LocalNote>>
    suspend fun getNotesFromServer()
}