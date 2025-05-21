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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ndhu.moneymanagementapp.components.CustomFormDialog
import com.ndhu.moneymanagementapp.components.FormField
import com.ndhu.moneymanagementapp.components.SearchBox
import com.ndhu.moneymanagementapp.controllers.StoreController
import com.ndhu.moneymanagementapp.models.Store
import com.ndhu.moneymanagementapp.enums.AppScreens
import com.ndhu.moneymanagementapp.sharedviewmodels.SharedViewModel
import kotlinx.coroutines.*

@Composable
fun ListStorePage(context: Context, navController: NavController, viewModel: SharedViewModel) {
//    // Get the actual window size (container size)
//    val windowInfo = LocalWindowInfo.current
//    val screenWidthPx = windowInfo?.containerSize?.width ?: 0
//    val screenHeightPx = windowInfo?.containerSize?.height ?: 0
//
//    // Convert pixels to Dp
//    val screenWidth = with(LocalDensity.current) { screenWidthPx.toDp() }
//    val screenHeight = with(LocalDensity.current) { screenHeightPx.toDp() }

    GetStoreListFromDatabase(context, navController, viewModel)
}

@Composable
fun GetStoreListFromDatabase(
    context: Context,
    navController: NavController,
    viewModel: SharedViewModel
) {

    val storeController = StoreController() // Initialize object

    val stores = remember { mutableStateListOf<Store>() }

    // TODO: doesnt update when adding store?
    // TODO: may try LaunchedEffect if runBlocking makes the app loading
    runBlocking {
        Log.d("LaunchTag", "DEBUG. Accessing Store List.")  // Prints in Logcat

        // clear stores list and add data from database into the stores list
        stores.clear()
        stores.addAll(storeController.getListOfStores())
        viewModel.storeList = storeController.getListOfStores().toMutableList()

    }

    // perform UI composition
    ComposeListStore(storeController, stores, navController, viewModel)
}

@Composable
fun ComposeListStore(
    storeController: StoreController,
    data: SnapshotStateList<Store>,
    navController: NavController,
    viewModel: SharedViewModel
) {
    var showDialogAddStore by remember { mutableStateOf(false) }

    var searchQuery = remember { mutableStateOf("") }

    // Use extracted filter function
//    val filteredStores = remember(searchQuery, data) {
//        filterStores(data, searchQuery.value)
//    }

//    val filteredStores by remember {
//        derivedStateOf {
//            filterStores(data, searchQuery.value) //  Reactive search
//        }
//    }

    val filteredStores = filterStores(data, searchQuery.value) //  no remember


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
            )
            {

                // Content Top Bar
                // TODO: search func
                ContentTopBar(navController = navController,
                    topBarText = "List of Store",
                    searchBox = { SearchBox(searchQuery = searchQuery) }
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(7f)
                        .clip(RoundedCornerShape(20.dp))
                        .background(darkGreyContainer)

                ) {

//                    LaunchedEffect(searchQuery.value) {
//                        println("Search query updated to: ${searchQuery.value}")
//                    }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentPadding = PaddingValues(5.dp)
                    ) {

                        // items(data) { item ->
                        ///items(data) { item ->
                        items(filteredStores) { item ->
                            ListDataStore(storeController, item, navController, data, viewModel)
                        }

                    }

                }

                Spacer(modifier = Modifier.height(15.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        //.fillMaxHeight()
                        .weight(1f)
                        .padding(2.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // we no longer need delete item, keep it for possible future change
                    /*
                Button(onClick = {navController.navigate(route = AppScreens.DeleteStoresScreen.name)}, modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth()
                    .weight(1f)) {
                    Text(text = "Delete (Temp)")
                }
                 */

                    Button(
                        onClick = { showDialogAddStore = true },
                        modifier = Modifier
                            //.height(60.dp)
                            .fillMaxWidth()
                            .fillMaxHeight()

                        //.weight(1f)
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
                        Text(
                            text = "Add Store",
                            fontSize = 24.sp,
                            //fontFamily = surannaFont,
                            textAlign = TextAlign.Center,
                            //modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)

                        )


                    }

                    if (showDialogAddStore) {
                        AddStoreDialog(
                            storeController = storeController,
                            onDismiss = { showDialogAddStore = false },
                            data,
                            viewModel
                        )
                    }

                }
            }
        }
    }
}

// filter function for search box
fun filterStores(storeList: SnapshotStateList<Store>, query: String): SnapshotStateList<Store> {
    val result = if (query.isBlank()) {
        storeList
    } else {
        storeList.filter {
            it.name.contains(query, ignoreCase = true) ||
                    it.address.contains(query, ignoreCase = true)
        }.toMutableStateList() // Convert result to SnapshotStateList
    }

    // println("Filtered ${result.size} items for query: '$query'") // <-- DEBUG
    return result
}

