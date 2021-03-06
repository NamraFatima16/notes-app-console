package controllers

import models.Note
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import persistence.JSONSerializer
import persistence.XMLSerializer
import persistence.YAMLSerializer
import java.io.File
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class NoteAPITest {

    private var learnKotlin: Note? = null
    private var summerHoliday: Note? = null
    private var codeApp: Note? = null
    private var testApp: Note? = null
    private var swim: Note? = null
    private var populatedNotes: NoteAPI? = NoteAPI(XMLSerializer(File("notes.xml")))
    private var emptyNotes: NoteAPI? = NoteAPI(XMLSerializer(File("notes.xml")))

    @BeforeEach
    fun setup() {
        val date = LocalDate.parse("2022-08-12")
        learnKotlin = Note("Learning Kotlin", 5, "College", false, "to do", "Complete Assignment", date)
        summerHoliday = Note("Summer Holiday to France", 1, "Holiday", false, "completed", "Look up prices for hotels", date)
        codeApp = Note("Code App", 4, "Work", true, "doing", "Ask question onn stack over flow", date)
        testApp = Note("Test App", 4, "Work", false, "to do", "Create the domain specific language parser to read pair coefficience coming from DFT hessian QM optimization.", date)
        swim = Note("Swim - Pool", 3, "Hobby", true, "completed", "Drink Water not monster", date)

        // adding 5 Note to the notes api
        populatedNotes!!.add(learnKotlin!!)
        populatedNotes!!.add(summerHoliday!!)
        populatedNotes!!.add(codeApp!!)
        populatedNotes!!.add(testApp!!)
        populatedNotes!!.add(swim!!)
    }

    @AfterEach
    fun tearDown() {
        learnKotlin = null
        summerHoliday = null
        codeApp = null
        testApp = null
        swim = null
        populatedNotes = null
        emptyNotes = null
    }

    @Nested
    inner class AddNotes {
        @Test
        fun `adding a Note to a populated list adds to ArrayList`() {
            val newNote = Note("Study Lambdas", 1, "College", false, "to do", "read class material", LocalDate.now())
            assertEquals(5, populatedNotes!!.numberOfNotes())
            assertTrue(populatedNotes!!.add(newNote))
            assertEquals(6, populatedNotes!!.numberOfNotes())
            assertEquals(newNote, populatedNotes!!.findNote(populatedNotes!!.numberOfNotes() - 1))
        }

        @Test
        fun `adding a Note to an empty list adds to ArrayList`() {
            val newNote = Note("Study Lambdas", 1, "College", false, "to do", "read class material", LocalDate.now())
            assertEquals(0, emptyNotes!!.numberOfNotes())
            assertTrue(emptyNotes!!.add(newNote))
            assertEquals(1, emptyNotes!!.numberOfNotes())
            assertEquals(newNote, emptyNotes!!.findNote(emptyNotes!!.numberOfNotes() - 1))
        }
    }

    @Nested
    inner class ListNotes {

        @Test
        fun `listAllNotes returns No Notes Stored message when ArrayList is empty`() {
            assertEquals(0, emptyNotes!!.numberOfNotes())
            assertTrue(emptyNotes!!.listAllNotes().lowercase().contains("no notes"))
        }

        @Test
        fun `listAllNotes returns Notes when ArrayList has notes stored`() {
            assertEquals(5, populatedNotes!!.numberOfNotes())
            val notesString = populatedNotes!!.listAllNotes().lowercase()
            assertTrue(notesString.contains("learning kotlin"))
            assertTrue(notesString.contains("code app"))
            assertTrue(notesString.contains("test app"))
            assertTrue(notesString.contains("swim"))
            assertTrue(notesString.contains("summer holiday"))
        }

        @Test
        fun `listArchivedNotes returns archived notes when array list have notes stored`() {
            assertEquals(2, populatedNotes!!.numberOfArchivedNotes())
            val notesString = populatedNotes!!.listArchivedNotes().lowercase()
            assertTrue(notesString.contains("code app"))
            assertTrue(notesString.contains("swim"))
        }

        @Test
        fun `listActiveNotes returns active notes when array list have notes stored`() {
            assertEquals(3, populatedNotes!!.numberOfActiveNotes())
            val notesString = populatedNotes!!.listActiveNotes().lowercase()
            assertTrue(notesString.contains("learning kotlin"))
            assertTrue(notesString.contains("test app"))
            assertTrue(notesString.contains("summer holiday"))
        }

        @Test
        fun `listArchivedNotes return No Archived Notes Stored message when ArrayList is empty`() {
            assertEquals(0, emptyNotes!!.numberOfNotes())
            assertTrue(emptyNotes!!.listArchivedNotes().lowercase().contains("no archived notes stored"))
        }

        @Test
        fun `listActiveNotes return No Active Notes Stored message when ArrayList is empty`() {
            assertEquals(0, emptyNotes!!.numberOfNotes())
            assertTrue(emptyNotes!!.listActiveNotes().lowercase().contains("no active notes stored"))
        }

        @Test
        fun `listNotesBySelectedPriority returns No Notes when ArrayList is empty`() {
            assertEquals(0, emptyNotes!!.numberOfNotes())
            assertTrue(
                emptyNotes!!.listNotesBySelectedPriority(1).lowercase().contains("no notes")
            )
        }
        @Test
        fun `listNotesBySelectedPriority returns no notes when no notes of that priority exist`() {
            assertEquals(5, populatedNotes!!.numberOfNotes())
            assertTrue(populatedNotes!!.listNotesBySelectedPriority(2).lowercase().contains("no notes"))
        }
        @Test
        fun `listNotesBySelectedPriority returns notes of priority when arraylist have notes stored`() {
            assertEquals(5, populatedNotes!!.numberOfNotes())
            val notesString = populatedNotes!!.listNotesBySelectedPriority(4).lowercase()
            assertTrue(notesString.contains("test app"))
            assertTrue(notesString.contains("code app"))
        }
        @Test
        fun `listNotesByCategory returns no notes when arraylist is empty`() {
            assertEquals(0, emptyNotes!!.numberOfNotes())
            assertTrue(emptyNotes!!.listNotesByCategory("work").lowercase().contains("no notes"))
        }
        @Test
        fun `listNotesByCategory return no notes when no notes of that category exist`() {
            assertEquals(5, populatedNotes!!.numberOfNotes())
            assertTrue(populatedNotes!!.listNotesByCategory("monster").lowercase().contains("no notes"))
        }
        @Test
        fun `listNotesByCategory returns notes of category when arraylist has notes stored`() {
            assertEquals(5, populatedNotes!!.numberOfNotes())
            val notesString = populatedNotes!!.listNotesByCategory("work").lowercase()
            assertTrue(notesString.contains("test app"))
            assertTrue(notesString.contains("code app"))
            assertTrue(notesString.contains("work"))
        }
        @Test
        fun`listNotesByStatus return no notes when arraylist is empty`() {
            assertEquals(0, emptyNotes!!.numberOfNotes())
            assertTrue(emptyNotes!!.listNotesByStatus("to do").lowercase().contains("no notes"))
        }
        @Test
        fun `listNotesByStatus return no notes when no notes of that status exist`() {
            assertEquals(5, populatedNotes!!.numberOfNotes())
            assertTrue(populatedNotes!!.listNotesByStatus("monster").lowercase().contains("no notes"))
        }
        @Test
        fun `listNotesByStatus returns notes of status when arraylist has notes stored`() {
            assertEquals(5, populatedNotes!!.numberOfNotes())
            val notesString = populatedNotes!!.listNotesByStatus("to do").lowercase()
            assertTrue(notesString.contains("test app"))
            assertTrue(notesString.contains("learning kotlin"))
        }
    }

    @Nested
    inner class DeleteNotes {

        @Test
        fun `deleting a Note that does not exist, returns null`() {
            assertNull(emptyNotes!!.deleteNote(0))
            assertNull(populatedNotes!!.deleteNote(-1))
            assertNull(populatedNotes!!.deleteNote(5))
        }

        @Test
        fun `deleting a note that exists delete and returns deleted object`() {
            assertEquals(5, populatedNotes!!.numberOfNotes())
            assertEquals(swim, populatedNotes!!.deleteNote(4))
            assertEquals(4, populatedNotes!!.numberOfNotes())
            assertEquals(learnKotlin, populatedNotes!!.deleteNote(0))
            assertEquals(3, populatedNotes!!.numberOfNotes())
        }
    }

    @Nested
    inner class UpdateNotes {
        @Test
        fun `updating a note that does not exist returns false`() {
            assertFalse(populatedNotes!!.updateNote(6, Note("Updating Note", 2, "Work", false, "to do", "blah", LocalDate.now())))
            assertFalse(populatedNotes!!.updateNote(-1, Note("Updating Note", 2, "Work", false, "to do", "blah", LocalDate.now())))
            assertFalse(emptyNotes!!.updateNote(0, Note("Updating Note", 2, "Work", false, "to do", "blah", LocalDate.now())))
        }

        @Test
        fun `updating a note that exists returns true and updates`() {
            // check note 5 exists and check the contents
            assertEquals(swim, populatedNotes!!.findNote(4))
            assertEquals("Swim - Pool", populatedNotes!!.findNote(4)!!.noteTitle)
            assertEquals(3, populatedNotes!!.findNote(4)!!.notePriority)
            assertEquals("Hobby", populatedNotes!!.findNote(4)!!.noteCategory)
            assertEquals("completed", populatedNotes!!.findNote(4)!!.noteStatus)
            assertEquals("Drink Water not monster", populatedNotes!!.findNote(4)!!.noteContent)
            assertEquals(LocalDate.parse("2022-08-12"), populatedNotes!!.findNote(4)!!.noteDate)

            // update note 5 with new information and ensure contents updated successfully
            assertTrue(populatedNotes!!.updateNote(4, Note("Updating Note", 2, "College", false, "to do", "blah", LocalDate.now())))
            assertEquals("Updating Note", populatedNotes!!.findNote(4)!!.noteTitle)
            assertEquals(2, populatedNotes!!.findNote(4)!!.notePriority)
            assertEquals("College", populatedNotes!!.findNote(4)!!.noteCategory)
            assertEquals("to do", populatedNotes!!.findNote(4)!!.noteStatus)
            assertEquals("blah", populatedNotes!!.findNote(4)!!.noteContent)
            assertEquals(LocalDate.now(), populatedNotes!!.findNote(4)!!.noteDate)
        }
    }
    @Nested
    inner class PersistenceTests {

        @Test
        fun `saving and loading an empty collection in XML doesn't crash app`() {
            // Saving an empty notes.XML file.
            val storingNotes = NoteAPI(XMLSerializer(File("notes.xml")))
            storingNotes.store()

            // Loading the empty notes.xml file into a new object
            val loadedNotes = NoteAPI(XMLSerializer(File("notes.xml")))
            loadedNotes.load()

            // Comparing the source of the notes (storingNotes) with the XML loaded notes (loadedNotes)
            assertEquals(0, storingNotes.numberOfNotes())
            assertEquals(0, loadedNotes.numberOfNotes())
            assertEquals(storingNotes.numberOfNotes(), loadedNotes.numberOfNotes())
        }

        @Test
        fun `saving and loading an loaded collection in XML doesn't loose data`() {
            // Storing 3 notes to the notes.XML file.
            val storingNotes = NoteAPI(XMLSerializer(File("notes.xml")))
            storingNotes.add(testApp!!)
            storingNotes.add(swim!!)
            storingNotes.add(summerHoliday!!)
            storingNotes.store()

            // Loading notes.xml into a different collection
            val loadedNotes = NoteAPI(XMLSerializer(File("notes.xml")))
            loadedNotes.load()

            // Comparing the source of the notes (storingNotes) with the XML loaded notes (loadedNotes)
            assertEquals(3, storingNotes.numberOfNotes())
            assertEquals(3, loadedNotes.numberOfNotes())
            assertEquals(storingNotes.numberOfNotes(), loadedNotes.numberOfNotes())
            assertEquals(storingNotes.findNote(0), loadedNotes.findNote(0))
            assertEquals(storingNotes.findNote(1), loadedNotes.findNote(1))
            assertEquals(storingNotes.findNote(2), loadedNotes.findNote(2))
        }

        @Test
        fun `saving and loading an empty collection in JSON doesn't crash app`() {
            // Saving an empty notes.json file.
            val storingNotes = NoteAPI(JSONSerializer(File("notes.json")))
            storingNotes.store()

            // Loading the empty notes.json file into a new object
            val loadedNotes = NoteAPI(JSONSerializer(File("notes.json")))
            loadedNotes.load()

            // Comparing the source of the notes (storingNotes) with the json loaded notes (loadedNotes)
            assertEquals(0, storingNotes.numberOfNotes())
            assertEquals(0, loadedNotes.numberOfNotes())
            assertEquals(storingNotes.numberOfNotes(), loadedNotes.numberOfNotes())
        }

        @Test
        fun `saving and loading an loaded collection in JSON doesn't loose data`() {
            // Storing 3 notes to the notes.json file.
            val storingNotes = NoteAPI(JSONSerializer(File("notes.json")))
            storingNotes.add(testApp!!)
            storingNotes.add(swim!!)
            storingNotes.add(summerHoliday!!)
            storingNotes.store()

            // Loading notes.json into a different collection
            val loadedNotes = NoteAPI(JSONSerializer(File("notes.json")))
            loadedNotes.load()

            // Comparing the source of the notes (storingNotes) with the json loaded notes (loadedNotes)
            assertEquals(3, storingNotes.numberOfNotes())
            assertEquals(3, loadedNotes.numberOfNotes())
            assertEquals(storingNotes.numberOfNotes(), loadedNotes.numberOfNotes())
            assertEquals(storingNotes.findNote(0), loadedNotes.findNote(0))
            assertEquals(storingNotes.findNote(1), loadedNotes.findNote(1))
            assertEquals(storingNotes.findNote(2), loadedNotes.findNote(2))
        }

        @Test
        fun `saving and loading an empty collection in YAML doesn't crash app`() {
            // Saving an empty notes.yaml file.
            val storingNotes = NoteAPI(YAMLSerializer(File("notes.yaml")))
            storingNotes.store()

            // Loading the empty notes.yaml file into a new object
            val loadedNotes = NoteAPI(YAMLSerializer(File("notes.yaml")))
            loadedNotes.load()

            // Comparing the source of the notes (storingNotes) with the yaml loaded notes (loadedNotes)
            assertEquals(0, storingNotes.numberOfNotes())
            assertEquals(0, loadedNotes.numberOfNotes())
            assertEquals(storingNotes.numberOfNotes(), loadedNotes.numberOfNotes())
        }
        @Test
        fun `saving and loading an loaded collection in YMAL doesn't loose data`() {
            // Storing 3 notes to the notes.yaml file.
            val storingNotes = NoteAPI(YAMLSerializer(File("notes.yaml")))
            storingNotes.add(testApp!!)
            storingNotes.add(swim!!)
            storingNotes.add(summerHoliday!!)
            storingNotes.store()

            // Loading notes.yaml into a different collection
            val loadedNotes = NoteAPI(YAMLSerializer(File("notes.yaml")))
            loadedNotes.load()

            // Comparing the source of the notes (storingNotes) with the YAML loaded notes (loadedNotes)
            assertEquals(3, storingNotes.numberOfNotes())
            assertEquals(3, loadedNotes.numberOfNotes())
            assertEquals(storingNotes.numberOfNotes(), loadedNotes.numberOfNotes())
            assertEquals(storingNotes.findNote(0), loadedNotes.findNote(0))
            assertEquals(storingNotes.findNote(1), loadedNotes.findNote(1))
            assertEquals(storingNotes.findNote(2), loadedNotes.findNote(2))
        }
    }

    @Nested
    inner class ArchiveNotes {
        @Test
        fun `archiving a note that does not exist returns false`() {
            assertFalse(populatedNotes!!.archiveNote(6))
            assertFalse(populatedNotes!!.archiveNote(-1))
            assertFalse(emptyNotes!!.archiveNote(0))
        }

        @Test
        fun `archiving an already archived note returns false`() {
            assertTrue(populatedNotes!!.findNote(2)!!.isNoteArchived)
            assertFalse(populatedNotes!!.archiveNote(2))
        }

        @Test
        fun `archiving an active note that exists returns true and archives`() {
            assertFalse(populatedNotes!!.findNote(1)!!.isNoteArchived)
            assertTrue(populatedNotes!!.archiveNote(1))
            assertTrue(populatedNotes!!.findNote(1)!!.isNoteArchived)
        }
    }

    @Nested
    inner class CountingMethods {

        @Test
        fun numberOfNotesCalculatedCorrectly() {
            assertEquals(5, populatedNotes!!.numberOfNotes())
            assertEquals(0, emptyNotes!!.numberOfNotes())
        }

        @Test
        fun numberOfArchivedNotesCalculatedCorrectly() {
            assertEquals(2, populatedNotes!!.numberOfArchivedNotes())
            assertEquals(0, emptyNotes!!.numberOfArchivedNotes())
        }

        @Test
        fun numberOfActiveNotesCalculatedCorrectly() {
            assertEquals(3, populatedNotes!!.numberOfActiveNotes())
            assertEquals(0, emptyNotes!!.numberOfActiveNotes())
        }

        @Test
        fun numberOfNotesByPriorityCalculatedCorrectly() {
            assertEquals(1, populatedNotes!!.numberOfNotesByPriority(1))
            assertEquals(0, populatedNotes!!.numberOfNotesByPriority(2))
            assertEquals(1, populatedNotes!!.numberOfNotesByPriority(3))
            assertEquals(2, populatedNotes!!.numberOfNotesByPriority(4))
            assertEquals(1, populatedNotes!!.numberOfNotesByPriority(5))
            assertEquals(0, emptyNotes!!.numberOfNotesByPriority(1))
        }
    }
    @Nested
    inner class SearchMethods {
        @Test
        fun`searchByTitle returns notes with titles when present in note list`() {
            val noteString = populatedNotes!!.searchByTitle("app").lowercase()
            assertTrue(noteString.contains("code"))
            assertTrue(noteString.contains("test "))
        }
        @Test
        fun `searchByTitle return no notes when title does not exist in arraylist`() {
            val noteString = populatedNotes!!.searchByTitle("java").lowercase()
            assertTrue(noteString.isEmpty())
        }
    }
}
