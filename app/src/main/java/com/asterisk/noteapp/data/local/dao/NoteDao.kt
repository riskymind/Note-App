package com.asterisk.noteapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.asterisk.noteapp.data.local.models.LocalNote
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    // Insert note to the DB
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: LocalNote)

    // Get all Notes Order by Date in Descending order
    @Query("SELECT * FROM LocalNote WHERE locallyDeleted = 0 ORDER BY date DESC")
    fun getAllNoteOrderByDate(): Flow<List<LocalNote>>

    // Delete a specify note from the DB
    @Query("DELETE FROM LocalNote WHERE noteId = :noteId")
    suspend fun deleteNote(noteId: String)

    // Update local deleted note
    @Query("UPDATE LocalNote SET locallyDeleted = 1 WHERE noteId = :noteId")
    suspend fun deleteNoteLocally(noteId: String)

    // Get all Local note that's not pushed to server
    @Query("SELECT * FROM LocalNote WHERE connected = 0")
    fun getAllLocalNotes(): List<LocalNote>

    // Get all local deleted notes
    @Query("SELECT * FROM LocalNote WHERE locallyDeleted = 1")
    fun getAllLocalDeletedNotes(): List<LocalNote>

}