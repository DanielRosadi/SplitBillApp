package com.ndhu.moneymanagementapp.ui.pages


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun ListOfItemPage(navController: NavController){
    val listItem = listOf("Item 1", "Item 2", "Item 3")

    Column (modifier = Modifier.fillMaxWidth().fillMaxHeight(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally) {

        Text(text = "List of Items")

        LazyColumn {

            items(listItem){ item ->
                Card {
                    Row {
                        Text(text = item)
                        Text(text = "NTD 100")
                        Button(onClick = {}) { Text(text = "-") }
                        Text(text = "1")
                        Button(onClick = {}) { Text(text = "+") }
                    }



                }

            }

        }

        Button (onClick = {} ) {
            Text(text = "Add Item")
        }

        Button (onClick = {} ) {
            Text(text = "Done")
        }

    }




}

/*
fun ListItemCompose(){

}

 */


