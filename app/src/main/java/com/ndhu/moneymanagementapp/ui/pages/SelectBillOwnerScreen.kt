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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ndhu.moneymanagementapp.models.User
import com.ndhu.moneymanagementapp.navigation.AppScreens
import com.ndhu.moneymanagementapp.reusable_composables.SearchBox
import com.ndhu.moneymanagementapp.reusable_composables.UniformDesignScaffold

@Composable
fun SelectBillOwnerScreen(context: Context, navController: NavController){

    // getContacts() here

    // val listBillOwners = listOf("User 1", "User 2", "User 3", "User 4", "User 5", "User 6", "User 7", "User 8", "User 9", "User 10", "User 11")

    val listBillOwners = List(17) { "User ${it + 1}" } // Example: 11 items

    ComposeSelectBillOwner(context, listBillOwners, navController)
}

@Composable
fun ComposeSelectBillOwner(context: Context, listBillOwner: List<String>, navController: NavController){
    UniformDesignScaffold(
        topAppBar = {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
                }
                Spacer(modifier = Modifier.width(50.dp))
                Text("Select Bill Owner", fontSize = 30.sp, fontWeight = FontWeight.Bold)
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

                LazyColumn (modifier = Modifier.weight(1f), contentPadding = PaddingValues(5.dp)) {

                    // chunked groups items into sets of 3 for each row
                    // it also automatically handles when the list is not perfectly divisible by 3
                    items(listBillOwner.chunked(3)) { rowItems ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            // iterate each item in list of 3 item
                            rowItems.forEach { item ->
                                ListBillOwners(item)
                            }
                            // Add empty spaces for the last row if it has less than 3 items
                            // need to add empty spaces for the remaining non-existing elements
                            // otherwise, Compose will make default Row alignment/behavior, which does not actually align like the other
                            // perfectly sets of 3 items (maintaining 3 column structure)
                            repeat(3 - rowItems.size) {
                                Spacer(modifier = Modifier.size(115.dp))
                            }
                        }

                    }

                }

                Spacer(modifier = Modifier.height(30.dp))

                Button(onClick = { navController.navigate(route = AppScreens.AssignSplitsScreen.name) }, modifier = Modifier.fillMaxWidth().height(60.dp)) {
                    Text(text = "Done")
                }

                Spacer(modifier = Modifier.height(20.dp))

            }
        }

    )
}

@Composable
fun ListBillOwners(billOwners: String){
    Card (
        modifier = Modifier
            .size(115.dp)
            .fillMaxWidth()
            .padding(5.dp),
        shape = CircleShape, // Makes it round
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
    ) {
        Text(
            text = billOwners,
            fontSize = 18.sp,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

