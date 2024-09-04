package com.codewithharsh.notesapp.data.di

import android.content.Context
import com.codewithharsh.notesapp.data.local.NoteDao
import com.codewithharsh.notesapp.data.local.NoteDatabase
import com.codewithharsh.notesapp.data.repository.NotesRepositoryImpl
import com.codewithharsh.notesapp.domain.repository.NotesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotesDataModule {

    @Provides
    @Singleton
    fun provideNotesDatabase(@ApplicationContext context: Context): NoteDatabase {
        return NoteDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideNotesDao(noteDatabase: NoteDatabase): NoteDao {
        return noteDatabase.getNotesDao()
    }

    @Provides
    @Singleton
    fun provideNotesRepository(noteDao: NoteDao): NotesRepository {
        return NotesRepositoryImpl(noteDao)
    }
}