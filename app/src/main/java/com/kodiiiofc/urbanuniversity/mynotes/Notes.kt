package com.kodiiiofc.urbanuniversity.mynotes

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Notes {

    companion object {
        private val dateFormatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.GERMAN)
    }

    private val notes: MutableList<Note> = mutableListOf()

    data class Note(val content: String,
                val createdAt: String = dateFormatter.format(Date()),
                var isChecked: Boolean = false) {

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