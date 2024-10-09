package com.kodiiiofc.urbanuniversity.mynotes

import android.annotation.SuppressLint
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Notes {

    companion object {
        @SuppressLint("SimpleDateFormat")
        private val dateFormatter = SimpleDateFormat("dd.MM.yyyy HH:mm")
        val DELETED_NOTE = Note("DELETED", "NOW", true)

        fun getDate(): String = dateFormatter.format(Date())
    }

    private val notes: MutableList<Note> = mutableListOf()

    data class Note(val content: String,
                val createdAt: String = getDate(),
                var isChecked: Boolean = false) : Serializable {

        override fun toString(): String {
            return "Note('$content', checked $isChecked, created at $createdAt)"
        }
    }



    fun addNote(content: String) {
        notes.add(Note(content))
    }

    fun addNoteAtIndex(position: Int, note: Note) {
            notes.add(position, note)
    }
    
    fun getNotes() = notes
    fun getNote(number: Int) = notes[number]
    fun getSize() = notes.size

    fun clear() {
        notes.clear()
    }
}