package com.ak.anima.ui.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.ak.anima.R
import com.ak.anima.utils.Keys
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.Serializable

class GenreDialog(
    genres: List<String>?,
    private var listener: GenreDialogListener?
) : DialogFragment() {

    constructor() : this(null, null)

    //TODO I can create itemsChecked in parent dialog and then send it here
    private lateinit var itemsChecked: BooleanArray
    private lateinit var genresArr: Array<String>
    private var _genres: ArrayList<String> = (genres?.run { ArrayList(this) } ?: ArrayList())

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = MaterialAlertDialogBuilder(requireContext())
        builder.setTitle("Series Genres")
            .setIcon(R.drawable.ic_genre)
            .setPositiveButton(
                "ok"
            ) { _, _ ->
                listener?.onGenreOkClicked(_genres) ?: Toast.makeText(
                    requireContext(),
                    "Failed to update Genres",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
            .setNeutralButton("cancel") { _, _ -> }
            .setNegativeButton("reset") { _: DialogInterface?, _: Int ->
                listener?.onGenreResetClicked() ?: Toast.makeText(
                    requireContext(),
                    "Failed to reset Genres",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
            .setMultiChoiceItems(R.array.genres, itemsChecked) { _, which, isChecked ->
                selectGenre(
                    which,
                    isChecked
                )
            }

        savedInstanceState?.run {
            _genres = getStringArrayList(Keys.STATE_GENRE)!!
            listener = getSerializable(Keys.STATE_PARENT_FRAGMENT_LISTENER) as GenreDialogListener?
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

    override fun onSaveInstanceState(outState: Bundle) {
        outState.run {
            putStringArrayList(Keys.STATE_GENRE, _genres)
            putSerializable(Keys.STATE_PARENT_FRAGMENT_LISTENER, listener)
        }
        super.onSaveInstanceState(outState)
    }

    interface GenreDialogListener : Serializable {
        fun onGenreOkClicked(genres: List<String>)
        fun onGenreResetClicked()
    }
}