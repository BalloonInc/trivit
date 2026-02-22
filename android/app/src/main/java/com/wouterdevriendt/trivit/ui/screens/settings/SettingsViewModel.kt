package com.wouterdevriendt.trivit.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wouterdevriendt.trivit.data.datastore.UserPreferencesData
import com.wouterdevriendt.trivit.data.datastore.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val preferences: StateFlow<UserPreferencesData> = preferencesRepository.preferences
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserPreferencesData()
        )

    fun setHapticsEnabled(enabled: Boolean) {
        viewModelScope.launch { preferencesRepository.setHapticsEnabled(enabled) }
    }

    fun setHideCounter(hide: Boolean) {
        viewModelScope.launch { preferencesRepository.setHideCounter(hide) }
    }

    fun setColorSchemeIndex(index: Int) {
        viewModelScope.launch {
            preferencesRepository.setColorSchemeIndex(index)
            preferencesRepository.setNextColorIndex(0)
        }
    }
}
