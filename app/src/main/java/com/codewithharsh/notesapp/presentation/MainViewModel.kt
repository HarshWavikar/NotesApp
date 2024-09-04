package com.codewithharsh.notesapp.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codewithharsh.notesapp.domain.model.Note
import com.codewithharsh.notesapp.domain.use_cases.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
): ViewModel() {

    val uiState = noteUseCases.getNotesUseCase.invoke()
        .map { UiState(it) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, UiState())

    fun insert(note: Note) = viewModelScope.launch {
        noteUseCases.insertNoteUseCase.invoke(note)
    }

    fun update(note: Note) = viewModelScope.launch {
        noteUseCases.updateNoteUseCase.invoke(note)
    }

    fun delete(note: Note) = viewModelScope.launch {
        noteUseCases.deleteNoteUseCase.invoke(note)
    }
}

data class UiState(
    val data : List<Note> = emptyList()
)