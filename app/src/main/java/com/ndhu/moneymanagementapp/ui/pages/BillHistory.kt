package com.ndhu.moneymanagementapp.ui.pages

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.Timestamp
import com.ndhu.moneymanagementapp.components.SearchBox
import com.ndhu.moneymanagementapp.controllers.SplitBillController
import com.ndhu.moneymanagementapp.controllers.StoreController
import com.ndhu.moneymanagementapp.controllers.UserController
import com.ndhu.moneymanagementapp.enums.AppScreens
import com.ndhu.moneymanagementapp.enums.UserPaymentStatus
import com.ndhu.moneymanagementapp.payloads.ListUserSplitBillResponse
import com.ndhu.moneymanagementapp.sharedviewmodels.SplitBillViewModel
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun BillHistory(context: Context, navController: NavController, viewModel: SplitBillViewModel) {
    val splitBillController = SplitBillController()


    var billResponseList = remember { mutableStateListOf<ListUserSplitBillResponse>() }

    runBlocking {
        Log.d("LaunchTag", "DEBUG. Accessing Bill History.")  // Prints in Logcat

        // clear stores list and add data from database into the stores list
        billResponseList.clear()
        billResponseList.addAll( splitBillController.getLoginUserListOfBills(context) )
        // TODO: add view model if needed
        // viewModel.billHistory = storeController.getListOfStores().toMutableList()

    }

    // Temporary hardcoded list of bill history records
//    val histories = listOf(
//        History("100", "Store W", "2024-03-10", "User 1", "Completed"),
//        History("200", "Store E", "2024-03-12", "User 2", "PENDING"),
//        History("300", "Store S", "2024-03-15", "User 3", "COMPLETED"),
//    )

    // Compose screen UI
    ComposeBillHistoryScreen(context = context, billResponseList = billResponseList, navController = navController, viewModel = viewModel)
}

@Composable
fun ComposeBillHistoryScreen(context: Context, billResponseList: SnapshotStateList<ListUserSplitBillResponse>, navController: NavController, viewModel: SplitBillViewModel) {
    var searchQuery = remember { mutableStateOf("") }

    val filteredHistories = filterHistories(context, billResponseList, searchQuery.value) //  no remember

    Box(modifier = Modifier.fillMaxSize()) {
        // Display background image
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
                // Top bar with title and navigation
                // TODO: search function
                ContentTopBar(navController = navController,
                    topBarText = "Bill History",
                    searchBox = { SearchBox(searchQuery = searchQuery) }
                    )

                // Main content container
                ContactContainer(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Handle empty state
                    if (billResponseList.isEmpty()) {
                        EmptyStateMessage("No bill history available")
                    } else {
                        // Display list of history items
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(filteredHistories) { billResponse ->
                                HistoryItem(context = context, billResponse = billResponse, navController = navController, viewModel = viewModel)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp)) // Bottom space
            }
        }
    }
}



// filter function for search box
fun filterHistories(context: Context, billResponseList: SnapshotStateList<ListUserSplitBillResponse>, query: String): SnapshotStateList<ListUserSplitBillResponse> {
    val result = if (query.isBlank()) {
        billResponseList
    } else {
        billResponseList.filter {

            var storeObject = runBlocking { StoreController().getStore(storeId = it.storeId) }
            var billOwner = runBlocking { UserController().searchUserByUserId(context, it.billOwnerId) }

            storeObject?.name?.contains(query, ignoreCase = true) ?: true ||
                    billOwner?.username?.contains(query, ignoreCase = true) ?: true ||
                    formatTimestamp(it.createdAt).contains(query, ignoreCase = true)
            // || it.status.toString().contains(query, ignoreCase = true) // pointless to search for status
            // we do not implement to price search, too time expensive

        }.toMutableStateList() // Convert result to SnapshotStateList
    }

    // println("Filtered ${result.size} items for query: '$query'") // <-- DEBUG
    return result
}


@Composable
fun HistoryItem(context: Context, billResponse: ListUserSplitBillResponse, navController: NavController, viewModel: SplitBillViewModel) {
    // Change background color based on transaction status
    val backgroundColor = when (billResponse.status) {
        UserPaymentStatus.UNPAID -> Color(0xFFFFC8C8) // Light red for pending
        else -> Color.White // Default white background
    }
// BUG HERE
//    viewModel.currentBillId = billResponse.billId

//    val totalPrice = billResponse.itemList
//        .groupBy { it.id }
//        .map { (_, items) ->
//            val totalQty = items.sumOf { it.qty.toDouble() }
//            val totalPrice = items.firstOrNull()?.price?.toDouble() ?: 0.0
//            val unitPrice = if (totalQty > 0) totalPrice / totalQty else 0.0
//            items.sumOf { it.qty.toDouble() * unitPrice }
//        }.sum()

    // this only calculate debt/price of the current logged in user
    val totalLoginUserDebt = billResponse.itemList.fold(0.0) { acc, item ->
        acc + (item.oneItemPrice * item.userTakenQuantity)
    }

    billResponse.itemList.forEach { item ->
        Log.d("CARD BILL HISTORY", item.toString())
    }


    // History card component
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(backgroundColor),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable {
                // Navigate to bill details
                navController.navigate(route = AppScreens.BillDetailsScreen.name)
                viewModel.currentBillId = billResponse.billId
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left side = store ID and date
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                val store = runBlocking { StoreController().getStore(storeId = billResponse.storeId) }
                if (store != null) {
                    Text(
                        text = store.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = primaryGreen
                    )
                }
                // TODO: date
                var createdAtString = formatTimestamp(billResponse.createdAt)
                Text(
                    text = createdAtString, // billResponse.transactionDate,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF666666)
                )
            }

            // Right side = total bill price and bill owner
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "NTD " + totalLoginUserDebt.toFloat().toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryGreen
                )
                val billOwner = runBlocking { UserController().searchUserByUserId(context = context, userId = billResponse.billOwnerId) }

                if (billOwner != null) {
                    Text(
                        text = billOwner.username, // billResponse.billOwnerId,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.DarkGray
                    )
                }
            }
        }
    }
}

fun formatTimestamp(timestamp: Timestamp): String {
    val date = timestamp.toDate()
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formatter.format(date)
}
