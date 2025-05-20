package com.ndhu.moneymanagementapp.ui.pages

import android.content.Context
import com.ndhu.moneymanagementapp.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
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
    val coroutine = rememberCoroutineScope()
    val userController = UserController()
    var user by remember { mutableStateOf<User?>(null) }
    var showClearUserDataDialog by remember { mutableStateOf(false) }
    var showLogOutDialog by remember { mutableStateOf(false) }
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
                .weight(0.5f)
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

                Spacer(modifier = Modifier.height(55.dp))

                var profilePic =
                    profilePictures.firstOrNull() { it.id == (user?.profilePictureId ?: 1) }

                // Profile Image
                if (profilePic != null) {
                    Image(
                        //                    painter = painterResource(id = R.drawable.profile_picture),
                        painter = painterResource(id = profilePic.imageRes),
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
                    color = Color(0xFFF5C542), // golden yellow
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
                        showClearUserDataDialog = true
                    }

                    ColoredSettingsButton(
                        text = "Log Out",
                        backgroundColor = Color(0xFF009669),
                        modifier = Modifier.weight(0.3f)
                    ) {
                        showLogOutDialog = true
                    }
                }
            }
        }
    }

    if (showClearUserDataDialog) {
        DeleteAccountDialog(
            onDismiss = { showClearUserDataDialog = false },
            onConfirm = {
                coroutine.launch {
                    userController.deleteCurrUser(context);
                }
                navController.navigate(AppScreens.LogInScreen.name) {
                    popUpTo(0) { inclusive = true }
                }
            }
        )
    }

    if (showLogOutDialog) {
        LogoutDialog(
            onDismiss = { showLogOutDialog = false },
            onConfirm = {
                coroutine.launch { userController.logout(context) }
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
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.3f))
            .wrapContentSize(Alignment.Center)
    ) {
        Box(
            contentAlignment = Alignment.TopCenter
        ) {
            Card(
                modifier = Modifier
                    .padding(horizontal = 30.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(
                        top = 40.dp,
                        bottom = 20.dp,
                        start = 16.dp,
                        end = 16.dp
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Are you sure you want to delete your account?",
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        color = Color.Black,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "This action cannot be undone",
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        color = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = onDismiss,
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF009669),
                                contentColor = Color.White
                            )
                        ) {
                            Text(
                                "Cancel",
                                fontSize = 18.sp,
                                fontFamily = surannaFont,
                                maxLines = 1,
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(
                            onClick = onConfirm,
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF961400),
                                contentColor = Color.White
                            )
                        ) {
                            Text(
                                "Delete",
                                fontSize = 18.sp,
                                fontFamily = surannaFont,
                                maxLines = 1,
                            )
                        }
                    }
                }
            }

            // "!" Logo symbol
            Box(
                modifier = Modifier
                    .offset(y = (-25).dp)
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF8B0000)),
                contentAlignment = Alignment.Center
            ) {
                Text("!", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun LogoutDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.3f))
            .wrapContentSize(Alignment.Center)
    ) {
        Card(
            modifier = Modifier
                .padding(horizontal = 30.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(vertical = 24.dp, horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Are you sure you want to log out?",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF009669),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Cancel", fontSize = 18.sp, fontFamily = surannaFont)
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = onConfirm,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF961400),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Log Out", fontSize = 18.sp, fontFamily = surannaFont)
                    }
                }
            }
        }
    }
}

fun navigateToEditAccountScreen(navController: NavController) {
    navController.navigate(route = AppScreens.EditProfileScreen.name)
}
