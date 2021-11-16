package com.ak.anima.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.ak.anima.R
import com.ak.anima.databinding.DialogFilterBinding
import com.ak.anima.utils.Keys
import com.ak.anima.utils.QueryFilterHelper
import com.ak.anima.utils.QueryFilters
import com.ak.type.MediaType
import com.google.android.material.snackbar.Snackbar
import java.io.Serializable

private const val TAG = "FilterQueryDialog"

class FilterQueryDialog(
    private var queryFilters: QueryFilters? = null,
    private var listener: OnFilterSaveClickListener? = null
) : DialogFragment(R.layout.dialog_filter), GenreDialog.GenreDialogListener {

    constructor() : this(null, null)

    //TODO Add advanced filter methods
    private lateinit var queryFiltersHelper: QueryFilterHelper
    private val _queryFilters get() = queryFilters!!
    private var _binding: DialogFilterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        setStyle(STYLE_NORMAL, Window.FEATURE_NO_TITLE)
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.attributes?.windowAnimations = R.style.DialogFilterAnimation
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        _binding = DialogFilterBinding.bind(view)

        binding.apply {
            btnGenre.setOnClickListener {
                val genreDialog =
                    GenreDialog(queryFiltersHelper.queryFilters.listGenre, this@FilterQueryDialog)
                genreDialog.show(parentFragmentManager, "GenreDialog")
            }
            btnClose.setOnClickListener {
                dismiss()
            }
            btnReset.setOnClickListener {
                queryFiltersHelper = QueryFilterHelper(_queryFilters.copy())
                setUpUi()
            }
            btnSave.setOnClickListener {
                updateQueryFilters()
            }
        }
        if (savedInstanceState != null) {
            savedInstanceState.run {
                queryFilters = getParcelable(Keys.STATE_QUERY_FILTERS_BEFORE)
                queryFiltersHelper = QueryFilterHelper(getParcelable(Keys.STATE_QUERY_FILTERS_AFTER)!!)
                listener = getSerializable(Keys.STATE_PARENT_FRAGMENT_LISTENER) as OnFilterSaveClickListener?
            }
        } else queryFiltersHelper = QueryFilterHelper(_queryFilters)

        setUpUi()
    }

    private fun setUpUi() {
        Log.d(TAG, "setUpUi: copy of filterquery ${queryFiltersHelper.queryFilters.printData()}")
        binding.apply {
            spinnerType.apply {
                setSelection(queryFiltersHelper.getTypeIndex())
                isEnabled = false
            }

            setSpinnerFormat()

            spinnerSeason.apply {
                setSelection(queryFiltersHelper.getSeasonIndex())
                isEnabled =
                    _queryFilters.type == MediaType.ANIME //false if manga as manga dose not have seasons
            }

            spinnerStatus.setSelection(queryFiltersHelper.getStatusIndex())
            spinnerSource.setSelection(queryFiltersHelper.getSourceIndex())
            datePicker.setCurrentYear(queryFiltersHelper.getYear())

            switchAllTime.apply {
                setOnCheckedChangeListener { _, isChecked ->
                    datePicker.isEnabled = !isChecked
                }
                isChecked = queryFiltersHelper.getYear() == null
            }

            btnGenre.text = queryFiltersHelper.getGenreCount()
        }
    }

    private fun setSpinnerFormat() {
        val adapterArrayId =
            if (binding.spinnerType.selectedItemPosition == 0) R.array.spinner_format_anime_array else R.array.spinner_format_manga_array

        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            adapterArrayId,
            android.R.layout.simple_spinner_item
        ).also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        binding.spinnerFormat.adapter = adapter
        binding.spinnerFormat.setSelection(queryFiltersHelper.getFormatIndex())
    }

    override fun onGenreOkClicked(genres: List<String>) {
        queryFiltersHelper.setGenres(genres)
        binding.btnGenre.text = queryFiltersHelper.getGenreCount()
        //in case user changes theme text does not changes
    }

    override fun onGenreResetClicked() {
        queryFiltersHelper.setGenres(_queryFilters.getGenre())
        binding.btnGenre.text = queryFiltersHelper.getGenreCount()
    }


    private fun updateQueryFilters() {
        confirmChanges()
        listener?.onSaveClickListener(queryFiltersHelper.queryFilters) ?: Snackbar.make(
            requireContext(),
            binding.btnSave,
            "Failed to update filters",
            Snackbar.LENGTH_SHORT
        )
        dismiss()
    }

    private fun confirmChanges() {
        Log.d(TAG, "Before confirmChanges: ${_queryFilters.printData()}")
        queryFiltersHelper.apply {
            setFormat(binding.spinnerFormat.selectedItemPosition)
            setStatus(binding.spinnerStatus.selectedItemPosition)
            setSeason(binding.spinnerSeason.selectedItemPosition)
            setYear(binding.datePicker.year, binding.switchAllTime.isChecked)
            setSource(binding.spinnerSource.selectedItemPosition)
            //genre is updated at function onGenreOkClicked()
            Log.d(TAG, "After confirmChanges: ${queryFilters.printData()}")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        confirmChanges()
        outState.apply {
            putParcelable(Keys.STATE_QUERY_FILTERS_BEFORE, queryFilters)
            putParcelable(Keys.STATE_QUERY_FILTERS_AFTER, queryFiltersHelper.queryFilters)
            putSerializable(Keys.STATE_PARENT_FRAGMENT_LISTENER, listener)
        }
        super.onSaveInstanceState(outState)
    }

    interface OnFilterSaveClickListener : Serializable {
        fun onSaveClickListener(queryFilters: QueryFilters)
    }
}