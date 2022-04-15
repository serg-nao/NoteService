package ru.netology

fun main() {
    val noteService = NoteService()
    val a = noteService.add("Моя запись", "Это моя первая запись", 2, 2)
    println("Результат добавления записи $a")
    val b = noteService.createComment(1u, 1u, null, "Это мой комментарий", "1")
    println("Результат добавления комментария $b")
    val k = noteService.edit(1u, "Обновленная запись", "Это моя обновленная запись", 1, 2)
    println("Результат обновления записи $k")
    val l = noteService.editComment(1u, "Это мой новый комментарий")
    println("Результат обновления комментария $l")
    val c1: Boolean = noteService.comments[0].undeleted
    println(c1)
    val j = noteService.deleteComment(1u)
    println("Результат удаления комментария $j")
    val c2: Boolean = noteService.comments[0].undeleted
    println(c2)
    noteService.restoreComment(1u)
    val c3: Boolean = noteService.comments[0].undeleted
    println(c3)
    val i = noteService.delete(1u)
    println("Результат удаления записи $i")
}


class NoteService {
    var notes = mutableListOf<Note>()
    var comments = mutableListOf<Comment>()

        fun add(newTitle: String, newText: String, privacy: Int, commentPrivacy: Int): UInt {
        val note = Note(
            noteId = if (notes.isEmpty()) 1u else (notes.last().noteId + 1u),
            title = newTitle,
            text = newText,
            privacy = privacy,
            commentPrivacy = commentPrivacy
        )
        notes += note
        return notes.last().noteId
    }

    @Throws(ElementNotFoundException::class)
    fun createComment(noteId: UInt, ownerId: UInt?, replyTo: UInt?, message: String, guid: String): UInt {
        val index: Int = findNoteInList(noteId)
        if (index >= 0) {
            val comment = Comment(
                commentId = if (comments.isEmpty()) 1u else (comments.last().commentId + 1u),
                noteId = noteId,
                ownerId = ownerId,
                replyTo = replyTo,
                message = message,
                guid = guid
            )
            comments += comment
            return comments.last().commentId
        } else throw ElementNotFoundException("Такая запись не найдена")
    }

    private fun findNoteInList(noteId: UInt): Int {
        for (i in notes.indices) {
            if (noteId == notes[i].noteId) return i
        }
        return -1
    }

    @Throws(ElementNotFoundException::class)
    fun delete(noteId: UInt): Int {
        val index = findNoteInList(noteId)
        if (findNoteInList(noteId) >= 0) {
            notes.removeAt(index)
            return 1
        } else {
            throw ElementNotFoundException("Такая запись не найдена")
        }
    }

    private fun findCommentInList(commentId: UInt): Int {
        for (i in comments.indices) {
            if (commentId == comments[i].commentId) return i
        }
        return -1
    }

    @Throws(ElementNotFoundException::class)
    fun deleteComment(commentId: UInt): Int {
        val index = findCommentInList(commentId)
        if (index >= 0) {
            if (comments[index].undeleted) {
                comments[index].undeleted = false
                return 1
            }
        }
        throw ElementNotFoundException("Такой комментарий не найден")
    }

    @Throws(ElementNotFoundException::class)
    fun edit(noteId: UInt, title: String, text: String, privacy: Int, commentPrivacy: Int): Int {
            val index = findNoteInList(noteId)
            if (index >= 0) {
                notes[index] = Note(noteId, title, text, privacy, commentPrivacy)
                return 1
            }
            throw ElementNotFoundException("Такая запись не найдена")
        }

    @Throws(ElementNotFoundException::class)
    fun editComment(commentId: UInt, message: String): Int {
        val index = findCommentInList(commentId)
        if (index >= 0 && comments[index].undeleted) {
                comments[index].message = message
                return 1
            }
        throw ElementNotFoundException("Такой комментарий не найден")
    }

    fun get(noteIds: MutableList<UInt>, count: Int): List<Note> {
        var listOfNotes = mutableListOf<Note>()
        var thisCount: Int = count
        for (i in notes.indices) {
            for (j in noteIds.indices) {
                if ((noteIds[j] == notes[i].noteId) && (thisCount > 0)) {
                    listOfNotes += notes[i]
                    thisCount --
                }
            }
        }
        return listOfNotes
    }

    @Throws(ElementNotFoundException::class)
    fun getById(noteId: UInt): Note {
        for (index in notes.indices) {
            if (notes[index].noteId == noteId) return notes[index]
        }
        throw ElementNotFoundException("Такая запись не найдена")
    }

    fun getComments(noteId: UInt, count: Int): List<Comment> {
        var listOfComments = mutableListOf<Comment>()
        var thisCount: Int = count
        for (index in comments.indices) {
            if ((comments[index].noteId == noteId) && comments[index].undeleted && thisCount > 0) {
                listOfComments += comments[index]
                thisCount--
            }
        }
        return listOfComments
    }

    fun restoreComment(commentId: UInt): Int {
        val index = findCommentInList(commentId)
        if (index >= 0 && !comments[index].undeleted) {
                comments[index].undeleted = true
                return 1
            }
        throw ElementNotFoundException("Такой комментарий не найден")
    }
}