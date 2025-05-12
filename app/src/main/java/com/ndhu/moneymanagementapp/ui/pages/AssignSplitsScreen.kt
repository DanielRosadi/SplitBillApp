package com.ndhu.moneymanagementapp.ui.pages

import android.content.Context
import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ndhu.moneymanagementapp.components.SearchBox
import com.ndhu.moneymanagementapp.components.UniformDesignScaffold
import com.ndhu.moneymanagementapp.controllers.SplitBillController
import com.ndhu.moneymanagementapp.enums.AppScreens
import com.ndhu.moneymanagementapp.models.User
import com.ndhu.moneymanagementapp.payloads.ItemAndOwner
import com.ndhu.moneymanagementapp.payloads.SplitBillRequest
import com.ndhu.moneymanagementapp.sharedviewmodels.SharedViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@Composable
fun AssignSplitsScreen(context: Context, navController: NavController, viewModel: SharedViewModel){

    // TODO: need to save bill owner in view model?
    // viewModel.setBillOwner()

    val splitBillController = SplitBillController()

//    val listParticipants = List(7) { "User ${it + 1}" } // Example: 7 items
//    val listItems = List(5) { "Item ${it + 1}" } // Example: 5 items

    val listParticipants = viewModel.billParticipants
    val listItems = viewModel.itemDetailList

    // TODO: need to re-load data after coming back from SplitItemScreen
    /*
    var itemAndUsersList = listItems.map { item ->
        ItemWithUsersList(item = item, ownership = mutableStateListOf())
    }.toMutableStateList()

     */

    /*
    viewModel.listItemAndUserQuantities = listItems.map { item ->
        ItemWithUsersList(item = item, ownership = mutableStateListOf())
    }.toMutableStateList()

     */

    val userColors = remember { mutableStateMapOf<User?, Color>() } // <-- HOISTED

    ComposeAssignSplits(context, listParticipants, viewModel.listItemAndUserQuantities, navController, splitBillController, viewModel, userColors)
}

@Composable
fun ComposeAssignSplits(context: Context, listParticipants: MutableList<User?>, listItemsAndOwners: MutableList<ItemWithUsersList>, navController: NavController, splitBillController: SplitBillController, viewModel: SharedViewModel, userColors: SnapshotStateMap<User?, Color>){
    // val userColors = remember { mutableStateMapOf<User?, Color>() }
    var selectedUser by remember { mutableStateOf<User?>(null) }
    // var selectedUser by remember { mutableStateOf<User?>("No User Selected") }

    /*
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

     */
    // for showing Toast message
    val localContext = LocalContext.current

    // save/cache the item and user quantities data initially (no need, already done in selectfriendpage)
    // viewModel.listItemAndUserQuantities = listItemsAndOwners

    var isAllItemsFullyAssigned by remember { mutableStateOf(false) }
    //var assignedQty by remember { mutableStateOf(0f) }
    //var isFullyAssigned by remember { mutableStateOf(false) }

    /*
    val isAllItemsFullyAssigned = listItemsAndOwners.all { itemWithUsers ->
        val assignedQty = itemWithUsers.ownership.fold(0f) { sum, itemOwnership-> sum + itemOwnership.itemQuantity.value }
        assignedQty == itemWithUsers.item.qty // check with total quantity
    }

     */

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


                // calculate if all items have been assigned, otherwise disabled save button
                isAllItemsFullyAssigned = listItemsAndOwners.all { itemWithUsers ->
                    val assignedQty = itemWithUsers.ownership.fold(0f) { sum, itemOwnership ->
                        sum + itemOwnership.itemQuantity.value
                    }
                    assignedQty == itemWithUsers.item.qty.toFloat() // Check if assigned quantity matches total quantity
                }



                /*
                // Perform the checks and update the state
                LaunchedEffect(listItemsAndOwners) { // Recalculate whenever the list changes
                    val fullyAssigned = listItemsAndOwners.all { itemWithUsers ->
                        //  val assignedQty
                        assignedQty = itemWithUsers.ownership.fold(0f) { sum, itemOwnership ->
                            sum + itemOwnership.itemQuantity.value
                        }
                        assignedQty == itemWithUsers.item.qty.toFloat() // Check if assigned quantity matches total quantity
                    }

                    // isAllItemsFullyAssigned.value = fullyAssigned // Update the state
                    isAllItemsFullyAssigned = fullyAssigned // Update the state
                }
                */


                Spacer(modifier = Modifier.height(10.dp))

                SearchBox()

                Spacer(modifier = Modifier.height(10.dp))

                // update current user selected text
                // Text(text = "Now Selecting: {${selectedUser?.username}}" ?: "No User Selected")
                Text(text = selectedUser?.let { selectedUser -> "Now Selecting: ${selectedUser.username}" } ?: "No User Selected")

                Spacer(modifier = Modifier.height(10.dp))

                LazyColumn (modifier = Modifier.weight(1f), contentPadding = PaddingValues(5.dp)) {

                    // chunked groups items into sets of 3 for each row
                    // it also automatically handles when the list is not perfectly divisible by 3
                    items(listItemsAndOwners) { itemAndUsers ->
                        ListItemSplits(
                            listItemAndUserQuantities = listItemsAndOwners,
                            navController = navController,
                            itemWithUsers = itemAndUsers,
                            selectedUser = selectedUser,
                            userColors = userColors,
                            onToggleUser = { user -> toggleUser(user, itemAndUsers) },
                            viewModel = viewModel
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
                    // *******

                    // TODO: need to discuss on qty for ItemAndOwner
                    // TODO: need to recheck if there is changes since we are implementing additional logic


                    val itemIdAndOwners = mapItemsToItemAndOwners(listItemsAndOwners)


                    val splitBillRequest = SplitBillRequest(
                        billOwnerId = viewModel.getBillOwnerId(),
                        itemIdAndOwners = itemIdAndOwners
                    )


                    runBlocking {
                        splitBillController.addSplitBill(context = context, request = splitBillRequest)
                    }

                    // TODO: need to put some toast/feedback

                    navController.navigate(route = AppScreens.HomeScreen.name)

                    // TODO: clear all data in viewModel because its for one transaction only?


                    // Example trigger (button click, etc.)
                    Toast.makeText(localContext, "Bill has been successfully saved", Toast.LENGTH_LONG).show()

                    /*
                    // provide feedback when user successfully save the bill
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = "Bill has been successfully saved",
                            // actionLabel = "Dismiss",
                            withDismissAction = true,
                            duration = SnackbarDuration.Long, // Adjust duration here

                        )
                    }
                    */


                },
                    modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                    enabled = isAllItemsFullyAssigned

                )
                {
                    Text(text = "Done")
                }



                Spacer(modifier = Modifier.height(20.dp))

            }
        }

    )
}

