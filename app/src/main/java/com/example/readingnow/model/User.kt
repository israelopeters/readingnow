package com.example.readingnow.model

data class User(
    val email: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val username: String? = null,
    val role: List<Role>? = null
)
