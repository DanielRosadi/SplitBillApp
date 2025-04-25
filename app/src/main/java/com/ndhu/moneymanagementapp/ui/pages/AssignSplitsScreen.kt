package com.ndhu.moneymanagementapp.ui.pages

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ndhu.moneymanagementapp.components.SearchBox
import com.ndhu.moneymanagementapp.components.UniformDesignScaffold
import com.ndhu.moneymanagementapp.controllers.SplitBillController
import com.ndhu.moneymanagementapp.models.User
import com.ndhu.moneymanagementapp.sharedviewmodels.SharedViewModel

@Composable
fun AssignSplitsScreen(context: Context, navController: NavController, viewModel: SharedViewModel){

    // TODO: need to save bill owner in view model?
    // viewModel.setBillOwner()

    val splitBillController = SplitBillController()

//    val listParticipants = List(7) { "User ${it + 1}" } // Example: 7 items
//    val listItems = List(5) { "Item ${it + 1}" } // Example: 5 items

    val listParticipants = viewModel.billParticipants
    val listItems = viewModel.itemDetailList

    var itemAndUsersList = listItems.map { item ->
        ItemWithUsersList(item = item, owners = mutableStateListOf())
    }.toMutableStateList()

    val userColors = remember { mutableStateMapOf<User?, Color>() } // <-- HOISTED

    ComposeAssignSplits(context, listParticipants, itemAndUsersList, navController, splitBillController, viewModel, userColors)
}

@Composable
fun ComposeAssignSplits(context: Context, listParticipants: MutableList<User?>, listItemsAndOwners: MutableList<ItemWithUsersList>, navController: NavController, splitBillController: SplitBillController, viewModel: SharedViewModel, userColors: SnapshotStateMap<User?, Color>){
    // val userColors = remember { mutableStateMapOf<User?, Color>() }
    var selectedUser by remember { mutableStateOf<User?>(null) }

    UniformDesignScaffold(
        topAppBar = {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
                }
                Spacer(modifier = Modifier.width(50.dp))
                Text("Assign Splits", fontSize = 30.sp, fontWeight = FontWeight.Bold)
            }
        },

        body = {paddingValues->
            Column (modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(paddingValues)
                .padding(10.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {

                Spacer(modifier = Modifier.height(10.dp))

                SearchBox()

                Spacer(modifier = Modifier.height(10.dp))

                // update current user selected text
                Text(text = "Now Selecting: {${selectedUser?.username}}" ?: "No User Selected")

                Spacer(modifier = Modifier.height(10.dp))

                LazyColumn (modifier = Modifier.weight(1f), contentPadding = PaddingValues(5.dp)) {

                    // chunked groups items into sets of 3 for each row
                    // it also automatically handles when the list is not perfectly divisible by 3
                    items(listItemsAndOwners) { itemAndUsers ->
                        ListItemSplits(
                            itemWithUsers = itemAndUsers,
                            selectedUser = selectedUser,
                            userColors = userColors,
                            onToggleUser = { user -> toggleUser(user, itemAndUsers) }
                        )
                    }

                }

                Spacer(modifier = Modifier.height(30.dp))

                LazyRow () {
                    items(listParticipants) { participant ->

                        ListBillParticipants(participant = participant, userColors = userColors, selectedUser = selectedUser, onUserSelected = { selectedUser = it}, listParticipants = listParticipants)
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))



                Button(onClick = {
                    // TODO: if done, create list of itemIdAndOwners

                    // TODO: then, submit bill data to database (with list of itemIdAndOwners)
                    // val splitBillRequest = SplitBillRequest(billOwnerId = viewModel.getBillOwner(), itemIdAndOwners = )
                    /*
                    runBlocking {
                        splitBillController.addSplitBill(context = context, request = splitBillRequest)
                    }
                     */

                },
                    modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)) {
                    Text(text = "Done")
                }



                Spacer(modifier = Modifier.height(20.dp))

            }
        }

    )
}

