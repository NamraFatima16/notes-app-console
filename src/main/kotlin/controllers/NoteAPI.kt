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
        return notes.stream()
            .filter{note: Note -> note.isNoteArchived}
            .count()
            .toInt()

    }

    fun numberOfActiveNotes(): Int {
        return notes.stream()
            .filter{note: Note -> !note.isNoteArchived}
            .count()
            .toInt()
    }

    fun listNotesBySelectedPriority(priority: Int): String {
        return if (numberOfNotesByPriority(priority) == 0) {
            "No Notes of that priority"
        } else {
            var noteBySel = ""
            for (i in notes.indices) {
                if (notes[i].notePriority == priority) {
                    noteBySel += "${i}: ${notes[i]} \n"
                }
            }
            return noteBySel
        }
    }

    fun numberOfNotesByPriority(priority: Int): Int {
        //helper method to determine how many notes there are of a specific priority
        return notes.stream()
            .filter{note: Note -> note.notePriority == priority}
            .count()
            .toInt()


    }

    fun listNotesByCategory(category: String): String{
        return if (numberOfNotesByCategory(category) == 0){
            "No Notes of that category"
        }else {
            var listByCat = "category: $category \n"

            for(i in notes.indices){
                if(notes[i].noteCategory.equals(category,true)){
                    listByCat += "${i}: ${notes[i]} \n"
                }
            }
            return listByCat
        }


     }
    fun numberOfNotesByCategory(category: String): Int{
        //number of notes in a specific category

        return notes.stream()
            .filter{note: Note -> note.noteCategory.equals(category,true)}
            .count()
            .toInt()
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