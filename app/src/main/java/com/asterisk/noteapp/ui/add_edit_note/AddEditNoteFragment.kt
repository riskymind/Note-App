package com.asterisk.noteapp.ui.add_edit_note

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.asterisk.noteapp.R
import com.asterisk.noteapp.databinding.FragmentAddEditNoteBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddEditNoteFragment : Fragment(R.layout.fragment_add_edit_note) {

    private var _binding: FragmentAddEditNoteBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<AddEditNoteViewModel>()
    private val args by navArgs<AddEditNoteFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddEditNoteBinding.bind(view)

        viewModel.oldNote = args.note

        viewModel.oldNote?.noteTitle?.let {
            binding.etTitle.setText(it)
        }

        viewModel.oldNote?.description?.let {
            binding.desc.setText(it)
        }

        binding.date.isVisible = viewModel.oldNote != null
        viewModel.oldNote?.date?.let {
            binding.date.text = viewModel.milliToDate(it)
        }

//        binding.fabSave.setOnClickListener {
//            noteOperation()
//        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onPause() {
        super.onPause()
        noteOperation()
    }

    private fun noteOperation() {
        if (viewModel.oldNote == null) {
            createNote()
        } else {
            updateNote()
        }
    }


    private fun createNote() {
        val title = binding.etTitle.text.toString().trim()
        val desc = binding.desc.text.toString().trim()

        if (title.isEmpty() || desc.isEmpty()) {
            Toast.makeText(requireContext(), "Note is empty", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.createNote(title, desc)

    }

    private fun updateNote() {
        val title = binding.etTitle.text.toString().trim()
        val desc = binding.desc.text.toString().trim()

        if (title.isNullOrEmpty() || desc.isNullOrEmpty()) {
            // todos later
            return
        }

        viewModel.updateNote(title, desc)

    }
}