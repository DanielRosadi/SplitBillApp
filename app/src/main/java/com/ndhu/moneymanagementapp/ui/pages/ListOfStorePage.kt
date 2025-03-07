package com.ndhu.moneymanagementapp.ui.pages

import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
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
import androidx.lifecycle.ViewModel
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

    val storeViewModel = StoreViewModel()

    // may try LaunchedEffect if runBlocking makes the app loading
    runBlocking {
        Log.d("LaunchTag", "DEBUG. Accessing Store List.")  // Prints in Logcat

        // clear stores list and add data from database into the stores list
        stores.clear()
        stores.addAll( store.getListOfStores() )
        storeViewModel.addAll(store.getListOfStores())

    }

    // perform UI composition
    ComposeListStore(stores, navController)
}

@Composable
fun ComposeListStore(data: SnapshotStateList<Store>, navController: NavController){

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

            // Text(text = "List of Store", fontSize = 30.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(20.dp))

            SearchBox()
            // ExpandableSearchBox()

            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn (modifier = Modifier.weight(1f), contentPadding = PaddingValues(5.dp)) {

                items(data) { item ->
                    ListDataItem(item, navController)
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

                Button(onClick = {}, modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth()
                    .weight(1f)) {
                    Text(text = "Add Store")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

        }

    }



}

@Composable
fun ListDataItem(obj: Store, navController: NavController) {
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clickable { navController.navigate(route = AppScreens.ListItemsScreen.name + "/" + obj.id) },
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

@Composable
fun SearchBox() {
    var query by remember { mutableStateOf("") }

    TextField(
        value = query,
        onValueChange = { query = it },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        placeholder = { Text("Search...") },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { query = "" }) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Clear Search")
                }
            }
        },
        singleLine = true
    )
}


@Composable
fun ExpandableSearchBox() {
    var query by remember { mutableStateOf("") }
    var isExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.Gray.copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isExpanded) {
            TextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Search...") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
                },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = { query = "" }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Clear Search")
                        }
                    }
                },
                singleLine = true
            )
        }

        IconButton(onClick = { isExpanded = !isExpanded }) {
            Icon(
                imageVector = if (isExpanded) Icons.Default.Close else Icons.Default.Search,
                contentDescription = "Toggle Search"
            )
        }
    }
}

class StoreViewModel : ViewModel() {
    private var stores = listOf<Store>()

    fun getStoreById(id: String): Store? {
        return stores.find { it.id == id }
    }

    fun addAll(listStore: List<Store>){
        stores = listStore
    }
}
