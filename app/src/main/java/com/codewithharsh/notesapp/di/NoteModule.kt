package com.codewithharsh.notesapp.di

import com.codewithharsh.notesapp.domain.repository.NotesRepository
import com.codewithharsh.notesapp.domain.use_cases.DeleteNoteUseCase
import com.codewithharsh.notesapp.domain.use_cases.GetNotesUseCase
import com.codewithharsh.notesapp.domain.use_cases.InsertNoteUseCase
import com.codewithharsh.notesapp.domain.use_cases.NoteUseCases
import com.codewithharsh.notesapp.domain.use_cases.UpdateNoteUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NoteModule {

    @Provides
    @Singleton
    fun provideNoteUseCases(notesRepository: NotesRepository): NoteUseCases{
        return NoteUseCases(
            insertNoteUseCase = InsertNoteUseCase(notesRepository),
            updateNoteUseCase = UpdateNoteUseCase(notesRepository),
            deleteNoteUseCase = DeleteNoteUseCase(notesRepository),
            getNotesUseCase = GetNotesUseCase(notesRepository)
        )
    }
}