package com.ndhu.moneymanagementapp.ui.oldpages

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun FirstLogin(context :Context, navController: NavController) {
    // Your UI elements go here
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Welcome to First Login Screen!")
        Button(
            onClick = { /* Handle button click */ },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Click Me")
        }
    }
}
