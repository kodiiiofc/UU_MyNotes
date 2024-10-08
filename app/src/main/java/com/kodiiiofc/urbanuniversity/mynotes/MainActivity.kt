package com.kodiiiofc.urbanuniversity.mynotes

import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var statusBar: View
    private lateinit var toolbar: Toolbar
    private lateinit var notesRV: RecyclerView

    private val db = DBHelper(this)

    private var notes: Notes? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        initToolbar()
        notes = db.loadNotes()
        notesRV = findViewById(R.id.rv_notes)
        val adapter = NotesAdapter(notes!!)
        notesRV.adapter = adapter
        notesRV.setHasFixedSize(true)
    }

    private fun initToolbar() {
        statusBar = findViewById(R.id.status_bar)
        val statusBarHeight = resources.getIdentifier("status_bar_height", "dimen", "android")
        statusBar.layoutParams.height = resources.getDimensionPixelSize(statusBarHeight)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_exit -> finishAffinity()
            R.id.menu_add_item -> addItem()
            R.id.menu_clear_database -> {
                db.removeAll()
                notes!!.clear()
                notesRV.adapter = NotesAdapter(db.loadNotes())
                notesRV.adapter?.notifyDataSetChanged()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addItem() {

        val dialogAddItem = layoutInflater.inflate(R.layout.dialog_add_item, null)
        val contentET = dialogAddItem.findViewById<EditText>(R.id.et_content)

        AlertDialog.Builder(this)
            .setTitle("Заметка")
            .setMessage("Введите текст заметки ниже")
            .setView(dialogAddItem)
            .setPositiveButton("Добавить заметку") {dialog, _ ->
                val content = contentET.text.toString().trim()
                if (content.isNotEmpty()) {
                    notes!!.addNote(content)
                    db.saveNotes(notes!!)
                    notesRV.adapter = NotesAdapter(db.loadNotes())
                    notesRV.adapter?.notifyDataSetChanged()
                }
                dialog.dismiss()
            }
            .setNeutralButton("Отмена", null)
            .create().show()
    }

}