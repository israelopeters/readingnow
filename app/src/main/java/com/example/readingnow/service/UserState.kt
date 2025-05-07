package com.example.readingnow.service

data class UserUiState(
    val email: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val authMode: AuthMode = AuthMode.SIGNED_OUT,
    val isLoading: Boolean = false,
    val error: String? = null
)

data class UserCreationState(
    val email: String? = null,
    val signUpMode: SignUpMode = SignUpMode.INACTIVE,
    val isLoading: Boolean = false,
    val error: String? = null
)

enum class AuthMode{
    SIGNED_IN,
    BUSY,
    SIGNED_OUT
}

enum class SignUpMode{
    ACTIVE,
    PROGRESS,
    INACTIVE
}