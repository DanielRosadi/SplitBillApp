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

        // --- Password Validation ---
        passwordError = when {
            trimmedPassword.isEmpty() -> {
                isValid = false
                "Password cannot be empty"
            }
            trimmedPassword.length < 8 -> {
                isValid = false
                "Password must be at least 8 characters"
            }
            !trimmedPassword.matches(Regex(".*[A-Z].*")) -> {
                isValid = false
                "Password must include an uppercase letter"
            }
            !trimmedPassword.matches(Regex(".*[a-z].*")) -> {
                isValid = false
                "Password must include a lowercase letter"
            }
            !trimmedPassword.matches(Regex(".*\\d.*")) -> {
                isValid = false
                "Password must include a number"
            }
            !trimmedPassword.matches(Regex("^[A-Za-z\\d]+$")) -> {
                isValid = false
                "Password must not contain symbols"
            }
            else -> null
        }

        // Optionally normalize values in state (e.g., trim input before sending to backend)
        email = trimmedEmail
        password = trimmedPassword

        return isValid
    }

    fun validateSignUp(): Boolean {
        var isValid = true

        // Trim all input to avoid issues with accidental spaces
        val trimmedEmail = email.trim()
        val trimmedPassword = password.trim()
        val trimmedUsername = username.trim()
        val trimmedConfirm = confirmPassword.trim()

        // --- Username Validation ---
        usernameError = when {
            trimmedUsername.isEmpty() -> {
                isValid = false
                "Username cannot be empty"
            }
            trimmedUsername.contains(" ") -> {
                isValid = false
                "Username cannot contain spaces"
            }
            trimmedUsername.length < 3 -> {
                isValid = false
                "Username must be at least 3 characters"
            }
            else -> null
        }

        // --- Email Validation ---
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

        // --- Password Validation ---
        passwordError = when {
            trimmedPassword.isEmpty() -> {
                isValid = false
                "Password cannot be empty"
            }
            trimmedPassword.length < 8 -> {
                isValid = false
                "Password must be at least 8 characters"
            }
            !trimmedPassword.matches(Regex(".*[A-Z].*")) -> {
                isValid = false
                "Password must include an uppercase letter"
            }
            !trimmedPassword.matches(Regex(".*[a-z].*")) -> {
                isValid = false
                "Password must include a lowercase letter"
            }
            !trimmedPassword.matches(Regex(".*\\d.*")) -> {
                isValid = false
                "Password must include a number"
            }
            !trimmedPassword.matches(Regex("^[A-Za-z\\d]+$")) -> {
                isValid = false
                "Password must not contain symbols"
            }
            else -> null
        }

        // --- Confirm Password Validation ---
        confirmPasswordError = when {
            trimmedConfirm.isEmpty() -> {
                isValid = false
                "Please confirm your password"
            }
            trimmedConfirm != trimmedPassword -> {
                isValid = false
                "Passwords do not match"
            }
            else -> null
        }

        // --- Apply trimmed values to state ---
        email = trimmedEmail
        password = trimmedPassword
        username = trimmedUsername
        confirmPassword = trimmedConfirm

        return isValid
    }

    // Reset all, work for Both Screens
    fun reset() {
        email = ""
        password = ""
        confirmPassword = ""
        username = ""

        emailError = null
        passwordError = null
        confirmPasswordError = null
        usernameError = null

        loginError = null
        signupError = null
    }

    // Reset when user types or switches forms
    fun resetErrors() {
        emailError = null
        passwordError = null
        confirmPasswordError = null
        usernameError = null
        loginError = null
        signupError = null
    }
}