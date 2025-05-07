package com.ndhu.moneymanagementapp.ui.pages

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ndhu.moneymanagementapp.components.SearchBox
import com.ndhu.moneymanagementapp.models.User
import com.ndhu.moneymanagementapp.sharedviewmodels.SharedViewModel

@Composable
fun SplitItemScreen(context: Context, navController: NavController, viewModel: SharedViewModel){
    ComposeSplitItem(context = context, navController = navController)
}

@Composable
fun ComposeSplitItem(context: Context, navController: NavController){

    Column (
        modifier = Modifier.fillMaxWidth().fillMaxHeight()
    ){
        // Top Bar
        Column (
            modifier = Modifier.fillMaxWidth().weight(1f)
        ) {
            SplitItemTopBar(navController = navController)
        }

        // Content
        Column (
            modifier = Modifier.fillMaxWidth().weight(7f)
        ) {
            // Header Text
            // TODO: Replace with real data
            Text(text = "Item 1", fontSize = 30.sp)
            Text(text = "You can adjust quantity of assigned items to friends.", fontSize = 15.sp)
            Text(text = "Decimal number is allowed. For example: 0.5 defines half portion, or other decimal number based on agreement.", fontSize = 15.sp)
            Text(text = "Price will be calculated based on quantity assigned.", fontSize = 15.sp)

            // TODO: make it responsive
            Text(text = "Total Item Quantity: x")
            Text(text = "Fully Assigned Item Quantity: y")

            // Body
            LazyColumn (
                modifier = Modifier.fillMaxWidth()
            ) {
                items(listOf("User 1", "User 2", "User 3", "User 4", "User 5")){ user ->
                    ListSplitItemNonEqual(user = user)

                }
            }


        }

        // Bottom
        Column (
            modifier = Modifier.fillMaxWidth().weight(1f)
        ) {
            // TODO: Save data
            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Save")
            }
        }


    }

}

@Composable
fun SplitItemTopBar(navController: NavController){
    /*
    Top Bar
    */
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(55.dp))

            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
            }
        }

        Spacer(modifier = Modifier.width(5.dp))

        Column(
            modifier = Modifier.weight(10f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Split Item",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = surannaFont
            )
            // SearchBox()
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.Close, contentDescription = "Cancel")
            }

            Spacer(modifier = Modifier.width(40.dp))
        }
    }
}


@Composable
fun ListSplitItemNonEqual(user: String){
    var initialQuantity by remember { mutableStateOf("1.0") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(5.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(text = user, modifier = Modifier.weight(2f))


            Button(onClick = {
                val qty = initialQuantity.toFloatOrNull() ?: 0f
                val newQty = (qty - 1f).coerceAtLeast(0f) // Prevent going below 0
                initialQuantity = "%.2f".format(newQty)
            },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "-")
            }

            TextField(

                value = initialQuantity,
                onValueChange = {input ->
                    // Allow only digits and at most one dot
                    if (input.isEmpty() || input.matches(Regex("""\d*\.?\d*"""))) {
                        initialQuantity = input
                    }
                },
                label = { Text("Qty") },
                modifier = Modifier.weight(1f) /*.width(100.dp)*/,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Button(onClick = {
                val qty = initialQuantity.toFloatOrNull() ?: 0f
                initialQuantity = "%.2f".format(qty + 1f)
            },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "+")
            }
        }
    }
}