package com.asterisk.noteapp.ui.notes

import androidx.lifecycle.ViewModel
import com.asterisk.noteapp.data.repository.NoteRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val noteRepo: NoteRepo
): ViewModel() {



}