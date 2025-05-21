package com.ndhu.moneymanagementapp.ui.pages

import android.content.Context
import android.widget.Toast
import com.ndhu.moneymanagementapp.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Lock
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ndhu.moneymanagementapp.components.CustomFormDialog
import com.ndhu.moneymanagementapp.components.FormField
import com.ndhu.moneymanagementapp.controllers.UserController
import com.ndhu.moneymanagementapp.models.User
import com.ndhu.moneymanagementapp.enums.AppScreens
import com.ndhu.moneymanagementapp.utils.Util
import kotlinx.coroutines.launch

@Composable
fun AccountPage(context: Context, navController: NavController) {
    val userController = UserController()
    val coroutine = rememberCoroutineScope()

    val showDeleteAccountDialog = remember { mutableStateOf(false) }
    val showLogOutDialog = remember { mutableStateOf(false) }

    // Change Password Dialog States
    var showChangePasswordDialog by remember { mutableStateOf(false) }
    var newPassword by remember { mutableStateOf(TextFieldValue("")) }
    var confirmPassword by remember { mutableStateOf(TextFieldValue("")) }
    var currentPasswordError by remember { mutableStateOf<String?>(null) }
    var newPasswordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }


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
                .weight(1f)
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

                val profilePic =
                    profilePictures.firstOrNull { it.id == (user?.profilePictureId ?: 1) }
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
                .weight(1f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("Settings", fontSize = 20.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(10.dp))
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    ColoredSettingsButton(
                        text = "Edit Profile",
                        backgroundColor = Color(0xFF009669),
                        modifier = Modifier.weight(1f)
                    ) {
                        navigateToEditAccountScreen(navController)
                    }

                    ColoredSettingsButton(
                        text = "Change Password",
                        backgroundColor = Color(0xFF009669),
                        modifier = Modifier.weight(1f)
                    ) {
                        showChangePasswordDialog = true
                    }

                    ColoredSettingsButton(
                        text = "Delete Account",
                        backgroundColor = Color(0xFF961400),
                        modifier = Modifier.weight(1f)
                    ) {
                        showDeleteAccountDialog.value = true
                    }

                    ColoredSettingsButton(
                        text = "Log Out",
                        backgroundColor = Color(0xFF009669),
                        modifier = Modifier.weight(1f)
                    ) {
                        showLogOutDialog.value = true
                    }
                }
            }
        }
    }

    // --- Password Change Dialog using CustomFormDialog ---
    if (showChangePasswordDialog) {
        val showDialogState = remember { mutableStateOf(true) }

        var currentPassword by remember { mutableStateOf(TextFieldValue("")) }

        CustomFormDialog(
            showDialog = showDialogState,
            title = "Change Password",
            icon = Icons.Default.Lock,
            fields = listOf(
                FormField(
                    label = "Current Password",
                    value = currentPassword.text,
                    onValueChange = {
                        currentPassword = TextFieldValue(it)
                        currentPasswordError = null
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = PasswordVisualTransformation(),
                    error = currentPasswordError
                ),
                FormField(
                    label = "New Password",
                    value = newPassword.text,
                    onValueChange = {
                        newPassword = TextFieldValue(it)
                        newPasswordError = null
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = PasswordVisualTransformation(),
                    error = newPasswordError
                ),
                FormField(
                    label = "Confirm New Password",
                    value = confirmPassword.text,
                    onValueChange = {
                        confirmPassword = TextFieldValue(it)
                        confirmPasswordError = null
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = PasswordVisualTransformation(),
                    error = confirmPasswordError
                )
            ),
            errorMessage = null, // Removed shared error
            confirmButtonText = "Save",
            cancelButtonText = "Cancel",
            onCancel = {
                showDialogState.value = false
                showChangePasswordDialog = false
                currentPassword = TextFieldValue("")
                newPassword = TextFieldValue("")
                confirmPassword = TextFieldValue("")
                currentPasswordError = null
                newPasswordError = null
                confirmPasswordError = null
            },
            onConfirm = {
                coroutine.launch {
                    val curr = currentPassword.text
                    val newP = newPassword.text
                    val confirmP = confirmPassword.text

                    val user = userController.getOnboardUser(context)
                    if (user == null) {
                        currentPasswordError = "User not found"
                        return@launch
                    }

                    var hasError = false

                    // Current Password Validation
                    if (curr.isBlank()) {
                        // Case 0: Blank
                        currentPasswordError = "Current password cannot be empty"
                        hasError = true
                    } else if (!Util.verifyPassword(curr, user.password)) {
                        // Case 1: Wrong Current password
                        currentPasswordError = "Current password is incorrect"
                        hasError = true
                    }

                    // New Password Validation
                    if (newP.isBlank()) {
                        // Case 0: Blank
                        newPasswordError = "New password cannot be empty"
                        hasError = true
                    } else if (newP == curr) {
                        // Case 2: New Password = Current Password
                        newPasswordError = "New password must be different from current"
                        hasError = true
                    } else if (!isValidPassword(newP)) {
                        // Case 3: Weak Password
                        newPasswordError =
                            "Password must be at least 8 characters, include upper/lowercase and a number"
                        hasError = true
                    }

                    // Confirm Password Validation
                    if (confirmP.isBlank()) {
                        // Case 0: Blank
                        confirmPasswordError = "Please confirm your new password"
                        hasError = true
                    } else if (newP != confirmP) {
                        // Case 4: Wrong Confirmation
                        confirmPasswordError = "Passwords do not match"
                        hasError = true
                    }

                    if (hasError) return@launch

                    // Case 5: Passed all checks — proceed
                    userController.updateUserPassword(context, newP)
                    Toast.makeText(context, "Password updated successfully", Toast.LENGTH_SHORT)
                        .show()

                    // Reset state
                    showDialogState.value = false
                    showChangePasswordDialog = false
                    currentPassword = TextFieldValue("")
                    newPassword = TextFieldValue("")
                    confirmPassword = TextFieldValue("")
                    currentPasswordError = null
                    newPasswordError = null
                    confirmPasswordError = null
                }
            }
        )
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
        //.shadow(4.dp, shape = RoundedCornerShape(1.dp))
        ,
        shape = RoundedCornerShape(12.dp)
    ) {
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
        icon = Icons.AutoMirrored.Filled.ExitToApp,
        iconTint = Color.Red,
        showDialog = showDialog,
        onConfirm = onConfirm
    )
}

fun navigateToEditAccountScreen(navController: NavController) {
    navController.navigate(route = AppScreens.EditProfileScreen.name)
}

fun isValidPassword(password: String): Boolean {
    val regex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,}$")
    // (?=.*[a-z]) → at least one lowercase letter
    // (?=.*[A-Z]) → at least one uppercase letter
    // (?=.*\\d) → at least one digit
    // [A-Za-z\\d]{8,} → only allows letters and digits, and at least 8 characters long
    return regex.matches(password)
}

