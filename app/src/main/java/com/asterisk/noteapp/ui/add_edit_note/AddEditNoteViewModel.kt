package com.asterisk.noteapp.ui.add_edit_note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asterisk.noteapp.data.local.models.LocalNote
import com.asterisk.noteapp.data.repository.NoteRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    val noteRepo: NoteRepo,
) : ViewModel() {

    var oldNote: LocalNote? = null

    // Create note
    fun createNote(
        title: String?,
        description: String?,
    ) = viewModelScope.launch(Dispatchers.IO) {
        val localNote = LocalNote(title, description)
        noteRepo.createNote(localNote)
    }

    // Update note
    fun updateNote(title: String?, description: String?) = viewModelScope.launch(Dispatchers.IO) {

        if (title == oldNote?.noteTitle && description == oldNote?.description && oldNote?.connected == true) {
            return@launch
        }

        val note = LocalNote(
            noteTitle = title,
            description = description,
            noteId = oldNote!!.noteId
        )

        noteRepo.updateNote(note)

    }

    fun milliToDate(time: Long): String {
        val date = Date(time)
        val simpleDateFormat = SimpleDateFormat("hh:mm a | MMM d, yyyy", Locale.getDefault())
        return simpleDateFormat.format(date)
    }
}