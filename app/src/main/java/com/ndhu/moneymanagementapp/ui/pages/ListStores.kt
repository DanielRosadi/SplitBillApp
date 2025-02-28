package com.ndhu.moneymanagementapp.ui.pages

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ndhu.moneymanagementapp.controllers.StoreController
import com.ndhu.moneymanagementapp.models.Store
import kotlinx.coroutines.*

@Composable
fun ListStorePage(){

    val store = StoreController() // Initialize object
    // var storeList: List<Store>

    // val stores = remember { mutableStateOf(emptyList<Store>()) }

    val stores = remember { mutableStateListOf<Store>() }

    // var storeList = remember { mutableStateOf(emptyList<Store>()) } // Starts as an empty list


    LaunchedEffect(Unit) {
        // var storeList = emptyList<Store>()
        // val storeList = store.getListOfStores()

        // println(store)
        Log.d("LaunchTag", "DEBUG. Accessing Store List.")  // Prints in Logcat
        stores.clear()

        stores.addAll( store.getListOfStores() )
    }



    /*
    runBlocking {
                // var storeList = emptyList<Store>()
        // val storeList = store.getListOfStores()

        // println(store)
        // Log.d("LaunchTag", "DEBUG. Accessing Store List.")  // Prints in Logcat

        stores.value = store.getListOfStores()
    }

     */



    /*
    for (store in storeList) {
        printStoreAttributes(store)
    }
    
     */

    ComposeListStores(stores)
}

@Composable
fun ComposeListStores(data: SnapshotStateList<Store>){
    // testGetStoreListFromDatabase()

    // val myData = listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5") // List<String>

    Column (modifier = Modifier.fillMaxWidth().fillMaxHeight(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "List of Store", fontSize = 30.sp, fontWeight = FontWeight.Bold)
        // drop down box here

        LazyColumn (contentPadding = PaddingValues(5.dp)) {

            items(data) { item ->
                ListItem(item)
            }

        }
    }
}

@Composable
fun ListItem(obj: Store) {
    Card (
        modifier = Modifier.fillMaxWidth(),
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

fun printStoreAttributes(obj: Store){
    print("Store id: ${obj.id}, Store name: ${obj.name} , Store description: ${obj.description}")
    // Log.d("MyTag", "DEBUG. Printing Store Attributes")
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