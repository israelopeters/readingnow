package com.example.readingnow.model

import java.time.LocalDate

data class BookPostCreation(
    val imageUrl: String? = null,
    val bookAuthor: String? = null,
    val numberOfPages: Long? = null,
    val bookSummary: String? = null,
    val readingStatus: ReadingStatus? = null,
)