package com.ndhu.moneymanagementapp.ui.pages

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ndhu.moneymanagementapp.R
import kotlinx.coroutines.runBlocking
import com.ndhu.moneymanagementapp.controllers.UserController
import com.ndhu.moneymanagementapp.enums.AppScreens

// TODO: for profile pic (need to be moved)
data class ProfilePicture(val id: Int, val imageRes: Int)

@Composable
fun Home(context: Context, navController: NavController) {
    val loginUser = runBlocking {
        UserController().getOnboardUser(context = context)?.username ?: "{FAIL-LOGIN}"
    }
    val syncopateFont = FontFamily(
        Font(R.font.syncopate_regular, FontWeight.Normal),
        Font(R.font.syncopate_bold, FontWeight.Bold)
    )
    val kronaOneFont = FontFamily(
        Font(R.font.krona_one_regular, FontWeight.Normal)
    )
    val sensationLightFont = FontFamily(
        Font(R.font.sensation_light, FontWeight.Light)
    )

    // TODO: for profile pic (need to be moved)
    var showDialog by remember { mutableStateOf(false) }
    var selectedPictureId by remember { mutableStateOf<Int?>(null) }

    // TODO: for profile pic (need to be moved)
    val profilePictures = listOf(
        ProfilePicture(1, R.drawable.banteng),
        ProfilePicture(2, R.drawable.black_panther),
        ProfilePicture(3, R.drawable.defaultavatarimage),
        ProfilePicture(4, R.drawable.download),
        ProfilePicture(5, R.drawable.download_1),
    )


    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.background_dark),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // "WE SPLIT"
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "WE",
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge,
                    fontFamily = kronaOneFont,
                    fontSize = 12.sp,
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "SPLIT",
                    color = Color(0xFF009669), // Green color
                    style = MaterialTheme.typography.titleLarge,
                    fontFamily = kronaOneFont,
                    fontSize = 12.sp,
                )
            }

            Spacer(modifier = Modifier.height(64.dp))

            // Profile Section
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .weight(0.2f)
                    .clickable(onClick = {
                        navController.navigate(AppScreens.AccountScreen.name)
                    })
            ) {
                Image(
                    painter = painterResource(id = R.drawable.profile_picture),
                    contentDescription = null,
                    modifier = Modifier
                        .weight(0.2f)
                        .aspectRatio(1f)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.weight(0.8f)
                ) {
                    Text(
                        text = "Hello",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = sensationLightFont,
                        fontSize = 12.sp
                    )
                    Text(
                        text = loginUser,
                        color = Color.hsv(43f, 1f, 1f),
                        style = MaterialTheme.typography.titleLarge,
                        fontFamily = syncopateFont,
                        fontSize = 24.sp
                    )
                }

            }

            Spacer(modifier = Modifier.height(78.dp))

            // Greyish color Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.8f)
                    .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                    .background(Color(0xFFD9D9D9))
                    .padding(start = 24.dp, top = 48.dp, bottom = 48.dp, end = 24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    HomeButton(
                        iconResId = R.drawable.icon_split_bill,
                        text = "Split Bill",
                        textFontSize = 24,
                        onClick = { navigateToSplitBillScreen(navController) },
                        modifier = Modifier
                            .weight(3f)
                            .fillMaxHeight()
                    )
                    Column(
                        modifier = Modifier
                            .weight(2f)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        HomeButton(
                            iconResId = R.drawable.icon_history,
                            text = "Bill History",
                            textFontSize = 16,
                            onClick = { navigateToBillHistoryScreen(navController) },
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                        )
                        HomeButton(
                            iconResId = R.drawable.icon_contact,
                            text = "Contact",
                            textFontSize = 20,
                            onClick = { navigateToContactScreen(navController) },
                            modifier = Modifier
                                .weight(2f)
                                .fillMaxWidth()
                        )

                        // Button (temporary)
                        Button (onClick = { showDialog = true }){
                            Text(text = "Add Profile Picture (TEMP)")
                        }

                    }
                }
            }
        }

        // TODO: for profile pic (need to be moved)
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Choose Your Profile Picture") },
                text = {
                    LazyVerticalGrid (
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
                                        color = if (picture.id == selectedPictureId) Color.Blue else Color.Gray,
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
                    TextButton (onClick = { showDialog = false }) {
                        selectedPictureId?.let { id ->
                            // Saved picture id
                            Toast.makeText(context, "Selected Picture ID: $id", Toast.LENGTH_SHORT).show()
                        }
                        Text("Save")
                    }

                },
                dismissButton = {
                    TextButton (onClick = { showDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }

    }
}


@Composable
fun HomeButton(
    iconResId: Int,
    text: String,
    textFontSize: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val surannaFont = FontFamily(
        Font(R.font.suranna_regular, FontWeight.Normal),
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFB2DFDB))
            .clickable { onClick() }
    ) {
        // Background for button
        Image(
            painter = painterResource(id = R.drawable.button_background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            colorFilter = ColorFilter.tint(
                color = Color(0xFF009669), // Light color tint
                blendMode = BlendMode.Multiply
            ),
            modifier = Modifier
                .matchParentSize()
                .alpha(0.8f)
        )

        // Button
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            Text(
                text = text,
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = textFontSize.sp,
                fontFamily = surannaFont,
                modifier = Modifier.align(Alignment.TopStart)
            )

            Image(
                painter = painterResource(id = iconResId),
                contentDescription = text,
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.Center)
            )
        }
    }
}

fun navigateToSplitBillScreen(navController: NavController) {
    navController.navigate(route = AppScreens.ListStoresScreen.name)
}

fun navigateToBillHistoryScreen(navController: NavController) {
    navController.navigate(route = AppScreens.BillHistoryScreen.name)
}

fun navigateToContactScreen(navController: NavController) {
    navController.navigate(route = AppScreens.ContactScreen.name)
}
