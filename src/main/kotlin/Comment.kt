package ru.netology

data class Comment (
    val commentId: UInt,
    val noteId: UInt,
    val ownerId: UInt?,
    val replyTo: UInt?,
    var message: String,
    val guid: String,
    var undeleted: Boolean = true
)