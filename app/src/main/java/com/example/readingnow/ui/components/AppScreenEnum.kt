package com.example.readingnow.ui.components

import androidx.annotation.StringRes
import com.example.readingnow.R

// Enums for app screens
enum class AppScreen(@StringRes val title: Int) {
    Welcome(title = R.string.welcome),
    SignUp(title = R.string.sign_up),
    Home(title = R.string.home),
    Profile(title = R.string.profile),
}