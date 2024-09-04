package com.codewithharsh.notesapp.domain.use_cases

data class NoteUseCases(
    val insertNoteUseCase: InsertNoteUseCase,
    val updateNoteUseCase: UpdateNoteUseCase,
    val deleteNoteUseCase: DeleteNoteUseCase,
    val getNotesUseCase: GetNotesUseCase
)
