package com.ndhu.moneymanagementapp.ui.pages.auth

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ndhu.moneymanagementapp.controllers.UserController
import com.ndhu.moneymanagementapp.enums.AppScreens
import com.ndhu.moneymanagementapp.sharedviewmodels.AuthViewModel
import com.ndhu.moneymanagementapp.ui.pages.primaryGreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun LogInScreen(navController: NavController) {
    val viewModel: AuthViewModel = viewModel()

    // Required for LoginForm's backend operation
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val userController = UserController()

    AuthScreen(
        screenTitle = "Log In",

        // Login form content
        formContent = {
            LoginForm(
                viewModel = viewModel,
                context = context,
                coroutineScope = coroutineScope,
                userController = userController,
                onLoginSuccess = {
                    navController.navigate(AppScreens.HomeScreen.name) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        },

        // Footer content (Sign Up prompt)
        footerContent = {
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                    //.padding(top = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "No Account? ",
                    color = Color.White,
                )
                Text(
                    text = "Register",
                    color = primaryGreen,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable {
                        navController.navigate(AppScreens.SignUpScreen.name) {
                            popUpTo(0) { inclusive = true } // Prevent going back to login
                        }
                    }
                )
            }
        }
    )
}

// Form UI + Logic for LogInScreen
@Composable
fun LoginForm(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    context: Context,
    coroutineScope: CoroutineScope,
    userController: UserController
) {
    var passwordVisible by remember { mutableStateOf(false) }

    // Email Field with Error Wrapper
    AuthInputWrapper(error = viewModel.emailError) {
        EmailField(
            value = viewModel.email,
            onValueChange = {
                viewModel.email = it
                viewModel.resetErrors()
                viewModel.loginError = null
            },
            error = null // Suppress internal error rendering
        )
    }

    // Password Field with Error Wrapper
    AuthInputWrapper(error = viewModel.passwordError) {
        PasswordField(
            value = viewModel.password,
            onValueChange = {
                viewModel.password = it
                viewModel.resetErrors()
                viewModel.loginError = null
            },
            error = null,
            passwordVisible = passwordVisible,
            onToggleVisibility = { passwordVisible = !passwordVisible }
        )
    }

    // Forgot Password Text
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.End
    ) {
        Text(
            text = "Forgot password?",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.clickable {
                // TODO: Implement forgot password navigation
            }
        )
    }

    // Log In Button
    Button(
        onClick = {
            coroutineScope.launch {
                if (viewModel.validateLogin()) {
                    val user = userController.loginUser(
                        context,
                        viewModel.email,
                        viewModel.password
                    )
                    if (user != null) {
                        viewModel.reset()
                        onLoginSuccess()
                    } else {
                        viewModel.password = ""
                        viewModel.loginError = "Invalid email or password"
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
        Text("Log In", color = Color.White)
    }

    // Login Failure (e.g., from backend)
    if (viewModel.loginError != null) {
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = viewModel.loginError ?: "",
                color = Color.Red,
                fontSize = 14.sp
            )
        }
    }
}
