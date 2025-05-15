package com.ndhu.moneymanagementapp.ui.pages

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ndhu.moneymanagementapp.controllers.ContactController
import com.ndhu.moneymanagementapp.controllers.UserController
import com.ndhu.moneymanagementapp.enums.AppScreens
import com.ndhu.moneymanagementapp.models.Contact
import com.ndhu.moneymanagementapp.models.User
import com.ndhu.moneymanagementapp.utils.Util

@Composable

        /*
        TODO:
            - PendingRequest Notification
            - Ganti font
            - Ganti image
            - Search function
        */

fun ContactPage(context: Context, navController: NavController) {

    val contactController = ContactController()
    val userController = UserController()
    val listFriends = remember { mutableStateListOf<Contact>() }

    LaunchedEffect(Unit) {
        val contactsFromDb = contactController.getCurrUserLoginContact(context)
        Log.d("Contact Screen", "Fetched ${contactsFromDb.size} contacts")
        listFriends.clear() // Clear old data
        listFriends.addAll(contactsFromDb) // Add fresh new data
    }

    ComposeContactScreen(listFriends, context, navController, contactController, userController)
}

@Composable
fun ComposeContactScreen(
    contacts: SnapshotStateList<Contact>,
    context: Context,
    navController: NavController,
    contactController: ContactController,
    userController: UserController
) {
    var showDialogAddFriend by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        // Background Image
        BackgroundImage()

        // Full Page
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(30.dp))
            TitleText()
            Spacer(modifier = Modifier.height(30.dp))

            // White Box
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(Color(0xFFD9D9D9))
                    .padding(10.dp)
            ) {
                // Content Top Bar
                // TODO: search func
                ContentTopBar(navController = navController, topBarText = "Contacts")

                // Use Card to make rounded corner for LazyColumn
                ContactContainer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(3f)
                ) {
                    // Handle empty list
                    if (contacts.isEmpty()) {
                        EmptyStateMessage("No contacts available")
                    } else {
                        LazyColumn(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(contacts) { contact ->
                                ContactCard(contacts, contact, context, userController)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Button Section
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    // Pending Request Button
                    Button(
                        onClick = { navController.navigate(route = AppScreens.PendingRequestScreen.name) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = primaryGreen,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "Pending Request",
                            //modifier = Modifier.background(Color.Blue),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = surannaFont,
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Add Friend Button
                    Button(
                        onClick = { showDialogAddFriend = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = primaryGreen,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "Add New Contact",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = surannaFont
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                if (showDialogAddFriend) {
                    PopUpAddFriend(
                        context = context,
                        contactController = contactController,
                        userController = userController,
                        onDismiss = { showDialogAddFriend = false },
                        listFriends = contacts
                    )
                }

            } // White Box
        } // Full Page
    } // Outer Box
}

// Card Item inside Lazy Column containing Contact information
@Composable
fun ContactCard(
    contacts: SnapshotStateList<Contact>,
    contact: Contact,
    context: Context,
    userController: UserController
) {
    // val coroutineScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(true) }

    // Get ID of both users
    val currentUserId = remember { Util.getUserId(context).toString() }
    val friendUserId = if (contact.senderId == currentUserId) contact.receiverId else contact.senderId

    // Mutable state to hold the fetched user object
    var userObject by remember { mutableStateOf<User?>(null) }

    // Fetch the user asynchronously when friendUserId changes
    LaunchedEffect(friendUserId) {
        isLoading = true
        try {
            userObject = userController.searchUserByUserId(context, friendUserId)
        } catch (e: Exception) {
            Log.e("ContactCard", "Failed to fetch user: ${e.message}")
        } finally {
            isLoading = false
        }
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
            ) {
                Icon(
                    Icons.Filled.Person,
                    contentDescription = "Profile Picture",
                    modifier = Modifier.size(40.dp),
                    tint = Color.Gray
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.height(18.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp,
                            color = primaryGreen
                        )
                    }
                }

                userObject != null -> {
                    userObject?.let { user ->
                        Text(
                            text = user.username,
                            color = primaryGreen,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = alexandriaFont
                        )
                    }
                }

                else -> {
                    Text(
                        text = "Unknown User",
                        color = Color.Gray,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

        }
    }
}
