package com.asterisk.noteapp.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.asterisk.noteapp.data.local.dao.NoteDao
import com.asterisk.noteapp.data.local.models.LocalNote
import com.asterisk.noteapp.data.remote.NoteApi
import com.asterisk.noteapp.data.remote.models.RemoteNote
import com.asterisk.noteapp.data.remote.models.User
import com.asterisk.noteapp.util.SessionManager
import com.asterisk.noteapp.util.Resource
import com.asterisk.noteapp.util.isNetworkConnected
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.M)
class NoteRepoImpl @Inject constructor(
    val noteApi: NoteApi,
    val noteDao: NoteDao,
    val sessionManager: SessionManager,
) : NoteRepo {

    override suspend fun createUser(user: User): Resource<String> {
        return try {
            // check network connection
            if (!isNetworkConnected(sessionManager.context)) {
                Resource.Error<String>("No Internet Connection")
            }

            val result = noteApi.createAccount(user)
            if (result.success) {
                sessionManager.saveJwtToken(result.message, user.name ?: "", user.email ?: "")
                Resource.Success("User Created successfully")
            } else {
                Resource.Error(result.message)
            }

        } catch (e: Exception) {
            Resource.Error(e.message ?: "some problem occured!!")
        }
    }

    override suspend fun login(user: User): Resource<String> {
        return try {
            // check network connection
            if (!isNetworkConnected(sessionManager.context)) {
                Resource.Error<String>("No Internet Connection")
            }

            val result = noteApi.loginAccount(user)
            if (result.success) {
                sessionManager.saveJwtToken(result.message, user.name ?: "", user.email ?: "")
                getNotesFromServer()
                Resource.Success("Logged in successfully")
            } else {
                Resource.Error(result.message)
            }

        } catch (e: Exception) {
            Resource.Error(e.message ?: "some problem occured!!")
        }
    }

    override suspend fun getUser(): Resource<User> {
        return try {
            val name = sessionManager.getCurrentUserName()
            val email = sessionManager.getCurrentUserEmail()
            if (name == null || email == null) {
                Resource.Error<String>("User not logged in")
            }

            Resource.Success(User(name, email ?: "", ""))
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Some Problem Occurred!!")
        }
    }

    override suspend fun logout(): Resource<String> {
        return try {
            sessionManager.logout()
            Resource.Success("Logout User")
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Some Problem Occurred!!")
        }
    }


    override suspend fun createNote(note: LocalNote): Resource<String> {
        return try {
            if (!isNetworkConnected(sessionManager.context)) {
                noteDao.insertNote(note)
                Resource.Success("Noted Saved Locally")
            }

            val token = sessionManager.getJwtToken()

            val result = noteApi.createNote(
                "Bearer $token",
                RemoteNote(
                    noteTitle = note.noteTitle,
                    description = note.description,
                    date = note.date,
                    id = note.noteId
                )
            )

            if (result.success) {
                noteDao.insertNote(note.copy(connected = true))
                Resource.Success("Note Saved Successfully")
            } else {
                Resource.Error(result.message)
            }


        } catch (e: Exception) {
            Resource.Error(e.message ?: "some problem occurred!!")
        }
    }

    override suspend fun updateNote(note: LocalNote): Resource<String> {
        return try {

            if (!isNetworkConnected(sessionManager.context)) {
                noteDao.insertNote(note)
                Resource.Success("Noted Updated Locally")
            }

            val token = sessionManager.getJwtToken()

            val result = noteApi.updateNote(
                "Bearer $token",
                RemoteNote(noteTitle = note.noteTitle,
                    description = note.description,
                    date = note.date,
                    id = note.noteId)
            )

            if (result.success) {
                noteDao.insertNote(note.copy(connected = true))
                Resource.Success("Note Updated Successfully")
            } else {
                Resource.Error(result.message)
            }

        } catch (e: Exception) {
            Resource.Error(e.message ?: "some problem occurred!!")
        }
    }


    override fun getAllNotes(): Flow<List<LocalNote>> = noteDao.getAllNoteOrderByDate()


    override suspend fun getNotesFromServer() {
        try {
            val token = sessionManager.getJwtToken() ?: ""
            // checking for network connectivity
            if (!isNetworkConnected(sessionManager.context)) {
                return
            }

            val result = noteApi.getAllNote("Bearer $token")
            result.forEach { remoteNote ->
                noteDao.insertNote(
                    LocalNote(
                        noteTitle = remoteNote.noteTitle,
                        description = remoteNote.description,
                        date = remoteNote.date,
                        connected = true,
                        noteId = remoteNote.id
                    )
                )
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}