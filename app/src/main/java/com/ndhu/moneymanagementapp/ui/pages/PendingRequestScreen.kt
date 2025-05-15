package com.ndhu.moneymanagementapp.ui.pages

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ndhu.moneymanagementapp.controllers.ContactController
import com.ndhu.moneymanagementapp.controllers.UserController
import com.ndhu.moneymanagementapp.models.Contact
import com.ndhu.moneymanagementapp.models.User
import kotlinx.coroutines.launch

/*
TODO:
    - Ganti font
    - Ganti image
    - Search function
*/

@Composable
fun PendingRequestScreen(context: Context, navController: NavController) {
    val contactController = ContactController()
    val userController = UserController()
    val listPending = remember { mutableStateListOf<Contact>() }

    LaunchedEffect(Unit) {
        val pendingContactsFromDb = contactController.getPendingUserListContact(context)
        Log.d("Pending Screen", "Fetched ${pendingContactsFromDb.size} pending contacts")
        listPending.clear()
        listPending.addAll(pendingContactsFromDb)
    }

    ComposePendingRequest(listPending, context, navController, userController, contactController)
}

@Composable
private fun ComposePendingRequest(
    contacts: SnapshotStateList<Contact>,
    context: Context,
    navController: NavController,
    userController: UserController,
    contactController: ContactController
) {
    val coroutineScope = rememberCoroutineScope()

    // Confirmation dialog for Accept/Reject All
    val showRejectAllDialog = remember { mutableStateOf(false) }
    val showAcceptAllDialog = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize()
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
                // TODO: search function
                ContentTopBar(navController = navController, topBarText = "Pending Request")

                // Use Card to make rounded corner for LazyColumn
                ContactContainer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(4f)
                ) {
                    // Handle empty list
                    if (contacts.isEmpty()) {
                        EmptyStateMessage("No pending friend requests")
                    } else {
                        LazyColumn(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(contacts, key = { it.senderId }) { contact ->
                                PendingCard(
                                    contacts, contact, context, userController, contactController
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Button Section
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Reject All
                    Button(
                        onClick = { showRejectAllDialog.value = true },
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                            .padding(10.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = dangerRed,
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = "Reject All", fontSize = 16.sp)
                    }

                    // Accept All
                    Button(
                        onClick = { showAcceptAllDialog.value = true },
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                            .padding(10.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = primaryGreen,
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = "Accept All", fontSize = 16.sp)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Show Reject All confirmation dialog
                if (showRejectAllDialog.value) {
                    ConfirmationDialog(
                        title = "Confirm Reject All",
                        message = "Are you sure you want to reject all friend requests?",
                        confirmText = "Reject All",
                        confirmColor = dangerRed,
                        onConfirm = {
                            coroutineScope.launch {
                                try {
                                    contacts.toList().forEach {
                                        contactController.rejectFriend(context, it.senderId)
                                    }
                                    contacts.clear()
                                    Log.d("PendingRequest", "All requests rejected.")
                                } catch (e: Exception) {
                                    Log.e("PendingRequest", "Failed to reject all: ${e.message}")
                                } finally {
                                    showRejectAllDialog.value = false // Manual dismissal
                                }
                            }
                        },
                        showDialog = showRejectAllDialog
                    )
                } // Reject All Dialog

                // Show Accept All confirmation dialog
                if (showAcceptAllDialog.value) {
                    ConfirmationDialog(
                        title = "Confirm Accept All",
                        message = "Are you sure you want to accept all friend requests?",
                        confirmText = "Accept All",
                        confirmColor = primaryGreen,
                        onConfirm = {
                            coroutineScope.launch {
                                try {
                                    contacts.toList().forEach {
                                        contactController.acceptFriend(context, it.senderId)
                                    }
                                    contacts.clear()
                                    Log.d("PendingRequest", "All requests accepted.")
                                } catch (e: Exception) {
                                    Log.e("PendingRequest", "Failed to accept all: ${e.message}")
                                } finally {
                                    showAcceptAllDialog.value = false // Manual dismissal
                                }
                            }
                        },
                        showDialog = showAcceptAllDialog
                    )
                } // Accept All Dialog

            } // White Box
        } // Full Page
    } // Box
}

// Card Item inside Lazy Column containing Pending Contact information
@Composable
fun PendingCard(
    contacts: SnapshotStateList<Contact>,
    contact: Contact,
    context: Context,
    userController: UserController,
    contactController: ContactController
) {
    val coroutineScope = rememberCoroutineScope()

    // Store the state of the sender's username
    val userObject = remember { mutableStateOf<User?>(null) }

    // Fetch the sender's username
    LaunchedEffect(contact.senderId) {
        val sender = userController.searchUserByUserId(context, contact.senderId)
        userObject.value = sender
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        // Outer Row for Card contents
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize()
        ) {
            // Row 1 = Profile Image and Name
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(2f)
                    .padding(20.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Profile Picture",
                    tint = Color.Gray
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Show sender's username
                userObject.value?.let {
                    Text(
                        text = it.username,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF009669)
                    )
                }

            } // Row 1

            // Row 2 = Accept/Reject buttons
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Reject
                BoxIconButton(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Outlined.Cancel,
                    backgroundColor = Color(0xFFD84315),
                    onClick = {
                        coroutineScope.launch {
                            try {
                                contactController.rejectFriend(context, contact.senderId)
                                contacts.remove(contact)
                                Log.d("PendingCard", "Rejected friend request: ${contact.senderId}")
                                // Optional: Show snackbar/toast
                            } catch (e: Exception) {
                                Log.e("PendingCard", "Failed to reject: ${e.message}")
                                // Optional: Show error feedback to user
                            }
                        }
                    },
                    contentDescription = "Reject"
                )

                // Accept
                BoxIconButton(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Outlined.CheckCircleOutline,
                    backgroundColor = Color(0xFF009688),
                    onClick = {
                        coroutineScope.launch {
                            try {
                                contactController.acceptFriend(context, contact.senderId)
                                contacts.remove(contact)
                                Log.d("PendingCard", "Accepted friend request: ${contact.senderId}")
                                // Optional: Show snackbar/toast
                            } catch (e: Exception) {
                                Log.e("PendingCard", "Failed to accept: ${e.message}")
                                // Optional: Show error feedback to user
                            }
                        }
                    },
                    contentDescription = "Accept"
                )

            } // Row 2
        } // Outer Row
    } // Card
}

