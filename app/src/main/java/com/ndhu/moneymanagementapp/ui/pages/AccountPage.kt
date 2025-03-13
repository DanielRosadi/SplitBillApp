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
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ndhu.moneymanagementapp.controllers.UserController
import com.ndhu.moneymanagementapp.models.User
import com.ndhu.moneymanagementapp.navigation.AppScreens
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
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
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            // Navigation and account text
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back button
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.size(44.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        tint = Color(0xFF039be5),
                        modifier = Modifier.size(30.dp)
                    )
                }

                // Account text
                Text(
                    text = "Account",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(50.dp))

            //Profile section
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Avatar
                Image(
                    painter = painterResource(id = R.drawable.defaultavatarimage),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.Cyan, CircleShape)
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Name and Bio
                Text(
                    text = user?.username ?: "Full name",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Bio",
                    fontSize = 20.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }

        HorizontalDivider(
            thickness = 2.dp,
            color = Color.Gray,
            modifier = Modifier.fillMaxWidth()
        )

        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Settings Section
            Text(
                text = "Settings",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 6.dp)
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Settings Buttons
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SettingsButton(text = "Change Currency", onClick = {}, false)
                SettingsButton(
                    text = "Clear User Data",
                    onClick = { showClearUserDataDialog = true },
                    false
                )
                SettingsButton(text = "Log Out", onClick = { showLogOutDialog = true }, true)
            }
        }
    }
    // Confirmation dialog on pressing button "Clear User Data"
    if (showClearUserDataDialog) {
        AlertDialog(
            onDismissRequest = { showClearUserDataDialog = false },
            title = { Text("Confirm clear user data") },
            confirmButton = {
                Button(
                    onClick = {
                        showClearUserDataDialog = false
                        Log.w("Info","Deleting data, sedih")
                        //TODO: Change this to clear data logic
                        coroutine.launch { userController.logout(context) }
                        navController.navigate(AppScreens.LogInScreen.name) {
                            popUpTo(0) { this.inclusive = true }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.hsv(120f, 0.53f, 0.31f))
                ) { Text("Yes") }
            },
            dismissButton = {
                Button(
                    onClick = { showClearUserDataDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) { Text("No") }
            }
        )
    }

    if (showLogOutDialog) {
        AlertDialog(
            onDismissRequest = { showLogOutDialog = false },
            title = { Text("Confirm log out") },
            confirmButton = {
                Button(
                    onClick = {
                        showLogOutDialog = false
                        Log.w("Info","User logged out, bye bye see you again")
                        coroutine.launch { userController.logout(context) }
                        navController.navigate(AppScreens.LogInScreen.name) {
                            popUpTo(0) { this.inclusive = true }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.hsv(120f, 0.53f, 0.31f))
                ) { Text("Yes") }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showLogOutDialog = false
                        Log.w("Info","No bye bye for you, only for Duolingo")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) { Text("No") }
            }
        )
    }
}

@Composable
fun SettingsButton(text: String, onClick : () -> Unit, isLogout: Boolean) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .border(1.dp, Color.Black, RoundedCornerShape(8.dp)),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isLogout) Color.hsv(355f, 0.51f, 0.96f) else Color.White,
            contentColor = Color.Black
        )
    ) {
        Text(text = text, fontSize = 20.sp)
    }
}
