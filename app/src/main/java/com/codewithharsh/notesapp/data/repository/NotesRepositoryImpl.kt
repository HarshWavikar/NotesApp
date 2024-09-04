package com.codewithharsh.notesapp.data.repository

import com.codewithharsh.notesapp.data.local.NoteDao
import com.codewithharsh.notesapp.data.mapper.toNote
import com.codewithharsh.notesapp.data.mapper.toNoteEntity
import com.codewithharsh.notesapp.domain.model.Note
import com.codewithharsh.notesapp.domain.repository.NotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NotesRepositoryImpl(
    private val noteDao: NoteDao
): NotesRepository {
    override suspend fun insert(note: Note) {
        noteDao.insertNote(note.toNoteEntity())
    }

    override suspend fun update(note: Note) {
       noteDao.updateNote(note.toNoteEntity())
    }

    override suspend fun delete(note: Note) {
        noteDao.deleteNote(note.toNoteEntity())
    }

    override fun getNotes(): Flow<List<Note>> {
      return  noteDao.getNotes().map {
            it.map {
                it.toNote()
            }
        }
    }
}