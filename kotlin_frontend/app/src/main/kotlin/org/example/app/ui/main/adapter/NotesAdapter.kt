package org.example.app.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.example.app.R
import org.example.app.data.Note
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * PUBLIC_INTERFACE
 * NotesAdapter
 * Adapter to render a list of notes with a minimalist card UI.
 */
class NotesAdapter(
    private val items: MutableList<Note>,
    private val listener: NoteClickListener
) : RecyclerView.Adapter<NotesAdapter.VH>() {

    interface NoteClickListener {
        // PUBLIC_INTERFACE
        fun onNoteClick(note: Note)
        // PUBLIC_INTERFACE
        fun onFavoriteToggle(note: Note)
    }

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val title: TextView = v.findViewById(R.id.tvTitle)
        val content: TextView = v.findViewById(R.id.tvContent)
        val timestamp: TextView = v.findViewById(R.id.tvTimestamp)
        val favorite: ImageView = v.findViewById(R.id.ivFavorite)
    }

    private val formatter = SimpleDateFormat("MMM d, yyyy h:mm a", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return VH(v)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val note = items[position]
        holder.title.text = note.title
        holder.content.text = note.content
        holder.timestamp.text = holder.itemView.context.getString(R.string.updated_at, formatter.format(Date(note.updatedAt)))
        holder.itemView.setOnClickListener { listener.onNoteClick(note) }
        holder.favorite.imageTintList = null
        holder.favorite.alpha = if (note.favorite == 1) 1f else 0.35f
        holder.favorite.setOnClickListener { listener.onFavoriteToggle(note) }
    }

    // PUBLIC_INTERFACE
    fun updateData(newItems: List<Note>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}
