package org.example.app.data

/**
 * PUBLIC_INTERFACE
 * Note
 * Data model for a personal note record.
 */
data class Note(
    val id: Long,
    val title: String,
    val content: String,
    val favorite: Int,
    val category: String?,
    val createdAt: Long,
    val updatedAt: Long
)
