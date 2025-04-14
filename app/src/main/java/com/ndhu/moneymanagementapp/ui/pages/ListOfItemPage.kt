package com.ndhu.moneymanagementapp.ui.pages


import android.content.Context
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ndhu.moneymanagementapp.models.Store
import com.ndhu.moneymanagementapp.enums.AppScreens
import com.ndhu.moneymanagementapp.components.SearchBox
import com.ndhu.moneymanagementapp.controllers.ItemController
import com.ndhu.moneymanagementapp.models.Item
import com.ndhu.moneymanagementapp.sharedviewmodels.SharedViewModel
import kotlinx.coroutines.runBlocking

@Composable
fun ListOfItemPage(context : Context, navController: NavController, storeId: String?, viewModel: SharedViewModel){
    // val listItem = listOf("Item 1", "Item 2", "Item 3")

    val itemController = ItemController()

    val items = remember { mutableStateListOf<Item>() }

    // items = itemController.getItemList(storeId)

    // TODO: may try LaunchedEffect if runBlocking makes the app loading
    runBlocking {
        Log.d("LaunchTag", "DEBUG. Accessing Item List.")  // Prints in Logcat

        // clear stores list and add data from database into the stores list
        items.clear()
        storeId?.let { itemController.getItemListBasedStoreId(it) }?.let { items.addAll(it) }
        // viewModel.setItemList(storeId?.let { itemController.getItemListBasedStoreId(it) })
        viewModel.setItemList(storeId?.let { itemController.getItemListBasedStoreId(it).toMutableList() })

    // = storeController.getListOfStores().toMutableList()

    }

    // search for specific store object using passed storeId from previous screen, from global viewModel
    val store = storeId?.let { viewModel.getStoreById(it) }
    viewModel.setSelectedStore(store)

    ComposeListItem(context, items, store, navController, itemController, viewModel)

}

@Composable
private fun ComposeListItem(context : Context, listItem: SnapshotStateList<Item>, store: Store?, navController: NavController, itemController: ItemController, viewModel: SharedViewModel) {

    var showDialogAddItem by remember { mutableStateOf(false) }

    Scaffold (
        topBar = {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
                }

                Column() {
                    Card (modifier = Modifier.fillMaxWidth().clickable { }) {
                        Text(
                            text = store?.name ?: "Unknown Store",
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = store?.address ?: "Unknown Store",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold)
                    }
            }
            }
        }
    )
    {paddingValues->

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight().padding(paddingValues),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        SearchBox()

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .weight(1f),
            contentPadding = PaddingValues(5.dp)
        ) {

            items(listItem) { item ->
                var itemQuantity by remember { mutableStateOf(0) }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .padding(7.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(text = item.name)
                        Spacer(modifier = Modifier.width(30.dp))

                        Text(text = item.price.toString())
                        Spacer(modifier = Modifier.width(25.dp))

                        Button(onClick = { itemQuantity-- }) { Text(text = "-") }
                        Spacer(modifier = Modifier.width(30.dp))

                        Text(text = itemQuantity.toString())
                        Spacer(modifier = Modifier.width(30.dp))

                        Button(onClick = { itemQuantity++ }) { Text(text = "+") }
                    }


                }

            }

        }

        Spacer(modifier = Modifier.height(20.dp))


        Column (modifier = Modifier.padding(20.dp)) {

            Button(
                // TODO: need to pass item list
                onClick = { navController.navigate(route = AppScreens.SelectFriendsScreen.name) /* need to also perform database update*/ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Text(text = "Done")
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row {
                // TODO: delete item
                Button(
                    onClick = {}, modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .weight(1f)
                ) {
                    Text(text = "Delete")
                }

                Spacer(modifier = Modifier.width(20.dp))

                // TODO: add item
                Button(
                    onClick = { showDialogAddItem = true}, modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .weight(1f)
                ) {
                    Text(text = "Add item")
                }

                if (showDialogAddItem){
                    if (store != null) {
                        PopUpAddItem(itemController = itemController,
                            onDismiss = {showDialogAddItem = false},
                            storeId = store.id,
                            itemList = listItem,
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))

    }

}

}

@Composable
fun PopUpAddItem(itemController: ItemController, onDismiss: () -> Unit, storeId: String, itemList: SnapshotStateList<Item>, viewModel: SharedViewModel) {
    var itemName by remember { mutableStateOf("") }
    var itemPrice by remember { mutableStateOf(0f) }

    // TODO: omitted, it does not fit current Item model
    // var itemDescription by remember { mutableStateOf("") }


    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Add Item") },
        text = {
            Column {
                TextField(
                    value = itemName,
                    onValueChange = { itemName = it },
                    label = { Text("Item Name") }
                )
                TextField(
                    // TextField works with String values, but your itemPrice is a MutableState<Float>, so mismatches
                    value = itemPrice.toString(), // Converts the Float to String for displaying.
                    onValueChange = { itemPrice = it.toFloatOrNull() ?: 0f }, // Safely parses the string input to Float, avoiding crashes.
                    label = { Text("Item Price") }
                )

                /*
                TODO: add/remove according to next discussion
                TextField(
                    value = itemDescription,
                    onValueChange = { itemDescription = it },
                    label = { Text("Item Description") }
                )
                */
            }
        },
        confirmButton = {
            Button(
                onClick = {

                    // add item record to the database
                    runBlocking {
                        itemController.addItem(
                            storeId,
                            itemName,
                            itemPrice
                            // itemDescription
                        )
                    }
                    // add item to the LazyColumn data
                    // this approach can only generate different id, which different from db
                    // itemList.add(Item(itemName, itemPrice))
                    runBlocking {
                        itemList.clear()
                        itemList.addAll(itemController.getItemListBasedStoreId(storeId))

                        // add item to SharedViewModel
                        viewModel.setItemList(itemController.getItemListBasedStoreId(storeId).toMutableList())
                    }

                    // TODO: to remove if above works as intended
                    // add item to SharedViewModel
                    // viewModel.setItemList(itemController.getItemListBasedStoreId(storeId).toMutableList())
                    // viewModel.addItem(item)
                    // viewModel.itemList?.add(Store(storeName, itemPrice, itemDescription))

                    // println("User entered: $itemName $itemPrice") // Handle input

                    onDismiss()
                }
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


