package com.ak.otaku_kun.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.DialogFragment
import com.ak.otaku_kun.R
import com.ak.type.MediaSort
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*
import kotlin.collections.ArrayList

private const val TAG = "SortDialog"

class SortDialog(private var sort: String, private val listener: SortDialogListener) :
    DialogFragment() {
    private lateinit var sortByGroup: RadioGroup
    private lateinit var sortInGroup: RadioGroup

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = layoutInflater.inflate(R.layout.dialog_sort, null)
        sortByGroup = view.findViewById(R.id.group_sort_by)
        sortInGroup = view.findViewById(R.id.group_sort_in)

        setRadioGroups()

        val alertBuilder = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Sort Series")
            .setIcon(R.drawable.ic_import_export)
            .setView(view)
            .setPositiveButton("ok") { _, _ -> done() }
            .setNegativeButton("cancel") { _, _ -> }

        return alertBuilder.create()
    }

    private fun setRadioGroups() {
        val strArr = sort.split("_")
        val strSize = strArr.size
        var strProp = ""
        //set ascending or descending
        val radioButtonCheckedIndex =
            if (strArr[strSize - 1] == "DESC") R.id.radio_descending else R.id.radio_ascending
        sortInGroup.check(radioButtonCheckedIndex)

        //set property
        if (strSize == 1) sortByGroup.check(
            getSortByPropIndex(strArr[0].substring(0, strProp.length - 1))
        ) //Ascending case
        else {
            for (i in 0..strSize - 2) {
                strProp += strArr[i] + " "
            }
            sortByGroup.check(getSortByPropIndex(strProp.substring(0, strProp.length - 1)))
        }
    }

    private fun getSortByPropIndex(strProp: String): Int =
        when (strProp) {
            "POPULARITY" -> R.id.radio_popularity
            "SCORE" -> R.id.radio_score
            "START DATE" -> R.id.radio_start_date
            "EPISODES" -> R.id.radio_episodes
            "CHAPTERS" -> R.id.radio_chapters
            "VOLUMES" -> R.id.radio_volumes
            else -> R.id.radio_popularity
        }

    private fun done() {
        val radioSortBy =
            requireDialog().findViewById<RadioButton>(sortByGroup.checkedRadioButtonId)
        val radioSortIn =
            requireDialog().findViewById<RadioButton>(sortInGroup.checkedRadioButtonId)

        val sortBy = radioSortBy!!.text.toString()
        val sortIn = radioSortIn.text.toString()

        sort = if (sortIn == resources.getString(R.string.sort_in_descending)) {
            val str = sortBy + " " + sortIn.substring(0, 4)
            str.toUpperCase(Locale.getDefault())
                .replace(' ', '_') //Ex: sort = "Score Desc" => SCORE_DESC
        } else {
            sortBy.toUpperCase(Locale.getDefault()) //Ex: sort = "Score" => SCORE
        }
        Log.d(TAG, "done: $sort")
        val sortList = ArrayList<MediaSort>(1)
        sortList.add(MediaSort.valueOf(sort))
        listener.onSortOkClickListener(sortList)
    }

    interface SortDialogListener {
        fun onSortOkClickListener(sort: List<MediaSort>)
    }
}