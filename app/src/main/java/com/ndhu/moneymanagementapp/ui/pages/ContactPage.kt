package com.ndhu.moneymanagementapp.ui.pages

import android.content.Context
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
import kotlinx.coroutines.runBlocking
@Composable

fun ContactPage(context: Context, navController: NavController) {

    val contactController = ContactController()
    val userController = UserController()
    val listFriends = remember { mutableStateListOf<Contact>() }

    // Fetch user's contacts from Firebase and display them when the screen is launched
    // Unit = "key" to make LaunchedEffect run only once
    LaunchedEffect(Unit) {
        val contactsFromDb = contactController.getCurrUserLoginContact(context)
        listFriends.clear() // Clear old data
        listFriends.addAll(contactsFromDb) // Add fresh new data
    }

    ComposeContactScreen(context, navController, listFriends, contactController, userController)
}

@Composable
fun ComposeContactScreen(
    context: Context,
    navController: NavController,
    contacts: SnapshotStateList<Contact>,
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
        ){
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
            )
            {
                // Content Top Bar
                // TODO: search func
                ContentTopBar(navController = navController, topBarText = "Contacts")

                // Use Card to make rounded corner for LazyColumn
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFBDBDBD)),
                    modifier = Modifier.fillMaxWidth().weight(3f)
                ) {
                    LazyColumn(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(contacts) { contact ->
                            ContactItem(context, contact, userController)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Column(modifier = Modifier.fillMaxWidth().weight(1f))
                {
                    // Pending Request Button
                    Button(
                        onClick = { navController.navigate(route = AppScreens.PendingRequestScreen.name) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF009669),
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = "Pending Request", fontSize = 16.sp)
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
                            containerColor = Color(0xFF009669),
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = "Add New Contact", fontSize = 16.sp)
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
fun ContactItem(context: Context, contact: Contact, userController: UserController) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth().height(80.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize().padding(20.dp)
        ) {
            // Box (canvas) to show profile picture
            Box(
                modifier = Modifier.size(32.dp).clip(CircleShape)
            ) {
                // TODO: Change Icon -> Image later
                Icon(
                    Icons.Filled.Person,
                    contentDescription = "Profile Picture",
                    modifier = Modifier.size(40.dp),
                    tint = Color.Gray
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Show friend's username
            val userId = contact.userId2
            val userObject = runBlocking{
                userController.searchUserByUserId(context, userId)
            }
            if (userObject != null) {
                Text(text = userObject.username, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF009669))
            }
        }
    } // Card
}