package com.ndhu.moneymanagementapp.ui.pages


import android.content.Context
import android.util.Log
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ndhu.moneymanagementapp.models.Store
import com.ndhu.moneymanagementapp.enums.AppScreens
import com.ndhu.moneymanagementapp.controllers.ItemController
import com.ndhu.moneymanagementapp.models.Item
import com.ndhu.moneymanagementapp.payloads.ItemDetail
import com.ndhu.moneymanagementapp.sharedviewmodels.SharedViewModel
import kotlinx.coroutines.runBlocking
import java.util.UUID

/*
To pay attention when performing change in item data (database or UI):
- loading data when screen is initially load (ListOfItemPage func) (using uiItems now, and listItems still there)
- listing data (ComposeListItem func via items() ) (using uiItems now)

- item quantity changes should also reflect in UI items (ListDataItem func) (using uiItems now)

- adding, editing item data so that the item data is really changed (PopUpAdd, PopUpEdit func) (using uiItems now, but listItems still there)
- deleting item data so that the item data is really changed (NEED other attention since delete item done via dashboard)

- reflect changes automatically when add/edit item are performed (PopUpAdd, PopUpEdit func) (using uiItems now, but listItems still there)
- reflect changes automatically when delete item are performed (NEED other attention since delete item done via dashboard)

Also, if you fetch data and it comes as duplicates, consider:
- uses list.clear()
- use LaunchedEffect() instead of runBlocking()
It might be due to the nature of Composables recompose often (on state changes, navigation, LiveData observers, etc),
this runBlocking block is not protected and will keep running.

 */

@Composable
fun ListOfItemPage(context : Context, navController: NavController, storeId: String?, viewModel: SharedViewModel){

    val itemController = ItemController()

    val items = remember { mutableStateListOf<Item>() }
    // val onDoneClicked: (MutableList<ItemDetail>) -> Unit

    val uiItems = remember { mutableStateListOf<UIItem>() }

    // TODO: may try LaunchedEffect if runBlocking makes the app loading
    runBlocking {
        Log.d("LaunchTag", "DEBUG. Accessing Item List.")  // Prints in Logcat

        // clear items list and add data from database into the stores list (this helps to avoid multiple/duplicate data being taken/shown. use clear() for similar operation)
        items.clear()

        // load items based on store id
        storeId?.let { itemController.getItemListBasedStoreId(it) }?.let { items.addAll(it) }

        // for tracking Item data in the UI (with quantity)

        storeId?.let {
            itemController.getItemListBasedStoreId(it).map { dbItem->
                UIItem(item = dbItem, quantity = 0) // initialise UI Item with quantity of 0
            }
        }?.let {
            // clear items list and add data from database into the stores list (this helps to avoid multiple/duplicate data being taken/shown. use clear() for similar operation)
            uiItems.clear()
            // add UI Item to the list uiItems
            uiItems.addAll(
                it
            )
        }

        // viewModel.setItemList(storeId?.let { itemController.getItemListBasedStoreId(it) })
        viewModel.setItemList(storeId?.let { itemController.getItemListBasedStoreId(it).toMutableList() })

    }

    // search for specific store object using passed storeId from previous screen, from global viewModel
    val store = storeId?.let { viewModel.getStoreById(it) }
    viewModel.setSelectedStore(store)

    ComposeListItem(context, items, uiItems, store, navController, itemController, viewModel)

}

