package ru.vorobev.notesappwithroomdb.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.vorobev.notesappwithroomdb.data.Note
import ru.vorobev.notesappwithroomdb.data.NoteDao

class NotesViewModel(
    private val dao: NoteDao
): ViewModel() {
    private val isSortedByDateAdded = MutableStateFlow(true)

    private val notes = isSortedByDateAdded.flatMapLatest { sort ->
        if (sort) {
            dao.getNotesOrderByDateAdded()
        } else {
            dao.getNotesOrderByTitle()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val _state = MutableStateFlow(NoteState())
    val state =
        combine(_state, isSortedByDateAdded, notes) { state, isisSortedByDateAdded, notes ->
            state.copy(
                notes = notes
            )

        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NoteState())
    fun onEvent(event: NotesEvent) {
        when (event) {
            is NotesEvent.DeleteNote -> {
                viewModelScope.launch {
                    dao.deleteNote(event.note)
                }
            }

            is NotesEvent.SaveNote -> {
                val note = Note(
                    title = Note (
                        title = state.value.title.value,
                        description = state.value.description.value,
                        dateAdded = System.currentTimeMillis()
                    )
                )
            }

            NotesEvent.SortNotes -> {
                isSortedByDateAdded.value = !isSortedByDateAdded.value
            }
        }
    }
}