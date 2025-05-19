package com.ndhu.moneymanagementapp.ui.pages

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ndhu.moneymanagementapp.enums.AppScreens
import com.ndhu.moneymanagementapp.models.User
import com.ndhu.moneymanagementapp.sharedviewmodels.SharedViewModel

@Composable
fun SplitItemScreen(context: Context, navController: NavController, viewModel: SharedViewModel){
    ComposeSplitItem(context = context, navController = navController, viewModel = viewModel)
}

@Composable
fun ComposeSplitItem(context: Context, navController: NavController, viewModel: SharedViewModel){
    // val listItemsAndOwners = viewModel.listItemAndUserQuantities
    var totalAssignedQuantity by remember { mutableStateOf(0f) }
    val totalItemQuantity by remember { mutableStateOf(viewModel.currentItem.qty.toFloat()) } // cast to Float type from a Number type

    // Calculate total assigned quantity and total item quantity for each item
    //totalItemQuantity = viewModel.currentItemAndUserQuantities.item.qty // total item quantity

    // calculate/sum the item quantity assigned to each user/owner
    totalAssignedQuantity = viewModel.currentItemAndUserQuantities.ownership.fold(0f) { sum, itemOwnership ->
        sum + itemOwnership.itemQuantity.value  // Add each itemQuantity.value (Float)
    }

    // Determine if the quantity is fully assigned, over-assigned, or under-assigned
    // uses annotated string to allow different part of the text to be modified individually
    val assignedStatus = when {
        totalAssignedQuantity.compareTo(totalItemQuantity) == 0 ->
            buildAnnotatedString {
                append("Item Quantity Assigned: ")
                withStyle(style = SpanStyle(color = Color(0xFF008000))) { // using "correct" green color code
                    append("FULLY assigned")
                }
            }

        totalAssignedQuantity.compareTo(totalItemQuantity) > 0 ->
            buildAnnotatedString {
                append("Item Quantity Assigned: ")
                withStyle(style = SpanStyle(color = Color(0xFFFF2C2C))) { // using "error" red color code
                    append("excess by ${(totalAssignedQuantity - totalItemQuantity)} (+)")
                }
            }

        totalAssignedQuantity.compareTo(totalItemQuantity) < 0 ->
            buildAnnotatedString {
                append("Item Quantity Assigned: ")
                withStyle(style = SpanStyle(color = Color(0xFFFF2C2C))) { // using "error" red color code
                    append("short by ${(totalItemQuantity - totalAssignedQuantity)} (-)")
                }
            }

        else -> buildAnnotatedString { append("") }
    }

    // Control save button state
    val isSaveEnabled = totalAssignedQuantity == totalItemQuantity





    // TODO: try debug viewModel real time to observe the data changes
    /*
    var copyCurrentItemAndUserQuantities by remember{
        mutableStateOf(
            viewModel.currentItemAndUserQuantities.copy(
            // Use mutableStateListOf to create a state-backed list
            ownership = mutableStateListOf<ItemOwnership>().apply {
                addAll(viewModel.currentItemAndUserQuantities.ownership.map { it.copy() })
            }
        ))
    }

     */

    // copy of current item with users/owners list (temp)
    viewModel.tempItemAndUserQuantities = deepCopyItemWithUsersList(viewModel.currentItemAndUserQuantities)

    /*
    var copyCurrentItemAndUserQuantities = viewModel.currentItemAndUserQuantities.copy()

    var tempItemAndUserQuantities by remember {
        mutableStateOf(viewModel.currentItemAndUserQuantities.copy())
    }

     */



    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {

        // Background Image
        BackgroundImage()

        // Full Page
        Column(
            modifier = Modifier
                .fillMaxWidth()
            //.fillMaxHeight()
            // .padding(paddingValues)
            //.padding(10.dp)
            ,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(30.dp))

            TitleText()

            Spacer(modifier = Modifier.height(30.dp))

            // White Box
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(Color(0xFFD9D9D9))
                    .padding(10.dp)
                //.padding(paddingValues)
                ,
                //verticalArrangement = Arrangement.Top,
                //horizontalAlignment = Alignment.CenterHorizontally
                ) {

                SplitItemContentTopBar(navController = navController, topBarText = "Split Item")

//                Column(
//                    modifier = Modifier.fillMaxWidth().fillMaxHeight()
//                ) {
//                    // Top Bar
//                    Column(
//                        modifier = Modifier.fillMaxWidth().weight(1f)
//                    ) {
//                        SplitItemTopBar(navController = navController, viewModel = viewModel)
//                    }

                    // Note
                    Column(
                        modifier = Modifier.fillMaxWidth().weight(4.5f)
                    ) {
                        // Header Text
                        // TODO: Replace with real data
                        Text(text = viewModel.currentItem.name,
                            color = primaryGreen,
                            fontSize = 25.sp,
                            fontFamily = alexandriaFont,
                            //modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Text(
                            text = "You can adjust quantity of assigned items to friends.",
                            fontSize = 15.sp,
                            fontFamily = sensationLightFont,
                            //modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "Decimal number is allowed. For example: 0.5 defines half portion, or other decimal number based on agreement.",
                            fontSize = 15.sp,
                            fontFamily = sensationLightFont,
                            //modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "Price will be calculated based on quantity assigned.",
                            fontSize = 15.sp,
                            fontFamily = sensationLightFont,
                            //modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )

                        // TODO: make it responsive (cannot add more if exceed. cannot save if is NOT fully assigned)
                        Text(
                            text = "Total Item Quantity: $totalItemQuantity",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = alexandriaFont,
                            style = MaterialTheme.typography.bodyLarge
                        )


                        // Text(text = "Fully Assigned Item Quantity: $totalAssignedQuantity", fontSize = 20.sp, fontWeight = FontWeight.Bold)

                        // Display assigned status (fully assigned, excess, lack)
                        Text(
                            text = assignedStatus,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = alexandriaFont,
                            style = MaterialTheme.typography.bodyLarge
                        )

                    }

                Spacer(modifier = Modifier.height(10.dp))

                    // Content / Body
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(6.5f)
                            .clip(RoundedCornerShape(20.dp))
                            .background(darkGrey)
                    ) {

                        LazyColumn(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(viewModel.currentItemAndUserQuantities.ownership) { ownership ->

                                ListSplitItemNonEqual(user = ownership.owner, viewModel = viewModel)

                            }
                        }


                    }

                Spacer(modifier = Modifier.height(10.dp))

                    // Bottom
                    Column(
                        modifier = Modifier.fillMaxWidth().weight(1f)
                    ) {
                        // TODO: Save data, need to save quantities
                        Button(
                            onClick = {
                                // viewModel.currentItemAndUserQuantities = viewModel.tempItemAndUserQuantities
                                viewModel.currentItemAndUserQuantities.ownership.clear()
                                viewModel.currentItemAndUserQuantities.ownership.addAll(viewModel.tempItemAndUserQuantities.ownership)
                                filterZeroQuantityUser(viewModel = viewModel)


                                // viewModel.listItemAndUserQuantities.
                                navController.navigate(route = AppScreens.AssignSplitsScreen.name)
                                // if save, then on;y we save the changes from copied copyCurrentItemAndUserQuantities

                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                                .weight(1f),
                            enabled = isSaveEnabled,
                            shape = RoundedCornerShape(18.dp),
                            colors = ButtonColors(
                                containerColor = primaryGreen,
                                contentColor = Color.White,
                                disabledContainerColor = Color.Gray,
                                disabledContentColor = Color.White
                            )
                        ) {
                            Text(text = "Save", fontSize = 24.sp)
                        }
                    }


                // }
        }

    }

    }

}

