package com.ndhu.moneymanagementapp.ui.pages

import android.content.Context
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

// Example data class (for clarity only)
data class UserItemEntry(
    val name: String,
    val quantity: Int,
    val totalPrice: Float
)

data class BillUserDetail(
    val username: String,
    val amount: Double,
    val paid: Boolean,
    val items: List<UserItemEntry>
)

@Composable
fun BillDetails(context: Context, navController: NavController) {
    val sampleDetails = listOf(
        BillUserDetail("User 1", 150.0, true, listOf(
            UserItemEntry("Burger", 2, 10.0f),
            UserItemEntry("Soda", 1, 4.0f)
        )),
        BillUserDetail("User 3", 200.0, false, listOf(
            UserItemEntry("Pizza", 2, 15.0f),
            UserItemEntry("Fries", 1, 5.0f)
        )),
        BillUserDetail("User 5", 100.0, true, listOf(
            UserItemEntry("Noodles", 1, 6.0f),
            UserItemEntry("Water", 2, 4.0f)
        ))
    )

    val totalUnpaid = sampleDetails.filter { !it.paid }.sumOf { it.amount }
    val allPaid = sampleDetails.all { it.paid }

    ComposeBillDetailsScreen(
        userDetails = sampleDetails,
        totalUnpaid = totalUnpaid,
        allPaid = allPaid,
        navController = navController
    )
}

@Composable
fun ComposeBillDetailsScreen(
    userDetails: List<BillUserDetail>,
    totalUnpaid: Double,
    allPaid: Boolean,
    navController: NavController
) {
    Box(modifier = Modifier.fillMaxSize()) {
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
                ContentTopBar(navController = navController, topBarText = "Bill Details")

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    items(userDetails) { user ->
                        UserBillCard(user)
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Total not paid = $${"%.2f".format(totalUnpaid)}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (allPaid) Color(0xFFA8E6CF) else Color(0xFFFFC8C8)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = if (allPaid) "Completed" else "Pending",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun UserBillCard(user: BillUserDetail) {
    val backgroundColor = if (user.paid) Color.White else Color(0xFFFFE6E6)
    val statusColor = if (user.paid) Color(0xFF388E3C) else Color.Red
    val statusText = if (user.paid) "Paid" else "Not Paid"

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 100.dp)
            .padding(horizontal = 16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Top row: Username - Amount - Status (centered)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = user.username,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF388E3C),
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = "$${"%.2f".format(user.amount)}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = statusText,
                    color = statusColor,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End
                )
            }

            // Divider between user summary and item list
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                thickness = 1.dp,
                color = Color(0xFFCCCCCC) // light gray
            )

            // Item List
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                user.items.forEach { item ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item.name,
                            fontSize = 13.sp,
                            color = Color.DarkGray,
                            modifier = Modifier.weight(1f)
                        )

                        Text(
                            text = "x${item.quantity}",
                            fontSize = 13.sp,
                            color = Color.DarkGray,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1f)
                        )

                        Text(
                            text = "$${"%.2f".format(item.totalPrice)}",
                            fontSize = 13.sp,
                            color = Color.Black,
                            textAlign = TextAlign.End,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}



