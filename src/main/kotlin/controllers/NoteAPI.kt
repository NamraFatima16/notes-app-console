package controllers

import models.Note
import persistence.Serializer


class NoteAPI(serializerType: Serializer){
    private var serializer: Serializer = serializerType
    private var notes = ArrayList<Note>()
    fun add(note: Note): Boolean {
        return notes.add(note)
    }

    fun listAllNotes(): String {
        return if (notes.isEmpty()) {
            "No notes stored"
        } else {
            var listOfNotes = ""
            for (i in notes.indices) {
                listOfNotes += "${i}: ${notes[i]} \n"
            }
            listOfNotes
        }

    }
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

    fun listActiveNotes(): String {
        return if(numberOfActiveNotes() == 0 ){
            "No Active Notes Stored"
        } else{
            var listOfActNotes = ""
            for(i in notes.indices){
                if(!notes[i].isNoteArchived){
                    listOfActNotes += "${i}: ${notes[i]} \n"
                }
            }
            return listOfActNotes
        }

    }

    fun listArchivedNotes(): String {
        return if(numberOfArchivedNotes() == 0 ){
            "No Archived Notes Stored"
        } else{
            var listOfArcNotes = ""
            for(i in notes.indices){
                if(notes[i].isNoteArchived){
                    listOfArcNotes += "${i}: ${notes[i]} \n"
                }
            }
            return listOfArcNotes
        }

    }

    fun numberOfArchivedNotes(): Int {
        //helper method to determine how many archived notes there are
        var archivedNote = 0
        for (note in notes){
            if (note.isNoteArchived){
                archivedNote++
            }
        }
        return archivedNote
    }

    fun numberOfActiveNotes(): Int {
        //helper method to determine how many active notes there are
        var activeNotes = 0
        for(note in notes){
            if(!note.isNoteArchived){
                activeNotes++
            }
        }
        return activeNotes
    }

    fun listNotesBySelectedPriority(priority: Int): String {
        return if (numberOfNotesByPriority(priority) == 0) {
            "No Notes of that priority"
        } else {
            var noteBySel = ""
            for (i in notes.indices) {
                if (notes[i].notePriority == priority) {
                    noteBySel += "${i}: ${notes[i]}"
                }
            }
            return noteBySel
        }
    }

    fun numberOfNotesByPriority(priority: Int): Int {
        //helper method to determine how many notes there are of a specific priority
        var priNumber = 0
        for(note in notes){
            if(note.notePriority == priority){
                priNumber++
            }
        }
            return priNumber

    }
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
            return true
        }

        //if the note was not found, return false, indicating that the update was not successful
        return false
    }
    fun isValidIndex(index: Int) :Boolean{
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


}