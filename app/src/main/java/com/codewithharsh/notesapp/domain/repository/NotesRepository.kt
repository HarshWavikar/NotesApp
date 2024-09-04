package com.codewithharsh.notesapp.domain.repository

import com.codewithharsh.notesapp.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NotesRepository {

    suspend fun insert(note: Note)

    suspend fun update(note: Note)

    suspend fun delete(note: Note)

    fun getNotes(): Flow<List<Note>>
}