@Composable
fun ListItemSplits(itemWithUsers: ItemWithUsersList, selectedUser: User?, userColors: SnapshotStateMap<User?, Color>, onToggleUser: (User)-> Unit) {


        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(5.dp)
                .clickable { // Handle click on the entire card to toggle user
                    selectedUser?.let { onToggleUser(it) }
                },
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
        ) {


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(9.dp),
                verticalAlignment = Alignment.CenterVertically, //  center content vertically
                horizontalArrangement = Arrangement.SpaceBetween // space between 3 main sections
            ) {
                Text(
                    text = itemWithUsers.item.name,
                    fontSize = 18.sp,
                    // modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .weight(1.3f) //  allow flexible width
                        .padding(end = 8.dp)
                )

                // TODO: implement selected users here

                /*
                Box(
                    modifier = Modifier
                        .fillMaxWidth(), // full width container
                    contentAlignment = Alignment.Center //  center contents
                ) {

                   */
                    // Display selected users as colored dots
                    LazyRow(
                        modifier = Modifier
                            .weight(1.6f) // take up most space
                            .height(32.dp), // Adjust height for good dot fit
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        items(itemWithUsers.owners) { user ->
                            val color = userColors[user] ?: Color.Gray
                            UserDot(user = user, color = color)
                        }

                        // If a user is selected, show a "plus" slot
                        if (selectedUser != null && !itemWithUsers.owners.contains(selectedUser)) {
                            item {
                                val alreadySelected = itemWithUsers.owners.contains(selectedUser)
                                if (!alreadySelected) {
                                    Card(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .padding(end = 4.dp)
                                            .clickable { onToggleUser(selectedUser) },
                                        shape = CircleShape,
                                        colors = CardDefaults.cardColors(containerColor = Color.LightGray),
                                        elevation = CardDefaults.cardElevation(2.dp)
                                    ) {
                                        Text(
                                            "+",
                                            modifier = Modifier.align(Alignment.CenterHorizontally),
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                //}

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .weight(1.9f) // share width evenly with item name
                        .padding(start = 4.dp),
                    horizontalArrangement = Arrangement.Center
                ){

                    Text(
                        // TODO: omit additional float 0 on price
                        text = itemWithUsers.item.qty.toString() + " x " + itemWithUsers.item.price.toString(),
                        fontSize = 18.sp,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )

                }



                /*
                Text(
                    text = "x",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    text = itemWithUsers.item.price.toString(),
                    fontSize = 18.sp,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )

                 */
            }

        }


}

@Composable
fun ListBillParticipants(participant: User?, userColors: SnapshotStateMap<User?, Color>, selectedUser: User?, onUserSelected: (User) -> Unit, listParticipants: MutableList<User?>){
    val index = listParticipants.indexOf(participant) // get participant index in the list
    val color = userColors.getOrPut(participant) {
        generateDistinctColor(index, listParticipants.size)
    }
    // update border color
    val borderColor = if (participant == selectedUser) Color.Black else Color.Transparent

    Card (
        modifier = Modifier
            .size(90.dp)
            .fillMaxWidth()
            .padding(5.dp)
            .clickable {
                if (participant != null) {
                    // apply selection, this function is required because we are passing state into a child function
                    // otherwise, error may occur
                    onUserSelected(participant)
                }
            },

        shape = CircleShape, // Makes it round
        elevation = CardDefaults.cardElevation(4.dp),
        border = BorderStroke(2.dp, borderColor),
        // colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
        // update card color based on user (according to distinct color generator)
        colors = CardDefaults.cardColors(
            containerColor = if (participant == selectedUser) color else Color.White
        )


    ) {
        Text(
            text = participant?.username ?: "Unknown User", // if participant is not null, execute participant.username else put "Unknown User"
            fontSize = 18.sp,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun UserDot(user: User, color: Color) {
    Box(
        modifier = Modifier
            .size(24.dp)
            // .padding(end = 4.dp)
            .background(color = color, shape = CircleShape) //  perfect circle
            .border(1.dp, Color.Black, shape = CircleShape),
        contentAlignment = Alignment.Center


        ,
        //shape = CircleShape,
        //colors = CardDefaults.cardColors(containerColor = color),
        //elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {}
}


fun toggleUser(user: User, item: ItemWithUsersList) {
    if (item.owners.contains(user)) {
        item.owners.remove(user)
    } else {
        item.owners.add(user)
    }
}



fun generateDistinctColor(index: Int, total: Int): Color {
    // using list participants index and list size, we can generate distinct colors

    // use the Hue component in HSL (Hue-Saturation-Lightness) to evenly space colors.
    val hue = (index * (360f / total)) % 360f
    return Color.hsl(hue, 0.6f, 0.85f)
}


// Helper: Generate a random light color
fun randomLightColor(): Color {
    val red = (150..255).random()
    val green = (150..255).random()
    val blue = (150..255).random()
    return Color(red, green, blue)
}