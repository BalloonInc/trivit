package com.wouterdevriendt.trivit.ui.screens.deleted

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wouterdevriendt.trivit.data.model.Trivit
import com.wouterdevriendt.trivit.data.repository.TrivitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeletedItemsViewModel @Inject constructor(
    private val repository: TrivitRepository
) : ViewModel() {

    val deletedTrivits: StateFlow<List<Trivit>> = repository.getAllDeleted()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        // Purge items older than 30 days on screen load
        viewModelScope.launch {
            repository.purgeOldDeleted()
        }
    }

    fun restore(trivitId: Long) {
        viewModelScope.launch {
            repository.restore(trivitId)
        }
    }

    fun permanentlyDelete(trivit: Trivit) {
        viewModelScope.launch {
            repository.permanentlyDelete(trivit)
        }
    }
}
