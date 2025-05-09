package com.example.readingnow.model

data class UserCreation(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val username: String? = null
)