package com.ndhu.moneymanagementapp.ui.pages

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ndhu.moneymanagementapp.enums.AppScreens
import com.ndhu.moneymanagementapp.models.History

@Composable
fun BillHistory(context: Context, navController: NavController) {
    // Simulated data — you can replace this with a ViewModel or DB call later
    val histories = listOf(
        History("100", "Store W", "2024-03-10", "User 1", "Completed"),
        History("200", "Store E", "2024-03-12", "User 2", "Pending"),
        History("300", "Store S", "2024-03-15", "User 3", "Completed"),
        History("400", "Store P", "2024-03-18", "User 1", "Completed"),
        History("500", "Store L", "2024-03-20", "User 4", "Pending"),
        History("600", "Store I", "2024-03-22", "User456", "Pending"),
        History("700", "Store T", "2024-03-25", "User456", "Completed"),
        History("800", "Telco456", "2024-03-28", "User456", "Completed"),
        History("900", "Stream999", "2024-03-30", "User456", "Completed"),
        History("1000", "Clinic123", "2024-04-01", "User456", "Pending")
    )

    // val histories = emptyList<History>()

    ComposeBillHistoryScreen(histories = histories, navController = navController)
}

@Composable
fun ComposeBillHistoryScreen(histories: List<History>, navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Background
        BackgroundImage()

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(30.dp))
            TitleText()
            Spacer(modifier = Modifier.height(30.dp))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(Color(0xFFD9D9D9))
                    .padding(10.dp)
            ) {
                ContentTopBar(navController = navController, topBarText = "Bill History",)

                ContactContainer(
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (histories.isEmpty()) {
                        EmptyStateMessage("No bill history available")
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(histories) { history ->
                                HistoryItem(history = history, navController = navController)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun HistoryItem(history: History, navController: NavController) {
    val backgroundColor = when (history.status) {
        "Completed" -> CompletedCardColor
        // "Pending" -> PendingCardColor
        "Pending", "Cancelled" -> RejectedCardColor
        else -> Color.White
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                navController.navigate(route = AppScreens.BillDetailsScreen.name)
            },
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = history.storeId,
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = history.transactionDate,
                    color = TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = history.billOwnerId,
                    color = TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "$ ${history.id}",
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = history.status,
                    color = TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}


/*@Composable
fun HistoryItem(history: History, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable{
                navController.navigate(route = AppScreens.BillDetailsScreen.name)
            },
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        //colors = CardDefaults.cardColors(Color(0xFFD0F2E0)), // green
        //colors = CardDefaults.cardColors(Color(0xFFFFE6B3)), // orange
        //colors = CardDefaults.cardColors(Color(0xFFF8C7C7)), // red

        // Dark Version
        //colors = CardDefaults.cardColors(Color(0xFFB8EACF)), // green
        //colors = CardDefaults.cardColors(Color(0xFFF8C7C7)), // orange
        //colors = CardDefaults.cardColors(Color(0xFFF8C7C7)), // red
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.Start) {
                Text(text = history.storeId, style = MaterialTheme.typography.titleMedium)
                Text(text = history.transactionDate, style = MaterialTheme.typography.bodyMedium)
            }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = history.billOwnerId , style = MaterialTheme.typography.titleMedium)
            }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.End) {
                Text(text = "$ ${history.id}", style = MaterialTheme.typography.titleMedium)//TODO: Change to price
                Text(text = history.status, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}*/

/*@Composable
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
                HistoryItem(history,navController)
            }
        }
    }
}*/
