package com.ndhu.moneymanagementapp.ui.pages

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ndhu.moneymanagementapp.enums.AppScreens
import com.ndhu.moneymanagementapp.controllers.ContactController
import com.ndhu.moneymanagementapp.controllers.UserController
import com.ndhu.moneymanagementapp.models.Contact
import com.ndhu.moneymanagementapp.sharedviewmodels.SharedViewModel
import kotlinx.coroutines.runBlocking

@Composable
fun SelectFriendsPage(context : Context, navController: NavController, viewModel: SharedViewModel){

    var contactController = ContactController()
    var userController = UserController()

    // TODO: need to save item list in viewModel
    // viewModel.setItemList()



    // val listFriends = List(7) { "Friend ${it + 1}" }
    var listFriends = remember { mutableStateListOf<Contact>() }

    // TODO: Get Contacts
    // getContacts() here
    runBlocking {
        listFriends.clear()
        listFriends.addAll(contactController.getCurrUserLoginContact(context))
    }

    ComposeSelectFriends(context, listFriends, navController, contactController, userController, viewModel)
}


@Composable
fun ComposeSelectFriends(context : Context, listFriends: SnapshotStateList<Contact>, navController: NavController, contactController: ContactController, userController: UserController, viewModel: SharedViewModel){
    // TODO: Bind with Contact
    val selectedFriends = remember { mutableStateListOf<Contact>() }
    // val selectedFriends = remember { mutableStateListOf<String>() }

    var showDialogAddFriend by remember { mutableStateOf(false) }

    //UniformDesignScaffold(
        /*
        topAppBar = {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
                }
                Spacer(modifier = Modifier.width(50.dp))
                Text("Select Friends", fontSize = 40.sp, fontWeight = FontWeight.Bold)
            }
        },
        */


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {

        // Background Image
        BackgroundImage()



        // Full page
        Column(
            modifier = Modifier
                .fillMaxWidth()
                // .fillMaxHeight()
            // .padding(paddingValues)
            //.padding(10.dp)
            ,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(30.dp))

            TitleText()

            Spacer(modifier = Modifier.height(30.dp))


            // White box



//body = {paddingValues->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(Color(0xFFD9D9D9))
                    .padding(10.dp)


                    // .padding(paddingValues)
                    //.padding(10.dp),
                //verticalArrangement = Arrangement.Center,
               // horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Content Top Bar
                // TODO: search func
                ContentTopBar(navController = navController, topBarText = "Select Friend")


                //Spacer(modifier = Modifier.height(10.dp))

                // TODO: implement search feature
                //SearchBox()

                // Spacer(modifier = Modifier.height(10.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(5.5f)
                        .clip(RoundedCornerShape(20.dp))
                        .background(darkGrey)
                ) {

                    LazyColumn(
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        contentPadding = PaddingValues(5.dp)
                    ) {

                        items(listFriends) { friend ->
                            // initially, isSelected is false for all friends (cards are unchecked)
                            // selectedFriends list are empty
                            val isSelected = friend in selectedFriends

                            // if user clicks the friend's card, it will be added to selectedFriends list
                            // else, if user unclick the friend's card, it will be remove from the selectedFriends list
                            // we only define the isSelected and onCheckedChange function as parameter here, execution happens later
                            ListFriends(
                                context = context,
                                friend = friend,
                                isSelected = isSelected,
                                onCheckedChange = { checked ->
                                    if (checked) selectedFriends.add(friend)
                                    else selectedFriends.remove(friend)
                                },
                                userController = userController
                            )
                        }

                    }
                }

                // Spacer(modifier = Modifier.height(30.dp))
                Spacer(modifier = Modifier.height(15.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    //.fillMaxHeight()
                    .weight(2.5f)
                    .padding(2.dp)
                ,
                horizontalAlignment = Alignment.CenterHorizontally
            ){

                Button(
                    onClick = { showDialogAddFriend = true },
                    modifier = Modifier.fillMaxWidth().height(60.dp)
                    ,
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonColors(
                        containerColor = primaryGreen,
                        contentColor = Color.White,
                        disabledContainerColor = Color.Gray,
                        disabledContentColor = Color.White
                    )
                ) {
                    // TODO: font family?
                    Text(text = "Add Friend",
                        fontSize = 24.sp,
                        //fontFamily = surannaFont
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        navController.navigate(route = AppScreens.SelectBillOwnerScreen.name)
                        viewModel.setFriendList(selectedFriends) // save the list of friends in SharedViewModel

                        // load and save item and users data for next screen (AssignSplitScreen)
                        viewModel.listItemAndUserQuantities = viewModel.itemDetailList.map { item ->
                            ItemWithUsersList(item = item, ownership = mutableStateListOf())
                        }.toMutableStateList()
                    },
                    modifier = Modifier.fillMaxWidth().height(60.dp)
                    ,
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonColors(
                        containerColor = primaryGreen,
                        contentColor = Color.White,
                        disabledContainerColor = Color.Gray,
                        disabledContentColor = Color.White
                    )
                ) {
                    // TODO: font family?
                    Text(text = "Done",
                        fontSize = 24.sp,
                        //fontFamily = surannaFont
                    )
                }


                Spacer(modifier = Modifier.height(20.dp))

                if (showDialogAddFriend) {
                    PopUpAddFriend(
                        context,
                        contactController,
                        userController,
                        onDismiss = { showDialogAddFriend = false },
                        listFriends = listFriends
                    )
                }

            }

            }
            //})
        }
    }
}

