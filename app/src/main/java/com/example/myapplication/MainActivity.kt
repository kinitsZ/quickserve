package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var adapter: NotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = NotesAdapter(listOf(), ::onEditNote, ::onDeleteNote)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        noteViewModel = ViewModelProvider(this)[NoteViewModel::class.java]

        // Observe notes LiveData
        noteViewModel.allNotes.observe(this) { notes ->
            adapter.updateList(notes)
        }

        // Handle Add button click
        binding.fabAddNote.setOnClickListener {
            val intent = Intent(this, AddEditNoteActivity::class.java)
            addEditNoteLauncher.launch(intent)
        }
    }

    private val addEditNoteLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            val id = data?.getIntExtra(AddEditNoteActivity.EXTRA_ID, -1) ?: -1
            val title = data?.getStringExtra(AddEditNoteActivity.EXTRA_TITLE) ?: ""
            val description = data?.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION) ?: ""

            if (title.isBlank()) return@registerForActivityResult // Ignore empty title

            val note = if (id != -1) {
                Note(id, title, description)
            } else {
                Note(title = title, description = description)
            }

            if (id != -1) {
                noteViewModel.update(note)
            } else {
                noteViewModel.insert(note)
            }
        }
    }

    private fun onEditNote(note: Note) {
        val intent = Intent(this, AddEditNoteActivity::class.java).apply {
            putExtra(AddEditNoteActivity.EXTRA_ID, note.id)
            putExtra(AddEditNoteActivity.EXTRA_TITLE, note.title)
            putExtra(AddEditNoteActivity.EXTRA_DESCRIPTION, note.description)
        }
        addEditNoteLauncher.launch(intent)
    }

    private fun onDeleteNote(note: Note) {
        noteViewModel.delete(note)
    }
}
