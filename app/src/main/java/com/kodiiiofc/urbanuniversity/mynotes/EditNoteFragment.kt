package com.kodiiiofc.urbanuniversity.mynotes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import java.util.Date

class EditNoteFragment : Fragment(), OnFragmentDataListener {

    private lateinit var onFragmentDataListener: OnFragmentDataListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val notePositionTV = view.findViewById<TextView>(R.id.tv_note_position)
        val editNoteET = view.findViewById<EditText>(R.id.et_edit_note)
        val saveBTN = view.findViewById<Button>(R.id.btn_save)
        val deleteBTN = view.findViewById<Button>(R.id.btn_delete)

        onFragmentDataListener = requireActivity() as OnFragmentDataListener

        val args = requireArguments()
        val note = args.getSerializable("note") as Notes.Note
        val position = args.getInt("position")

        notePositionTV.text = "№ " + (position + 1).toString()
        editNoteET.setText(note.content)

        saveBTN.setOnClickListener {
            val content = editNoteET.text.toString().trim()
            if (content.isNotEmpty()) {
                val updatedNote = Notes.Note(content, Notes.getDate(), note.isChecked)
                onData(updatedNote, position)
            }
            else {
                onData(Notes.DELETED_NOTE, position)
            }
        }

        deleteBTN.setOnClickListener {
            onData(Notes.DELETED_NOTE, position)
        }

    }

    override fun onData(note: Notes.Note?, position: Int) {
        val bundle = Bundle()
        bundle.putInt("position", position)
        bundle.putSerializable("updatedNote", note)

        val transaction = this.fragmentManager?.beginTransaction()
        val recycleViewFragment = RecycleViewFragment()
        recycleViewFragment.arguments = bundle
        transaction?.replace(R.id.fragment_container, recycleViewFragment)
        transaction?.addToBackStack(null)
        transaction?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction?.commit()
    }
}