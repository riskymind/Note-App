package com.asterisk.noteapp.ui.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asterisk.noteapp.data.local.models.LocalNote
import com.asterisk.noteapp.data.repository.NoteRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val noteRepo: NoteRepo,
) : ViewModel() {


    val notes = noteRepo.getAllNotes()

    var searchQuery: String = ""

    fun deleteNote(noteId: String) = viewModelScope.launch(Dispatchers.IO) {
        noteRepo.deleteNote(noteId)
    }

    fun undoDeleteNote(note: LocalNote) = viewModelScope.launch(Dispatchers.IO) {
        noteRepo.createNote(note)
    }

    fun syncNotes(
        done: (() -> Unit)? = null
    )  = viewModelScope.launch{
        noteRepo.syncNotes()
        done?.invoke()
    }


}