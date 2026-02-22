package com.wouterdevriendt.trivit.ui.screens.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wouterdevriendt.trivit.data.datastore.UserPreferencesData
import com.wouterdevriendt.trivit.data.datastore.UserPreferencesRepository
import com.wouterdevriendt.trivit.data.model.Trivit
import com.wouterdevriendt.trivit.data.repository.TrivitRepository
import com.wouterdevriendt.trivit.domain.EasterEggs
import com.wouterdevriendt.trivit.domain.ExampleNames
import com.wouterdevriendt.trivit.service.HapticsService
import com.wouterdevriendt.trivit.ui.theme.TrivitColors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TrivitListUiState(
    val trivits: List<Trivit> = emptyList(),
    val preferences: UserPreferencesData = UserPreferencesData(),
    val showTutorial: Boolean = false,
    val easterEggMessage: String? = null
)

@HiltViewModel
class TrivitListViewModel @Inject constructor(
    private val repository: TrivitRepository,
    private val preferencesRepository: UserPreferencesRepository,
    private val hapticsService: HapticsService,
    private val exampleNames: ExampleNames
) : ViewModel() {

    private val _easterEggMessage = MutableStateFlow<String?>(null)
    private val _undoEvent = MutableSharedFlow<Trivit>()
    val undoEvent = _undoEvent.asSharedFlow()

    val uiState: StateFlow<TrivitListUiState> = combine(
        repository.getAllActive(),
        preferencesRepository.preferences,
        _easterEggMessage
    ) { trivits, prefs, easterEgg ->
        TrivitListUiState(
            trivits = trivits,
            preferences = prefs,
            showTutorial = !prefs.tutorialSeen && trivits.isEmpty(),
            easterEggMessage = easterEgg
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TrivitListUiState()
    )

    fun addTrivit() {
        viewModelScope.launch {
            val prefs = uiState.value.preferences
            val colorIndex = preferencesRepository.getAndIncrementColorIndex(
                TrivitColors.getPalette(prefs.colorSchemeIndex).colors.size
            )
            val trivit = Trivit(
                name = exampleNames.random(),
                colorIndex = colorIndex,
                isExpanded = true
            )
            repository.createTrivit(trivit)
            if (prefs.hapticsEnabled) hapticsService.lightImpact()
        }
    }

    fun increment(trivitId: Long) {
        viewModelScope.launch {
            repository.increment(trivitId)
            val trivit = repository.getTrivitById(trivitId)
            if (trivit != null) {
                val message = EasterEggs.getMessage(trivit.count)
                if (message != null) {
                    _easterEggMessage.value = message
                }
                if (uiState.value.preferences.hapticsEnabled) {
                    hapticsService.tick()
                }
            }
        }
    }

    fun decrement(trivitId: Long) {
        viewModelScope.launch {
            repository.decrement(trivitId)
            if (uiState.value.preferences.hapticsEnabled) {
                hapticsService.lightImpact()
            }
        }
    }

    fun toggleExpanded(trivitId: Long) {
        viewModelScope.launch {
            val trivit = repository.getTrivitById(trivitId) ?: return@launch
            repository.setExpanded(trivitId, !trivit.isExpanded)
        }
    }

    fun rename(trivitId: Long, name: String) {
        viewModelScope.launch {
            repository.rename(trivitId, name)
        }
    }

    fun updateColor(trivitId: Long, colorIndex: Int) {
        viewModelScope.launch {
            repository.updateColor(trivitId, colorIndex)
        }
    }

    fun resetCount(trivitId: Long) {
        viewModelScope.launch {
            repository.resetCount(trivitId)
        }
    }

    fun softDelete(trivitId: Long) {
        viewModelScope.launch {
            val trivit = repository.getTrivitById(trivitId) ?: return@launch
            repository.softDelete(trivitId)
            _undoEvent.emit(trivit)
            if (uiState.value.preferences.hapticsEnabled) {
                hapticsService.mediumImpact()
            }
        }
    }

    fun undoDelete(trivitId: Long) {
        viewModelScope.launch {
            repository.restore(trivitId)
        }
    }

    fun reorder(trivits: List<Trivit>) {
        viewModelScope.launch {
            repository.updateSortOrders(trivits)
        }
    }

    fun dismissTutorial() {
        viewModelScope.launch {
            preferencesRepository.setTutorialSeen(true)
        }
    }

    fun clearEasterEgg() {
        _easterEggMessage.value = null
    }
}
