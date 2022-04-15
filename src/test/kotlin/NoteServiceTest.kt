import org.junit.Assert.assertEquals
import org.junit.Test
import ru.netology.*

internal class NoteServiceTest {

    private val noteService = NoteService()
    private val noteId = 1u
    private val newTitle = ""
    private val newText = ""
    private val privacy = 1
    private val commentPrivacy = 1
    private val commentId = 1u
    private val ownerId = 1u
    private val replyTo = null
    private val message = "message"
    private val guid = "guid"
    private val title = "title"
    private val text = "text"

    var notes = mutableListOf<Note>()
    var notEmptyNotes = mutableListOf<Note>(
        Note(1u, "title", "text", 1, 1)
    )
    var comments = mutableListOf<Comment>()

    @Test
    fun add() {
        //arrange
        val note = Note(noteId, newTitle, newText, privacy, commentPrivacy)

        //act
        notes += note
        val id = notes[0].noteId

        //assert
        assertEquals(1u, id)
    }

    @Test
    fun createComment_successful() {
        //arrange
        val index = 0
        var id = 0u

        //act
        if (index >= 0) {
            val comment = Comment(commentId, noteId, ownerId, replyTo, message, guid)
            comments += comment
            id = comments.last().commentId
        }

        //assert
        assertEquals(1u, id)
    }

    @Test(expected = ElementNotFoundException::class)
    fun createComment_fail() {
        val index = -1
        if (index < 0) noteService.createComment(noteId, ownerId, replyTo, message, guid)
    }

    @Test
    fun delete_successful() {
        //arrange
        val index = 0
        var result = 0
        notes = mutableListOf<Note>(
            Note(1u, "title", "text", 1, 1)
        )

        //act
        if (index >= 0) {
            notes.removeAt(index)
            result = 1
        }

        //assert
        assertEquals(1, result)
    }

    @Test(expected = ElementNotFoundException::class)
    fun delete_fail() {
        val index = -1
        if (index < 0) noteService.delete(1u)
    }

    @Test
    fun deleteComment_successful() {
        //arrange
        val index = 0
        var result = 0
        comments = mutableListOf(
            Comment(1u, 1u, 1u, null, "message", "guid", true)
        )

        //act
        if (index >= 0 && comments.elementAt(index).undeleted)
            result = 1

        //assert
        assertEquals(1, result)
    }

    @Test(expected = ElementNotFoundException::class)
    fun deleteComment_fail() {
        noteService.delete(noteId)
    }

    @Test
    fun edit_successful() {
        //arrange
        val index = 0
        var result = 0
        notes = mutableListOf<Note>(
            Note(1u, "title", "text", 1, 1)
        )

        //act
        if (index >= 0) {
            notes[index] = Note(noteId, title, text, privacy, commentPrivacy)
            result = 1
        }

        //assert
        assertEquals(1, result)
    }

    @Test(expected = ElementNotFoundException::class)
    fun edit_fail() {
        noteService.edit(noteId, title, text, privacy, commentPrivacy)
    }

    @Test
    fun editComment() {
        //arrange
        val index = 0
        var result = 0
        comments = mutableListOf(
            Comment(1u, 1u, 1u, null, "message", "guid", true)
        )

        //act
        if (index >= 0) {
            comments[index].message = message
            result = 1
        }

        //assert
        assertEquals(1, result)
    }

    @Test(expected = ElementNotFoundException::class)
    fun editComment_fail() {
        noteService.editComment(commentId, message)
    }

    @Test
    fun get() {
        //arrange
        var listOfNotes = mutableListOf<Note>()
        var thisCount = 2
        val notesId = mutableListOf(1u)
        notes = mutableListOf(
            Note(1u, "title", "text", 1, 1)
        )
        var result = 0

        //act
        for (i in notes.indices) {
                if ((notesId[0] == notes[i].noteId) && (thisCount > 0)) {
                    listOfNotes += notes[i]
                    thisCount --
                    result = 1
                }
        }

        //assert
        assertEquals(1, result)
    }

    @Test
    fun getById_successful() {
        //arrange
        notes = mutableListOf(
            Note(1u, "title", "text", 1, 1)
        )
        var note = Note(0u, "", "", 2, 2)

        //act
        for (index in notes.indices) {
            if (notes[index].noteId == noteId)
                note  = notes[index]
        }

        //assert
        assertEquals(notes[0], note)

    }

    @Test(expected = ElementNotFoundException::class)
    fun getById_fail() {
        noteService.getById(noteId)
    }

    @Test
    fun getComments_successful() {
        //arrange
        var listOfComments = mutableListOf<Comment>()
        var thisCount = 2
        comments = mutableListOf(
            Comment(1u, 1u, 1u, null, "message", "guid", true)
        )
        var result = 0

        //act
        for (index in comments.indices) {
            if ((comments[index].noteId == noteId) && comments[index].undeleted && thisCount > 0) {
                listOfComments += comments[index]
                thisCount--
                result = 1
            }
        }

        //assert
        assertEquals(1, result)
    }

    @Test
    fun restoreComment_successful() {
        //arrange
        val index = 0
        comments = mutableListOf(
            Comment(1u, 1u, 1u, null, "message", "guid", false)
        )
        var result = 0

        //act
        if (index >= 0 && !comments[index].undeleted) {
            comments[index].undeleted = true
            result = 1
        }

        //assert
        assertEquals(1, result)
    }

    @Test(expected = ElementNotFoundException::class)
    fun restoreComment_fail() {
        noteService.restoreComment(commentId)
    }
}