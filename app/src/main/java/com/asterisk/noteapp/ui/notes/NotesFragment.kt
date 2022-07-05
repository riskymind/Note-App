package com.asterisk.noteapp.ui.notes

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.asterisk.noteapp.R
import com.asterisk.noteapp.databinding.FragmentNotesBinding
import com.asterisk.noteapp.ui.adapters.NoteAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotesFragment : Fragment(R.layout.fragment_notes) {

    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<NoteViewModel>()
    private lateinit var noteAdapter: NoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNotesBinding.bind(view)

        // setup tool bar
        (activity as AppCompatActivity).setSupportActionBar(binding.customToolBar)

        setupRecyclerView()
        subscribeToNotes()

        binding.fabAdd.setOnClickListener {
            val action = NotesFragmentDirections.actionNotesFragmentToAddEditNoteFragment()
            findNavController().navigate(action)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Setup Recyclerview
    private fun setupRecyclerView() {
        noteAdapter = NoteAdapter()
        noteAdapter.setOnItemClickListener {
            val action = NotesFragmentDirections.actionNotesFragmentToAddEditNoteFragment(it)
            findNavController().navigate(action)
        }
        binding.rvNotes.apply {
            adapter = noteAdapter
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }
    }

    // subscribe to notes flow
    private fun subscribeToNotes() {
        lifecycleScope.launch {
            viewModel.notes.collect { notes ->
                noteAdapter.notes = notes
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.app_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
//            R.id.searchAction -> {}
        }

        return super.onOptionsItemSelected(item)
    }

}