@Composable
private fun ComposeListItem(context : Context, listItem: SnapshotStateList<Item>, uiItems:SnapshotStateList<UIItem>, store: Store?, navController: NavController, itemController: ItemController, viewModel: SharedViewModel) {

    var showDialogAddItem by remember { mutableStateOf(false) }

    //Scaffold (
//        topBar = {
//            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
//                IconButton(onClick = { navController.popBackStack() }) {
//                    Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
//                }
//
//                Column() {
//                    Card (modifier = Modifier.fillMaxWidth().clickable { }) {
//                        Text(
//                            text = store?.name ?: "Unknown Store",
//                            fontSize = 25.sp,
//                            fontWeight = FontWeight.Bold
//                        )
//                        Text(
//                            text = store?.address ?: "Unknown Store",
//                            fontSize = 15.sp,
//                            fontWeight = FontWeight.Bold)
//                    }
//            }
//            }
//        }
    // )
    //{paddingValues->

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

                    // Content Top Bar
                    // TODO: search func
                    ItemContentTopBar(navController = navController, store = store)

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(6f)
                            .clip(RoundedCornerShape(20.dp))
                            .background(darkGrey)

                    ){

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .weight(1f),
                            contentPadding = PaddingValues(5.dp)
                        ) {

                            items(uiItems) { uiItem ->
                                ListDataItem(
                                    uiItem,
                                    store,
                                    itemController,
                                    listItem,
                                    viewModel,
                                    uiItems
                                )

                            }

                        }
                }

                    Spacer(modifier = Modifier.height(20.dp))


                    Column(
                        // modifier = Modifier.padding(20.dp)
                        modifier = Modifier
                            .fillMaxWidth()
                            //.fillMaxHeight()
                            .weight(2.5f)
                            .padding(2.dp)
                        ,
                        horizontalAlignment = Alignment.CenterHorizontally

                    ) {

                        Row {
                            // we no longer need delete item, keep it for possible future change
                            /*
                                Button(
                                    onClick = {}, modifier = Modifier
                                        .fillMaxWidth()
                                        .height(60.dp)
                                        .weight(1f)
                                ) {
                                    Text(text = "Delete (Temp)")
                                }
                                Spacer(modifier = Modifier.width(20.dp))
                                 */

                            Button(
                                onClick = { showDialogAddItem = true },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(60.dp)
                                    .weight(1f),
                                shape = RoundedCornerShape(18.dp),
                                colors = ButtonColors(
                                    containerColor = primaryGreen,
                                    contentColor = Color.White,
                                    disabledContainerColor = Color.Gray,
                                    disabledContentColor = Color.White
                                )
                            ) {
                                Text(text = "Add item",
                                    fontSize = 24.sp)
                            }

                            if (showDialogAddItem) {
                                if (store != null) {
                                    PopUpAddItem(
                                        itemController = itemController,
                                        onDismiss = { showDialogAddItem = false },
                                        storeId = store.id,
                                        itemList = listItem,
                                        viewModel = viewModel,
                                        uiItems = uiItems
                                    )
                                }
                            }


                        }

                        Spacer(modifier = Modifier.height(15.dp))

                        Button(
                            // TODO: need to pass item list
                            onClick = {
                                navController.navigate(route = AppScreens.SelectFriendsScreen.name) /* need to also perform database update */
                                val result = uiItems
                                    .filter { uiItem -> uiItem.quantity > 0 } // we don't send items data that are not in the transaction
                                    .map { uiItem ->
                                        ItemDetail(
                                            id = uiItem.item.id,
                                            name = uiItem.item.name,
                                            price = uiItem.item.price,
                                            qty = uiItem.quantity
                                        )
                                    }

                                // TODO: what to do with result? e.g onDoneClicked(result)
                                // save the items for Assign_Split page
                                viewModel.itemDetailList = result.toMutableList()

                                // TODO: should notify user if there is item with 0 quantity (that is, the item is unassign)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp),
                            shape = RoundedCornerShape(18.dp),
                            colors = ButtonColors(
                                containerColor = primaryGreen,
                                contentColor = Color.White,
                                disabledContainerColor = Color.Gray,
                                disabledContentColor = Color.White
                            )
                        ) {
                            Text(text = "Done",
                                fontSize = 24.sp)
                        }


                    }
                    Spacer(modifier = Modifier.height(10.dp))

                }
    }
}

//}

}

