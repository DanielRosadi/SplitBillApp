package com.ndhu.moneymanagementapp.ui.pages


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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun ListOfItemPage(navController: NavController){
    val listItem = listOf("Item 1", "Item 2", "Item 3")

    Column (modifier = Modifier.fillMaxWidth().fillMaxHeight(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally) {

        Text(text = "List of Items", fontSize = 30.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn (modifier = Modifier.fillMaxWidth().fillMaxHeight().weight(1f), contentPadding = PaddingValues(5.dp)) {

            items(listItem){ item ->
                Card (modifier = Modifier.fillMaxWidth().height(80.dp).padding(7.dp)) {
                    Row (modifier = Modifier.padding(8.dp), horizontalArrangement = Arrangement.Start) {
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

        Button (onClick = {}, modifier = Modifier.fillMaxWidth().height(60.dp) ) {
            Text(text = "Add Item")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button (onClick = {}, modifier = Modifier.fillMaxWidth().height(60.dp) ) {
            Text(text = "Done")
        }

        Spacer(modifier = Modifier.height(20.dp))

    }




}

/*
fun ListItemCompose(){

}

 */


