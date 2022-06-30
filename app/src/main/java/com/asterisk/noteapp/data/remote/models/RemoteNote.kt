package com.asterisk.noteapp.data.remote.models

import androidx.room.PrimaryKey
import java.util.*

data class RemoteNote(
    val noteTitle: String?,
    val description: String?,
    val date: Long,
    val noteId: String,
)
