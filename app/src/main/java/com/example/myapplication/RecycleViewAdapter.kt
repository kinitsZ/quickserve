package com.example.myapplication
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
class NotesAdapter(
    private var notes: List<Note>,
    private val onEditClick: (Note) -> Unit,
    private val onDeleteClick: (Note) -> Unit
) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleText: TextView = itemView.findViewById(R.id.noteTitle)
        val descText: TextView = itemView.findViewById(R.id.noteDescription)
        val editButton: ImageButton = itemView.findViewById(R.id.btnEdit)
        val deleteButton: ImageButton = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.titleText.text = note.title
        holder.descText.text = note.description
        holder.editButton.setOnClickListener { onEditClick(note) }
        holder.deleteButton.setOnClickListener { onDeleteClick(note) }
    }

    override fun getItemCount(): Int = notes.size

    fun updateList(newNotes: List<Note>) {
        notes = newNotes
        notifyDataSetChanged()
    }
}