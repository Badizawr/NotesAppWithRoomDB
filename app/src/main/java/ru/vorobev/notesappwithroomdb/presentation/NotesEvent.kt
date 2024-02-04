package ru.vorobev.notesappwithroomdb.presentation

import ru.vorobev.notesappwithroomdb.data.Note

sealed interface NotesEvent {
    data object SortNotes: NotesEvent

    data class DeleteNote(val note: Note): NotesEvent

    data class  SaveNote(
        val title: String,
        val description: String
    ): NotesEvent
}