@Composable
fun ListItemSplits(listItemAndUserQuantities: MutableList<ItemWithUsersList>, navController: NavController, itemWithUsers: ItemWithUsersList, selectedUser: User?, userColors: SnapshotStateMap<User?, Color>, onToggleUser: (User)-> Unit, viewModel: SharedViewModel) {

    var isFullyAssigned by remember {mutableStateOf(false)}

    // keep track of quantities to provide feedback to user if the item quantities has not been assigned
    isFullyAssigned = itemWithUsers.item.qty.toFloat() == itemWithUsers.ownership.fold(0f) {sum, itemOwnership -> sum + itemOwnership.itemQuantity.value}

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(5.dp)
                .clickable { // Handle click on the entire card to toggle user
                    selectedUser?.let { onToggleUser(it) }
                },
            elevation = CardDefaults.cardElevation(4.dp),
            // colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
            colors = CardDefaults.cardColors(
                containerColor = if (!isFullyAssigned) Color(0xFFFFEBEE) // light red
                else MaterialTheme.colorScheme.surface
            )
        ) {


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(9.dp),
                verticalAlignment = Alignment.CenterVertically, //  center content vertically
                // horizontalArrangement = Arrangement.SpaceBetween // space between 3 main sections
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = itemWithUsers.item.name,
                    fontSize = 15.sp, // 18.sp
                    // modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .weight(0.5f) //  allow flexible width
                        .padding(end = 7.dp)
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
                            .weight(0.7f) // take up most space
                            .height(32.dp), // Adjust height for good dot fit
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        items(itemWithUsers.ownership) { ownership ->
                            // user == ownership.owner
                            val color = userColors[ownership.owner] ?: Color.Gray
                            UserDot(user = ownership.owner, color = color)
                        }

                        // If a user is selected, show a "plus" slot
                        if (selectedUser != null && !itemWithUsers.ownership.any{ it.owner == selectedUser }) {
                            item {
                                val alreadySelected = itemWithUsers.ownership.any{ it.owner == selectedUser }
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
                        .weight(1.0f) // share width evenly with item name
                        .padding(start = 2.dp),
                    horizontalArrangement = Arrangement.Center
                    // horizontalArrangement = Arrangement.SpaceBetween
                ){
                    // TODO: rework design for custom quantity (move to rightmost, use pencil icon)
                    /*
                    Button(
                        onClick = { navController.navigate(route = AppScreens.SplitItemScreen.name) },
                        modifier = Modifier.width(70.dp).height(70.dp),
                        shape = RoundedCornerShape(50.dp)
                    ) {
                        Text(text = "C", fontSize = 30.sp) // Custom quantity
                    }

                     */

                    Column(modifier = Modifier.fillMaxHeight()){

                        // TODO: delete when not needed, temporary for debugging

                        // This ensures:
                        // You only check and access once.
                        // You avoid inconsistencies between the two ownership[0] calls during recomposition.

                        /*
                        val firstOwnership = viewModel.currentItemAndUserQuantities.ownership.getOrNull(0)
                        val currentItem = viewModel.currentItemAndUserQuantities
                        if (firstOwnership != null){
                            // Text(text = "user: ${firstOwnership.owner.username}, itemQuantity: ${firstOwnership.itemQuantity.value}",  fontSize = 7.sp,)
                            // Text(text = "equal: " + (currentItem.item.qty == currentItem.ownership.fold(0f) {sum, itemOwnership -> sum + itemOwnership.itemQuantity.value}).toString(),  fontSize = 7.sp,)
                            Text(text = "total: " + currentItem.item.qty.toFloat().toString() + ", sum: " + currentItem.ownership.fold(0f) {sum, itemOwnership -> sum + itemOwnership.itemQuantity.value}.toString(),  fontSize = 7.sp,)

                        }
                        */


                        Text(
                            // TODO: omit additional float 0 on price
                            text = itemWithUsers.item.qty.toString() + " x " + itemWithUsers.item.price.toString(),
                            fontSize = 15.sp,
                            modifier = Modifier.padding(8.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )

                    }



                    TransparentEditButton {
                        // save/cache the item and user quantities data
                        viewModel.listItemAndUserQuantities = listItemAndUserQuantities
                        viewModel.currentItemAndUserQuantities = itemWithUsers
                        viewModel.currentItem = itemWithUsers.item // passing current item detail
                        navController.navigate(route = AppScreens.SplitItemScreen.name)
                    }

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
fun ListBillParticipants(participant: User?, userColors: SnapshotStateMap<User?, Color>, selectedUser: User?, onUserSelected: (User?) -> Unit, listParticipants: MutableList<User?>){
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
                // Toggle selection of the same user card
                if (participant == selectedUser) {
                    onUserSelected(null)  // Deselect if the same user is clicked again
                }
                else {
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
fun TransparentEditButton(clickEvent: () -> Unit) {
    Button(
        onClick = clickEvent,
        modifier = Modifier
            .padding(4.dp)
            .size(40.dp), // Make it small and square-ish
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = green, // Visible color (blue)
            contentColor = Color.White
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Edit,
            contentDescription = "Edit",
            modifier = Modifier.size(20.dp)
        )
    }
}


/*
@Composable
fun TransparentEditButton( clickEvent: () -> Unit ) {
    Button(
        onClick = clickEvent,
        modifier = Modifier.padding(16.dp),
        colors = ButtonDefaults.buttonColors(
            // containerColor = Color.Transparent,  // Make button background transparent
            containerColor = Color.Blue,
            contentColor = Color.Black          // Set the icon and text color
        )
    ) {
        Icon(
            imageVector = Icons.Filled.Edit, // Pencil/Edit icon
            contentDescription = "Edit",
            modifier = Modifier.padding(end = 8.dp) // Space between icon and text
        )
        Text(text = "Edit")
    }
}
*/


// TODO: need to adapt this code with new logic
fun mapItemsToItemAndOwners(items: MutableList<ItemWithUsersList>): List<ItemAndOwner?> {
    // flatMap transform one item into many items. in our case, one item produces multiple ItemAndOwner.
    // and then flatMap flatten all produced items into one list
    return items.flatMap { itemWithUsers ->
        itemWithUsers.ownership.map { user ->
            ItemAndOwner(
                itemId = itemWithUsers.item.id,
                ownerId = user.owner.id,
                qty = user.itemQuantity.value
            )
        }
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

/*
fun toggleUser(user: User, item: ItemWithUsersList) {
    if (item.ownership.any{it.owner == user}) {
        item.ownership.remove(user)
    } else {
        item.ownership.add(user)
    }
}

 */

fun toggleUser(user: User, item: ItemWithUsersList) {
    val ownership = item.ownership
    val existingOwnership = ownership.find { it.owner == user }

    if (existingOwnership != null) {
        ownership.remove(existingOwnership)  // Remove the ItemOwnership object
    } else {
        // TODO: update according to latest viewModel
        // TODO: need to turn off toggle? maybe toggle on causes it to reset to 1.0f item quantity
        ownership.add(ItemOwnership(owner = user, itemQuantity = mutableStateOf(1.0f)))  // Add with default quantity (by one)
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