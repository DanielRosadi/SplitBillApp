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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ndhu.moneymanagementapp.enums.AppScreens
import com.ndhu.moneymanagementapp.models.History

@Composable
fun BillHistory(context: Context, navController: NavController) {
    // Simulated data — you can replace this with a ViewModel or DB call later
    val histories = listOf(
        History("100", "Store W", "2024-03-10", "User 1", "Completed"),
        History("200", "Store E", "2024-03-12", "User 2", "PENDING"),
        History("300", "Store S", "2024-03-15", "User 3", "COMPLETED"),
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
            verticalArrangement = Arrangement.Center,
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
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
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
    // Determine card background color based on status
    val backgroundColor = when (history.status.uppercase()) {
        "PENDING" -> Color(0xFFFFC8C8) // Red-ish
        else -> Color.White // Default card
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(backgroundColor),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable {
                navController.navigate(route = AppScreens.BillDetailsScreen.name)
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Store info
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = history.storeId,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryGreen
                )
                Text(
                    text = history.transactionDate,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF666666)
                )
            }

            // Amount + Owner
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "$ ${history.id}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryGreen
                )
                Text(
                    text = history.billOwnerId,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.DarkGray
                )
            }
        }
    }
}
