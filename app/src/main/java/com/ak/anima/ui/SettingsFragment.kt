package com.ak.anima.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.preference.*
import com.ak.anima.R
import com.ak.anima.db.AnimaDatabase
import com.ak.anima.utils.Keys
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {

    @Inject lateinit var database: AnimaDatabase
    private lateinit var logout: Preference
    private lateinit var theme: ListPreference
    private lateinit var restartListener: OnRestartListener

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        restartListener = requireActivity() as OnRestartListener
        logout = findPreference(Keys.KEY_OAUTH_USER)!!
        theme = findPreference("theme")!!

        logout.setOnPreferenceClickListener {
            database.mediaDao().clearAll()
            PreferenceManager.getDefaultSharedPreferences(requireContext())
                .edit {
                    putString(Keys.KEY_TOKEN, null)
                    putString(Keys.KEY_OAUTH_USER, null)
                    commit()
                }
            restartListener.restartApp()
            true
        }

        theme.setOnPreferenceChangeListener { _, newValue ->
            val mode = when (newValue as String) {
                "light" -> AppCompatDelegate.MODE_NIGHT_NO
                "dark" -> AppCompatDelegate.MODE_NIGHT_YES
                else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM //default
            }
            AppCompatDelegate.setDefaultNightMode(mode)
            true
        }
    }

    interface OnRestartListener {
        fun restartApp()
    }
}