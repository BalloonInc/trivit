package com.wouterdevriendt.trivit.ui.screens.statistics

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wouterdevriendt.trivit.data.model.Trivit
import com.wouterdevriendt.trivit.data.repository.TrivitRepository
import com.wouterdevriendt.trivit.domain.StatisticsCalculator
import com.wouterdevriendt.trivit.domain.TrivitStatistics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class StatisticsUiState(
    val trivit: Trivit? = null,
    val statistics: TrivitStatistics? = null,
    val isLoading: Boolean = true
)

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: TrivitRepository
) : ViewModel() {

    private val trivitId: Long = savedStateHandle.get<Long>("trivitId") ?: 0L

    private val _uiState = MutableStateFlow(StatisticsUiState())
    val uiState: StateFlow<StatisticsUiState> = _uiState.asStateFlow()

    init {
        loadStatistics()
    }

    private fun loadStatistics() {
        viewModelScope.launch {
            val trivit = repository.getTrivitById(trivitId) ?: return@launch
            val events = repository.getEventsForTrivitOnce(trivitId)
            val stats = StatisticsCalculator.calculate(events, trivit.count)
            _uiState.value = StatisticsUiState(
                trivit = trivit,
                statistics = stats,
                isLoading = false
            )
        }
    }
}
