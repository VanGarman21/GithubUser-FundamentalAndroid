package com.armand.githubuser

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.armand.githubuser.databinding.ActivitySettingBinding

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding
    private lateinit var viewModel: SettingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewModel()
        observeThemeSettings()
        setupThemeSwitch()
    }

    private fun initViewModel() {
        val pref = Setting.getInstance(dataStore)
        viewModel = ViewModelProvider(this, ViewModel(pref))[SettingViewModel::class.java]
    }

    private fun observeThemeSettings() {
        viewModel.getThemeSettings().observe(this) { isDarkModeActive ->
            val mode = if (isDarkModeActive) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            AppCompatDelegate.setDefaultNightMode(mode)
            binding.switchTheme.isChecked = isDarkModeActive
        }
    }

    private fun setupThemeSwitch() {
        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            viewModel.saveThemeSetting(isChecked)
        }
    }
}