@Composable
fun ListDataStore(
    storeController: StoreController,
    storeObject: Store,
    navController: NavController,
    data: SnapshotStateList<Store>,
    viewModel: SharedViewModel
) {
    var showDialogEditStore by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .padding(5.dp)
            //.background(Color.White)
            // pass respective storeId to the screen
            .clickable {
                navController.navigate(route = AppScreens.ListItemsScreen.name + "/" + storeObject.id)
                viewModel.selectedStore = storeObject
            },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {

        // Spacer(modifier = Modifier.width(10.dp))

        // pressable text
        Column(
            modifier = Modifier
                .wrapContentSize()
                .padding(10.dp)
                // pass respective storeId to the screen
                .clickable { showDialogEditStore = true }
                .background(Color.White),
            // elevation = CardDefaults.cardElevation(4.dp),
            // colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
            //colors = CardDefaults.cardColors(Color.White),
            // shape = RoundedCornerShape(10.dp),
        ) {

            Text(
                text = storeObject.name,
                // TODO: add shadow text via other approach if this does not work
                /*
                modifier = Modifier
                    .shadow(4.dp, shape = RectangleShape, clip = false)
                    .graphicsLayer {
                        // Adjusting shadow to only show below the text by modifying offset
                        translationY = 2.dp.toPx() // Moves the shadow downward
                    },

                 */
                color = primaryGreen,
                fontSize = 20.sp,
                fontFamily = alexandriaFont,
                //modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = storeObject.address,
                fontSize = 15.sp,
                fontFamily = surannaFont,
                //modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyLarge
            )
            // TODO: Need confirmation for store details to display
            /*
            Text(
                text = storeObject.description,
                fontSize = 20.sp,
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyLarge
            )
         */
        }
        if (showDialogEditStore) {
            EditStoreDialog(
                storeObject = storeObject, storeController = storeController,
                onDismiss = { showDialogEditStore = false }, storeList = data, viewModel = viewModel
            )
        }
    }
}

@Composable
fun AddStoreDialog(
    storeController: StoreController,
    onDismiss: () -> Unit,
    storeList: SnapshotStateList<Store>,
    viewModel: SharedViewModel
) {
    val showDialog = remember { mutableStateOf(true) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Form state
    var storeName by remember { mutableStateOf("") }
    var storeAddress by remember { mutableStateOf("") }
    var storeDescription by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Trimmed input for validation
    val trimmedName = storeName.trim()

    // Validation checks
    val isNameDuplicate = storeList.any { it.name.equals(trimmedName, ignoreCase = true) }
    val isFormValid = trimmedName.isNotEmpty() && !isNameDuplicate

    CustomFormDialog(
        showDialog = showDialog,
        title = "Add Store",
        icon = Icons.Default.Store,
        fields = listOf(
            FormField(
                label = "Store Name",
                value = storeName,
                onValueChange = {
                    storeName = it
                    errorMessage = null // Clear error on change
                },
                // Inline error shown under text field
                error = when {
                    trimmedName.isEmpty() -> "Store Name cannot be empty."
                    isNameDuplicate       -> "Store Name already exists."
                    else                  -> null
                }
            ),
            FormField(
                label = "Store Address",
                value = storeAddress,
                onValueChange = { storeAddress = it }
            ),
            FormField(
                label = "Store Description",
                value = storeDescription,
                onValueChange = { storeDescription = it }
            )
        ),
        confirmButtonText = "Save",
        cancelButtonText = "Cancel",
        confirmButtonEnabled = isFormValid, // Only enable if input is valid

        // Cancel closes the dialog
        onCancel = {
            showDialog.value = false
            onDismiss()
        },

        // Confirm saves the data using coroutine
        onConfirm = {
            scope.launch {
                // Add store to database
                storeController.addStore(
                    trimmedName,
                    storeAddress.trim(),
                    storeDescription.trim()
                )

                // Refresh store list from DB
                val updatedStores = storeController.getListOfStores()
                storeList.clear()
                storeList.addAll(updatedStores)
                viewModel.storeList = updatedStores.toMutableList()

                // Show toast
                Toast.makeText(context, "Store added successfully", Toast.LENGTH_SHORT).show()

                // Close dialog
                showDialog.value = false
                onDismiss()
            }
        }
    )
}

@Composable
fun EditStoreDialog(
    storeObject: Store,
    storeController: StoreController,
    onDismiss: () -> Unit,
    storeList: SnapshotStateList<Store>,
    viewModel: SharedViewModel
) {
    val showDialog = remember { mutableStateOf(true) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Pre-fill with current store data
    var storeName by remember { mutableStateOf(storeObject.name) }
    var storeAddress by remember { mutableStateOf(storeObject.address) }
    var storeDescription by remember { mutableStateOf(storeObject.description) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Validation logic
    val trimmedName = storeName.trim()
    val isNameDuplicate = storeList.any {
        it.name.equals(trimmedName, ignoreCase = true) && it.id != storeObject.id
    }
    val isFormValid = trimmedName.isNotEmpty() && !isNameDuplicate

    // Show the form dialog
    CustomFormDialog(
        showDialog = showDialog,
        title = "Edit Store",
        icon = Icons.Default.Edit,
        fields = listOf(
            FormField(
                label = "Store Name",
                value = storeName,
                onValueChange = {
                    storeName = it
                    errorMessage = null // Clear error on edit
                },
                error = when {
                    trimmedName.isEmpty() -> "Store Name cannot be empty."
                    isNameDuplicate       -> "Store Name already exists."
                    else                  -> null
                }
            ),
            FormField(
                label = "Store Address",
                value = storeAddress,
                onValueChange = { storeAddress = it }
            ),
            FormField(
                label = "Store Description",
                value = storeDescription,
                onValueChange = { storeDescription = it }
            )
        ),
        confirmButtonText = "Save",
        cancelButtonText = "Cancel",
        confirmButtonEnabled = isFormValid, // Disable confirm if invalid

        // Cancel logic
        onCancel = {
            showDialog.value = false
            onDismiss()
        },

        // Save logic (async)
        onConfirm = {
            scope.launch {
                // Update store in database
                storeController.updateStore(
                    id = storeObject.id,
                    newName = trimmedName,
                    newAddress = storeAddress.trim(),
                    newDescription = storeDescription.trim()
                )

                // Refresh store list
                val updatedStores = storeController.getListOfStores()
                storeList.clear()
                storeList.addAll(updatedStores)
                viewModel.storeList = updatedStores.toMutableList()

                // Show toast
                Toast.makeText(context, "Store updated successfully", Toast.LENGTH_SHORT).show()

                // Close dialog
                showDialog.value = false
                onDismiss()
            }
        }
    )
}

