package com.ndhu.moneymanagementapp.ui.pages

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ndhu.moneymanagementapp.components.SearchBox
import com.ndhu.moneymanagementapp.controllers.ContactController
import com.ndhu.moneymanagementapp.controllers.UserController
import com.ndhu.moneymanagementapp.enums.AppScreens
import com.ndhu.moneymanagementapp.enums.ContactAddResult
import com.ndhu.moneymanagementapp.models.Contact
import com.ndhu.moneymanagementapp.models.User
import com.ndhu.moneymanagementapp.utils.Util
import kotlinx.coroutines.launch

@Composable
fun ContactPage(context: Context, navController: NavController) {

    val contactController = ContactController()
    val userController = UserController()

    val currentUserId = remember { Util.getUserId(context).toString() }
    val contactList = remember { mutableStateListOf<Contact>() }
    val pendingList = remember { mutableStateListOf<Contact>() }

    LaunchedEffect(Unit) {
        val contactsFromDb = contactController.getCurrUserLoginContact(context)
        val pendingContactsFromDb = contactController.getPendingUserListContact(context)

        Log.d("Contact Screen", "Fetched ${contactsFromDb.size} contacts")
        Log.d("Contact Screen", "Fetched ${pendingContactsFromDb.size} pending contacts")

        contactList.clear()
        contactList.addAll(contactsFromDb)

        pendingList.clear()
        pendingList.addAll(pendingContactsFromDb)
    }

    ComposeContactScreen(currentUserId, contactList, pendingList, context, navController, contactController, userController)
}

@Composable
fun ComposeContactScreen(
    currentUserId: String,
    contacts: SnapshotStateList<Contact>,
    pendingContacts: SnapshotStateList<Contact>,
    context: Context,
    navController: NavController,
    contactController: ContactController,
    userController: UserController
) {
    val showAddFriendDialog = remember { mutableStateOf(false) }

    val searchQuery = remember { mutableStateOf("") }
    val filteredFriends = filterFriends(context, contacts, searchQuery.value, userController) //  no remember

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
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
                // Content Top Bar + Search Box (Username)
                ContentTopBar(navController = navController,
                    topBarText = "Contacts",
                    searchBox = { SearchBox(searchQuery = searchQuery) }
                    )

                // Use Card to make rounded corner for LazyColumn
                ContactContainer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp)
                        //.weight(3f)
                ) {
                    // Handle empty list
                    if (contacts.isEmpty()) {
                        EmptyStateMessage("No contacts available")
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(filteredFriends, key = { it.id }) { contact ->
                                ContactCard(currentUserId, contact, context, userController)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Button Section
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                        //.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    // Pending Request Button WITH badge
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                    ) {
                        // Pending Request Button
                        Button(
                            onClick = { navController.navigate(route = AppScreens.PendingRequestScreen.name) },
                            modifier = Modifier
                                .fillMaxSize(), // ensures full Box size
                            shape = RoundedCornerShape(18.dp),
                            colors = ButtonColors(
                                containerColor = primaryGreen,
                                contentColor = Color.White,
                                disabledContainerColor = Color.Gray,
                                disabledContentColor = Color.White
                            )
                        ) {
                            Text(
                                text = "Pending Request",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                //fontFamily = surannaFont,
                            )
                        }

                        // Notification Badge
                        if (pendingContacts.isNotEmpty()) {

                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .offset(x = (-8).dp, y = 4.dp)
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(Color.Red),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = pendingContacts.size.toString(),
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                    }

                    // Add Friend Button in a Box too (even if no badge)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                    ) {
                        // Add Friend Button
                        Button(
                            onClick = { showAddFriendDialog.value = true },
                            modifier = Modifier
                                .fillMaxSize(),
                            shape = RoundedCornerShape(18.dp),
                            colors = ButtonColors(
                                containerColor = primaryGreen,
                                contentColor = Color.White,
                                disabledContainerColor = Color.Gray,
                                disabledContentColor = Color.White
                            )
                        ) {
                            Text(
                                text = "Add New Contact",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                //fontFamily = surannaFont
                            )
                        }
                    }
                }

                //Spacer(modifier = Modifier.height(20.dp))

                if (showAddFriendDialog.value) { // Observe the boolean value
                    AddFriendDialog(
                        listFriends = contacts,
                        showDialog = showAddFriendDialog, // Pass the same state down
                        context = context,
                        contactController = contactController,
                        userController = userController,
                    )
                }

            } // White Box
        } // Full Page
    } // Outer Box
}

