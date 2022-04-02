package controllers

import models.Note
import persistence.Serializer


class NoteAPI(serializerType: Serializer) {
    private var serializer: Serializer = serializerType
    private var notes = ArrayList<Note>()
    fun add(note: Note): Boolean {
        return notes.add(note)
    }

    fun listAllNotes(): String =
        if (notes.isEmpty()) "No notes stored"
        else formatListString(notes)

    fun numberOfNotes(): Int {
        return notes.size
    }

    fun findNote(index: Int): Note? {
        return if (isValidListIndex(index, notes)) {
            notes[index]

        } else null
    }

    //utility method to determine if an index is valid in a list.
    fun isValidListIndex(index: Int, list: List<Any>): Boolean {
        return (index >= 0 && index < list.size)

    }

    fun listActiveNotes(): String =
        if (numberOfActiveNotes() == 0) "No Active Notes Stored"
        else formatListString(notes.filter { note: Note -> !note.isNoteArchived })


    fun listArchivedNotes(): String =
        if (numberOfArchivedNotes() == 0) "No Archived Notes Stored"
        else formatListString(notes.filter { note: Note -> note.isNoteArchived })

    fun numberOfArchivedNotes(): Int = notes.count { note: Note -> note.isNoteArchived }

    fun numberOfActiveNotes(): Int = notes.count { note: Note -> !note.isNoteArchived }

    fun listNotesBySelectedPriority(priority: Int):
            String =
        if (numberOfNotesByPriority(priority) == 0) "No Notes of that priority"
        else formatListString(notes.filter { note: Note -> note.notePriority == priority })


    fun numberOfNotesByPriority(priority: Int): Int = notes.count { note: Note -> note.notePriority == priority }

    fun listNotesByCategory(category: String):

            String =
        if (numberOfNotesByCategory(category) == 0) "No Notes of that category"
        else formatListString(notes.filter { note: Note -> note.noteCategory.equals(category,true)})



    fun numberOfNotesByCategory(category: String): Int = notes.count { note: Note -> note.noteCategory.equals(category,true)}

    fun numberOfNotesByStatus(status: String): Int = notes.count{ note: Note -> note.noteStatus.equals(status,true)}

    fun listNotesByStatus(status:String):
            String =
        if (numberOfNotesByStatus(status) == 0) "No Notes of $status"
        else formatListString(notes.filter { note: Note -> note.noteStatus.equals(status,true)})


    fun deleteNote(indexToDelete: Int): Note? {
        return if (isValidListIndex(indexToDelete, notes)) {
            notes.removeAt(indexToDelete)
        } else null
    }

    fun updateNote(indexToUpdate: Int, note: Note?): Boolean {
        //find the note object by the index number
        val foundNote = findNote(indexToUpdate)

        //if the note exists, use the note details passed as parameters to update the found note in the ArrayList.
        if ((foundNote != null) && (note != null)) {
            foundNote.noteTitle = note.noteTitle
            foundNote.notePriority = note.notePriority
            foundNote.noteCategory = note.noteCategory
            foundNote.noteStatus = note.noteStatus
            foundNote.noteContent = note.noteContent
            foundNote.noteDate = note.noteDate
            return true
        }

        //if the note was not found, return false, indicating that the update was not successful
        return false
    }

    fun isValidIndex(index: Int): Boolean {
        return isValidListIndex(index, notes);
    }

    @Throws(Exception::class)
    fun load() {
        notes = serializer.read() as ArrayList<Note>
    }

    @Throws(Exception::class)
    fun store() {
        serializer.write(notes)
    }

    fun archiveNote(indexToArchive: Int): Boolean {
        if (isValidIndex(indexToArchive)) {
            val noteToArchive = notes[indexToArchive]
            if (!noteToArchive.isNoteArchived) {
                noteToArchive.isNoteArchived = true
                return true
            }
        }
        return false
    }

    fun searchByTitle(title: String): String =
        formatListString(
             notes.stream().filter { note -> note.noteTitle.contains(title, true) }
            .toList()
        )

    fun formatListString(notesToFormat : List<Note>) : String =
        notesToFormat
            .joinToString (separator = "\n") { note ->
                notes.indexOf(note).toString() + ": " + note.toString() }

}