// filter out user with 0 item quantity
fun filterZeroQuantityUser(viewModel: SharedViewModel) {
    // This mutates the list in-place → Compose will recompose correctly
    viewModel.currentItemAndUserQuantities.ownership.apply {
        val filtered = filter { ownership -> ownership.itemQuantity.value > 0 }
        clear()
        addAll(filtered)
    }

}

@Composable
fun deepCopyItemWithUsersList(itemWithUsersList: ItemWithUsersList): ItemWithUsersList {
    // TODO: need to try to instantiate new object instead of .copy()
    // Deep copy the ItemDetail object (this makes a copy of the `item` field)
    val copiedItem = itemWithUsersList.item.copy()  // Deep copy of ItemDetail

    // Deep copy the ownership list (this creates a new list with deep copies of ItemOwnership)
    val newOwnership = itemWithUsersList.ownership.map { itemOwnership ->
        itemOwnership.copy()  // Deep copy of each ItemOwnership object
    }.toMutableStateList() // Convert back to SnapshotStateList

    // Return the deep copy of the entire ItemWithUsersList object
    return itemWithUsersList.copy(
        item = copiedItem,  // Assign the deep copied item
        ownership = newOwnership  // Assign the deep copied ownership list
    )
}

@Composable
fun SplitItemTopBar(navController: NavController, viewModel: SharedViewModel){
    /*
    Top Bar
    */
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(55.dp))

            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
            }
        }

        Spacer(modifier = Modifier.width(5.dp))

        Column(
            modifier = Modifier.weight(10f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Split Item",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = surannaFont
            )
            // SearchBox()
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.Close, contentDescription = "Cancel")
            }

            Spacer(modifier = Modifier.width(40.dp))
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListSplitItemNonEqual(user: User, viewModel: SharedViewModel){
    // TODO: need to pass the real itemWithUsers, and update the quantities on the data variable so we can save it
    //var initialQuantity by remember { mutableStateOf( viewModel.tempItemAndUserQuantities.ownership.find { it.owner == user }?.itemQuantity ) }

    var initialQuantity by remember {
        mutableStateOf(
            viewModel.tempItemAndUserQuantities.ownership
                .find { it.owner == user }
                ?.itemQuantity
                ?.let { "%.2f".format(it.value) } ?: ""
        )
    }


    // var initialQuantity by remember { mutableStateOf("1.0") } // this initial quantity needs to change

    var tempItemAndUserQuantities = viewModel.tempItemAndUserQuantities

    // find the owner/user in itemWithUsers
    val index = tempItemAndUserQuantities.ownership.indexOfFirst { it.owner == user }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(7.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Spacer(modifier = Modifier.width(10.dp))

            Row(
                modifier = Modifier.fillMaxHeight().weight(1f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){

                Box(modifier = Modifier.size(50.dp).clip(CircleShape)) { //  Added Box for layering

                    var profilePic = profilePictures.firstOrNull(){ it.id == (user.profilePictureId ?: 1) }

                    if (profilePic != null) {
                        Image(
                            painter = painterResource(id = profilePic.imageRes),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                }

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = user.username,
                    modifier = Modifier.weight(1f),
                    color = primaryGreen,
                    fontSize = 20.sp,
                    fontFamily = alexandriaFont,
                    style = MaterialTheme.typography.bodyLarge
                )

            }



            Spacer(modifier = Modifier.width(25.dp))

            Row(
                modifier = Modifier.fillMaxHeight().weight(1f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){

//                Button(
//                    onClick = {
//                        val qty = initialQuantity.toFloatOrNull() ?: 0f
//                        // val qty = initialQuantity
//                        val newQty = (qty - 1f).coerceAtLeast(0f) // Prevent going below 0
//                        initialQuantity = "%.2f".format(newQty)
//                        // TODO: decrement quantity from itemWithUsers
//                        if (index != -1) {
//                            val currentOwnership = tempItemAndUserQuantities.ownership[index]
//                            // change itemQuantity of User from ownership list through state (.value) to allow correct data observation
//                            tempItemAndUserQuantities.ownership[index].itemQuantity.value = newQty
//                            // tempItemAndUserQuantities.ownership[index] = currentOwnership.copy(itemQuantity = mutableStateOf(newQty))
//                        }
//                    },
//                    modifier = Modifier.weight(1f)
//                ) {
//                    Text(text = "-")
//                }


                IconButton(onClick = {
                    val qty = initialQuantity.toFloatOrNull() ?: 0f
                    // val qty = initialQuantity
                    val newQty = (qty - 1f).coerceAtLeast(0f) // Prevent going below 0
                    initialQuantity = "%.2f".format(newQty)
                    // TODO: decrement quantity from itemWithUsers
                    if (index != -1) {
                        val currentOwnership = tempItemAndUserQuantities.ownership[index]
                        // change itemQuantity of User from ownership list through state (.value) to allow correct data observation
                        tempItemAndUserQuantities.ownership[index].itemQuantity.value = newQty
                        // tempItemAndUserQuantities.ownership[index] = currentOwnership.copy(itemQuantity = mutableStateOf(newQty))
                    }
                },
                    modifier = Modifier.weight(1f)

                    ) {
                    Icon(Icons.Default.Remove, contentDescription = "Remove", modifier = Modifier.size(30.dp))
                }

                TextField(

                    value = initialQuantity,
                    onValueChange = { input: String ->
                        // Allow only digits and at most one dot
                        if (input.isEmpty() || input.matches(Regex("""\d*\.?\d*"""))) {
                            initialQuantity = input

                            //val updatedList = tempItemAndUserQuantities.ownership.toMutableStateList() // deep copy
                            //val idx = updatedList.indexOfFirst { it.owner == user }
                            if (index != -1) {
                                //updatedList[idx] = updatedList[idx].copy(itemQuantity = input.toFloatOrNull() ?: 0f) // deep copy
                                //tempItemAndUserQuantities = tempItemAndUserQuantities.copy(ownership = updatedList) // deep copy

                                val currentOwnership = tempItemAndUserQuantities.ownership[index]
                                // change itemQuantity of User from ownership list through state (.value) to allow correct data observation
                                tempItemAndUserQuantities.ownership[index].itemQuantity.value =
                                    input.toFloatOrNull() ?: 0f // provides a fallback value (0.0f).
                                // tempItemAndUserQuantities.ownership[index] = currentOwnership.copy(itemQuantity = mutableStateOf(input.toFloatOrNull() ?: 0f)) // provides a fallback value (0.0f).
                            }

                        }
                    },
                    label = { Text(text = "Qty") },
                    modifier = Modifier.weight(2f) /*.width(100.dp)*/,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.White,
                        // focusedIndicatorColor = Color.Transparent,   // removes line
                        // unfocusedIndicatorColor = Color.Transparent, // removes line
                        // disabledIndicatorColor = Color.Transparent
                    )
                )

//                Button(
//                    onClick = {
//                        val qty = initialQuantity.toFloatOrNull() ?: 0f
//                        val newQty = (qty + 1f)
//                        initialQuantity = "%.2f".format(newQty)
//                        // TODO: increment quantity from itemWithUsers
//                        if (index != -1) {
//                            val currentOwnership = tempItemAndUserQuantities.ownership[index]
//                            // change itemQuantity of User from ownership list through state (.value) to allow correct data observation
//                            tempItemAndUserQuantities.ownership[index].itemQuantity.value = newQty
//                            //  tempItemAndUserQuantities.ownership[index] = currentOwnership.copy(itemQuantity = mutableStateOf(newQty))
//                            // tempItemAndUserQuantities.ownership[index] = ItemOwnership(user, newQty) // tried new object
//                        }
//                    },
//                    modifier = Modifier.weight(1f)
//                ) {
//                    Text(text = "+")
//                }

                IconButton(onClick = {
                    val qty = initialQuantity.toFloatOrNull() ?: 0f
                    val newQty = (qty + 1f)
                    initialQuantity = "%.2f".format(newQty)
                    // TODO: increment quantity from itemWithUsers
                    if (index != -1) {
                        val currentOwnership = tempItemAndUserQuantities.ownership[index]
                        // change itemQuantity of User from ownership list through state (.value) to allow correct data observation
                        tempItemAndUserQuantities.ownership[index].itemQuantity.value = newQty
                        //  tempItemAndUserQuantities.ownership[index] = currentOwnership.copy(itemQuantity = mutableStateOf(newQty))
                        // tempItemAndUserQuantities.ownership[index] = ItemOwnership(user, newQty) // tried new object
                    }

                },
                    modifier = Modifier.weight(1f)

                    ) {
                    Icon(Icons.Filled.Add, contentDescription = "Add", modifier = Modifier.size(30.dp) )
                }

        }


        }
    }
}