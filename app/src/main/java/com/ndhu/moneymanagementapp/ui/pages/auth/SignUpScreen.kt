package com.ndhu.moneymanagementapp.ui.pages.auth

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ndhu.moneymanagementapp.controllers.UserController
import com.ndhu.moneymanagementapp.enums.AppScreens
import com.ndhu.moneymanagementapp.sharedviewmodels.AuthViewModel
import com.ndhu.moneymanagementapp.ui.pages.primaryGreen
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(context: Context, navController: NavController) {
    val viewModel: AuthViewModel = viewModel()
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    AuthScreen(
        screenTitle = "Sign Up",
        formContent = {

            val coroutineScope = rememberCoroutineScope()
            val userController = remember { UserController() }

            val usernameFocused = remember { mutableStateOf(false) }
            val emailFocused = remember { mutableStateOf(false) }

            // Username
            AuthInputWrapper(error = viewModel.usernameError) {
                UsernameField(
                    value = viewModel.username,
                    onValueChange = {
                        // Update state and clear any existing errors as the user types
                        viewModel.username = it
                        viewModel.resetErrors()
                        viewModel.signupError = null
                    },
                    error = null, // Let AuthInputWrapper handle error display
                    modifier = Modifier.onFocusChanged { focusState ->
                        // Check if the username field was focused and now lost focus
                        if (usernameFocused.value && !focusState.isFocused) {
                            if (viewModel.username.isNotBlank()) {
                                coroutineScope.launch {
                                    val result = userController.checkUsernameOrEmailTaken(
                                        username = viewModel.username,
                                        email = null
                                    )
                                    viewModel.usernameError =
                                        if (result == "Username is already in use.") result else null
                                }
                            }
                        }
                        // Track focus state for future transitions
                        usernameFocused.value = focusState.isFocused
                    }
                )
            }

            // Email
            AuthInputWrapper(error = viewModel.emailError) {
                EmailField(
                    value = viewModel.email,
                    onValueChange = {
                        // Update state and clear any existing errors as the user types
                        viewModel.email = it
                        viewModel.resetErrors()
                        viewModel.signupError = null
                    },
                    error = null, // Let AuthInputWrapper handle error display
                    modifier = Modifier.onFocusChanged { focusState ->
                        // When the user leaves the field (lost focus after being focused)
                        if (emailFocused.value && !focusState.isFocused) {
                            coroutineScope.launch {
                                // Check if email is already registered
                                val result = userController.checkUsernameOrEmailTaken(
                                    username = null,
                                    email = viewModel.email
                                )
                                if (result == "Email is already in use.") {
                                    viewModel.emailError = result
                                }
                            }
                        }
                        // Update focus tracker
                        emailFocused.value = focusState.isFocused
                    }
                )
            }

            // Password
            AuthInputWrapper(error = viewModel.passwordError) {
                PasswordField(
                    value = viewModel.password,
                    onValueChange = {
                        viewModel.password = it
                        viewModel.resetErrors()
                        viewModel.signupError = null
                    },
                    error = null,
                    passwordVisible = passwordVisible,
                    onToggleVisibility = { passwordVisible = !passwordVisible }
                )
            }

            // Confirm Password
            AuthInputWrapper(error = viewModel.confirmPasswordError) {
                ConfirmPasswordField(
                    value = viewModel.confirmPassword,
                    onValueChange = {
                        viewModel.confirmPassword = it
                        viewModel.resetErrors()
                        viewModel.signupError = null
                    },
                    error = null,
                    passwordVisible = confirmPasswordVisible,
                    onToggleVisibility = { confirmPasswordVisible = !confirmPasswordVisible }
                )
            }

            // Submit Button
            Button(
                onClick = {
                    coroutineScope.launch {
                        if (viewModel.validateSignUp()) {
                            // Backend check for duplicates
                            val takenResult = userController.checkUsernameOrEmailTaken(
                                viewModel.username,
                                viewModel.email
                            )

                            // Handle if either field is already taken
                            when (takenResult) {
                                "Username is already in use." -> {
                                    viewModel.usernameError = takenResult
                                    return@launch
                                }
                                "Email is already in use." -> {
                                    viewModel.emailError = takenResult
                                    return@launch
                                }
                            }

                            // If not taken, attempt registration
                            val result = userController.registerUser(
                                viewModel.username,
                                viewModel.email,
                                viewModel.password,
                                "No bio provided"
                            )

                            // Handle registration errors or login
                            if (result != null) {
                                viewModel.signupError = result
                            } else {
                                userController.loginUser(
                                    context,
                                    viewModel.username,
                                    viewModel.password
                                )
                                viewModel.reset()
                                navController.navigate(AppScreens.HomeScreen.name) {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryGreen)
            ) {
                Text("Submit", color = Color.White)
            }

            // Signup-level error
            if (viewModel.signupError != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = viewModel.signupError ?: "",
                    color = Color.Red,
                    fontSize = 14.sp
                )
            }
        },

        footerContent = {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "Already registered?", color = Color.White)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Sign in",
                    color = primaryGreen,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable {
                        navController.navigate(AppScreens.LogInScreen.name) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }
    )
}
