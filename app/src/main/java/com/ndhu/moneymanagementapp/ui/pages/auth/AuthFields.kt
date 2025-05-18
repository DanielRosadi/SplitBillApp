package com.ndhu.moneymanagementapp.ui.pages.auth

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.ndhu.moneymanagementapp.ui.pages.primaryGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsernameField(
    value: String,
    onValueChange: (String) -> Unit,
    error: String?
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Username", color = primaryGreen) },
        leadingIcon = {
            Icon(Icons.Default.Person, contentDescription = "Username", tint = primaryGreen)
        },
        trailingIcon = {
            if (value.isNotEmpty()) {
                IconButton(onClick = { onValueChange("") }) {
                    Icon(Icons.Default.Close, contentDescription = "Clear username", tint = Color.Gray)
                }
            }
        },
        singleLine = true,
        isError = error != null,
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.White,
            focusedIndicatorColor = primaryGreen,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = primaryGreen
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailField(
    value: String,
    onValueChange: (String) -> Unit,
    error: String?
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Email", color = primaryGreen) },
        leadingIcon = {
            Icon(Icons.Default.Email, contentDescription = "Email", tint = primaryGreen)
        },
        trailingIcon = {
            if (value.isNotEmpty()) {
                IconButton(onClick = { onValueChange("") }) {
                    Icon(Icons.Default.Close, contentDescription = "Clear email", tint = Color.Gray)
                }
            }
        },
        singleLine = true,
        isError = error != null,
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.White,
            focusedIndicatorColor = primaryGreen,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = primaryGreen
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    error: String?,
    passwordVisible: Boolean,
    onToggleVisibility: () -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Password", color = primaryGreen) },
        leadingIcon = {
            Icon(Icons.Default.Lock, contentDescription = "Password", tint = primaryGreen)
        },
        trailingIcon = {
            val icon = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
            IconButton(onClick = onToggleVisibility) {
                Icon(icon, contentDescription = "Toggle visibility", tint = primaryGreen)
            }
        },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine = true,
        isError = error != null,
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.White,
            focusedIndicatorColor = primaryGreen,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = primaryGreen
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    error: String?,
    passwordVisible: Boolean,
    onToggleVisibility: () -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Confirm Password", color = primaryGreen) },
        leadingIcon = {
            Icon(Icons.Default.Lock, contentDescription = "Confirm Password", tint = primaryGreen)
        },
        trailingIcon = {
            val icon = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
            IconButton(onClick = onToggleVisibility) {
                Icon(icon, contentDescription = "Toggle visibility", tint = primaryGreen)
            }
        },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine = true,
        isError = error != null,
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.White,
            focusedIndicatorColor = primaryGreen,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = primaryGreen
        )
    )
}