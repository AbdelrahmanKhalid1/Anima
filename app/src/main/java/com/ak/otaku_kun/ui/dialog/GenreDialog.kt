package com.ak.otaku_kun.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.ak.otaku_kun.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class GenreDialog(
    genres: List<String>?,
    private val listener: GenreDialogListener
) : DialogFragment() {

    //TODO I can create itemsChecked in parent dialog and then send it here
    private lateinit var itemsChecked: BooleanArray
    private lateinit var genresArr: Array<String>
    private val _genres : ArrayList<String> = (genres ?: ArrayList()) as ArrayList<String>

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = MaterialAlertDialogBuilder(requireContext())
        builder.setTitle("Series Genres")
            .setIcon(R.drawable.ic_genre)
            .setPositiveButton(
                "ok"
            ) { _, _ -> listener.onGenreOkClicked(_genres) }
            .setNeutralButton("cancel") { _, _ -> }
            .setNegativeButton("reset") { _: DialogInterface?, _: Int -> listener.onGenreResetClicked() }
            .setMultiChoiceItems(R.array.genres, itemsChecked) { _, which, isChecked ->
                selectGenre(
                    which,
                    isChecked
                )
            }
        return builder.create()
    }

    private fun selectGenre(which: Int, isChecked: Boolean) {
        itemsChecked[which] = isChecked
        if (isChecked) _genres.add(genresArr[which])
        else _genres.remove(genresArr[which])
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        genresArr = resources.getStringArray(R.array.genres)
        itemsChecked = BooleanArray(genresArr.size)

        //TODO I can create itemsChecked in parent dialog and then send it here
        if (_genres.isNotEmpty()) {
            for (genre: String in _genres) {
                for (i in genresArr.indices) {
                    if (genre == genresArr[i]) {
                        itemsChecked[i] = true
                        break
                    }
                }
            }
        }
    }

    interface GenreDialogListener {
        fun onGenreOkClicked(genres: List<String>)
        fun onGenreResetClicked()
    }
}