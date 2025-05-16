package com.ndhu.moneymanagementapp.ui.pages

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff

import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ndhu.moneymanagementapp.controllers.UserController
import com.ndhu.moneymanagementapp.enums.AppScreens
import kotlinx.coroutines.launch

@Composable
fun SignUp(context : Context, navController: NavController) {
    val coroutine = rememberCoroutineScope()
    // States variable
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var username by remember { mutableStateOf(TextFieldValue("")) }
    var userAge by remember { mutableStateOf(TextFieldValue("")) }
    var userPassword by remember { mutableStateOf(TextFieldValue("")) }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // UI elements/components (Composable here)
    // TODO: Need TopBar, BottomBar?  (i.e. Scaffold)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        // Sign Up Text
        Text(text = "Sign Up", fontSize = 40.sp, fontWeight = FontWeight.Bold)

        // Add some space
        Spacer(modifier = Modifier.height(15.dp))

        // email or username text field
        // TODO: need to make mandatory input functionality
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth())

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth())


        // password text field with visibility
        // TODO: need to make mandatory input functionality
        OutlinedTextField(
            value = userPassword,
            onValueChange = { userPassword = it },
            label = { Text("Password") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = "Toggle Password Visibility")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        // Add some space
        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = {
                coroutine.launch {
                    val isValidEmail = android.util.Patterns.EMAIL_ADDRESS.matcher(email.text).matches()
                    val isValidUsername = username.text.matches(Regex("^[a-zA-Z0-9_.]{3,20}$"))
                    val isValidPassword = userPassword.text.matches(
                        Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$")
                    )

                    if (!isValidEmail) {
                        errorMessage = "Invalid email format."
                    } else if (!isValidUsername) {
                        errorMessage = "Username must be 3–20 characters and only contain letters, numbers, underscores, or periods."
                    } else if (!isValidPassword) {
                        errorMessage = "Password must be at least 8 characters, with uppercase, lowercase, digit, and special character."
                    } else {
                        val controller = UserController()
                        val result = controller.registerUser(
                            username.text,
                            email.text,
                            userPassword.text,
                            "No bio provided"
                        )
                        controller.loginUser(context, username.text, userPassword.text) //Login after sign up
                        if (result != null) {
                            errorMessage = result
                        } else {
                            errorMessage = ""
                            username = TextFieldValue("")
                            email = TextFieldValue("")
                            userPassword = TextFieldValue("")
                            navController.navigate(route = AppScreens.HomeScreen.name)
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            shape = RoundedCornerShape(5.dp)
        ) {
            Text(text = "Submit")
        }

        // Show error if exists
        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Already registered?")
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Sign in",
                color = Color.Blue,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable {
                    navController.navigate(AppScreens.LogInScreen.name){
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