// Reusable for LazyColumn Container
@Composable
fun ContactContainer(
    modifier: Modifier = Modifier,
    containerColor: Color = darkGrey,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
    ) {
        content()
    }
}

// Reusable for Empty State in LazyColumn
@Composable
fun EmptyStateMessage(message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFBDBDBD)), // light gray
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            color = Color(0xFF616161), // darker gray
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
    }
}

// Reusable for accept/reject-type actions in any list card
@Composable
fun BoxIconButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    backgroundColor: Color,
    onClick: () -> Unit,
    contentDescription: String
) {
    Box(
        modifier = modifier
            .clickable { onClick() }
            .fillMaxHeight()
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = Color.White
        )
    }
}

// Reusable for Confirmation Dialog (pop up)
@Composable
fun ConfirmationDialog(
    title: String,
    message: String,
    confirmText: String,
    cancelText: String = "Cancel",
    confirmColor: Color,
    onConfirm: () -> Unit,
    showDialog: MutableState<Boolean>
) {
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text(title) },
            text = { Text(message) },
            confirmButton = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { showDialog.value = false },
                        modifier = Modifier
                            .height(48.dp)
                            .weight(5f),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = darkGrey,
                            contentColor = Color.White
                        ),
                    ) {
                        Text(cancelText)
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = {
                            onConfirm()
                        },
                        modifier = Modifier
                            .height(48.dp)
                            .weight(5f),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = confirmColor,
                            contentColor = Color.White
                        )
                    ) {
                        Text(confirmText)
                    }
                }
            },
            containerColor = Color.White
        )
    }
}