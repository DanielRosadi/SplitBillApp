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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
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
import com.ndhu.moneymanagementapp.models.Contact
import kotlinx.coroutines.runBlocking

/*
TODO:
    - tampilin list Pending
    - finish design UI
    - function button acc all and reject all
    - Ganti font
    - Ganti image
    - Search
*/

@Composable
fun PendingRequestScreen (context: Context, navController: NavController)
{
    val contactController = ContactController()
    val userController = UserController()
    val listPending = remember { mutableStateListOf<Contact>() }

    LaunchedEffect(Unit) {
        val pendingContactsFromDb = contactController.getPendingUserListContact(context)
        Log.d("PendingScreen", "Fetched ${pendingContactsFromDb.size} pending contacts")
        listPending.clear()
        listPending.addAll(pendingContactsFromDb)
    }

    // Compose UI
    ComposePendingRequest(listPending, context, navController, userController)
}

@Composable
private fun ComposePendingRequest(
    contacts: SnapshotStateList<Contact>,
    context: Context,
    navController: NavController,
    userController: UserController
) {

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
                ContentTopBar(navController = navController, topBarText = "Pending Request")

                // Use Card to make rounded corner for LazyColumn
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFBDBDBD)),
                    modifier = Modifier.fillMaxWidth().weight(4f)
                ) {

                    /*TODO: change to pending list*/
                    LazyColumn(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(contacts) { contact ->
                            PendingCard(contact, context, userController)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Button Section
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        //.padding(10.dp)
                ) {
                    // Reject All
                    Button(
                        onClick = { /* TODO: Reject All Request  */ },
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                            .padding(10.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF961400), // Red
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = "Reject All", fontSize = 16.sp)
                    }

                    // Accept All
                    Button(
                        onClick = { /* TODO: Accept All Request  */ },
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                            .padding(10.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF009669), // Green
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = "Accept All", fontSize = 16.sp)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            } // White Box
        } // Full Page
    } // Box
}

// Card Item inside Lazy Column containing Pending Contact information
@Composable
fun PendingCard(contact: Contact, context: Context, userController: UserController) {
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

            // Show username of pending user
            val userId = contact.userId2
            val userObject = runBlocking{
                userController.searchUserByUserId(context, userId)
            }
            if (userObject != null) {
                Text(text = userObject.username, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF009669))
            }

            /*
            // Button Section
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                //.padding(10.dp)
            ) {
                // Reject All
                Button(
                    onClick = { *//* TODO: Reject All Request  *//* },
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .padding(10.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF961400), // Red
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Reject All", fontSize = 16.sp)
                }

                // Accept All
                Button(
                    onClick = { *//* TODO: Accept All Request  *//* },
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .padding(10.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF009669), // Green
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Accept All", fontSize = 16.sp)
                }
            } // Buttons
            */

        }
    } // Card
}