package com.codewithharsh.notesapp.data.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteQueryBuilder
import com.codewithharsh.notesapp.data.local.NOTES_TABLE
import com.codewithharsh.notesapp.data.local.NOTE_DATABASE
import com.codewithharsh.notesapp.data.local.NoteDao
import com.codewithharsh.notesapp.data.local.NoteDatabase
import com.codewithharsh.notesapp.data.local.NoteEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class NotesContentProvider : ContentProvider() {

    lateinit var noteDatabase: NoteDatabase
    lateinit var noteDao: NoteDao

    companion object {
        val AUTHORITY = "com.codewithharsh.notesapp.provider"
        val CONTENT_URI = Uri.parse("content://$AUTHORITY/$NOTES_TABLE")
    }

    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI(AUTHORITY, NOTES_TABLE, 1)
        addURI(AUTHORITY, NOTES_TABLE.plus("#"), 2)
    }

    override fun onCreate(): Boolean {
        val context = context ?: return false
        noteDatabase =
            Room.databaseBuilder(context, NoteDatabase::class.java, NOTE_DATABASE).build()
        noteDao = noteDatabase.getNotesDao()
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor {
        return when (uriMatcher.match(uri)) {
            1 -> {
                val queryBuilder = SupportSQLiteQueryBuilder.builder(NOTES_TABLE)
                    .columns(projection)
                    .selection(selection, selectionArgs)
                    .orderBy(sortOrder)
                    .create()
                runBlocking(Dispatchers.IO) { noteDatabase.query(queryBuilder) }
            }

            2 -> {
                val noteId = ContentUris.parseId(uri).toInt()
                val queryBuilder = SupportSQLiteQueryBuilder.builder(NOTES_TABLE)
                    .columns(projection)
                    .selection("${NoteEntity::id.name} = ?", arrayOf(noteId.toString()))
                    .orderBy(sortOrder)
                    .create()
                runBlocking(Dispatchers.IO) { noteDatabase.query(queryBuilder) }
            }

            else -> {
                throw IllegalArgumentException("No uri found")
            }
        }
    }

    override fun getType(uri: Uri): String? {      //MIME type" (Multipurpose Internet Mail Extensions type)
        when (uriMatcher.match(uri)) {
            1 -> return "vnd.android.cursor.dir/$AUTHORITY.$NOTES_TABLE"   // This will represent list of NoteEntity
            2 -> return "vnd.android.cursor.item/$AUTHORITY.$NOTES_TABLE"  // This will represent single NoteEntity
            else -> throw IllegalArgumentException("No uri found")
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val noteEntity = NoteEntity(
            title = values?.getAsString(NoteEntity::title.name).orEmpty(),
            desc = values?.getAsString(NoteEntity::desc.name).orEmpty()
        )
        val id = runBlocking(Dispatchers.IO) { noteDao.insertNote(noteEntity) }
        return ContentUris.withAppendedId(CONTENT_URI, id)
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val noteId = ContentUris.parseId(uri).toInt()
        val noteEntity = runBlocking(Dispatchers.IO) { noteDao.getNoteById(noteId) }
        val id = runBlocking(Dispatchers.IO) { noteDao.deleteNote(noteEntity) }
        return id
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        val noteId = ContentUris.parseId(uri).toInt()
        val noteEntity = runBlocking(Dispatchers.IO) { noteDao.getNoteById(noteId) }
        val updatedNoteEntity = noteEntity.copy(
            title = values?.getAsString(NoteEntity::title.name).orEmpty(),
            desc = values?.getAsString(NoteEntity::desc.name).orEmpty(),
            id = noteEntity.id
        )
        val id = runBlocking(Dispatchers.IO) { noteDao.updateNote(updatedNoteEntity) }
        return id
    }
}

/*
    Why Use runBlocking?
    runBlocking is generally not recommended in production code for UI-based operations because
    it blocks the thread it's running on. However, it might be used in situations where you need to
    perform a database operation synchronously, such as in initialization code or unit tests where
    you don't have access to a coroutine scope.
 */