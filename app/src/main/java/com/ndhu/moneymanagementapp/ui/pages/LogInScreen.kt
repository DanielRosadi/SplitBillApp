package com.ndhu.moneymanagementapp.ui.pages

import android.content.Context
import android.util.Log
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ndhu.moneymanagementapp.controllers.UserController
import com.ndhu.moneymanagementapp.navigation.AppScreens
import com.ndhu.moneymanagementapp.utils.Util
import kotlinx.coroutines.launch

@Composable
fun LogIn(context : Context, navController: NavController) {
    // Your UI elements go here

    var email by remember { mutableStateOf(TextFieldValue("")) }
    // var username by remember { mutableStateOf(TextFieldValue("")) }
    var userPassword by remember { mutableStateOf(TextFieldValue("")) }
    var passwordVisible by remember { mutableStateOf(false) }
    var userController = UserController()
    val coroutine = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        // Log In Text
        Text(text = "Log In", fontSize = 40.sp, fontWeight = FontWeight.Bold)

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

        /*
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth())

         */


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

        // Log In button
        Button(
            onClick = {
                coroutine.launch {
                    var emValue = email.text.toString()
                    var pasValue = userPassword.text.toString()
                    var user = userController.loginUser(context, emValue, pasValue)
                    if(user != null){
                        Log.w("INFO", "User id : " + Util.getUserId(context).toString())
                        email = TextFieldValue("")
                        userPassword = TextFieldValue("")
                        navController.navigate(AppScreens.HomeScreen.name) {
                            popUpTo(0) { this.inclusive = true }
                        }
                    }else {
                        Log.w("ERROR", "user tidak ketemu aku cedih")
                    }
                }

            },
            modifier = Modifier.fillMaxWidth().height(60.dp),
            shape = RoundedCornerShape(5.dp)
        ) {
            Text(text = "Log In")
        }
    }
}
