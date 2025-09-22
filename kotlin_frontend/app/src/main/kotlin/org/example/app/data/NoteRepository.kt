package org.example.app.data

import android.content.Context

/**
 * PUBLIC_INTERFACE
 * NoteRepository
 * Simple repository to abstract the DAO for UI layer.
 */
class NoteRepository(context: Context) {
    private val dao = NoteDao(context)

    // PUBLIC_INTERFACE
    fun getAll(): List<Note> = dao.getAll()

    // PUBLIC_INTERFACE
    fun getById(id: Long): Note? = dao.getById(id)

    // PUBLIC_INTERFACE
    fun insert(note: Note): Long = dao.insert(note)

    // PUBLIC_INTERFACE
    fun update(note: Note): Int = dao.update(note)

    // PUBLIC_INTERFACE
    fun delete(id: Long): Int = dao.delete(id)
}
