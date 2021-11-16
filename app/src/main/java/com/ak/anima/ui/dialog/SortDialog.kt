package com.ak.anima.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.ak.anima.R
import com.ak.type.MediaSort
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

private const val TAG = "SortDialog"

class SortDialog(private var sort: String ="", private var listener : SortDialogListener?= null) :
    DialogFragment() {

    constructor() : this("", null)

    private lateinit var sortByGroup: RadioGroup
    private lateinit var sortInGroup: RadioGroup

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        savedInstanceState?.run {
            //restore data in case of process killed or user changes system theme
            sort = getString("sort", "")
            listener = getSerializable("listener") as SortDialogListener?
        }
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
        var arrSize = strArr.size -1 //-1 to be zero based
        //set ascending or descending
        val radioButtonCheckedIndex =
            if (strArr[arrSize] == "DESC"){
                arrSize-- //-1 to remove index of 'DESC'
                R.id.radio_descending
            } else{
                R.id.radio_ascending
            }
        sortInGroup.check(radioButtonCheckedIndex)

        var strProp = ""
        for (i in 0..arrSize) {
            strProp += strArr[i] + " "
        }
        sortByGroup.check(getSortByPropIndex(strProp.substring(0, strProp.length-1))) //-1 to remove last ' '
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

        var sort = radioSortBy.text.toString()
        val sortIn = radioSortIn.text.toString()

        if (sortIn == resources.getString(R.string.sort_in_descending)) {
            sort += " ${sortIn.substring(0, 4)}" //Score Desc
        }

        sort = sort.uppercase(Locale.getDefault())
            .replace(' ', '_') //Ex: sort = "Score Desc" => SCORE_DESC

        Log.d(TAG, "done: $sort")
        val sortList = ArrayList<MediaSort>(1)
        sortList.add(MediaSort.valueOf(sort))
        listener?.onSortOkClickListener(sortList) ?: Toast.makeText(requireContext(), "Failed to load data", Toast.LENGTH_SHORT).show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.run {
            putString("sort", sort)
            putSerializable("listener", listener)
        }
        super.onSaveInstanceState(outState)
    }

    interface SortDialogListener : Serializable{
        fun onSortOkClickListener(sort: List<MediaSort>)
    }
}