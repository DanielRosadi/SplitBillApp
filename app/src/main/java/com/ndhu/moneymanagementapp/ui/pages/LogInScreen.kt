package com.ndhu.moneymanagementapp.ui.pages

import android.content.Context
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ndhu.moneymanagementapp.controllers.UserController
import com.ndhu.moneymanagementapp.enums.AppScreens
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogIn(context: Context, navController: NavController) {
    val coroutine = rememberCoroutineScope()
    val userController = UserController()

    // Input state variables
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Controls password visibility
    var passwordVisible by remember { mutableStateOf(false) }

    // Stores error message (if any)
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Background container
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Background Image
        BackgroundImage()

        // Full Page
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // App title
            CenterTitleText()

            Spacer(modifier = Modifier.height(40.dp))

            // Screen title
            Text("Log In", fontSize = 30.sp, color = Color.White)

            Spacer(modifier = Modifier.height(30.dp))

            // Card for input fields and button
            Card(
                modifier = Modifier.fillMaxWidth()
                    //.padding(10.dp)
                    .padding(20.dp)
                ,shape = RoundedCornerShape(16.dp),
                //colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9)) // Soft White = dialog Background
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)) // new darker Soft White /*TODO: use this for card bg*/
            ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    // Email input
                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            if (errorMessage != null) errorMessage = null
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(20.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        label = {
                            Text(
                                text = "Email",
                                color = primaryGreen,
                            )
                        },
                        leadingIcon = {
                            //Icon(Icons.Filled.Email, contentDescription = null, tint = Color(0xFFF1C40F)) // gold
                            //Icon(Icons.Filled.Email, contentDescription = null, tint = primaryGreen)
                            Icon(Icons.Filled.Email, contentDescription = null)
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.White,
                            focusedIndicatorColor = primaryGreen,
                            unfocusedIndicatorColor = Color.Transparent,
                            cursorColor = primaryGreen
                        ),
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Password input with visibility toggle
                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            if (errorMessage != null) errorMessage = null
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(20.dp),
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        label = {
                            Text(
                                text = "Password",
                                color = primaryGreen,
                            )
                        },
                        leadingIcon = {
                            //Icon(Icons.Filled.Lock, contentDescription = null, tint = Color(0xFFF1C40F)) // gold
                            //Icon(Icons.Filled.Lock, contentDescription = null, tint = primaryGreen)
                            Icon(Icons.Filled.Lock, contentDescription = null)
                        },
                        trailingIcon = {
                            val visibilityIcon = if (passwordVisible)
                                Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(visibilityIcon, contentDescription = null)
                            }
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.White,
                            focusedIndicatorColor = primaryGreen,
                            unfocusedIndicatorColor = Color.Transparent,
                            cursorColor = primaryGreen
                        ),
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Forgot password text (not functional yet)
                    Text(
                        text = "Forgot password?",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier
                            .align(Alignment.End)
                            .clickable {
                                // TODO: Navigate to Forgot Password Screen
                            }
                    )

                    // Show error message if needed
                    if (errorMessage != null) {
                        //Spacer(modifier = Modifier.height(8.dp))
                        Text(text = errorMessage ?: "", color = Color.Red, fontSize = 14.sp)
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Log In button
                    Button(
                        onClick = {
                            coroutine.launch {
                                // Check for empty fields
                                if (email.isBlank() || password.isBlank()) {
                                    errorMessage = "Fields cannot be empty"
                                    return@launch
                                }

                                // Attempt login
                                val user = userController.loginUser(context, email, password)
                                if (user != null) {
                                    // Login success: go to home screen
                                    email = ""
                                    password = ""
                                    navController.navigate(AppScreens.HomeScreen.name) {
                                        popUpTo(0) { inclusive = true }
                                    }
                                } else {
                                    // Login failed
                                    errorMessage = "Invalid credentials"
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
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Link to Sign Up screen
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .wrapContentWidth(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Not registered? ",
                    color = Color.White,
                    fontSize = 14.sp
                )
                Text(
                    text = "Sign up",
                    color = primaryGreen,
                    fontSize = 14.sp,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable {
                        navController.navigate(AppScreens.SignUpScreen.name) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

        }
    }
}

@Composable
fun CenterTitleText() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "WE ",
            color = Color.White,
            fontSize = 24.sp
        )
        Text(
            text = "SPLIT",
            color = primaryGreen,
            fontSize = 24.sp
        )
    }
}
