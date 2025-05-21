package com.ndhu.moneymanagementapp.ui.pages

import android.content.Context
import com.ndhu.moneymanagementapp.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ndhu.moneymanagementapp.controllers.UserController
import com.ndhu.moneymanagementapp.models.User
import com.ndhu.moneymanagementapp.enums.AppScreens
import kotlinx.coroutines.launch

@Composable
fun AccountPage(context: Context, navController: NavController) {
    val userController = UserController()
    val coroutine = rememberCoroutineScope()

    val showDeleteAccountDialog  = remember { mutableStateOf(false) }
    val showLogOutDialog = remember { mutableStateOf(false) }

    var user by remember { mutableStateOf<User?>(null) }
    var bio by remember { mutableStateOf(TextFieldValue("")) }

    LaunchedEffect(Unit) {
        user = userController.getOnboardUser(context)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.5f)
        ) {
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                ) {
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

                Spacer(modifier = Modifier.height(55.dp))

                val profilePic = profilePictures.firstOrNull { it.id == (user?.profilePictureId ?: 1) }
                profilePic?.let {
                    Image(
                        painter = painterResource(id = it.imageRes),
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .size(150.dp)
                            .clip(CircleShape)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))
                Text(
                    text = user?.username ?: "{FAIL-LOGIN}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = syncopateFont,
                    color = Color(0xFFF5C542),
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = user?.bio ?: "No bio set up",
                    fontSize = 14.sp,
                    color = Color.LightGray,
                    modifier = Modifier.padding(horizontal = 32.dp),
                    textAlign = TextAlign.Center
                )
            }
        }

        // Bottom Section
        Surface(
            color = Color(0xFFF5F5F5),
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.5f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 40.dp),
                verticalArrangement = Arrangement.spacedBy(30.dp)
            ) {
                Text("Settings", fontSize = 18.sp, color = Color.Gray)
                Spacer(modifier = Modifier.weight(0.1f))
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 30.dp),
                    verticalArrangement = Arrangement.spacedBy(30.dp)
                ) {
                    ColoredSettingsButton(
                        text = "Edit Profile",
                        backgroundColor = Color(0xFF009669),
                        modifier = Modifier.weight(0.3f)
                    ) {
                        navigateToEditAccountScreen(navController)
                    }

                    ColoredSettingsButton(
                        text = "Delete Account",
                        backgroundColor = Color(0xFF961400),
                        modifier = Modifier.weight(0.3f)
                    ) {
                        showDeleteAccountDialog.value = true
                    }

                    ColoredSettingsButton(
                        text = "Log Out",
                        backgroundColor = Color(0xFF009669),
                        modifier = Modifier.weight(0.3f)
                    ) {
                        showLogOutDialog.value = true
                    }
                }
            }
        }
    }

    if (showDeleteAccountDialog.value) {
        DeleteAccountDialog(
            showDialog = showDeleteAccountDialog,
            onConfirm = {
                coroutine.launch {
                    userController.deleteCurrUser(context)
                }
                navController.navigate(AppScreens.LogInScreen.name) {
                    popUpTo(0) { inclusive = true }
                }
            }
        )
    }

    if (showLogOutDialog.value) {
        LogoutDialog(
            showDialog = showLogOutDialog,
            onConfirm = {
                coroutine.launch {
                    userController.logout(context)
                }
                navController.navigate(AppScreens.LogInScreen.name) {
                    popUpTo(0) { inclusive = true }
                }
            }
        )
    }
}

@Composable
fun ColoredSettingsButton(
    text: String,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = Color.White
        ),
        modifier = modifier
            .fillMaxWidth()
            .shadow(4.dp, shape = RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp)
    ) {
        //Text(text, fontSize = 20.sp, fontFamily = surannaFont)
        Text(text, fontSize = 20.sp)
    }
}

@Composable
fun DeleteAccountDialog(
    showDialog: MutableState<Boolean>,
    onConfirm: () -> Unit
) {
    ConfirmationDialog(
        title = "Delete your account?",
        message = "This action can't be undone.",
        confirmText = "Delete",
        cancelText = "Cancel",
        confirmColor = dangerRed,
        icon = Icons.Default.Warning,
        iconTint = Color.Red,
        showDialog = showDialog,
        onConfirm = onConfirm
    )
}

@Composable
fun LogoutDialog(
    showDialog: MutableState<Boolean>,
    onConfirm: () -> Unit
) {
    ConfirmationDialog(
        title = "Log out?",
        message = "Any unsaved changes will be lost.",
        confirmText = "Log Out",
        cancelText = "Cancel",
        confirmColor = dangerRed,
        icon = Icons.Default.ExitToApp,
        iconTint = Color.Red,
        showDialog = showDialog,
        onConfirm = onConfirm
    )
}

fun navigateToEditAccountScreen(navController: NavController) {
    navController.navigate(route = AppScreens.EditProfileScreen.name)
}