@Composable
fun ListFriends(context: Context, friend: Contact, isSelected: Boolean, onCheckedChange: (Boolean) -> Unit, userController: UserController){
    var friendName = runBlocking { userController.searchUserByUserId(context, friend.receiverId)?.username
        ?: "Unknown Contact" }

    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clickable{onCheckedChange(!isSelected)},
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(modifier = Modifier.
            fillMaxWidth().
            padding(15.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ){

            Spacer(modifier = Modifier.width(5.dp))

            // Box (canvas) to show profile picture
            Box(
                modifier = Modifier.size(50.dp).clip(CircleShape)
            ) {
                // TODO: Change Icon -> Image later
                Icon(
                    Icons.Filled.Person,
                    contentDescription = "Profile Picture",
                    modifier = Modifier.size(70.dp),
                    tint = Color.Gray
                )
            }

            Spacer(modifier = Modifier.width(1.dp))


            Text(
                text = friendName,
                color = primaryGreen,
                fontSize = 20.sp,
                fontFamily = alexandriaFont,
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyLarge
            )
            // Added checkbox based on whether friend/contact is selected or not
            // If user clicks the Friend's card, isSelected is true. Else, if unclicks, isSelected is false
            // Using the defined onCheckedChange function, it gets evaluated in this code block
            // This is done separately in each card/friend/record


            // TODO: no need spacer if dont use checkbox
            Spacer(modifier = Modifier.width(120.dp))


            // TODO: should save checked friends for when user navigates back?
            Checkbox(
                checked = isSelected,
                onCheckedChange = onCheckedChange
            )
        }
    }
}

@Composable
fun PopUpAddFriend(context: Context, contactController: ContactController, userController: UserController, onDismiss: () -> Unit, listFriends: SnapshotStateList<Contact>) {
    var userName by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Username") },
        text = {
            Column {
                TextField(
                    value = userName,
                    onValueChange = { userName = it },
                    label = { /*Text("Username")*/ }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {


                    // TODO: try rememberCoroutineScope(), coroutineScope.launch{}
                    runBlocking {
                        userController.searchUserByUserName(context, userName)
                            ?.let { contactController.addContact(context = context, it.id) }
                    }


                    runBlocking {
                        listFriends.clear()
                        listFriends.addAll(contactController.getCurrUserLoginContact(context))
                    }

                    onDismiss()
                },
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
        }
    )

}