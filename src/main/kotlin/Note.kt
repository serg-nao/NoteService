package ru.netology

data class Note (
    val noteId: UInt,
    val title: String,
    val text: String,
    val privacy: Int,
    val commentPrivacy: Int
        )