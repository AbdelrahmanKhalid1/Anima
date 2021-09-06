package com.ak.otaku_kun.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.ak.otaku_kun.R
import com.ak.otaku_kun.databinding.DialogFilterBinding
import com.ak.otaku_kun.utils.QueryFilterHelper
import com.ak.otaku_kun.utils.QueryFilters
import com.ak.type.MediaType

private const val TAG = "FilterQueryDialog"
class FilterQueryDialog(
    private var queryFilters: QueryFilters,
    private val listener: OnFilterSaveClickListener
) : DialogFragment(R.layout.dialog_filter), GenreDialog.GenreDialogListener {

    //TODO Add advanced filter methods
    private lateinit var queryFiltersHelper: QueryFilterHelper
    private var _binding: DialogFilterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        setStyle(STYLE_NORMAL, Window.FEATURE_NO_TITLE)
        val dialog =super.onCreateDialog(savedInstanceState)
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        _binding = DataBindingUtil.bind(view)

        binding.btnGenre.setOnClickListener {
            val genreDialog = GenreDialog(queryFiltersHelper.queryFilters.listGenre, this)
            genreDialog.show(parentFragmentManager, "GenreDialog")
        }

        binding.btnClose.setOnClickListener {
            dismiss()
        }

        binding.btnReset.setOnClickListener {
            setUpUi()
        }

        binding.btnSave.setOnClickListener {
            confirmChanges()
            listener.onSaveClickListener(queryFiltersHelper.queryFilters)
            dismiss()
        }

        setUpUi()
    }

    private fun setUpUi() {
        queryFiltersHelper = QueryFilterHelper(queryFilters.copy())
        binding.apply {
            spinnerType.apply {
                setSelection(queryFiltersHelper.getTypeIndex())
                isEnabled = false
            }

            setSpinnerFormat()

            spinnerSeason.apply {
                setSelection(queryFiltersHelper.getSeasonIndex())
                isEnabled =
                    queryFilters.type == MediaType.ANIME //false if manga as manga dose not have seasons
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
    }

    override fun onGenreResetClicked() {
        queryFiltersHelper.setGenres(ArrayList(queryFilters.getGenre()))
        binding.btnGenre.text = queryFiltersHelper.getGenreCount()
    }

    private fun confirmChanges() {
        Log.d(TAG, "Before confirmChanges: ${queryFilters.printData()}")
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

    interface OnFilterSaveClickListener {
        fun onSaveClickListener(queryFilters: QueryFilters)
    }
}