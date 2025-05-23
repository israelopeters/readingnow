package com.example.readingnow.data

import android.util.Log
import com.example.readingnow.exception.UnauthorizedAccessException
import com.example.readingnow.exception.UnspecifiedException
import com.example.readingnow.exception.UserNotFoundException
import com.example.readingnow.model.User
import com.example.readingnow.model.UserCreation
import com.example.readingnow.service.AuthMode
import com.example.readingnow.service.SignUpMode
import com.example.readingnow.service.UserCreationState
import com.example.readingnow.service.UserUiState
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.basicAuth
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import javax.inject.Inject

const val TAG: String = "ReadingNowActivity"

class ApiRepository @Inject constructor(private val client: HttpClient) {

    private var userCredentials: List<String> = listOf("", "")

    suspend fun addNewUser(newUser: UserCreation): UserCreationState {
        Log.v(TAG,"API repository - Adding user --- ${newUser.email}")
        val response: User = client.post("/api/v1/users/add") {
            contentType(ContentType.Application.Json)
            setBody(newUser)
        }.processBody()
        Log.v(TAG, "API repository - raw response --- $response")
        Log.v(TAG, "Api Repository - mapped response --- ${mapToUserCreationState(response)}")
        return mapToUserCreationState(response)
    }

    suspend fun getUser(credentials: List<String>): UserUiState {
        val response = client.get("/api/v1/users/email?email=${credentials[0]}") {
            basicAuth(
                username = credentials[0],
                password = credentials[1]
            )
        }
        var currentUser = UserUiState()
        if (response.status == HttpStatusCode.OK) {
            userCredentials = credentials
            currentUser = mapToUserUiState(response.processBody())
            currentUser = currentUser.copy(authMode = AuthMode.SIGNED_IN)
        }
        return currentUser
    }

    // An extension function to handle the response body and exceptions when making network
    // requests for a user
    suspend inline fun <reified T> HttpResponse.processBody(): T {
        if (this.status == HttpStatusCode.OK ||
            this.status == HttpStatusCode.Created
            ) {
            return body<T>()
        } else if (this.status.value == 404) {
            Log.v(TAG, "API repository - 404 Error: User not found!")
            throw UserNotFoundException()
        } else if (this.status.value == 401) {
            Log.v(TAG, "API repository - 401 Error: Unauthorized access!")
            throw UnauthorizedAccessException()
        } else {
            Log.v(TAG, "API repository - Error: Something went wrong")
            throw UnspecifiedException()
        }
    }

    private fun mapToUserCreationState(user: User): UserCreationState {
        val userCreationState = UserCreationState(
            email = user.email,
            signUpMode = SignUpMode.ACTIVE,
            isLoading = false,
            error = null
        )
        return userCreationState
    }

    private fun mapToUserUiState(user: User): UserUiState {
        val userUiState = UserUiState(
            email = user.email,
            firstName = user.firstName,
            lastName = user.lastName,
        )
        return userUiState
    }
}

