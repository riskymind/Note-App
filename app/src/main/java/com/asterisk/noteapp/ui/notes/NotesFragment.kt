package com.asterisk.noteapp.ui.notes

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.asterisk.noteapp.R
import com.asterisk.noteapp.databinding.FragmentNotesBinding
import com.asterisk.noteapp.ui.adapters.NoteAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
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
        setupSwipeLayout()

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

            ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(this)
        }
    }

    // subscribe to notes flow
    private fun subscribeToNotes() {
        lifecycleScope.launch {
            viewModel.notes.collect { notes ->
                noteAdapter.notes = notes.filter {
                    it.noteTitle?.contains(viewModel.searchQuery, true) == true ||
                            it.description?.contains(viewModel.searchQuery, true) == true
                }
            }
        }
    }

    // TouchHelper to delete note from recyclerview
    private val itemTouchHelperCallback =
        object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.layoutPosition
                val note = noteAdapter.notes[position]
                viewModel.deleteNote(note.noteId)
                Snackbar.make(requireView(), "Note Deleted Successfully", Snackbar.LENGTH_LONG)
                    .apply {
                        setAction("UNDO") { viewModel.undoDeleteNote(note) }
                        show()
                    }
            }

        }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.app_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)

        val item = menu.findItem(R.id.searchAction)
        val searchView = item.actionView as SearchView

        item.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                viewModel.searchQuery = ""
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                viewModel.searchQuery = ""
                return true
            }

        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    searchNote(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    searchNote(it)
                }
                return true
            }

        })

    }

    private fun searchNote(searchQuery: String) = lifecycleScope.launch {
        viewModel.searchQuery = searchQuery
        noteAdapter.notes = viewModel.notes.first().filter {
            it.noteTitle?.contains(searchQuery, true) == true ||
                    it.description?.contains(searchQuery, true) == true

        }
    }

    private fun setupSwipeLayout() {
        binding.swipe.setOnRefreshListener {
            viewModel.syncNotes {
                binding.swipe.isRefreshing = false
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return super.onOptionsItemSelected(item)
    }

}