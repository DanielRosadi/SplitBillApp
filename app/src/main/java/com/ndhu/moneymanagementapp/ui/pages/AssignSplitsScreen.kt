package com.ndhu.moneymanagementapp.ui.pages

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ndhu.moneymanagementapp.reusable_composables.SearchBox
import com.ndhu.moneymanagementapp.reusable_composables.UniformDesignScaffold

@Composable
fun AssignSplitsScreen(context: Context, navController: NavController){

    val listParticipants = List(7) { "User ${it + 1}" } // Example: 7 items
    val listItems = List(5) { "Item ${it + 1}" } // Example: 5 items

    ComposeAssignSplits(context, listParticipants, listItems, navController)
}

@Composable
fun ComposeAssignSplits(context: Context, listParticipants: List<String>, listItems: List<String>, navController: NavController){
    UniformDesignScaffold(
        topAppBar = {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
                }
                Spacer(modifier = Modifier.width(50.dp))
                Text("Assign Splits", fontSize = 30.sp, fontWeight = FontWeight.Bold)
            }
        },

        body = {paddingValues->
            Column (modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(paddingValues)
                .padding(10.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {

                Spacer(modifier = Modifier.height(10.dp))

                SearchBox()

                Spacer(modifier = Modifier.height(10.dp))

                Text(text = "Now Selecting: {User 1}")

                Spacer(modifier = Modifier.height(10.dp))

                LazyColumn (modifier = Modifier.weight(1f), contentPadding = PaddingValues(5.dp)) {

                    // chunked groups items into sets of 3 for each row
                    // it also automatically handles when the list is not perfectly divisible by 3
                    items(listItems) { item ->
                        ListItemSplits(item)

                    }

                }

                Spacer(modifier = Modifier.height(30.dp))

                LazyRow () {
                    items(listParticipants) { participant ->
                        ListBillParticipants(participant)

                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                Button(onClick = { }, modifier = Modifier.fillMaxWidth().height(60.dp)) {
                    Text(text = "Done")
                }

                Spacer(modifier = Modifier.height(20.dp))

            }
        }

    )
}

@Composable
fun ListItemSplits(item: String){

}

@Composable
fun ListBillParticipants(participant: String){

}