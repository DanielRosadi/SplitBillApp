package com.ndhu.moneymanagementapp.ui.pages

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ndhu.moneymanagementapp.controllers.StoreController
import com.ndhu.moneymanagementapp.models.Store
import com.ndhu.moneymanagementapp.navigation.AppScreens
import kotlinx.coroutines.*

@Composable
fun ListStorePage(navController: NavController){
    GetStoreListFromDatabase(navController)
}

@Composable
fun GetStoreListFromDatabase(navController: NavController){

    val store = StoreController() // Initialize object

    val stores = remember { mutableStateListOf<Store>() }

    // may try LaunchedEffect if runBlocking makes the app loading
    runBlocking {
        Log.d("LaunchTag", "DEBUG. Accessing Store List.")  // Prints in Logcat

        // clear stores list and add data from database into the stores list
        stores.clear()
        stores.addAll( store.getListOfStores() )
    }

    // perform UI composition
    ComposeListStore(stores, navController)
}

@Composable
fun ComposeListStore(data: SnapshotStateList<Store>, navController: NavController){
    Column (modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(10.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {

        Text(text = "List of Store", fontSize = 30.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(20.dp))

        // TODO: drop down box here
        Text(text = "DROP DOWN/SEARCH BOX HERE")

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn (modifier = Modifier.weight(1f), contentPadding = PaddingValues(5.dp)) {

            items(data) { item ->
                ListDataItem(item, navController)
            }

        }

        Spacer(modifier = Modifier.height(30.dp))

        Button(onClick = {}, modifier = Modifier.fillMaxWidth().height(60.dp)) {
            Text(text = "Add Store")
        }

        Spacer(modifier = Modifier.height(20.dp))

    }
}

@Composable
fun ListDataItem(obj: Store, navController: NavController) {
    Card (
        modifier = Modifier.fillMaxWidth().padding(5.dp).clickable { navigateToListOfItemScreen(navController) },
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
    ) {
        Text(
            text = obj.name,
            fontSize = 30.sp,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = obj.description,
            fontSize = 20.sp,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyLarge
        )
    }
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

fun navigateToListOfItemScreen(navController: NavController) {
    navController.navigate(route = AppScreens.ListItemsScreen.name)
}