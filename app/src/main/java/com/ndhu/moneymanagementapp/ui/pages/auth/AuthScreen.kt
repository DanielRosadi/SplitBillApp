package com.ndhu.moneymanagementapp.ui.pages.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ndhu.moneymanagementapp.ui.pages.BackgroundImage
import com.ndhu.moneymanagementapp.ui.pages.kronaOneFont
import com.ndhu.moneymanagementapp.ui.pages.primaryGreen

// A reusable screen layout for authentication screen (Log In and Sign Up)
@Composable
fun AuthScreen(
    screenTitle: String,
    formContent: @Composable ColumnScope.() -> Unit,
    footerContent: @Composable () -> Unit = {}
) {
    Box(modifier = Modifier.fillMaxSize()) {
        BackgroundImage()

        // Full column containing fixed header and scrollable content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // App Header Section
            Spacer(modifier = Modifier.height(60.dp)) // consistent top spacing
            AppBrandHeader()
            Spacer(modifier = Modifier.height(24.dp))

            // Screen Title ("Log In", "Sign Up")
            Text(
                text = screenTitle,
                fontSize = 28.sp,
                color = Color.White,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(32.dp))

            // Scrollable Form + Footer Section
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Form container
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        formContent()
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Sign-up / footer content
                footerContent()

                Spacer(modifier = Modifier.height(32.dp)) // bottom breathing room
            }
        }
    }
}


// Bigger and Centered "WESPLIT" Title
@Composable
fun AppBrandHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "WE ",
            color = Color.White,
            fontSize = 32.sp,
            fontFamily = kronaOneFont,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "SPLIT",
            color = primaryGreen,
            fontSize = 32.sp,
            fontFamily = kronaOneFont,
            fontWeight = FontWeight.Bold
        )
    }
}

// Reusable Email Fields
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
        leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null, tint = primaryGreen) },
        trailingIcon = {
            if (value.isNotEmpty()) {
                IconButton(onClick = { onValueChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear email",
                        tint = Color.Gray
                    )
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

    if (error != null) {
        Text(text = error, color = Color.Red, fontSize = 12.sp)
    }
}

// Reusable Password Fields
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
        leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null, tint = primaryGreen) },
        trailingIcon = {
            val icon = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
            IconButton(onClick = onToggleVisibility) {
                Icon(icon, contentDescription = null, tint = primaryGreen)
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
    if (error != null) {
        Text(text = error, color = Color.Red, fontSize = 12.sp)
    }
}