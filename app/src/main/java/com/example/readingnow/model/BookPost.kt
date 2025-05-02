package com.example.readingnow.model

import java.time.LocalDate

data class BookPost(
    val imageUrl: String? = null,
    val bookAuthor: String? = null,
    val userAuthor: User? = null,
    val numberOfPages: Long? = null,
    val bookSummary: String? = null,
    val readingStatus: ReadingStatus? = null,
    val dateCreated: LocalDate? = null
)
