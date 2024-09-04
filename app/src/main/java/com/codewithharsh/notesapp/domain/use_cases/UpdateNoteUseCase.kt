package com.codewithharsh.notesapp.domain.use_cases

import com.codewithharsh.notesapp.domain.model.Note
import com.codewithharsh.notesapp.domain.repository.NotesRepository
import javax.inject.Inject

class UpdateNoteUseCase @Inject constructor(
    private val notesRepository: NotesRepository
) {
    suspend operator fun invoke(note: Note){
        notesRepository.update(note)
    }
}