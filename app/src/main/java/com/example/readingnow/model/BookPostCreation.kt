package com.example.readingnow.model

data class BookPostCreation(
    val imageUrl: String? = null,
    val bookAuthor: String? = null,
    val numberOfPages: Long? = null,
    val bookSummary: String? = null,
    val readingStatus: ReadingStatus? = null,
)