package com.ndhu.moneymanagementapp.sharedviewmodels

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel

// State Holder and Logic Manager for your authentication screens (Login, Sign Up)
class AuthViewModel : ViewModel() {

    // State: Input Fields
    var email by mutableStateOf("")
    var password by mutableStateOf("")

    // State: Error Messages
    var emailError by mutableStateOf<String?>(null)
    var passwordError by mutableStateOf<String?>(null)

    fun validateLogin(): Boolean {
        var isValid = true

        // Check if email is empty
        emailError = if (email.isBlank()) {
            isValid = false
            "Email cannot be empty"
        } else null

        // Check if password is too short
        passwordError = if (password.length < 6) {
            isValid = false
            "Password must be at least 6 characters"
        } else null

        return isValid
    }

    // clear the form after successful login
    fun reset() {
        email = ""
        password = ""
        emailError = null
        passwordError = null
    }
}