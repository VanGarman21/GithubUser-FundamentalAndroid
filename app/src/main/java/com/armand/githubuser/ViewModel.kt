package com.armand.githubuser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModel(private val pref: Setting) :
    ViewModelProvider.Factory {
    // ViewModel class responsible for creating instances of SettingViewModel

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Overrides the create method of ViewModelProvider.Factory
        return when {
            modelClass.isAssignableFrom(SettingViewModel::class.java) -> {
                // Checks if the requested ViewModel class is SettingViewModel
                SettingViewModel(pref) as T
                // If so, creates and returns an instance of SettingViewModel with the provided Setting
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            // Throws an IllegalArgumentException if the requested ViewModel class is unknown
        }
    }
}

