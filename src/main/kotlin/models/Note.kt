package models

import java.util.*

data class Note(
    var noteTitle: String,
    var notePriority: Int,
    var noteCategory: String,
    var isNoteArchived :Boolean,
    var noteStatus: String,
    var noteContent: String,
    var noteDate: Date ){
}