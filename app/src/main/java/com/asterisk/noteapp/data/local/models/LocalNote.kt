package com.asterisk.noteapp.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity
data class LocalNote(
    val noteTitle: String?,
    val description: String?,
    val date: Long = System.currentTimeMillis(),
    val connected: Boolean = false,
    val locallyDeleted: Boolean = false,
    @PrimaryKey(autoGenerate = false)
    val noteId: String = UUID.randomUUID().toString(),
) : Serializable
