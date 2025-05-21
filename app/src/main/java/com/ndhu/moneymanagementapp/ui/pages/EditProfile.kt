package com.ndhu.moneymanagementapp.ui.pages

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ndhu.moneymanagementapp.R
import com.ndhu.moneymanagementapp.controllers.UserController
import com.ndhu.moneymanagementapp.models.User
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfile(context: Context, navController: NavController) {
    val userController = UserController()
    val coroutineScope = rememberCoroutineScope()

    // State variables for user info
    var username by remember { mutableStateOf(TextFieldValue("")) }
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var bio by remember { mutableStateOf(TextFieldValue("")) }

    // State for input validation errors
    var usernameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var bioError by remember { mutableStateOf<String?>(null) }

    // State for profile picture dialog
    var showEditProfilePictureDialog by remember { mutableStateOf(false) }
    var selectedPictureId by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(Unit) {
        val user = userController.getOnboardUser(context)
        if (user != null) {
            username = TextFieldValue(user.username)
            email = TextFieldValue(user.email)
            bio = TextFieldValue(user.bio)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // --- Top Banner with Logo and Back Button ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.1f)
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
                // Scaffold section
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                ) {
                    // Back button
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

                    // App title
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
            }
        }

        // --- Main Form Section ---
        Surface(
            color = Color(0xFFF5F5F5),
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.9f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 40.dp),
                verticalArrangement = Arrangement.spacedBy(30.dp)
            ) {
                // Container for form content
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Color(0xFFD9D9D9),
                            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
                        )
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                ) {
                    Text(
                        text = "Account",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )

                    // --- Profile Picture Section ---
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(160.dp)
                            .align(Alignment.CenterHorizontally)
                    ) {

                        var user: User? = null
                        runBlocking {
                            user = userController.getOnboardUser(context)
                        }

                        var profilePic =
                            profilePictures.firstOrNull() { it.id == (user?.profilePictureId ?: 1) }

                        // Display selected profile picture
                        if (profilePic != null) {
                            Image(
                                // painter = painterResource(id = R.drawable.defaultavatarimage),
                                painter = painterResource(id = profilePic.imageRes),
                                contentDescription = "Profile Picture",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(140.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, Color.Gray, CircleShape)
                            )
                        }

                        // Edit picture button
                        IconButton(
                            onClick = { showEditProfilePictureDialog = true },
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color.Gray)
                                .align(Alignment.TopEnd)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Icon",
                                tint = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // --- Input Fields ---
                    RoundedInputField(
                        label = "username",
                        value = username,
                        onValueChange = {
                            if (it.text.length <= 20) {
                                username = it
                            }
                        },
                        centerPlaceholder = true,
                        modifier = Modifier
                            .fillMaxWidth(),
                        error = usernameError
                    )

                    RoundedInputField(
                        label = "email",
                        value = email,
                        keyboardType = KeyboardType.Email,
                        onValueChange = { email = it },
                        centerPlaceholder = true,
                        error = emailError
                    )

                    RoundedInputField(
                        label = "bio",
                        value = bio,
                        maxLines = 2,
                        centerPlaceholder = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 60.dp),
                        onValueChange = {
                            if (it.text.length <= 50) {
                                bio = it
                            }
                        },
                        error = bioError
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    // --- Save/Cancel Buttons ---
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Cancel
                        Button(
                            onClick = { navController.popBackStack() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .padding(end = 8.dp)
                        ) {
                            Text("Cancel", color = Color.White, fontSize = 16.sp)
                        }

                        // Save
                        Button(
                            onClick = {
                                // Reset previous errors
                                usernameError = null
                                emailError = null

                                val trimmedUsername = username.text.trim()
                                val trimmedEmail = email.text.trim()

                                var isValid = true

                                // Validate fields
                                if (trimmedUsername.length < 3) {
                                    usernameError = "Username must be at least 3 characters"
                                    isValid = false
                                }

                                if (!isValidEmail(trimmedEmail)) {
                                    emailError = "Invalid email format"
                                    isValid = false
                                }

                                if (bio.text.length > 50) {
                                    bioError = "Bio: max 50 characters"
                                    isValid = false
                                }

                                // Submit if valid
                                if (isValid) {
                                    coroutineScope.launch {
                                        val result = userController.updateUserData(
                                            context = context,
                                            newUsername = trimmedUsername,
                                            newEmail = trimmedEmail,
                                            newBio = bio.text
                                        )
                                        if (result != null) {
                                            Toast.makeText(context, result, Toast.LENGTH_SHORT)
                                                .show()
                                        } else {
                                            navController.popBackStack()
                                        }
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)), // Green
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .padding(start = 8.dp)
                        ) {
                            Text("Save", color = Color.White, fontSize = 16.sp)
                        }
                    }
                }
            }
        }
    }

    // TODO: for profile pic (need to be moved)
    // --- Profile Picture Selection Dialog ---
    if (showEditProfilePictureDialog) {
        AlertDialog(
            onDismissRequest = { showEditProfilePictureDialog = false },
            title = { Text("Choose Your Profile Picture") },
            text = {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.height(250.dp)
                ) {
                    items(profilePictures) { picture ->
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .border(
                                    width = if (picture.id == selectedPictureId) 4.dp else 2.dp,
                                    color = if (picture.id == selectedPictureId) primaryGreen else Color.Gray,
                                    shape = CircleShape
                                )
                                .clickable {
                                    selectedPictureId = picture.id
                                    // showDialog = false // Close dialog after selection
                                }
                        ) {
                            Image(
                                painter = painterResource(id = picture.imageRes),
                                contentDescription = "Profile Picture ${picture.id}",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showEditProfilePictureDialog = false }) {
                    selectedPictureId?.let { id ->
                        // Saved picture id

                        coroutineScope.launch {
                            userController.updateUserProfile(
                                context = context,
                                newProfilePictureId = id
                            )
                        }

                        Toast.makeText(context, "Selected Picture ID: $id", Toast.LENGTH_SHORT)
                            .show()

                    }
                    Text("Save")
                }

            },
            dismissButton = {
                TextButton(onClick = { showEditProfilePictureDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun RoundedInputField(
    label: String,
    value: TextFieldValue,
    keyboardType: KeyboardType = KeyboardType.Text,
    maxLines: Int = 1,
    centerPlaceholder: Boolean = false,
    modifier: Modifier = Modifier,
    error: String? = null,
    onValueChange: (TextFieldValue) -> Unit
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            isError = error != null,
            placeholder = {
                Text(
                    text = label,
                    textAlign = if (centerPlaceholder) TextAlign.Center else TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            maxLines = maxLines,
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            textStyle = TextStyle(textAlign = if (centerPlaceholder) TextAlign.Center else TextAlign.Start),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Gray,
                unfocusedBorderColor = Color.Transparent,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedPlaceholderColor = Color.Gray,
                unfocusedPlaceholderColor = Color.Gray
            )
        )
        if (error != null) {
            Text(
                text = error,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

fun isValidEmail(email: String): Boolean {
    val regex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
    return regex.matches(email)
}