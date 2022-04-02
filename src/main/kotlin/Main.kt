import controllers.NoteAPI
import models.Note
import mu.KotlinLogging
import persistence.JSONSerializer
import persistence.YAMLSerializer
import persistence.XMLSerializer
import utils.ScannerInput.readNextDate
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import java.io.File
import java.lang.System.exit

private val logger = KotlinLogging.logger {}
//private val noteAPI = NoteAPI(XMLSerializer(File("notes.xml")))
private val noteAPI = NoteAPI(JSONSerializer(File("notes.json")))
//private val noteAPI = NoteAPI(YAMLSerializer(File("notes.yaml1")))


fun main(args: Array<String>) {
    runMenu()
}

fun runMenu() {
    do {
        val option = mainMenu()
        when (option) {
            1  -> addNote()
            2  -> runSubmenu()
            3  -> updateNote()
            4  -> deleteNote()
            5 -> archiveNote()
            6 -> searchByTitle()
            20 -> save()
            21 -> load()
            0  -> exitApp()
            else -> System.out.println("Invalid option entered: ${option}")
        }
    } while (true)
}
fun runSubmenu(){
    do{
        val option = subMenu()
        when(option){
            1 -> listNotes()
            2 -> listActiveNotes()
            3 -> listArchivedNote()
            4 -> listNotesByPriority()
            5 -> listNotesByCategory()
            6 -> listNotesByStatus()
            0 -> runMenu()
            else -> System.out.println("Invalid option entered: ${option}")
        }
    } while (true)
}

fun listNotesByStatus() {
    val sta = readNextLine("Enter the status: ")
    println(noteAPI.listNotesByStatus(sta))
}

fun listNotesByCategory() {
    val cat = readNextLine("Enter the name of the category: ")
    println(noteAPI.listNotesByCategory(cat))
}

fun listNotesByPriority() {
    val prio = readNextInt("Enter the priority: ")
    println(noteAPI.listNotesBySelectedPriority(prio))

}



fun mainMenu() : Int {
    return readNextInt(""" 
         > ----------------------------------
         > |        NOTE KEEPER APP         |
         > ----------------------------------
         > | NOTE MENU                      |
         > |   1) Add a note                |
         > |   2) List notes                |
         > |   3) Update a note             |
         > |   4) Delete a note             |
         > |   5) Archive a note            |  
         > |   6) Search for a note         | 
         > |   20) Save Notes               |
         > |   21) Load Notes               |
         > ----------------------------------
         > |   0) Exit                      |
         > ----------------------------------
         > ==>> """.trimMargin(">"))
}


fun subMenu() : Int {
    return readNextInt(""" 
         > ----------------------------------
         > |        NOTE KEEPER APP         |
         > ----------------------------------
         > | List Submenu                   |
         > |   1) List all notes            |
         > |   2) List active notes         |
         > |   3) List archived notes       |
         > |   4) List Notes of Priority    |
         > |   5) List Notes of category    |
         > |   6) List Notes of status      |
         > ----------------------------------
         > |   0) Return to main menu       |
         > ----------------------------------
         > ==>> """.trimMargin(">"))

}

fun addNote(){
    //logger.info { "addNote() function invoked" }
    val noteTitle = readNextLine("Enter a title for the note: ")
    val notePriority = readNextInt("Enter a priority (1-low, 2, 3, 4, 5-high): ")
    val noteCategory = readNextLine("Enter a category for the note: ")
    val noteStatus = readNextLine("Enter the status for the note: ")
    val noteDate = readNextDate("Enter the date for the note: ")
    val noteContent = readNextLine("Enter a content for the note: ")
    val isAdded = noteAPI.add(Note(noteTitle, notePriority, noteCategory, false,noteStatus,noteContent,noteDate)
     )

    if (isAdded) {
        println("Added Successfully")
    } else {
        println("Add Failed")
    }
}

fun listNotes(){
    //logger.info { "listNotes() function invoked" }
    println(noteAPI.listAllNotes())
}


fun updateNote() {
    //logger.info { "updateNotes() function invoked" }
    listNotes()
    if (noteAPI.numberOfNotes() > 0) {
        //only ask the user to choose the note if notes exist
        val indexToUpdate = readNextInt("Enter the index of the note to update: ")
        if (noteAPI.isValidIndex(indexToUpdate)) {
            val noteTitle = readNextLine("Enter a title for the note: ")
            val notePriority = readNextInt("Enter a priority (1-low, 2, 3, 4, 5-high): ")
            val noteCategory = readNextLine("Enter a category for the note: ")
            val noteStatus = readNextLine("Enter the status for the note: ")
            val noteDate = readNextDate("Enter the date for the note: ")
            val noteContent = readNextLine("Enter a content for the note: ")


            //pass the index of the note and the new note details to NoteAPI for updating and check for success.
            if (noteAPI.updateNote(indexToUpdate, Note(noteTitle, notePriority, noteCategory, false,noteStatus,noteContent,noteDate))){
                println("Update Successful")
            } else {
                println("Update Failed")
            }
        } else {
            println("There are no notes for this index number")
        }
    }
}

fun deleteNote(){
    //logger.info { "deleteNotes() function invoked" }
    listNotes()
    if (noteAPI.numberOfNotes() > 0) {
        //only ask the user to choose the note to delete if notes exist
        val indexToDelete = readNextInt("Enter the index of the note to delete: ")
        //pass the index of the note to NoteAPI for deleting and check for success.
        val noteToDelete = noteAPI.deleteNote(indexToDelete)
        if (noteToDelete != null) {
            println("Delete Successful! Deleted note: ${noteToDelete.noteTitle}")
        } else {
            println("Delete NOT Successful")
        }
    }
}

fun save() {
    try {
        noteAPI.store()
    } catch (e: Exception) {
        System.err.println("Error writing to file: $e")
    }
}

fun load() {
    try {
        noteAPI.load()
    } catch (e: Exception) {
        System.err.println("Error reading from file: $e")
    }
    fun listActiveNotes() {
        println(noteAPI.listActiveNotes())
    }


}
fun listActiveNotes() {
    println(noteAPI.listActiveNotes())
}

fun archiveNote() {
    listActiveNotes()
    if (noteAPI.numberOfActiveNotes() > 0) {
        //only ask the user to choose the note to archive if active notes exist
        val indexToArchive = readNextInt("Enter the index of the note to archive: ")
        //pass the index of the note to NoteAPI for archiving and check for success.
        if (noteAPI.archiveNote(indexToArchive)) {
            println("Archive Successful!")
        } else {
            println("Archive NOT Successful")
        }
    }
}
fun listArchivedNote(){
    println(noteAPI.listArchivedNotes())
}
fun searchByTitle(): String {
    val title = readNextLine("Enter the title you want to search: ")
    val result = noteAPI.searchByTitle(title)
    return if (result.isEmpty()){
        "No notes found by that $title title"
    }
    else result

}

fun exitApp(){
    println("Exiting...bye")
    exit(0)
}