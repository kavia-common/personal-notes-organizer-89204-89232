package org.example.app.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * PUBLIC_INTERFACE
 * NotesDbHelper
 * SQLiteOpenHelper for managing notes database schema and upgrades.
 */
class NotesDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE $TABLE_NOTES (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_TITLE TEXT NOT NULL,
                $COL_CONTENT TEXT NOT NULL,
                $COL_FAVORITE INTEGER NOT NULL DEFAULT 0,
                $COL_CATEGORY TEXT,
                $COL_CREATED_AT INTEGER NOT NULL,
                $COL_UPDATED_AT INTEGER NOT NULL
            );
            """.trimIndent()
        )
        db.execSQL("CREATE INDEX idx_notes_updated ON $TABLE_NOTES($COL_UPDATED_AT DESC)")
        db.execSQL("CREATE INDEX idx_notes_favorite ON $TABLE_NOTES($COL_FAVORITE)")
        db.execSQL("CREATE INDEX idx_notes_category ON $TABLE_NOTES($COL_CATEGORY)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // For simplicity: drop and recreate
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NOTES")
        onCreate(db)
    }

    companion object {
        const val DATABASE_NAME = "notes.db"
        const val DATABASE_VERSION = 1

        const val TABLE_NOTES = "notes"
        const val COL_ID = "id"
        const val COL_TITLE = "title"
        const val COL_CONTENT = "content"
        const val COL_FAVORITE = "favorite"
        const val COL_CATEGORY = "category"
        const val COL_CREATED_AT = "created_at"
        const val COL_UPDATED_AT = "updated_at"
    }
}
