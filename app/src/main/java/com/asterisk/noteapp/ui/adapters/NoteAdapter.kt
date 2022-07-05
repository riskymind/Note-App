package com.asterisk.noteapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.asterisk.noteapp.R
import com.asterisk.noteapp.data.local.models.LocalNote
import com.asterisk.noteapp.databinding.NoteListItemBinding

class NoteAdapter : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            NoteListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.binding.apply {
            noteText.isVisible = note.noteTitle != null
            noteDescription.isVisible = note.description != null

            note.noteTitle?.let {
                noteText.text = it
            }

            note.description?.let {
                noteDescription.text = it
            }

            noteSync.setBackgroundColor(
                if (note.connected) R.drawable.sync else R.drawable.not_sync
            )

            root.setOnClickListener {
                onItemClickListener?.invoke(note)
            }
        }

    }

    override fun getItemCount(): Int {
        return notes.size
    }

    private var onItemClickListener: ((LocalNote) -> Unit)? = null
    fun setOnItemClickListener(listener: (LocalNote) -> Unit) {
        onItemClickListener = listener
    }

    private val diffUtil = object : DiffUtil.ItemCallback<LocalNote>() {
        override fun areItemsTheSame(oldItem: LocalNote, newItem: LocalNote): Boolean {
            return oldItem.noteId == newItem.noteId
        }

        override fun areContentsTheSame(oldItem: LocalNote, newItem: LocalNote): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, diffUtil)

    var notes: List<LocalNote>
        get() = differ.currentList
        set(value) = differ.submitList(value)


    inner class NoteViewHolder(val binding: NoteListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }
}