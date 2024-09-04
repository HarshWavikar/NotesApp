package com.codewithharsh.notesapp.data.mapper

import com.codewithharsh.notesapp.domain.model.Note
import com.codewithharsh.notesapp.data.local.NoteEntity

fun Note.toNoteEntity(): NoteEntity {
    return NoteEntity(id = id, title = title, desc = desc)
}

fun NoteEntity.toNote(): Note {
    return Note(id = id, title = title, desc = desc)
}