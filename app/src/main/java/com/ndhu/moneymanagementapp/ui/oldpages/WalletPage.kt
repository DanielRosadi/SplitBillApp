package com.ndhu.moneymanagementapp.ui.oldpages

import android.content.Context
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ndhu.moneymanagementapp.navigation.AppScreens

@Composable
fun WalletPage(context : Context) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "Wallet",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Wallet Options Section
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            WalletOption(title = "Cash", value = 9999)
            WalletOption(title = "Chunghwa Post", value = 99999)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Add Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = { /* TODO: Handle add action */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Cyan,
                    contentColor = Color.Black
                ),
                shape = CircleShape,
                modifier = Modifier
                    .width(100.dp)  // Increased width for oval shape
                    .height(40.dp)  // Keep the height smaller
            ) {
                Text(text = "Add", fontSize = 20.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(thickness = 2.dp)
        Spacer(modifier = Modifier.height(16.dp))

        // Report Section
        Text(
            text = "Report this month",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Placeholder for Graph
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            DrawPlaceholderGraph()
        }
    }
}

@Composable
fun WalletOption(title: String, value: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween, // Ensures left and right alignment
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.weight(1f) // Takes up remaining space to push text to the right
        ) {
            Canvas(
                modifier = Modifier
                    .size(64.dp)
                    .padding(8.dp)
            ) {
                drawCircle(
                    color = Color.Black,
                    style = Stroke(2.dp.toPx())
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Normal
            )
        }

        // Value text aligned to the right
        Text(
            text = value.toString(),
            fontSize = 24.sp,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
fun DrawPlaceholderGraph() {
    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {
        // Draw axes
        drawLine(
            color = Color.DarkGray,
            start = Offset(0f, size.height),
            end = Offset(0f, 0f),
            strokeWidth = 4f,
            cap = StrokeCap.Round
        )
        drawLine(
            color = Color.DarkGray,
            start = Offset(0f, size.height),
            end = Offset(size.width, size.height),
            strokeWidth = 4f,
            cap = StrokeCap.Round
        )

        // Draw graph line
        drawLine(
            color = Color.Cyan,
            start = Offset(0f, size.height),
            end = Offset(size.width / 2, size.height / 2),
            strokeWidth = 4f,
            cap = StrokeCap.Round
        )
        drawLine(
            color = Color.Cyan,
            start = Offset(size.width / 2, size.height / 2),
            end = Offset(size.width, size.height / 3),
            strokeWidth = 4f,
            cap = StrokeCap.Round
        )
    }
}

@Composable
fun HomePage(context : Context, navController: NavController) {

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
        Button(onClick = { navController.navigate(route = AppScreens.ListStoresScreen.name) } ) {
            Text(text = "Split Bill")
        }

        Button(onClick = { navController.navigate(route = AppScreens.ContactScreen.name) } ) {
            Text(text = "Contact")
        }

        Button(onClick = { navController.navigate(route = AppScreens.AccountScreen.name) } ) {
            Text(text = "Account")
        }

        Button(onClick = { navController.navigate(route = AppScreens.FirstLoginScreen.name) } ) {
            Text(text = "First LogIn (Temp)")
        }

        Button(onClick = { navController.navigate(route = AppScreens.LogInScreen.name) } ) {
            Text(text = "LogIn (Temp)")
        }

        Button(onClick = { navController.navigate(route = AppScreens.SignUpScreen.name) } ) {
            Text(text = "SignUp (Temp)")
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
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp), // Rounded corners
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
                        .border(
                            5.dp,
                            Color.Black,
                            androidx.compose.foundation.shape.RoundedCornerShape(20.dp)
                        ), // Black border
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp), // Rounded corners
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