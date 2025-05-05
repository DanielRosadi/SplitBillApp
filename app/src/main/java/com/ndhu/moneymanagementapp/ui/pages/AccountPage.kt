package com.ndhu.moneymanagementapp.ui.pages

import android.content.Context
import android.util.Log
import com.ndhu.moneymanagementapp.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ndhu.moneymanagementapp.controllers.UserController
import com.ndhu.moneymanagementapp.models.User
import com.ndhu.moneymanagementapp.enums.AppScreens
import kotlinx.coroutines.launch

@Composable
fun AccountPage(context: Context, navController: NavController) {
    val coroutine = rememberCoroutineScope()
    val userController = UserController()
    var user by remember { mutableStateOf<User?>(null) }
    var showClearUserDataDialog by remember { mutableStateOf(false) }
    var showLogOutDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        user = userController.getOnboardUser(context)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Top section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            // Background image
            Image(
                painter = painterResource(id = R.drawable.background_dark),
                contentDescription = "Background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Scaffold section (for navigation and text)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                ) {
                    // Back button aligned to the start
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }

                    // We Split text
                    Row(
                        modifier = Modifier.align(Alignment.Center),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "WE",
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge,
                            fontFamily = kronaOneFont,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "SPLIT",
                            color = Color(0xFF009669),
                            style = MaterialTheme.typography.titleLarge,
                            fontFamily = kronaOneFont,
                            fontSize = 12.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(65.dp))

                // Profile Image
                Image(
                    painter = painterResource(id = R.drawable.profile_picture),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = user?.username ?: "Daniel Rosadi",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFF5C542), // golden yellow
                )

                Text(
                    text = "Be Believer",
                    fontSize = 14.sp,
                    color = Color.LightGray
                )
            }
        }

        // Bottom Section: Settings
        Surface(
            color = Color(0xFFF5F5F5),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Settings", fontSize = 18.sp, color = Color.Gray)

                ColoredSettingsButton("Edit Profile", Color(0xFF009B77)) {
                    navigateToEditAccountScreen(navController)
                }
                ColoredSettingsButton("Delete Account", Color(0xFF8B0000)) {
                    showClearUserDataDialog = true
                }
                ColoredSettingsButton("Log Out", Color(0xFF009B77)) {
                    showLogOutDialog = true
                }
            }
        }
    }

    if (showClearUserDataDialog) {
        AlertDialog(
            onDismissRequest = { showClearUserDataDialog = false },
            title = { Text("Delete account?") },
            confirmButton = {
                Button(onClick = {
                    coroutine.launch { userController.logout(context) }
                    navController.navigate(AppScreens.LogInScreen.name) {
                        popUpTo(0) { inclusive = true }
                    }
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(onClick = { showClearUserDataDialog = false }) {
                    Text("No")
                }
            }
        )
    }

    if (showLogOutDialog) {
        AlertDialog(
            onDismissRequest = { showLogOutDialog = false },
            title = { Text("Log out?") },
            confirmButton = {
                Button(onClick = {
                    coroutine.launch { userController.logout(context) }
                    navController.navigate(AppScreens.LogInScreen.name) {
                        popUpTo(0) { inclusive = true }
                    }
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(onClick = { showLogOutDialog = false }) {
                    Text("No")
                }
            }
        )
    }
}

@Composable
fun ColoredSettingsButton(text: String, backgroundColor: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = Color.White
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .shadow(4.dp, shape = RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(text)
    }
}

fun navigateToEditAccountScreen(navController: NavController) {
    navController.navigate(route = AppScreens.EditProfileScreen.name)
}