@Composable
private fun ListDataItem(
    // item: Item,
    uiItem: UIItem,
    store: Store?,
    itemController: ItemController,
    listItem: SnapshotStateList<Item>,
    viewModel: SharedViewModel,
    uiItems: SnapshotStateList<UIItem>
) {
    var showDialogEditItem by remember { mutableStateOf(false) }
    // var itemQuantity by remember { mutableStateOf(0) }
    var itemQuantity by remember { mutableStateOf(uiItem.quantity) }

    // inner function only for updating item quantity
    fun updateItemQuantity(newItemQuantity: Int){
        itemQuantity = newItemQuantity
        uiItem.quantity = newItemQuantity
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(85.dp)
            .padding(7.dp)
            .clickable { showDialogEditItem = true },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(text = uiItem.item.name)
            Spacer(modifier = Modifier.width(20.dp))

            Text(text = uiItem.item.price.toString())
            Spacer(modifier = Modifier.width(25.dp))

            Button(onClick = {
                if (itemQuantity > 0) {
                    updateItemQuantity(itemQuantity - 1)
                }
            }) { Text(text = "-") }
            Spacer(modifier = Modifier.width(30.dp))

            Text(text = itemQuantity.toString())
            Spacer(modifier = Modifier.width(20.dp))

            Button(onClick = { updateItemQuantity(itemQuantity + 1) }) { Text(text = "+") }
        }


        if (showDialogEditItem) {
            if (store != null) {
                PopUpEditItem(
                    itemObject = uiItem.item,
                    storeId = store.id,
                    itemController = itemController,
                    onDismiss = { showDialogEditItem = false },
                    itemList = listItem,
                    viewModel = viewModel,
                    uiItems = uiItems
                )
            }
        }

    }
}

@Composable
fun PopUpAddItem(itemController: ItemController, onDismiss: () -> Unit, storeId: String, itemList: SnapshotStateList<Item>, viewModel: SharedViewModel, uiItems: SnapshotStateList<UIItem>) {
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

                        // sync data addition from database to observed UI Items
                        syncUiItemsFromDb(itemController.getItemListBasedStoreId(storeId), uiItems)

                    }

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


@Composable
fun PopUpEditItem(itemObject: Item, storeId: String, itemController: ItemController, onDismiss: () -> Unit, itemList: SnapshotStateList<Item>, viewModel: SharedViewModel, uiItems: SnapshotStateList<UIItem>) {
    var itemName by remember { mutableStateOf("") }
    var itemPrice by remember { mutableStateOf(0f) }
    // var itemDescription by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Edit Item") },
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

                    runBlocking {
                        itemController.updateItem(itemObject.id,
                            Item(
                                id = UUID.randomUUID().toString(),
                                name = itemName,
                                price = itemPrice,
                                storeId = storeId
                            )
                        )
                    }

                    runBlocking {
                        itemList.clear()

                        // load items based on store id
                        storeId.let { itemController.getItemListBasedStoreId(it) }.let { itemList.addAll(it) }

                        // viewModel.setItemList(storeId?.let { itemController.getItemListBasedStoreId(it) })
                        viewModel.setItemList(storeId.let { itemController.getItemListBasedStoreId(it).toMutableList() })

                        // sync data addition from database to observed UI Items
                        syncUiItemsFromDb(itemController.getItemListBasedStoreId(storeId), uiItems)

                    }

                    // println("User edited: ${storeObject.id} $storeName $storeAddress $storeDescription") // Handle input

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

// whenever we add/edit item data, the current observed UI items need to also sync with the database changes
// we do NOT implement delete changes to this sync yet, due to agreed implementation of delete item to be via dashboard
fun syncUiItemsFromDb(
    newDbItems: List<Item>,
    currentUiItems: SnapshotStateList<UIItem>
) {
    val newUiItems = newDbItems.map { dbItem ->
        val existing = currentUiItems.find { it.item.id == dbItem.id }
        UIItem(
            item = dbItem,
            quantity = existing?.quantity ?: 0 // preserve qty if exists
        )
    }

    currentUiItems.clear()
    currentUiItems.addAll(newUiItems)
}





