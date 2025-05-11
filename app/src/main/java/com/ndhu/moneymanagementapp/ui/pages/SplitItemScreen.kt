package com.ndhu.moneymanagementapp.ui.pages

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
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
    val assignedStatus = when {
        totalAssignedQuantity == totalItemQuantity -> "0"
        totalAssignedQuantity > totalItemQuantity -> "+${"%.2f".format(totalAssignedQuantity - totalItemQuantity)}"
        totalAssignedQuantity < totalItemQuantity -> "-${"%.2f".format(totalItemQuantity - totalAssignedQuantity)}"
        else -> ""
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


    Column (
        modifier = Modifier.fillMaxWidth().fillMaxHeight()
    ){
        // Top Bar
        Column (
            modifier = Modifier.fillMaxWidth().weight(1f)
        ) {
            SplitItemTopBar(navController = navController, viewModel = viewModel)
        }

        // Content
        Column (
            modifier = Modifier.fillMaxWidth().weight(7f)
        ) {
            // Header Text
            // TODO: Replace with real data
            Text(text = viewModel.currentItem.name, fontSize = 30.sp)
            Text(text = "You can adjust quantity of assigned items to friends.", fontSize = 15.sp)
            Text(text = "Decimal number is allowed. For example: 0.5 defines half portion, or other decimal number based on agreement.", fontSize = 15.sp)
            Text(text = "Price will be calculated based on quantity assigned.", fontSize = 15.sp)

            // TODO: make it responsive (cannot add more if exceed. cannot save if is NOT fully assigned)
            Text(text = "Total Item Quantity: $totalItemQuantity", fontSize = 20.sp, fontWeight = FontWeight.Bold)


            // Text(text = "Fully Assigned Item Quantity: $totalAssignedQuantity", fontSize = 20.sp, fontWeight = FontWeight.Bold)

            // Display assigned status (fully assigned, excess, lack)
            Text(
                text = "Fully Assigned: $assignedStatus",
                color = when {
                    totalAssignedQuantity == totalItemQuantity -> Color.Green
                    totalAssignedQuantity > totalItemQuantity -> Color.Red
                    totalAssignedQuantity < totalItemQuantity -> Color.Red
                    else -> Color.Gray
                },
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            // Body
            LazyColumn (
                modifier = Modifier.fillMaxWidth()
            ) {
                items( viewModel.currentItemAndUserQuantities.ownership ){ ownership ->

                    ListSplitItemNonEqual(user = ownership.owner, viewModel = viewModel)

                }
            }


        }

        // Bottom
        Column (
            modifier = Modifier.fillMaxWidth().weight(1f)
        ) {
            // TODO: Save data, need to save quantities
            Button(
                onClick = {
                    // viewModel.currentItemAndUserQuantities = viewModel.tempItemAndUserQuantities
                    viewModel.currentItemAndUserQuantities.ownership.clear()
                    viewModel.currentItemAndUserQuantities.ownership.addAll(viewModel.tempItemAndUserQuantities.ownership)


                    // viewModel.listItemAndUserQuantities.
                    navController.navigate(route = AppScreens.AssignSplitsScreen.name)
                    // if save, then on;y we save the changes from copied copyCurrentItemAndUserQuantities

                },
                modifier = Modifier.fillMaxWidth(),
                enabled = isSaveEnabled
            ) {
                Text(text = "Save")
            }
        }


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
            .padding(5.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(text = user.username, modifier = Modifier.weight(2f))


            Button(onClick = {
                val qty = initialQuantity.toFloatOrNull() ?: 0f
                // val qty = initialQuantity
                val newQty = (qty - 1f ).coerceAtLeast(0f) // Prevent going below 0
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
                Text(text = "-")
            }

            TextField(

                value = initialQuantity,
                onValueChange = {input: String ->
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
                            tempItemAndUserQuantities.ownership[index].itemQuantity.value = input.toFloatOrNull() ?: 0f // provides a fallback value (0.0f).
                            // tempItemAndUserQuantities.ownership[index] = currentOwnership.copy(itemQuantity = mutableStateOf(input.toFloatOrNull() ?: 0f)) // provides a fallback value (0.0f).
                        }

                    }
                },
                label = { Text("Qty") },
                modifier = Modifier.weight(1f) /*.width(100.dp)*/,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Button(onClick = {
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
                Text(text = "+")
            }
        }
    }
}