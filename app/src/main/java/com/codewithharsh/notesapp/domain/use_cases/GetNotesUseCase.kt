package com.codewithharsh.notesapp.domain.use_cases

import com.codewithharsh.notesapp.domain.model.Note
import com.codewithharsh.notesapp.domain.repository.NotesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNotesUseCase @Inject constructor(
    private val notesRepository: NotesRepository
) {
    operator fun invoke(): Flow<List<Note>>{
        return notesRepository.getNotes()
    }
}