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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ndhu.moneymanagementapp.models.BillDetails
import com.ndhu.moneymanagementapp.models.BillItemDetails

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BillDetails(context: Context, navController: NavController) {


    val billDetails = listOf(
        BillDetails(id = "B001", historyId = "H123", userId = "U456", status = "Paid"),
        BillDetails(id = "B002", historyId = "H124", userId = "U789", status = "Unpaid"),
        BillDetails(id = "B003", historyId = "H125", userId = "U321", status = "Paid"),
    )


    val billItemDetailsMap = mapOf(
        "B001" to listOf(
            BillItemDetails(id = "1", billDetailId = "B001", itemId = "Item001", qty = "2"),
            BillItemDetails(id = "2", billDetailId = "B001", itemId = "Item002", qty = "1"),
            BillItemDetails(id = "3", billDetailId = "B001", itemId = "Item003", qty = "5"),
        ),
        "B002" to listOf(
            BillItemDetails(id = "4", billDetailId = "B002", itemId = "Item004", qty = "3"),
            BillItemDetails(id = "5", billDetailId = "B002", itemId = "Item005", qty = "2"),
        ),
        "B003" to listOf(
            BillItemDetails(id = "6", billDetailId = "B003", itemId = "Item006", qty = "4"),
            BillItemDetails(id = "7", billDetailId = "B003", itemId = "Item007", qty = "6"),
            BillItemDetails(id = "8", billDetailId = "B003", itemId = "Item008", qty = "1"),
        ),
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

            Text(text = "Bill Details", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        )
        {
            Text(text = "\${Store}", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text(text = "\${TotalPrice}", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(billDetails) { bill ->
                val itemsForBill = billItemDetailsMap[bill.id] ?: emptyList()
                DetailItemByUser(bill, itemsForBill)
            }
        }
    }
}

@Composable
fun DetailItemByUser(billDetails: BillDetails, billItems: List<BillItemDetails>) {
    val itemDetailsText = billItems.joinToString("\n") { "Item ID: ${it.itemId}, Qty: ${it.qty}" }

    val statusColor = if (billDetails.status.lowercase() == "paid") {
        Color.hsv(158f, 0.92f, 0.58f) // Green
    } else {
        MaterialTheme.colorScheme.error // Red
    }

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
                Text(text = "User ID: ${billDetails.userId}", style = MaterialTheme.typography.titleMedium)
                Text(text = itemDetailsText, style = MaterialTheme.typography.bodyMedium)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = billDetails.status,
                    style = MaterialTheme.typography.titleMedium,
                    color = statusColor
                )
            }
        }
    }
}