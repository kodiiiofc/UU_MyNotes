package com.kodiiiofc.urbanuniversity.mynotes

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView

class NotesAdapter(private val notes: Notes) : RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    class ViewHolder(noteView: View) : RecyclerView.ViewHolder(noteView) {
        val numberTV = noteView.findViewById<TextView>(R.id.tv_number)
        val contentTV = noteView.findViewById<TextView>(R.id.tv_content)
        val createdAtTV = noteView.findViewById<TextView>(R.id.tv_created_at)
        val checkCB = noteView.findViewById<CheckBox>(R.id.cb_check)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val noteView = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(noteView)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = notes.getNote(position)
        holder.numberTV.text = "№ " + (position + 1).toString()
        holder.contentTV.text = note.content
        holder.createdAtTV.text = note.createdAt
        holder.checkCB.isChecked = note.isChecked

        holder.checkCB.setOnCheckedChangeListener { buttonView, isChecked ->
            notes.getNote(position).isChecked = isChecked
            Toast.makeText(buttonView.context,notes.getNote(position).toString(),Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount() = notes.getSize()
}