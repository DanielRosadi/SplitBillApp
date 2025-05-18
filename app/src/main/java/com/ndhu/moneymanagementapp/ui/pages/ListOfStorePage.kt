package com.ndhu.moneymanagementapp.ui.pages

import android.content.Context
import android.util.Log
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ndhu.moneymanagementapp.controllers.StoreController
import com.ndhu.moneymanagementapp.models.Store
import com.ndhu.moneymanagementapp.enums.AppScreens
import com.ndhu.moneymanagementapp.sharedviewmodels.SharedViewModel
import kotlinx.coroutines.*


@Composable
fun ListStorePage(context : Context, navController: NavController, viewModel: SharedViewModel){
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
fun GetStoreListFromDatabase(context : Context, navController: NavController, viewModel: SharedViewModel){

    val storeController = StoreController() // Initialize object

    val stores = remember { mutableStateListOf<Store>() }

    // TODO: doesnt update when adding store?
    // TODO: may try LaunchedEffect if runBlocking makes the app loading
    runBlocking {
        Log.d("LaunchTag", "DEBUG. Accessing Store List.")  // Prints in Logcat

        // clear stores list and add data from database into the stores list
        stores.clear()
        stores.addAll( storeController.getListOfStores() )
        viewModel.storeList = storeController.getListOfStores().toMutableList()

    }

    // perform UI composition
    ComposeListStore(storeController, stores, navController, viewModel)
}

@Composable
fun ComposeListStore(storeController: StoreController, data: SnapshotStateList<Store>, navController: NavController, viewModel: SharedViewModel) {
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
                    searchBox = { SearchBoxStore(searchQuery = searchQuery) }
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(7f)
                        .clip(RoundedCornerShape(20.dp))
                        .background(darkGrey)

                ){

//                    LaunchedEffect(searchQuery.value) {
//                        println("Search query updated to: ${searchQuery.value}")
//                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxWidth().weight(1f),
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
                        .padding(2.dp)
                    ,
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
                        PopUpAddStore(
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



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBoxStore(searchQuery: MutableState<String>) {
    // TODO: implement search feature
    // var query by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .width(300.dp)
            .padding(8.dp)
    ){
        TextField(
            value = searchQuery.value,
            onValueChange = { searchQuery.value = it },
            modifier = Modifier
                .fillMaxWidth() // width(300.dp)
                .height(56.dp)
                .clip(RoundedCornerShape(50.dp))
                .background(Color.White)
            ,
            placeholder = { Text("search") },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
            },
            trailingIcon = {
                if (searchQuery.value.isNotEmpty()) {
                    IconButton(onClick = { searchQuery.value = "" }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Clear Search")
                    }
                }
            },
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White, // <-- keeps background clean
                unfocusedIndicatorColor = Color.Transparent, // <-- removes line
                focusedIndicatorColor = Color.Transparent,   // <-- removes line
                disabledIndicatorColor = Color.Transparent   // <-- removes line
            ),
            shape = RoundedCornerShape(50.dp) // <-- Best to pass shape here
        )


    }

}

@Composable
fun LazyColumnTest(storeController: StoreController, data: SnapshotStateList<Store>, navController: NavController, viewModel: SharedViewModel){
    LazyColumn(
        modifier = Modifier.fillMaxWidth().fillMaxHeight(),
        contentPadding = PaddingValues(5.dp)) {

        // items(data) { item ->
        items(data) { item ->
            ListDataStore(storeController, item, navController, data, viewModel)
        }

    }
}


@Composable
fun ListDataStore(storeController: StoreController, storeObject: Store, navController: NavController, data: SnapshotStateList<Store>, viewModel: SharedViewModel) {
    var showDialogEditStore by remember { mutableStateOf(false) }

    Card (
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .padding(5.dp)
            //.background(Color.White)
            // pass respective storeId to the screen
            .clickable { navController.navigate(route = AppScreens.ListItemsScreen.name + "/" + storeObject.id) },
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
        if (showDialogEditStore){
            PopUpEditStore(storeObject = storeObject, storeController = storeController,
                onDismiss = {showDialogEditStore = false}, storeList = data, viewModel = viewModel
            )
        }
    }
}

// TODO: rework design
@Composable
fun PopUpAddStore(storeController: StoreController, onDismiss: () -> Unit, storeList: SnapshotStateList<Store>, viewModel: SharedViewModel) {
    var storeName by remember { mutableStateOf("") }
    var storeAddress by remember { mutableStateOf("") }
    var storeDescription by remember { mutableStateOf("") }


            AlertDialog(
                onDismissRequest = onDismiss,
                title = { Text(text = "Add Store") },
                text = {
                    Column {
                        TextField(
                            value = storeName,
                            onValueChange = { storeName = it },
                            label = { Text("Store Name") }
                        )
                        TextField(
                            value = storeAddress,
                            onValueChange = { storeAddress = it },
                            label = { Text("Store Address") }
                        )
                        TextField(
                            value = storeDescription,
                            onValueChange = { storeDescription = it },
                            label = { Text("Store Description") }
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {

                            // add store record to the database
                            runBlocking {
                                storeController.addStore(
                                    storeName,
                                    storeAddress,
                                    storeDescription
                                )
                            }
                            // add store to the LazyColumn data
                            // this approach can only generate different id, which different from db
                            // storeList.add(Store(storeName, storeAddress, storeDescription))
                            runBlocking {
                                storeList.clear()
                                storeList.addAll(storeController.getListOfStores())
                                viewModel.storeList = storeController.getListOfStores().toMutableList()

                            }

                            // add store to SharedViewModel
                            // TODO: need to check for possibility of having different Store object.
                            // TODO: if yes, then need to use the same Store object created at the top.
                            // viewModel.storeList?.add(Store(storeName, storeAddress, storeDescription))

                            // println("User entered: $storeName $storeAddress $storeDescription") // Handle input

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

// TODO: rework design
@Composable
fun PopUpEditStore(storeObject: Store, storeController: StoreController, onDismiss: () -> Unit, storeList: SnapshotStateList<Store>, viewModel: SharedViewModel) {
    var storeName by remember { mutableStateOf("") }
    var storeAddress by remember { mutableStateOf("") }
    var storeDescription by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Edit Store") },
        text = {
            Column {
                TextField(
                    value = storeName,
                    onValueChange = { storeName = it },
                    label = { Text("Store Name") }
                )
                TextField(
                    value = storeAddress,
                    onValueChange = { storeAddress = it },
                    label = { Text("Store Address") }
                )
                TextField(
                    value = storeDescription,
                    onValueChange = { storeDescription = it },
                    label = { Text("Store Description") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {

                    runBlocking {
                        storeController.updateStore(
                            storeObject.id,
                            storeName,
                            storeAddress,
                            storeDescription
                        )
                    }


                    runBlocking {
                        storeList.clear()
                        storeList.addAll(storeController.getListOfStores())
                        viewModel.storeList = storeController.getListOfStores().toMutableList()

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





fun testPrintStoreAttributes(obj: Store){
    print("Store id: ${obj.id}, Store name: ${obj.name} , Store description: ${obj.description}")
    Log.d("StoreTag", "Store id: ${obj.id}, Store name: ${obj.name} , Store description: ${obj.description}")
}

fun testGetStoreListFromDatabase(){
    val storeList = StoreController() // Initialize object

    runBlocking {
        storeList.getListOfStores()
        println(storeList)
        Log.d("MyTag", "This is a debug message")  // Prints in Logcat
    }
}

