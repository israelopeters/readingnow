package com.example.readingnow.exception

class UserNotFoundException: Exception("User not found!")

class UnauthorizedAccessException: Exception("Unauthorized access!")

class UnspecifiedException: Exception("Something went wrong!")