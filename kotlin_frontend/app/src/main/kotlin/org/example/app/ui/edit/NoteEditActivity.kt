package org.example.app.ui.edit

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import org.example.app.R
import org.example.app.data.Note
import org.example.app.data.NoteRepository

/**
 * PUBLIC_INTERFACE
 * NoteEditActivity
 * Screen to create or edit a note and save/delete it.
 * Intent extras:
 * - EXTRA_NOTE_ID (Long) optional: if present, loads existing note for editing
 */
class NoteEditActivity : Activity() {

    companion object {
        const val EXTRA_NOTE_ID = "extra_note_id"
    }

    private lateinit var repo: NoteRepository
    private var currentNote: Note? = null

    private lateinit var etTitle: TextInputEditText
    private lateinit var etContent: TextInputEditText
    private lateinit var btnSave: MaterialButton
    private lateinit var btnDelete: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_edit)

        repo = NoteRepository(this)

        etTitle = findViewById(R.id.etTitle)
        etContent = findViewById(R.id.etContent)
        btnSave = findViewById(R.id.btnSave)
        btnDelete = findViewById(R.id.btnDelete)

        val noteId = intent.getLongExtra(EXTRA_NOTE_ID, -1)
        if (noteId > 0) {
            currentNote = repo.getById(noteId)
            currentNote?.let {
                etTitle.setText(it.title)
                etContent.setText(it.content)
            }
        } else {
            btnDelete.isEnabled = false
        }

        btnSave.setOnClickListener {
            val title = etTitle.text?.toString()?.trim().orEmpty()
            val content = etContent.text?.toString()?.trim().orEmpty()
            if (title.isBlank() && content.isBlank()) {
                Toast.makeText(this, "Nothing to save", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (currentNote == null) {
                val created = Note(
                    id = 0L,
                    title = title.ifBlank { "Untitled" },
                    content = content,
                    favorite = 0,
                    category = null,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )
                val id = repo.insert(created)
                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                val updated = currentNote!!.copy(
                    title = title.ifBlank { "Untitled" },
                    content = content,
                    updatedAt = System.currentTimeMillis()
                )
                repo.update(updated)
                Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        btnDelete.setOnClickListener {
            currentNote?.let {
                repo.delete(it.id)
                Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
