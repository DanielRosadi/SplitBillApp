package com.ndhu.moneymanagementapp.ui.oldpages

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
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ndhu.moneymanagementapp.components.SearchBoxDefault
import com.ndhu.moneymanagementapp.controllers.StoreController
import com.ndhu.moneymanagementapp.models.Store
import com.ndhu.moneymanagementapp.sharedviewmodels.SplitBillViewModel
import kotlinx.coroutines.runBlocking

@Composable
fun DeleteStoreScreen(context : Context, navController: NavController, viewModel: SplitBillViewModel){
    GetStoreListFromDatabase(context, navController)
}

@Composable
fun GetStoreListFromDatabase(context : Context, navController: NavController /*, viewModel: SharedStoreViewModel */){

    val storeController = StoreController() // Initialize object

    val stores = remember { mutableStateListOf<Store>() }

    // TODO: doesnt update when adding store?
    // TODO: may try LaunchedEffect if runBlocking makes the app loading
    runBlocking {
        Log.d("LaunchTag", "DEBUG. Accessing Store List.")  // Prints in Logcat

        // clear stores list and add data from database into the stores list
        stores.clear()
        stores.addAll( storeController.getListOfStores() )
        // viewModel.storeList = storeController.getListOfStores().toMutableList()

    }

    // perform UI composition
    ComposeListStoreForDelete(storeController, stores, navController)
}

@Composable
fun ComposeListStoreForDelete(storeController: StoreController, data: SnapshotStateList<Store>, navController: NavController){

    var showDialogDeleteStore by remember { mutableStateOf(false) }
    // selectedStores stores currently selected stores. Its null initially because no store is selected.
    // When the user clicks an item, it updates to that item.
    // When the delete button is pressed, the selected store is removed, and selectedStores is reset to null.
    val selectedStores = remember { mutableStateListOf<Store>() }

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

            // TODO: implement search feature
            SearchBoxDefault()

            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn (modifier = Modifier.weight(1f), contentPadding = PaddingValues(5.dp)) {

                items(data) { store ->
                    ListDataStoreForDelete(storeController, store, navController, isSelected = store in selectedStores, onClick = {
                        if (store in selectedStores) {
                            selectedStores.remove(store) // Deselect if already selected
                        } else {
                            selectedStores.add(store) // Select if not already selected
                        }
                    })
                }

            }

            Spacer(modifier = Modifier.height(30.dp))

            Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {

                // if user wants to delete, it turns on the delete dialog flag
                Button(onClick = { showDialogDeleteStore = true },
                    modifier = Modifier
                        .height(60.dp)
                        .fillMaxWidth()
                        .weight(1f)) {
                    Text(text = "Delete Store")
                }

                // when delete dialog flag is on/true, call the pop up
                // pass selectedStores list into the pop up
                if (showDialogDeleteStore){
                    PopUpDeleteStore(storeController = storeController,
                        onDismiss = {showDialogDeleteStore = false},
                        data, selectedStores
                    )
                }

            }

            Spacer(modifier = Modifier.height(20.dp))

        }

    }
}

/*
@Composable
fun ListDataStoreForDelete(storeController: StoreController, storeObject: Store, navController: NavController) {
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

        }
    }
}
 */

@Composable
fun ListDataStoreForDelete(storeController: StoreController, storeObject: Store, navController: NavController, isSelected: Boolean, onClick: () -> Unit) {
    var showDialogEditStore by remember { mutableStateOf(false) }

    Card (
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .padding(5.dp)
            // onClick() notes down which stores are currently selected. Selected stores will be put inside selectedStores list
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp),
        // colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
        // change card color to red if it isSelected, else stays gray
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color.Red else Color.Gray
        )
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

        }
    }
}

@Composable
fun PopUpDeleteStore(storeController: StoreController, onDismiss: () -> Unit, storeList: SnapshotStateList<Store>, selectedStores: SnapshotStateList<Store>) {

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Confirm to Delete Store ?") },
        text = {
        },
        confirmButton = {
            Button(
                onClick = {
                    // tracked selectedStores are passed into the pop up

                    // TODO: try rememberCoroutineScope(), coroutineScope.launch{}
                    // iteratively delete store in database
                    for(store in selectedStores){
                        runBlocking {
                            storeController.deleteStore(store.id)
                        }
                    }
                    // remove from the selectedStores list all stores
                    storeList.removeAll(selectedStores)
                    // Clear the selection after deletion
                    selectedStores.clear()

                    // println("User entered: $storeName $storeAddress $storeDescription") // Handle input

                    onDismiss()
                },
                enabled = selectedStores.isNotEmpty()
            ) {
                Text("Yes")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss
            ) {
                Text("No")
            }
        }
    )

}

