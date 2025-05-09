package com.example.readingnow.service

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readingnow.data.ApiRepository
import com.example.readingnow.model.UserCreation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG: String = "ReadingNowActivity"

@HiltViewModel
class UserViewModel @Inject constructor(private val repository: ApiRepository): ViewModel() {

    var userEmail by mutableStateOf("")
    var userPassword by mutableStateOf("")
    var currentUser by mutableStateOf(UserUiState())
    var addedUser by mutableStateOf(UserCreationState())

    fun updateEmail(input: String) {
        viewModelScope.launch(Dispatchers.IO) {
            userEmail = input
        }
    }

    fun updatePassword(input: String) {
        viewModelScope.launch(Dispatchers.IO) {
            userPassword = input
        }
    }

    fun getUser() {
        viewModelScope.launch(Dispatchers.IO) {
            currentUser = currentUser.copy(authMode = AuthMode.BUSY)
            try {
                currentUser = repository.getUser(
                    listOf(userEmail, userPassword)
                )
                Log.v(TAG, "VM - User after sign in ::: $currentUser")
            } catch (e: Exception) {
                currentUser = currentUser.copy(
                    authMode = AuthMode.SIGNED_OUT,
                    error = e.message
                )
                Log.v(TAG, "VM - Error after sign in attempt ::: $currentUser")
            }
        }
    }

    fun addNewUser(newUser: UserCreation): UserCreationState {
        viewModelScope.launch(Dispatchers.IO) {
            addedUser = addedUser.copy(signUpMode = SignUpMode.PROGRESS)
            Log.v(TAG,"VM - Adding user (set status to progress) ::: ${newUser.email}")

            try {
                addedUser = repository.addNewUser(newUser)
                if (addedUser.email != null) {
                    Log.v(TAG, "VM - User added ::: $addedUser")
                    addedUser = addedUser.copy(signUpMode = SignUpMode.ACTIVE)
                }
            } catch (e: Exception) {
                Log.v(TAG, "VM - Error ::: ${e.message}")
                addedUser = addedUser.copy(
                    signUpMode = SignUpMode.ERROR,
                    error = e.message
                )
                Log.v(TAG, "VM - Error ::: Failed to add user")
            }
        }
        return addedUser
    }

    fun signOut() {
        viewModelScope.launch {
            currentUser = currentUser.copy(authMode = AuthMode.BUSY)
            currentUser = UserUiState()
        }
    }

    fun clearAddedUser() {
        viewModelScope.launch(Dispatchers.IO) {
            addedUser = UserCreationState()
        }
    }

    fun retrySignUp() {
        viewModelScope.launch(Dispatchers.IO) {
            Log.v(TAG, "VM - Sign up error: Trying again")
            addedUser = addedUser.copy(signUpMode = SignUpMode.INACTIVE)
        }
    }
}