package com.armand.githubuser

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SettingViewModel(private val pref: Setting) : ViewModel() {

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeMode().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeMode(isDarkModeActive)
        }
    }
}