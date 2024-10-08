package com.kodiiiofc.urbanuniversity.mynotes

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class DBHelper(val context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "notes.db"
        private const val TABLE_NAME = "notes"
        private const val KEY_POSITION = "position"
        private const val KEY_CONTENT = "content"
        private const val KEY_CHECKED = "checked"
        private const val KEY_CREATED_AT = "created_at"
    }


    override fun onCreate(db: SQLiteDatabase?) {
        val TABLE =
            "CREATE TABLE $TABLE_NAME ($KEY_POSITION INTEGER PRIMARY KEY, $KEY_CONTENT TEXT, $KEY_CHECKED INTEGER, $KEY_CREATED_AT TEXT)"
        db?.execSQL(TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    @SuppressLint("Range")
    fun saveNotes(notes: Notes) {
        val db = this.writableDatabase
        val cursor = db.rawQuery(
            "SELECT $KEY_POSITION FROM $TABLE_NAME ORDER BY $KEY_POSITION DESC LIMIT 1",
            null
        )
        val lastIndex = if (cursor.moveToFirst()) {
            cursor.getInt(cursor.getColumnIndex(KEY_POSITION))
        } else -1
        cursor.close()

        for (note in notes.getNotes().withIndex()) {
            if (note.index > lastIndex) {
                val contentValues = ContentValues()
                contentValues.put(KEY_POSITION, note.index)
                contentValues.put(KEY_CONTENT, note.value.content)
                contentValues.put(KEY_CHECKED, note.value.isChecked)
                contentValues.put(KEY_CREATED_AT, note.value.createdAt)
                db.insert(TABLE_NAME, null, contentValues)
            }
        }
        db.close()
    }

    @SuppressLint("Range")
    fun loadNotes(): Notes {
        val notes = Notes()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        if (cursor.moveToFirst()) {
            do {
                val position = cursor.getInt(cursor.getColumnIndex(KEY_POSITION))
                val content = cursor.getString(cursor.getColumnIndex(KEY_CONTENT))
                val checked = cursor.getInt(cursor.getColumnIndex(KEY_CHECKED)) == 1
                val createdAt = cursor.getString(cursor.getColumnIndex(KEY_CREATED_AT))
                notes.addNoteAtIndex(position, Notes.Note(content, createdAt, checked))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return notes
    }

    fun removeAll() {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, null, null)
        db.close()
    }

}