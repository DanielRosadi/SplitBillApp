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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ndhu.moneymanagementapp.controllers.StoreController
import com.ndhu.moneymanagementapp.models.Store
import com.ndhu.moneymanagementapp.enums.AppScreens
import com.ndhu.moneymanagementapp.components.SearchBox
import com.ndhu.moneymanagementapp.sharedviewmodels.SharedStoreViewModel
import kotlinx.coroutines.*

@Composable
fun ListStorePage(context : Context, navController: NavController, viewModel: SharedStoreViewModel){
    GetStoreListFromDatabase(context, navController, viewModel)
}

@Composable
fun GetStoreListFromDatabase(context : Context, navController: NavController, viewModel: SharedStoreViewModel){

    val storeController = StoreController() // Initialize object

    val stores = remember { mutableStateListOf<Store>() }

    // TODO: doesnt update when adding store?
    // may try LaunchedEffect if runBlocking makes the app loading
    runBlocking {
        Log.d("LaunchTag", "DEBUG. Accessing Store List.")  // Prints in Logcat

        // clear stores list and add data from database into the stores list
        stores.clear()
        stores.addAll( storeController.getListOfStores() )
        viewModel.storeList = storeController.getListOfStores()

    }

    // perform UI composition
    ComposeListStore(storeController, stores, navController)
}

@Composable
fun ComposeListStore(storeController: StoreController, data: SnapshotStateList<Store>, navController: NavController){

    var showDialogAddStore by remember { mutableStateOf(false) }

    Scaffold (
        topBar = {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
                }
                Spacer(modifier = Modifier.width(50.dp))
                Text("List of Store", fontSize = 40.sp, fontWeight = FontWeight.Bold)
            }
        }
    )
    { paddingValues ->

        Column (modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(paddingValues)
            .padding(10.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {

            Spacer(modifier = Modifier.height(10.dp))

            SearchBox()

            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn (modifier = Modifier.weight(1f), contentPadding = PaddingValues(5.dp)) {

                items(data) { item ->
                    ListDataStore(storeController, item, navController)
                }

            }

            Spacer(modifier = Modifier.height(30.dp))

            Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Button(onClick = {}, modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth()
                    .weight(1f)) {
                    Text(text = "Delete")
                }

                Button(onClick = { showDialogAddStore = true },
                    modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth()
                    .weight(1f)) {
                    Text(text = "Add Store")
                }

                if (showDialogAddStore){
                    PopUpAddStore(storeController = storeController,
                        onDismiss = {showDialogAddStore = false}
                    )
                }

            }

            Spacer(modifier = Modifier.height(20.dp))

        }

    }



}

@Composable
fun ListDataStore(storeController: StoreController, storeObject: Store, navController: NavController) {
    var showDialogEditStore by remember { mutableStateOf(false) }

    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            // pass respective storeId to the screen
            .clickable { navController.navigate(route = AppScreens.ListItemsScreen.name + "/" + storeObject.id) },
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
    ) {
        Text(
            text = storeObject.name,
            fontSize = 30.sp,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = storeObject.description,
            fontSize = 20.sp,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyLarge
        )
        Button(onClick = { showDialogEditStore = true } ) {
            Text(text = "Edit (Temp)")
        }
        if (showDialogEditStore){
            PopUpEditStore(storeObject = storeObject, storeController = storeController,
                onDismiss = {showDialogEditStore = false}
            )
        }
    }
}

@Composable
fun PopUpAddStore(storeController: StoreController, onDismiss: () -> Unit) {
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

                            runBlocking {
                                storeController.addStore(
                                    storeName,
                                    storeAddress,
                                    storeDescription
                                )
                            }

                            println("User entered: $storeName $storeAddress $storeDescription") // Handle input

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
fun PopUpEditStore(storeObject: Store, storeController: StoreController, onDismiss: () -> Unit) {
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

                    println("User edited: ${storeObject.id} $storeName $storeAddress $storeDescription") // Handle input

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

