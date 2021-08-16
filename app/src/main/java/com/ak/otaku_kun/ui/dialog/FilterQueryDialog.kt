package com.ak.otaku_kun.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.ak.otaku_kun.R
import com.ak.otaku_kun.databinding.DialogFilterBinding
import com.ak.otaku_kun.utils.QueryFilterHelper
import com.ak.otaku_kun.utils.QueryFilters
import com.ak.type.MediaType

class FilterQueryDialog(
    private var queryFilters: QueryFilters,
    private val selectedNavIndex: Int,
    private val listener: OnFilterSaveClickListener
) : DialogFragment(R.layout.dialog_filter), GenreDialog.GenreDialogListener {

    //TODO Add advanced filter methods
    private lateinit var queryFiltersHelper: QueryFilterHelper
    private var _binding: DialogFilterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        setStyle(STYLE_NORMAL, Window.FEATURE_NO_TITLE)
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        _binding = DataBindingUtil.bind(view)

        val toolbar: Toolbar = binding.toolbar as Toolbar
        val activity = (requireActivity() as AppCompatActivity)
        activity.setSupportActionBar(toolbar)
        activity.supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
            setHomeAsUpIndicator(R.drawable.ic_close)
        }
        binding.btnGenre.setOnClickListener {
            val genreDialog = GenreDialog(queryFiltersHelper.queryFilters.listGenre, this)
            genreDialog.show(parentFragmentManager, "GenreDialog")
        }
        setUpUi()
    }

    private fun setUpUi() {
        queryFiltersHelper = QueryFilterHelper(queryFilters.copy())
        binding.spinnerSeason.setSelection(queryFiltersHelper.getSeasonIndex())
        binding.spinnerType.setSelection(queryFiltersHelper.getTypeIndex())

        //TODO I can make an option for the user to customize the tabs to be displayed ex: (season, format, genres..)
        // and the chosen feature will be disabled
        //if condition here
        binding.spinnerSeason.isEnabled = false

        if (selectedNavIndex == R.id.nav_browse_anime || selectedNavIndex == R.id.nav_browse_manga)
            binding.spinnerType.isEnabled = false

        binding.spinnerType.onItemSelectedListener = spinnerTypeItemSelectListener

        binding.spinnerStatus.setSelection(queryFiltersHelper.getStatusIndex())
        binding.spinnerSource.setSelection(queryFiltersHelper.getSourceIndex())
        binding.datePicker.setCurrentYear(queryFiltersHelper.queryFilters.seasonYear)

        binding.switchAllTime.setOnCheckedChangeListener { _, isChecked ->
            binding.datePicker.isEnabled = !isChecked
        }
        binding.switchAllTime.isChecked = queryFiltersHelper.queryFilters.seasonYear == null
        binding.btnGenre.text = queryFiltersHelper.getGenreCount()
    }

    private val spinnerTypeItemSelectListener = object : AdapterView.OnItemSelectedListener{
        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            if (p2 == 0) {//anime or default ==> anime
                val adapter = ArrayAdapter.createFromResource(
                    requireContext(),
                    R.array.spinner_format_anime_array,
                    android.R.layout.simple_spinner_item
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerFormat.adapter = adapter
                binding.spinnerFormat.setSelection(queryFiltersHelper.getFormatAnimeIndex())
            } else { //manga
                val adapter = ArrayAdapter.createFromResource(
                    requireContext(),
                    R.array.spinner_format_manga_array,
                    android.R.layout.simple_spinner_item
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerFormat.adapter = adapter
                binding.spinnerFormat.setSelection(queryFiltersHelper.getFormatMangaIndex())
            }
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.filter_menu, menu)
        menu.setGroupVisible(R.id.main_group, false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                dismiss()
                true
            }
            R.id.action_filter_ok -> {
                confirmChanges()
                listener.onSaveClickListener(queryFiltersHelper.queryFilters)
                dismiss()
                true
            }
            R.id.action_filter_reset -> {
                setUpUi()
                true
            }
            else ->
                return super.onOptionsItemSelected(item)
        }
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
        queryFiltersHelper.queryFilters.seasonYear =
            if (binding.switchAllTime.isChecked) null else binding.datePicker.year

        queryFiltersHelper.setType(binding.spinnerType.selectedItemPosition)
        queryFiltersHelper.setFormat(binding.spinnerFormat.selectedItemPosition)
        queryFiltersHelper.setSource(binding.spinnerSource.selectedItemPosition)
        queryFiltersHelper.setStatus(binding.spinnerStatus.selectedItemPosition)
    }

    interface OnFilterSaveClickListener {
        fun onSaveClickListener(queryFilters: QueryFilters)
    }
}
