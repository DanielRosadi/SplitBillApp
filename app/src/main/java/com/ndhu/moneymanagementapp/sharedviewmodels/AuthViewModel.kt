package com.ndhu.moneymanagementapp.sharedviewmodels

import android.util.Patterns
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel

// State Holder and Logic Manager for your authentication screens (Login, Sign Up)
class AuthViewModel : ViewModel() {

    // State: Input Fields
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var username by mutableStateOf("")
    var confirmPassword by mutableStateOf("")

    // State: Error Messages
    var emailError by mutableStateOf<String?>(null)
    var passwordError by mutableStateOf<String?>(null)
    var loginError by mutableStateOf<String?>(null)
    var usernameError by mutableStateOf<String?>(null)
    var confirmPasswordError by mutableStateOf<String?>(null)
    var signupError by mutableStateOf<String?>(null)

    fun validateLogin(): Boolean {
        var isValid = true

        // Trim inputs to ignore accidental spaces
        val trimmedEmail = email.trim()
        val trimmedPassword = password.trim()

        // Email validation: required + format check
        emailError = when {
            trimmedEmail.isEmpty() -> {
                isValid = false
                "Email cannot be empty"
            }
            !Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches() -> {
                isValid = false
                "Invalid email format"
            }
            else -> null
        }

        // Password validation: required + minimum length
        passwordError = when {
            trimmedPassword.isEmpty() -> {
                isValid = false
                "Password cannot be empty"
            }
            trimmedPassword.length < 6 -> {
                isValid = false
                "Password must be at least 6 characters"
            }
            else -> null
        }

        // Optionally normalize values in state (e.g., trim input before sending to backend)
        email = trimmedEmail
        password = trimmedPassword

        return isValid
    }

    // Clear the form after successful login
    fun reset() {
        email = ""
        password = ""
        emailError = null
        passwordError = null
    }

    // Clear previous error messages when the user starts typing again
    fun resetErrors() {
        emailError = null
        passwordError = null
    }
}