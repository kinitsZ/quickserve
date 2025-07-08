package com.example.myapplication

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityAddEditNoteBinding

class AddEditNoteActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ID = "com.example.myapplication.EXTRA_ID"
        const val EXTRA_TITLE = "com.example.myapplication.EXTRA_TITLE"
        const val EXTRA_DESCRIPTION = "com.example.myapplication.EXTRA_DESCRIPTION"
    }

    private lateinit var binding: ActivityAddEditNoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Check if editing existing note
        val intent = intent
        if (intent.hasExtra(EXTRA_ID)) {
            title = "Edit Note"
            binding.editTitle.setText(intent.getStringExtra(EXTRA_TITLE))
            binding.editDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION))
        } else {
            title = "Add Note"
        }

        binding.buttonSave.setOnClickListener {
            saveNote()
        }
    }

    private fun saveNote() {
        val title = binding.editTitle.text.toString().trim()
        val description = binding.editDescription.text.toString().trim()

        if (title.isEmpty()) {
            binding.editTitle.error = "Title cannot be empty"
            return
        }

        val data = intent.apply {
            putExtra(EXTRA_TITLE, title)
            putExtra(EXTRA_DESCRIPTION, description)
        }

        val id = intent.getIntExtra(EXTRA_ID, -1)
        if (id != -1) {
            data.putExtra(EXTRA_ID, id)
        }

        setResult(Activity.RESULT_OK, data)
        finish()
    }
}
