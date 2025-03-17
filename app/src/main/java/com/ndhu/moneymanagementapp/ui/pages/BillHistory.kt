package com.ndhu.moneymanagementapp.ui.pages

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ndhu.moneymanagementapp.models.History
import com.ndhu.moneymanagementapp.navigation.AppScreens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BillHistory(context: Context, navController: NavController) {
    val histories = listOf(
        History("1", "Store123", "2024-03-10", "User456", "Completed"),
        History("2", "Utility789", "2024-03-12", "User456", "Pending"),
        History("3", "ISP001", "2024-03-15", "User456", "Completed"),
        History("4", "DineOut456", "2024-03-18", "User456", "Completed"),
        History("5", "FitClub789", "2024-03-20", "User456", "Completed"),
        History("6", "Shop123", "2024-03-22", "User456", "Pending"),
        History("7", "FuelStation001", "2024-03-25", "User456", "Completed"),
        History("8", "Telco456", "2024-03-28", "User456", "Completed"),
        History("9", "Stream999", "2024-03-30", "User456", "Completed"),
        History("10", "Clinic123", "2024-04-01", "User456", "Pending")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 56.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = { navController.popBackStack() }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(text = "Bill History", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier=Modifier.height(30.dp))

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(histories) { history ->
                HistoryItem(history)
            }
        }
    }
}

@Composable
fun HistoryItem(history: History) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = history.storeId, style = MaterialTheme.typography.titleMedium)
                Text(text = history.billOwnerId , style = MaterialTheme.typography.bodyMedium)
                Text(text = history.transactionDate, style = MaterialTheme.typography.bodySmall)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(text = history.id, style = MaterialTheme.typography.titleMedium)//TODO: Change to price
                Text(text = history.status, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}