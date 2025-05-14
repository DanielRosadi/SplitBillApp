package com.ndhu.moneymanagementapp.ui.pages

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfile(context: Context, navController: NavController) {
    val userController = UserController()
    var user by remember { mutableStateOf<User?>(null) }

    var username by remember { mutableStateOf(TextFieldValue("")) }
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var bio by remember { mutableStateOf(TextFieldValue("")) }

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
            }
        }

        // Bottom Section
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
                verticalArrangement = Arrangement.spacedBy(30.dp)           ) {
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

                    // Profile Image
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(160.dp)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.defaultavatarimage),
                            contentDescription = "Profile Picture",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(160.dp)
                                .clip(CircleShape)
                                .border(2.dp, Color.Gray, CircleShape)
                        )

                        IconButton(
                            onClick = { /* Handle edit photo */ },
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

                    // Input Fields
                    RoundedInputField(
                        label = "username",
                        value = username,
                        onValueChange = { username = it },
                        centerPlaceholder = true
                    )

                    RoundedInputField(
                        label = "email",
                        value = email,
                        keyboardType = KeyboardType.Email,
                        onValueChange = { email = it },
                        centerPlaceholder = true
                    )

                    RoundedInputField(
                        label = "bio",
                        value = bio,
                        maxLines = Int.MAX_VALUE,
                        centerPlaceholder = true,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        onValueChange = { bio = it }
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    // Buttons
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
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

                        Button(
                            onClick = {
                                // TODO: Save data
                                navController.popBackStack()
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
}

@Composable
fun RoundedInputField(
    label: String,
    value: TextFieldValue,
    keyboardType: KeyboardType = KeyboardType.Text,
    maxLines: Int = 1,
    centerPlaceholder: Boolean = false,
    modifier: Modifier = Modifier,
    onValueChange: (TextFieldValue) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = label,
                textAlign = if (centerPlaceholder) TextAlign.Center else TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        maxLines = maxLines,
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
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
}
