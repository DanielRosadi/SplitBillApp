package com.ndhu.moneymanagementapp.ui.pages


import android.content.Context
import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ndhu.moneymanagementapp.components.CustomFormDialog
import com.ndhu.moneymanagementapp.components.FormField
import com.ndhu.moneymanagementapp.components.SearchBox
import com.ndhu.moneymanagementapp.models.Store
import com.ndhu.moneymanagementapp.enums.AppScreens
import com.ndhu.moneymanagementapp.controllers.ItemController
import com.ndhu.moneymanagementapp.models.Item
import com.ndhu.moneymanagementapp.payloads.ItemDetail
import com.ndhu.moneymanagementapp.sharedviewmodels.SharedViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

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
fun ListOfItemPage(
    context: Context,
    navController: NavController,
    storeId: String?,
    viewModel: SharedViewModel
) {

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
            itemController.getItemListBasedStoreId(it).map { dbItem ->
                UIItem(
                    item = dbItem,
                    quantity = mutableStateOf(0)
                ) // initialise UI Item with quantity of 0
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
        viewModel.setItemList(storeId?.let {
            itemController.getItemListBasedStoreId(it).toMutableList()
        })

    }

    // search for specific store object using passed storeId from previous screen, from global viewModel
    val store = storeId?.let { viewModel.getStoreById(it) }
    if (store != null) {
        viewModel.selectedStore = store
    }

    ComposeListItem(context, items, uiItems, store, navController, itemController, viewModel)

}

