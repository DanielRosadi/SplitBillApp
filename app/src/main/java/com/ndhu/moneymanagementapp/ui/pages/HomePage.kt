package com.ndhu.moneymanagementapp.ui.pages

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ndhu.moneymanagementapp.navigation.AppScreens

@Composable
fun HomePage(navController: NavController) {

    val LoginUser = "Daniel"
    val UserMoney = 99999
    val ChosenCurrency = "NTD"

    // List of button data
    val mainFeatures = listOf(
        "Income/Outcome",
        "Split Bill"
    )

    val updatesList = listOf(
        "Today: Cloudy 19°C",
        "Tomorrow: Sunny 22°C"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    )
    {
        Text(text = "Welcome to Homepage Screen!")
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Text(
                text = "Hello ${LoginUser}, Welcome!",
                fontSize = 20.sp,
                modifier = Modifier
            )
            Text(
                text = "$ChosenCurrency $UserMoney",
                color = if (UserMoney >= 0) Color(0xFF4E9968) else Color(0xFFA1291F), // Green or Red
                fontSize = 20.sp,
                modifier = Modifier
            )
        }

        Spacer(modifier = Modifier.height(32.dp)) // Space before the buttons

        // temporary card for split bill (only for testing)
        // TODO: please compose UI according to the agreed concept/design
        Button(onClick = { navigateToSplitBillScreen(navController) } ) {
            Text(text = "Split Bill")
        }

        Button(onClick = { navigateToContactScreen(navController) } ) {
            Text(text = "Contact")
        }

        Button(onClick = { navigateToAccountScreen(navController) } ) {
            Text(text = "Account")
        }

        // LazyColumn for buttons
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp) // Space between buttons
        )
        {
            items(mainFeatures.size) { index ->
                Button(
                    onClick =
                    {

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .border(5.dp, Color.Black, RoundedCornerShape(20.dp)), // Black border
                    shape = RoundedCornerShape(20.dp), // Rounded corners
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White // White background
                    )
                )
                {
                    Text(
                        text = mainFeatures[index],
                        color = Color.Black,
                        fontSize = 20.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp)) // Space before the buttons
        HorizontalDivider(thickness = 2.dp)
        Spacer(modifier = Modifier.height(20.dp)) // Space before the buttons
        Text(
            text = "Information/Updates",
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.height(16.dp))

        // LazyColumn for buttons
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp) // Space between buttons
        )
        {
            items(updatesList.size) { index ->
                Button(
                    onClick =
                    {

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .border(5.dp, Color.Black, RoundedCornerShape(20.dp)), // Black border
                    shape = RoundedCornerShape(20.dp), // Rounded corners
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White // White background
                    )
                )
                {
                    Text(
                        text = updatesList[index],
                        color = Color.Black,
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}


fun navigateToSplitBillScreen(navController: NavController) {
    navController.navigate(route = AppScreens.ListStoresScreen.name)
}

fun navigateToContactScreen(navController: NavController) {
    navController.navigate(route = AppScreens.ContactScreen.name)
}

fun navigateToAccountScreen(navController: NavController) {
    navController.navigate(route = AppScreens.AccountScreen.name)
}