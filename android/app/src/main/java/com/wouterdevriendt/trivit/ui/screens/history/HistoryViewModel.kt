package com.wouterdevriendt.trivit.ui.screens.history

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wouterdevriendt.trivit.data.model.TallyEvent
import com.wouterdevriendt.trivit.data.model.Trivit
import com.wouterdevriendt.trivit.data.repository.TrivitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.Locale
import javax.inject.Inject

data class AggregatedEvent(
    val events: List<TallyEvent>,
    val totalDelta: Int,
    val timestamp: Long,
    val minuteKey: String
)

data class DayGroup(
    val label: String,
    val date: LocalDate,
    val events: List<AggregatedEvent>
)

data class HistoryUiState(
    val trivit: Trivit? = null,
    val dayGroups: List<DayGroup> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class HistoryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: TrivitRepository
) : ViewModel() {

    private val trivitId: Long = savedStateHandle.get<Long>("trivitId") ?: 0L

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init {
        loadHistory()
    }

    private fun loadHistory() {
        viewModelScope.launch {
            val trivit = repository.getTrivitById(trivitId) ?: return@launch
            repository.getEventsForTrivit(trivitId).collect { events ->
                val groups = groupEvents(events)
                _uiState.value = HistoryUiState(
                    trivit = trivit,
                    dayGroups = groups,
                    isLoading = false
                )
            }
        }
    }

    private fun groupEvents(events: List<TallyEvent>): List<DayGroup> {
        val zone = ZoneId.systemDefault()
        val today = LocalDate.now()
        val yesterday = today.minusDays(1)
        val formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy")
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

        // Group by day
        val byDay = events.groupBy { event ->
            Instant.ofEpochMilli(event.timestamp).atZone(zone).toLocalDate()
        }.toSortedMap(compareByDescending { it })

        return byDay.map { (date, dayEvents) ->
            val label = when {
                date == today -> "Today"
                date == yesterday -> "Yesterday"
                ChronoUnit.DAYS.between(date, today) < 7 -> {
                    date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
                }
                else -> date.format(formatter)
            }

            // Aggregate by minute
            val aggregated = dayEvents.groupBy { event ->
                val dt = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(event.timestamp), zone
                )
                dt.format(timeFormatter)
            }.map { (minuteKey, minuteEvents) ->
                AggregatedEvent(
                    events = minuteEvents,
                    totalDelta = minuteEvents.sumOf { it.delta },
                    timestamp = minuteEvents.first().timestamp,
                    minuteKey = minuteKey
                )
            }.sortedByDescending { it.timestamp }

            DayGroup(label = label, date = date, events = aggregated)
        }
    }

    fun deleteEvent(event: TallyEvent) {
        viewModelScope.launch {
            repository.deleteEvent(event)
        }
    }
}
