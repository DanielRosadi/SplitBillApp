package com.ndhu.moneymanagementapp.ui.pages


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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ndhu.moneymanagementapp.models.Store
import com.ndhu.moneymanagementapp.reusable_composables.SearchBox
import com.ndhu.moneymanagementapp.sharedviewmodels.SharedStoreViewModel

@Composable
fun ListOfItemPage(navController: NavController, storeId: String?, viewModel: SharedStoreViewModel){
    val listItem = listOf("Item 1", "Item 2", "Item 3")

    // search for specific store object using passed storeId from previous screen, from global viewModel
    val store = storeId?.let { viewModel.getStoreById(it) }
    // viewModel.storeList
    // Text("Store: ${store?.name}, Description: ${store?.description}")

    ComposeListItem(listItem, store, navController)

}

@Composable
private fun ComposeListItem(listItem: List<String>, store: Store?, navController: NavController) {

    Scaffold (
        topBar = {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
                }

                Column() {
                    Card (modifier = Modifier.fillMaxWidth().clickable { }) {
                        Text(
                            text = store?.name ?: "Unknown Store",
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text("{Store Address}", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }
            }
            }
        }
    )
    {paddingValues->

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight().padding(paddingValues),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Text(text = "List of Items", fontSize = 30.sp, fontWeight = FontWeight.Bold)
        SearchBox()

        // Spacer(modifier = Modifier.height(0.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .weight(1f),
            contentPadding = PaddingValues(5.dp)
        ) {

            items(listItem) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .padding(7.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(text = item)
                        Spacer(modifier = Modifier.width(30.dp))

                        Text(text = "NTD 100")
                        Spacer(modifier = Modifier.width(25.dp))

                        Button(onClick = {}) { Text(text = "-") }
                        Spacer(modifier = Modifier.width(30.dp))

                        Text(text = "1")
                        Spacer(modifier = Modifier.width(30.dp))

                        Button(onClick = {}) { Text(text = "+") }
                    }


                }

            }

        }

        Spacer(modifier = Modifier.height(20.dp))


        Column (modifier = Modifier.padding(20.dp)) {

            Button(
                onClick = {}, modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Text(text = "Done")
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row {
                Button(
                    onClick = {}, modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .weight(1f)
                ) {
                    Text(text = "Delete")
                }

                Spacer(modifier = Modifier.width(20.dp))

                Button(
                    onClick = {}, modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .weight(1f)
                ) {
                    Text(text = "Add item")
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))

    }

}

}

/*
fun ListItemCompose(){

}

 */