// Card Item inside Lazy Column containing Contact information
@Composable
fun ContactCard(
    currentUserId: String,
    contact: Contact,
    context: Context,
    userController: UserController
) {
    val friendUserId = if (contact.senderId == currentUserId) contact.receiverId else contact.senderId

    // Fetch the user asynchronously when friendUserId changes
    val userResult by produceState<User?>(initialValue = null, friendUserId) {
        try {
            value = userController.searchUserByUserId(context, friendUserId)
        } catch (e: Exception) {
            Log.e("ContactCard", "Failed to fetch user: ${e.message}")
            value = null
        }
    }

    // Contact Card (Image + Username)
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
        /*Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {*/
            // Container for Profile Picture
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
            ) {
                // Profile Picture
                val profilePic = profilePictures.firstOrNull(){ it.id == (userResult?.profilePictureId ?: 1) }
                if (profilePic != null) {
                    Image(
                        painter = painterResource(id = profilePic.imageRes),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            when {
                // Case 1: Null = assumes loading
                userResult == null -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = primaryGreen
                    )
                }

                // Case 2: Blank username = “Unknown User”
                userResult?.username.isNullOrBlank() -> {
                    Text(
                        text = "Unknown User",
                        color = Color.Gray,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Case 3: Have valid username
                else -> {
                    Text(
                        text = userResult?.username ?: "Unknown",
                        color = primaryGreen,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = alexandriaFont
                    )
                }
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFriendDialog(
    showDialog: MutableState<Boolean>,
    listFriends: SnapshotStateList<Contact>,
    context: Context,
    contactController: ContactController,
    userController: UserController,
) {
    val coroutineScope = rememberCoroutineScope()
    var userName by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { showDialog.value = false },
        containerColor = Color(0xFFF5F5F5), // White Smoke
        shape = RoundedCornerShape(20.dp),
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = null,
                    tint = primaryGreen,
                    modifier = Modifier
                        .size(40.dp)
                        .padding(bottom = 4.dp)
                )

                Text(
                    text = "Add New Contact",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                OutlinedTextField(
                    value = userName,
                    onValueChange = { userName = it },
                    singleLine = true,
                    shape = RoundedCornerShape(20.dp),
                    label = {
                        Text(
                            text = "Username",
                            color = primaryGreen,
                            fontSize = 14.sp
                        )
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.White,
                        focusedIndicatorColor = primaryGreen,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = primaryGreen
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                )
            }
        },
        confirmButton = {
            // Combine Cancel + Confirm button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Cancel Button
                OutlinedButton(
                    onClick = { showDialog.value = false },
                    modifier = Modifier
                        .height(48.dp)
                        .weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF666666), // Dark Gray
                    ),
                    border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp),
                ) {
                    Text("Cancel", fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Add Button
                Button(
                    onClick = {
                        coroutineScope.launch {
                            val trimmedUsername = userName.trim()

                            // Case 1: Empty username
                            if (trimmedUsername.isEmpty()) {
                                Toast.makeText(context, "Username cannot be empty", Toast.LENGTH_SHORT).show()
                                return@launch
                            }

                            try {
                                val user = userController.searchUserByUserName(context, trimmedUsername)

                                // Case 2: User not found
                                if (user == null) {
                                    Toast.makeText(context, "User not found", Toast.LENGTH_SHORT).show()
                                    return@launch
                                }

                                // Try to add contact using the controller
                                val result = contactController.addContact(context, user.id)

                                // Case 3 : Username found
                                when (result) {
                                    ContactAddResult.SUCCESS -> {
                                        // Refresh contact list and show success message
                                        listFriends.clear()
                                        listFriends.addAll(contactController.getCurrUserLoginContact(context))
                                        Toast.makeText(context, "Contact added successfully", Toast.LENGTH_SHORT).show()
                                    }
                                    // Other Fail Cases
                                    ContactAddResult.SELF_ADD -> Toast.makeText(context, "You cannot add yourself", Toast.LENGTH_SHORT).show()
                                    ContactAddResult.USER_NOT_FOUND -> Toast.makeText(context, "User not found", Toast.LENGTH_SHORT).show()
                                    ContactAddResult.ALREADY_FRIENDS -> Toast.makeText(context, "You are already friends", Toast.LENGTH_SHORT).show()
                                    ContactAddResult.REQUEST_ALREADY_PENDING -> Toast.makeText(context, "Friend request is already pending", Toast.LENGTH_SHORT).show()
                                    ContactAddResult.REQUEST_REJECTED_BLOCKED -> Toast.makeText(context, "You cannot resend a rejected request", Toast.LENGTH_SHORT).show()
                                    ContactAddResult.ERROR -> Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: Exception) {
                                // Catch and log unexpected errors
                                Log.e("AddFriendDialog", "Error adding friend: ${e.message}")
                                Toast.makeText(context, "Failed to add contact", Toast.LENGTH_SHORT).show()
                            } finally {
                                // Reset input and close dialog
                                userName = ""
                                showDialog.value = false
                            }
                        }
                    },
                    modifier = Modifier
                        .height(48.dp)
                        .weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryGreen,
                        contentColor = Color.White
                    )
                ) {
                    Text("Add")
                }
            }
        }
    )
}