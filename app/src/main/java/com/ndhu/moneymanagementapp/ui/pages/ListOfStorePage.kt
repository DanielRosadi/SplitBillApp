package com.ndhu.moneymanagementapp.ui.pages

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ndhu.moneymanagementapp.R
import com.ndhu.moneymanagementapp.controllers.StoreController
import com.ndhu.moneymanagementapp.models.Store
import com.ndhu.moneymanagementapp.enums.AppScreens
import com.ndhu.moneymanagementapp.components.SearchBox
import com.ndhu.moneymanagementapp.sharedviewmodels.SharedViewModel
import kotlinx.coroutines.*


@Composable
fun ListStorePage(context : Context, navController: NavController, viewModel: SharedViewModel){
    // Get the actual window size (container size)
    val windowInfo = LocalWindowInfo.current
    val screenWidthPx = windowInfo?.containerSize?.width ?: 0
    val screenHeightPx = windowInfo?.containerSize?.height ?: 0

    // Convert pixels to Dp
    val screenWidth = with(LocalDensity.current) { screenWidthPx.toDp() }
    val screenHeight = with(LocalDensity.current) { screenHeightPx.toDp() }


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
    // ComposeListStore(storeController, stores, navController, viewModel)
    IncrementalDesignTest(storeController, stores, navController, viewModel)
}

@Composable
fun IncrementalDesignTest(storeController: StoreController, data: SnapshotStateList<Store>, navController: NavController, viewModel: SharedViewModel) {
    var showDialogAddStore by remember { mutableStateOf(false) }

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
                    .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                    .background(Color(0xFFF0F0F0))
            )
            {

                Spacer(modifier = Modifier.width(10.dp))

                // Content Top Bar
                ContentTopBar(navController = navController, topBarText = "List of Store")



                LazyColumn(
                    modifier = Modifier.fillMaxWidth().weight(4f),
                    contentPadding = PaddingValues(5.dp)
                ) {

                    // items(data) { item ->
                    items(data) { item ->
                        ListDataStore(storeController, item, navController, data, viewModel)
                    }

                }

                Spacer(modifier = Modifier.height(30.dp))

                Row(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    horizontalArrangement = Arrangement.SpaceBetween
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
                            .height(60.dp)
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        Text(text = "Add Store")
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
fun ComposeListStore(storeController: StoreController, data: SnapshotStateList<Store>, navController: NavController, viewModel: SharedViewModel){

    var showDialogAddStore by remember { mutableStateOf(false) }

    /*
    Scaffold (
    )
    { paddingValues ->
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

                TitleText()

                Spacer(modifier = Modifier.height(60.dp))

                // White Box (Content Card)
                Column (modifier = Modifier
                    .fillMaxWidth()
                    // .fillMaxHeight()
                    //.weight(1f)
                    .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                    .background(Color(0xFFF0F0F0))


                ) {// TODO: Search func

                    // Content Top Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
                        }
                        Spacer(modifier = Modifier.width(10.dp))

                        Column (
                            modifier = Modifier.fillMaxWidth().weight(1f),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("List of Store", fontSize = 30.sp, fontWeight = FontWeight.Bold)
                            SearchBox()
                        }

                    }

                    // Content
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        //.fillMaxHeight()
                        // .weight(1f)
                        .background(Color(0xFFD9D9D9))

                    ){
                        Spacer(modifier = Modifier.height(10.dp))

                        LazyColumn(
                            modifier = Modifier.fillMaxWidth().weight(1f),
                            contentPadding = PaddingValues(5.dp)) {

                            // items(data) { item ->
                            items(data) { item ->
                                ListDataStore(storeController, item, navController, data, viewModel)
                            }

                        }

                        Spacer(modifier = Modifier.height(30.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                            horizontalArrangement = Arrangement.SpaceBetween
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
                                    .height(60.dp)
                                    .fillMaxWidth()
                                    .weight(1f)
                            ) {
                                Text(text = "Add Store")
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

                        Spacer(modifier = Modifier.height(20.dp))
                    }


                }

            }
        }

    //}


}

@Composable
fun ListDataStore(storeController: StoreController, storeObject: Store, navController: NavController, data: SnapshotStateList<Store>, viewModel: SharedViewModel) {
    var showDialogEditStore by remember { mutableStateOf(false) }

    Card (
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .padding(5.dp)
            // pass respective storeId to the screen
            .clickable { navController.navigate(route = AppScreens.ListItemsScreen.name + "/" + storeObject.id) },
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
    ) {
        Card(
            modifier = Modifier
                .wrapContentSize()
                .padding(10.dp)
                // pass respective storeId to the screen
                .clickable { showDialogEditStore = true },
            // elevation = CardDefaults.cardElevation(4.dp),
            // colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)

        ) {
            Text(
                text = storeObject.name,
                fontSize = 30.sp,
                //modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = storeObject.address,
                fontSize = 20.sp,
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

