package com.ndhu.moneymanagementapp.ui.pages

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ndhu.moneymanagementapp.models.Bill
import com.ndhu.moneymanagementapp.models.BillItemDetail

@Composable
fun BillDetails(context: Context, navController: NavController) {
    ComposeBillDetailsScreen(navController = navController)
}

@Composable
fun ComposeBillDetailsScreen(navController: NavController) {
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

                Spacer(modifier = Modifier.height(32.dp))

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No bill details to display",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Total debt = $0",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA8E6CF)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            ) {
                Text(
                    text = "Completed",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

/*@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BillDetails(context: Context, navController: NavController) {
    var showConfirmationDialog by remember { mutableStateOf(false) }
    var selectedBillId by remember { mutableStateOf<String?>(null) }

//    var billDetails by remember {
//        mutableStateOf(
//            listOf(
//                BillDetails(id = "B001", historyId = "H123", userId = "U456", status = "Paid"),
//                BillDetails(id = "B002", historyId = "H124", userId = "U789", status = "Unpaid"),
//                BillDetails(id = "B003", historyId = "H125", userId = "U321", status = "Paid"),
//            )
//        )
//    }

    var totalNotPaid by remember { mutableStateOf(0) }

//    LaunchedEffect(billDetails) {
//        totalNotPaid = billDetails.count { it.status == "Unpaid" }
//    }

    val billAllPaid = totalNotPaid == 0

//    val billItemDetailsMap = mapOf(
//        "B001" to listOf(
//            BillItemDetails(id = "1", billDetailId = "B001", itemId = "Item001", qty = "2"),
//            BillItemDetails(id = "2", billDetailId = "B001", itemId = "Item002", qty = "1"),
//            BillItemDetails(id = "3", billDetailId = "B001", itemId = "Item003", qty = "5"),
//        ),
//        "B002" to listOf(
//            BillItemDetails(id = "4", billDetailId = "B002", itemId = "Item004", qty = "3"),
//            BillItemDetails(id = "5", billDetailId = "B002", itemId = "Item005", qty = "2"),
//        ),
//        "B003" to listOf(
//            BillItemDetails(id = "6", billDetailId = "B003", itemId = "Item006", qty = "4"),
//            BillItemDetails(id = "7", billDetailId = "B003", itemId = "Item007", qty = "6"),
//            BillItemDetails(id = "8", billDetailId = "B003", itemId = "Item008", qty = "1"),
//        ),
//    )

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

        //In flutter, it is named as listview.builder
//        LazyColumn(modifier = Modifier.fillMaxWidth().padding(bottom = 120.dp)) {
//            items(billDetails) { bill ->
//                val itemsForBill = billItemDetailsMap[bill.id] ?: emptyList()
//                DetailItemByUser(
//                    billDetails = bill,
//                    billItems = itemsForBill,
//                    onStatusClick = { billId ->
//                        selectedBillId = billId
//                        showConfirmationDialog = true
//                    }
//                )
//            }
//        }
    }

    //If confirmation dialog wants to be shown
    if (showConfirmationDialog && selectedBillId != null) {
//        confirmationDialog(
//            onDismiss = { showConfirmationDialog = false },
//            onConfirm = {
//                billDetails = billDetails.map { bill ->
//                    if (bill.id == selectedBillId) {
//                        bill.copy(status = if (bill.status == "Paid") "Unpaid" else "Paid")
//                    } else bill
//                }.toList() // Ensuring a new list is created
//                Log.d("Recalculating", "Recalculating...Total not paid: $totalNotPaid")
//                showConfirmationDialog = false
//            },
//            status = billDetails.firstOrNull { it.id == selectedBillId }?.status == "Paid"
//        )
    }

    //Green button below
    Column(
        modifier = Modifier.fillMaxSize().padding(all=16.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Total debt = \$$totalNotPaid", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors(
                containerColor = if (billAllPaid) Color(0xFFA8E6CF) else Color(0xFFFF8A80)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        ) {
            Text(text = if (billAllPaid) "Completed" else "Not completed yet", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }
}


@Composable
fun DetailItemByUser(
    billDetails: Bill,
    billItems: List<BillItemDetail>,
    onStatusClick: (String) -> Unit
) {
    val itemDetailsText = billItems.joinToString("\n") { "Item ID: ${it.itemId}, Qty: ${it.qty}" }

//    val statusColor = if (billDetails.status.lowercase() == "paid") {
//        Color.hsv(158f, 0.92f, 0.58f) // Green
//    } else {
//        MaterialTheme.colorScheme.error // Red
//    }

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
                Text(text = "User ID: ${billDetails.ownerId}", style = MaterialTheme.typography.titleMedium)
                Text(text = itemDetailsText, style = MaterialTheme.typography.bodyMedium)
            }
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.clickable {
                    onStatusClick(billDetails.id)
                }
            ) {
                Text(
//                    text = billDetails.status,
                    text = "",
                    style = MaterialTheme.typography.titleMedium,
//                    color = statusColor
                )
            }
        }
    }
}

@Composable
fun confirmationDialog(onDismiss: () -> Unit, onConfirm: () -> Unit, status: Boolean) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                if (status)
                    "Do you want to change back to not paid?"
                else
                    "Has this person paid?"
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    Log.w("Info", "Status changed for the paid/ unpaid situation")
                    onConfirm()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.hsv(120f, 0.53f, 0.31f))
            ) { Text("Yes") }
        },
        dismissButton = {
            Button(
                onClick = {
                    Log.w("Info", "User cancelled")
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) { Text("No") }
        }
    )
}*/
