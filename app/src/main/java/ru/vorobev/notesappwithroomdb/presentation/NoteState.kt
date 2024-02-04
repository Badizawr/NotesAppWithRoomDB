package ru.vorobev.notesappwithroomdb.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class NoteState(
    val notes: List<ru.vorobev.notesappwithroomdb.data.Note> = emptyList(),
    val title: MutableState<String> = mutableStateOf(value = ""),
    val description: MutableState<String> = mutableStateOf(value = "")
)
