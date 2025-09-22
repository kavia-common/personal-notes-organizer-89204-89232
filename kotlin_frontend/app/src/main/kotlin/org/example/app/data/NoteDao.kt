package org.example.app.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import org.example.app.data.NotesDbHelper.Companion.COL_CATEGORY
import org.example.app.data.NotesDbHelper.Companion.COL_CONTENT
import org.example.app.data.NotesDbHelper.Companion.COL_CREATED_AT
import org.example.app.data.NotesDbHelper.Companion.COL_FAVORITE
import org.example.app.data.NotesDbHelper.Companion.COL_ID
import org.example.app.data.NotesDbHelper.Companion.COL_TITLE
import org.example.app.data.NotesDbHelper.Companion.COL_UPDATED_AT
import org.example.app.data.NotesDbHelper.Companion.TABLE_NOTES

/**
 * PUBLIC_INTERFACE
 * NoteDao
 * Provides CRUD operations using the SQLite database.
 */
class NoteDao(context: Context) {
    private val dbHelper = NotesDbHelper(context)

    // PUBLIC_INTERFACE
    fun getAll(): List<Note> {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            TABLE_NOTES,
            null,
            null,
            null,
            null,
            null,
            "$COL_UPDATED_AT DESC"
        )
        return cursor.use { mapCursor(it) }
    }

    // PUBLIC_INTERFACE
    fun getById(id: Long): Note? {
        val db = dbHelper.readableDatabase
        val c = db.query(
            TABLE_NOTES, null,
            "$COL_ID=?",
            arrayOf(id.toString()), null, null, null
        )
        c.use {
            if (it.moveToFirst()) {
                return mapRow(it)
            }
        }
        return null
    }

    // PUBLIC_INTERFACE
    fun insert(note: Note): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(COL_TITLE, note.title)
            put(COL_CONTENT, note.content)
            put(COL_FAVORITE, note.favorite)
            put(COL_CATEGORY, note.category)
            put(COL_CREATED_AT, note.createdAt)
            put(COL_UPDATED_AT, note.updatedAt)
        }
        return db.insert(TABLE_NOTES, null, values)
    }

    // PUBLIC_INTERFACE
    fun update(note: Note): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(COL_TITLE, note.title)
            put(COL_CONTENT, note.content)
            put(COL_FAVORITE, note.favorite)
            put(COL_CATEGORY, note.category)
            put(COL_UPDATED_AT, note.updatedAt)
        }
        return db.update(TABLE_NOTES, values, "$COL_ID=?", arrayOf(note.id.toString()))
    }

    // PUBLIC_INTERFACE
    fun delete(id: Long): Int {
        val db = dbHelper.writableDatabase
        return db.delete(TABLE_NOTES, "$COL_ID=?", arrayOf(id.toString()))
    }

    private fun mapCursor(cursor: Cursor): List<Note> {
        val list = mutableListOf<Note>()
        if (cursor.moveToFirst()) {
            do {
                list.add(mapRow(cursor))
            } while (cursor.moveToNext())
        }
        return list
    }

    private fun mapRow(c: Cursor): Note {
        val id = c.getLong(c.getColumnIndexOrThrow(COL_ID))
        val title = c.getString(c.getColumnIndexOrThrow(COL_TITLE))
        val content = c.getString(c.getColumnIndexOrThrow(COL_CONTENT))
        val favorite = c.getInt(c.getColumnIndexOrThrow(COL_FAVORITE))
        val category =
            if (c.isNull(c.getColumnIndexOrThrow(COL_CATEGORY))) null else c.getString(c.getColumnIndexOrThrow(COL_CATEGORY))
        val createdAt = c.getLong(c.getColumnIndexOrThrow(COL_CREATED_AT))
        val updatedAt = c.getLong(c.getColumnIndexOrThrow(COL_UPDATED_AT))
        return Note(id, title, content, favorite, category, createdAt, updatedAt)
    }
}
