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
import androidx.compose.material.icons.filled.Person
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

// Form fields container that manages error display and spacing
@Composable
fun AuthInputWrapper(
    error: String?,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        content()

        if (error != null) {
            Text(
                text = error,
                color = Color.Red,
                fontSize = 12.sp
            )
        } else {
            Spacer(modifier = Modifier.height(16.dp)) // normal spacing
        }
    }
}