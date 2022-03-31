package controllers

import models.Note

private var notes = ArrayList<Note>()
class NoteAPI {

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

}