package com.kodiiiofc.urbanuniversity.mynotes

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView

class RecycleViewFragment : Fragment() {

    private var db: DBHelper? = null
    private var notes: Notes? = null

    var adapter: NotesAdapter? = null

    private lateinit var notesRV: RecyclerView

    private lateinit var onFragmentDataListener: OnFragmentDataListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        db = DBHelper(context)
    }

    override fun onResume() {
        super.onResume()
        var updatedNote = arguments?.getSerializable("updatedNote") as Notes.Note?
        val position = arguments?.getInt("position")
        if (updatedNote != null) {
            if (updatedNote == Notes.DELETED_NOTE) {
                deleteNote(position)
            } else {
                updateNote(updatedNote, position!!)
            }
        }
    }

    private fun deleteNote(position: Int?) {
        db?.deleteNote(position!!)
        adapter!!.updateNotes(db!!.loadNotes())
        notesRV.adapter?.notifyDataSetChanged()
    }

    private fun updateNote(
        updatedNote: Notes.Note,
        position: Int
    ) {
        db?.updateNote(updatedNote, position)
        adapter!!.updateNotes(db!!.loadNotes())
        notesRV.adapter?.notifyDataSetChanged()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recycle_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onFragmentDataListener = requireActivity() as OnFragmentDataListener
        notes = db?.loadNotes()
        notesRV = view.findViewById(R.id.rv_notes)
        adapter = NotesAdapter(notes!!)
        notesRV.adapter = adapter
        notesRV.setHasFixedSize(true)

        adapter!!.setOnNoteLongClickListener { note, position ->
            onFragmentDataListener.onData(note, position)
        }
    }

    fun clearDatabase() {
        db!!.removeAll()
        notes!!.clear()
        adapter!!.updateNotes(db!!.loadNotes())
        notesRV.adapter?.notifyDataSetChanged()
    }

    fun addNote(content: String) {
        if (content.isNotEmpty()) {
            notes!!.addNote(content)
            db!!.saveNotes(notes!!)
            adapter!!.updateNotes(db!!.loadNotes())
            notesRV.adapter?.notifyDataSetChanged()
        }
    }

}