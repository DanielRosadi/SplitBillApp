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
import com.ndhu.moneymanagementapp.models.Bill
import com.ndhu.moneymanagementapp.models.BillItemDetails

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BillDetails(context: Context, navController: NavController) {


    val billDetails = listOf(
        Bill(id = "B001", history_id = "H123", user_id = "U456", status = "Paid"),
        Bill(id = "B002", history_id = "H124", user_id = "U789", status = "Unpaid"),
        Bill(id = "B003", history_id = "H125", user_id = "U321", status = "Paid"),
    )


    val billItemDetailsMap = mapOf(
        "B001" to listOf(
            BillItemDetails(id = "1", bill_detail_id = "B001", item_id = "Item001", qty = "2"),
            BillItemDetails(id = "2", bill_detail_id = "B001", item_id = "Item002", qty = "1"),
            BillItemDetails(id = "3", bill_detail_id = "B001", item_id = "Item003", qty = "5"),
        ),
        "B002" to listOf(
            BillItemDetails(id = "4", bill_detail_id = "B002", item_id = "Item004", qty = "3"),
            BillItemDetails(id = "5", bill_detail_id = "B002", item_id = "Item005", qty = "2"),
        ),
        "B003" to listOf(
            BillItemDetails(id = "6", bill_detail_id = "B003", item_id = "Item006", qty = "4"),
            BillItemDetails(id = "7", bill_detail_id = "B003", item_id = "Item007", qty = "6"),
            BillItemDetails(id = "8", bill_detail_id = "B003", item_id = "Item008", qty = "1"),
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
            // Section Title for Bill Items
            item {
                Text(
                    text = "Bill Items",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // Display multiple Bill details with respective items
            items(billDetails) { bill ->
                val itemsForBill = billItemDetailsMap[bill.id] ?: emptyList()
                DetailItemByUser(bill, itemsForBill)
            }
        }
    }
}

@Composable
fun DetailItemByUser(bill: Bill, billItems: List<BillItemDetails>) {
    val itemDetailsText = billItems.joinToString("\n") { "Item ID: ${it.item_id}, Qty: ${it.qty}" }

    val statusColor = if (bill.status.lowercase() == "paid") {
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
                Text(text = "User ID: ${bill.user_id}", style = MaterialTheme.typography.titleMedium)
                Text(text = itemDetailsText, style = MaterialTheme.typography.bodyMedium)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = bill.status,
                    style = MaterialTheme.typography.titleMedium,
                    color = statusColor
                )
            }
        }
    }
}