@Composable
private fun ComposeListItem(
    context: Context,
    listItem: SnapshotStateList<Item>,
    uiItems: SnapshotStateList<UIItem>,
    store: Store?,
    navController: NavController,
    itemController: ItemController,
    viewModel: SharedViewModel
) {

    var showDialogAddItem by remember { mutableStateOf(false) }

    val searchQuery = remember { mutableStateOf("") }

    val filteredItems = filterItems(uiItems, searchQuery.value) //  no remember

    val isDoneEnabled by remember {
        derivedStateOf {
            uiItems.any { it.quantity.value > 0 }
        }
    }


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
                    .padding(10.dp),
                //.padding(paddingValues)
                //verticalArrangement = Arrangement.Top,
                //horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Content Top Bar
                // TODO: search func
                ItemContentTopBar(navController = navController,
                    store = store,
                    searchBox = { SearchBox(searchQuery = searchQuery) }
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(6f)
                        .clip(RoundedCornerShape(20.dp))
                        .background(darkGreyContainer)

                ) {

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .weight(1f),
                        contentPadding = PaddingValues(5.dp)
                    ) {

                        items(filteredItems) { uiItem ->
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
                        .padding(2.dp),
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
                            Text(
                                text = "Add item",
                                fontSize = 24.sp
                            )
                        }

                        if (showDialogAddItem) {
                            if (store != null) {
                                AddItemDialog(
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
                                .filter { uiItem -> uiItem.quantity.value > 0 } // we don't send items data that are not in the transaction
                                .map { uiItem ->
                                    ItemDetail(
                                        id = uiItem.item.id,
                                        name = uiItem.item.name,
                                        price = uiItem.item.price,
                                        qty = uiItem.quantity.value.toFloat()
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
                        enabled = isDoneEnabled, // <-- CHANGES HERE
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonColors(
                            containerColor = primaryGreen,
                            contentColor = Color.White,
                            disabledContainerColor = Color.Gray,
                            disabledContentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "Done",
                            fontSize = 24.sp
                        )
                    }


                }
                Spacer(modifier = Modifier.height(10.dp))

            }
        }
    }
}

// filter function for search box
fun filterItems(itemList: SnapshotStateList<UIItem>, query: String): SnapshotStateList<UIItem> {
    val result = if (query.isBlank()) {
        itemList
    } else {
        itemList.filter {
            it.item.name.contains(query, ignoreCase = true) ||
                    it.item.price.toString().contains(query, ignoreCase = true)
        }.toMutableStateList() // Convert result to SnapshotStateList
    }

    // println("Filtered ${result.size} items for query: '$query'") // <-- DEBUG
    return result
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
    var itemQuantity by remember { mutableStateOf(uiItem.quantity.value) }

    // inner function only for updating item quantity
    fun updateItemQuantity(newItemQuantity: Int) {
        itemQuantity = newItemQuantity
        uiItem.quantity.value = newItemQuantity
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .padding(7.dp)
            .clickable { showDialogEditItem = true },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            horizontalArrangement = Arrangement.Start
        ) {

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
            ) {
                Text(
                    text = uiItem.item.name,
                    color = primaryGreen,
                    fontSize = 20.sp,
                    fontFamily = alexandriaFont,
                    //modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )

                // to remove floating point if its x.0
                val price = uiItem.item.price
                val priceText = if (price.rem(1.0) == 0.0) {
                    price.toInt().toString()
                } else {
                    price.toString()
                }


                Text(
                    text = "NTD $priceText",
                    fontSize = 17.sp,
                    // fontWeight = FontWeight.W600,
                    fontFamily = surannaFont,
                    //modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            // Spacer(modifier = Modifier.width(20.dp))


            Spacer(modifier = Modifier.width(25.dp))

            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(onClick = {
                    if (itemQuantity > 0) {
                        updateItemQuantity(itemQuantity - 1)
                    }
                }) {
                    Icon(
                        Icons.Default.Remove,
                        contentDescription = "Remove",
                        modifier = Modifier.size(30.dp)
                    )
                }

                /*
                Button(onClick = {
                    if (itemQuantity > 0) {
                        updateItemQuantity(itemQuantity - 1)
                    }
                }) { Text(text = "-") }
                */
                Spacer(modifier = Modifier.width(20.dp))

                Text(
                    text = itemQuantity.toString(),
                    fontSize = 22.sp,
                    fontFamily = surannaFont,
                    //modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.width(20.dp))

                // Button(onClick = { updateItemQuantity(itemQuantity + 1) }) { Text(text = "+") }

                IconButton(onClick = { updateItemQuantity(itemQuantity + 1) }) {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = "Add",
                        modifier = Modifier.size(30.dp)
                    )
                }

            }

        }


        if (showDialogEditItem) {
            if (store != null) {
                EditItemDialog(
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
fun AddItemDialog(
    itemController: ItemController,
    onDismiss: () -> Unit,
    storeId: String,
    itemList: SnapshotStateList<Item>,
    viewModel: SharedViewModel,
    uiItems: SnapshotStateList<UIItem>
) {
    // Dialog visibility state
    val showDialog = remember { mutableStateOf(true) }

    // For showing toast
    val context = LocalContext.current

    // For running suspend functions from UI
    val scope = rememberCoroutineScope()

    // Form input state
    var itemName by remember { mutableStateOf("") }
    var itemPrice by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Trim and parse input for validation
    val trimmedName = itemName.trim()
    val parsedPrice = itemPrice.toFloatOrNull() ?: 0f

    // Check if item with same name already exists (case-insensitive)
    val isDuplicate = itemList.any {
        it.name.equals(trimmedName, ignoreCase = true)
    }

    // Determine whether the form is valid
    val isFormValid = trimmedName.isNotEmpty() && parsedPrice > 0 && !isDuplicate

    // Show custom input dialog
    CustomFormDialog(
        showDialog = showDialog,
        title = "Add Item",
        icon = Icons.Default.Add,

        // Input fields
        fields = listOf(
            FormField(
                label = "Item Name",
                value = itemName,
                onValueChange = {
                    itemName = it
                    errorMessage = null // Clear error on edit
                },
                error = when {
                    trimmedName.isEmpty() -> "Item name cannot be empty."
                    isDuplicate -> "Item with this name already exists."
                    else -> null
                }
            ),
            FormField(
                label = "Item Price",
                value = itemPrice,
                onValueChange = {
                    itemPrice = it
                    errorMessage = null
                },
                error = if (parsedPrice <= 0f) "Price must be greater than 0." else null
            )
        ),

        confirmButtonText = "Save",
        cancelButtonText = "Cancel",
        confirmButtonEnabled = isFormValid,

        // Cancel action
        onCancel = {
            showDialog.value = false
            onDismiss()
        },

        // Save action
        onConfirm = {
            scope.launch {
                // Add item to DB
                itemController.addItem(storeId, trimmedName, parsedPrice)

                // Refresh local and viewModel data
                val updatedItems = itemController.getItemListBasedStoreId(storeId)
                itemList.clear()
                itemList.addAll(updatedItems)
                viewModel.setItemList(updatedItems.toMutableList())

                // Sync UI list (with quantities)
                syncUiItemsFromDb(updatedItems, uiItems)

                // Show toast
                Toast.makeText(context, "Item added successfully", Toast.LENGTH_SHORT).show()

                // Close dialog
                showDialog.value = false
                onDismiss()
            }
        }
    )
}

@Composable
fun EditItemDialog(
    itemObject: Item,
    storeId: String,
    itemController: ItemController,
    onDismiss: () -> Unit,
    itemList: SnapshotStateList<Item>,
    viewModel: SharedViewModel,
    uiItems: SnapshotStateList<UIItem>
) {
    // Dialog visibility state
    val showDialog = remember { mutableStateOf(true) }

    // For toast messages
    val context = LocalContext.current

    // Coroutine scope for background work
    val scope = rememberCoroutineScope()

    // Pre-filled form state
    var itemName by remember { mutableStateOf(itemObject.name) }
    var itemPrice by remember { mutableStateOf(itemObject.price.toString()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Trimmed name and parsed price
    val trimmedName = itemName.trim()
    val parsedPrice = itemPrice.toFloatOrNull() ?: 0f

    // Check for duplicate name (excluding the current item itself)
    val isDuplicate = itemList.any {
        it.name.equals(trimmedName, ignoreCase = true) && it.id != itemObject.id
    }

    // Form validity check
    val isFormValid = trimmedName.isNotEmpty() && parsedPrice > 0 && !isDuplicate

    // Show reusable form dialog
    CustomFormDialog(
        showDialog = showDialog,
        title = "Edit Item",
        icon = Icons.Default.Edit,

        // Input fields
        fields = listOf(
            FormField(
                label = "Item Name",
                value = itemName,
                onValueChange = {
                    itemName = it
                    errorMessage = null
                },
                error = when {
                    trimmedName.isEmpty() -> "Item name cannot be empty."
                    isDuplicate -> "Item with this name already exists."
                    else -> null
                }
            ),
            FormField(
                label = "Item Price",
                value = itemPrice,
                onValueChange = {
                    itemPrice = it
                    errorMessage = null
                },
                error = if (parsedPrice <= 0f) "Price must be greater than 0." else null
            )
        ),

        confirmButtonText = "Save",
        cancelButtonText = "Cancel",
        confirmButtonEnabled = isFormValid,

        // Cancel button closes the dialog
        onCancel = {
            showDialog.value = false
            onDismiss()
        },

        // Confirm and save changes
        onConfirm = {
            scope.launch {
                // Update item in the database
                itemController.updateItem(
                    itemObject.id,
                    Item(
                        id = itemObject.id,
                        name = trimmedName,
                        price = parsedPrice,
                        storeId = storeId
                    )
                )

                // Refresh item list and view model
                val updatedItems = itemController.getItemListBasedStoreId(storeId)
                itemList.clear()
                itemList.addAll(updatedItems)
                viewModel.setItemList(updatedItems.toMutableList())

                // Sync UI item list (with quantities)
                syncUiItemsFromDb(updatedItems, uiItems)

                // Show success toast
                Toast.makeText(context, "Item updated successfully", Toast.LENGTH_SHORT).show()

                // Close dialog
                showDialog.value = false
                onDismiss()
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
            quantity = existing?.quantity ?: mutableStateOf(0)  // preserve qty if exists
        )
    }

    currentUiItems.clear()
    currentUiItems.addAll(newUiItems)
}