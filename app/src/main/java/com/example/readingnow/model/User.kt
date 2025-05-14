package com.example.readingnow.model

data class User(
    val email: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val username: String? = null,
    val profilePicUrl: String? = null,
    val roles: List<Role>? = null
)
