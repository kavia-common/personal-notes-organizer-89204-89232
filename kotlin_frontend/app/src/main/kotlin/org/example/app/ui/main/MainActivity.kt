package org.example.app.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import org.example.app.R
import org.example.app.data.Note
import org.example.app.data.NoteRepository
import org.example.app.ui.edit.NoteEditActivity
import org.example.app.ui.main.adapter.NotesAdapter
import java.util.Locale

/**
 * PUBLIC_INTERFACE
 * MainActivity
 * This is the entry point that displays the list of notes with search and bottom navigation.
 * Users can create, edit, organize (favorites/categories), and delete notes via UI.
 */
class MainActivity : Activity(), NotesAdapter.NoteClickListener {

    private lateinit var repo: NoteRepository
    private lateinit var adapter: NotesAdapter
    private lateinit var recycler: RecyclerView
    private lateinit var emptyView: View
    private lateinit var searchInput: TextInputEditText
    private lateinit var bottomNav: BottomNavigationView

    private var currentFilter: Filter = Filter.ALL
    private var allNotes: List<Note> = emptyList()

    enum class Filter { ALL, FAVORITES, CATEGORIES }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        repo = NoteRepository(this)

        recycler = findViewById(R.id.notesRecycler)
        emptyView = findViewById(R.id.emptyView)
        searchInput = findViewById(R.id.searchInput)
        bottomNav = findViewById(R.id.bottomNav)

        adapter = NotesAdapter(mutableListOf(), this)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        findViewById<View>(R.id.fabAdd).setOnClickListener {
            val intent = Intent(this, NoteEditActivity::class.java)
            startActivity(intent)
        }

        bottomNav.setOnItemSelectedListener { item ->
            currentFilter = when (item.itemId) {
                R.id.nav_favorites -> Filter.FAVORITES
                R.id.nav_categories -> Filter.CATEGORIES
                else -> Filter.ALL
            }
            applyFilters()
            true
        }

        searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                applyFilters()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        loadNotes()
    }

    private fun loadNotes() {
        allNotes = repo.getAll()
        applyFilters()
    }

    private fun applyFilters() {
        val query = (searchInput.text?.toString() ?: "").lowercase(Locale.getDefault())
        val filtered = allNotes.filter { note ->
            val matchesQuery = if (query.isBlank()) true else {
                note.title.lowercase(Locale.getDefault()).contains(query) ||
                    note.content.lowercase(Locale.getDefault()).contains(query)
            }
            val matchesFilter = when (currentFilter) {
                Filter.ALL -> true
                Filter.FAVORITES -> note.favorite == 1
                Filter.CATEGORIES -> note.category?.isNotBlank() == true
            }
            matchesQuery && matchesFilter
        }
        adapter.updateData(filtered)
        emptyView.visibility = if (filtered.isEmpty()) View.VISIBLE else View.GONE
    }

    // PUBLIC_INTERFACE
    override fun onNoteClick(note: Note) {
        val intent = Intent(this, NoteEditActivity::class.java)
        intent.putExtra(NoteEditActivity.EXTRA_NOTE_ID, note.id)
        startActivity(intent)
    }

    // PUBLIC_INTERFACE
    override fun onFavoriteToggle(note: Note) {
        val toggled = note.copy(favorite = if (note.favorite == 1) 0 else 1)
        repo.update(toggled)
        loadNotes()
    }
}
