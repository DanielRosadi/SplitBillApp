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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ndhu.moneymanagementapp.components.SearchBox
import com.ndhu.moneymanagementapp.controllers.SplitBillController
import com.ndhu.moneymanagementapp.controllers.UserController
import com.ndhu.moneymanagementapp.enums.UserPaymentStatus
import com.ndhu.moneymanagementapp.models.FullBillDetail
import com.ndhu.moneymanagementapp.models.FullItemDetail
import com.ndhu.moneymanagementapp.models.User
import com.ndhu.moneymanagementapp.models.UserItemGroup
import com.ndhu.moneymanagementapp.sharedviewmodels.SharedViewModel
import kotlinx.coroutines.runBlocking

// Data class representing an item associated with a user
data class UserItemEntry(
    val name: String,
    val quantity: Int,
    val totalPrice: Float
)

// Data class representing the details of a user in a bill
data class BillUserDetail(
    val username: String,
    val amount: Double,
    val paid: Boolean,
    val items: List<UserItemEntry>
)

@Composable
fun BillDetails(context: Context, navController: NavController, viewModel: SharedViewModel) {
    val splitBillController = SplitBillController()

    // var billDetail = remember { mutableStateOf<FullBillDetail?>(null) }

    var billDetail = remember { mutableStateOf<FullBillDetail?>(null) }


    runBlocking {
        Log.d("LaunchTag", "DEBUG. Accessing Bill Details.")  // Prints in Logcat

        // clear stores list and add data from database into the stores list
        // billDetail.clear()
        // billDetail.addAll( splitBillController.getLoginUserListOfBills(context) )

        billDetail.value = splitBillController.getBillDetailGroupedByUser(viewModel.currentBillId)

        // TODO: add view model if needed
        // viewModel.billHistory = storeController.getListOfStores().toMutableList()
    }

    // Sample hardcoded bill details for demonstration purposes
//    val sampleDetails = listOf(
//        BillUserDetail("User 1", 150.0, true, listOf(
//            UserItemEntry("Burger", 2, 10.0f),
//            UserItemEntry("Soda", 1, 4.0f)
//        )),
//        BillUserDetail("User 3", 200.0, false, listOf(
//            UserItemEntry("Pizza", 2, 15.0f),
//            UserItemEntry("Fries", 1, 5.0f)
//        )),
//        BillUserDetail("User 5", 100.0, true, listOf(
//            UserItemEntry("Noodles", 1, 6.0f),
//            UserItemEntry("Water", 2, 4.0f)
//        ))
//    )

    val totalItemQuantities = mutableMapOf<String, Float>()

    billDetail.value?.users?.forEach { user ->
        user.items.forEach { item ->
            val current = totalItemQuantities[item.id] ?: 0f
            totalItemQuantities[item.id] = current + item.userTakenQuantity
        }
    }


    // TODO: calculation time expensive due to hot-fix, need to refactor backend data classes
    // Calculate total unpaid amount
    val unpaidUsers = billDetail.value?.users?.filter { it.status.value == UserPaymentStatus.UNPAID }
    val totalUnpaid = unpaidUsers?.fold(0f) {
            sum, userItemGroup -> sum + (userItemGroup.items.fold(0f){
            innerSum, item ->
        var itemTotalQuantity = totalItemQuantities[item.id]
        if (itemTotalQuantity != null && itemTotalQuantity != 0f) {
            innerSum + (item.oneItemPrice * item.userTakenQuantity)
        }
        else{
            0f
        }
    })
    }

    // Check if all users have paid
    val allPaid = billDetail.value?.users?.all { it.status.value == UserPaymentStatus.PAID }

    // Compose screen UI with calculated values and user data
    ComposeBillDetailsScreen(
        context = context,
        billDetails = billDetail,
        totalUnpaid = totalUnpaid,
        allPaid = allPaid,
        navController = navController,
        totalItemQuantities = totalItemQuantities,
        splitBillController = splitBillController,
        viewModel = viewModel
    )
}

@Composable
fun ComposeBillDetailsScreen(
    context: Context,
    billDetails: MutableState<FullBillDetail?>,
    totalUnpaid: Float?,
    allPaid: Boolean?,
    navController: NavController,
    totalItemQuantities: MutableMap<String, Float>,
    splitBillController: SplitBillController,
    viewModel: SharedViewModel
) {
    var searchQuery = remember { mutableStateOf("") }


    // var filteredDetails = listOf(UserItemGroup("0000000", UserPaymentStatus.PAID, listOf(ItemDetail())))
    var filteredDetails = filterDetails(context, billDetails.value!!.users, searchQuery.value)



    Box(modifier = Modifier.fillMaxSize()) {
        BackgroundImage()

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(30.dp))
            TitleText()
            Spacer(modifier = Modifier.height(30.dp))

            // Container with rounded corners and background
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(Color(0xFFD9D9D9))
                    .padding(10.dp)
            ) {
                // Top navigation bar with a label
                ContentTopBar(navController = navController,
                    topBarText = "Bill Details",
                    searchBox = { SearchBox(searchQuery = searchQuery) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Scrollable list of user bill cards
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    billDetails.value?.let {
                        items(filteredDetails) { user ->
                            UserBillCard(context, billDetails, user, totalItemQuantities, splitBillController)
                        }
                    }
                }

                // Bottom section: show total unpaid and overall status
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Total not paid = NTD ${"%.2f".format(totalUnpaid)}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Status button shows "Pending" or "Completed" depending on payment status
                    Button(
                        onClick = {}, // Placeholder action
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (allPaid == true) Color(0xFFA8E6CF) else Color(0xFFFFC8C8)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = if (allPaid == true) "Completed" else "Pending",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

fun filterDetails(
    context: Context,
    users: List<UserItemGroup>,
    query: String
): List<UserItemGroup> {
    // TODO: search item price, item quantity, and total price may not work

    val queryLower = query.trim().lowercase()
    val queryNumber = query.toFloatOrNull()

    return if (queryLower.isBlank()) {
        users
    } else {
        users.mapNotNull { user ->
            val userObject = runBlocking { UserController().searchUserByUserId(context, user.userId) }
            val matchesUsername = userObject?.username?.contains(queryLower, ignoreCase = true) == true

            val totalPrice = user.items.fold(0f) { sum, item -> sum + item.oneItemPrice }
            val matchesTotalPrice = queryNumber != null && totalPrice == queryNumber

            val matchesItem = user.items.any { item ->
                item.name.contains(queryLower, ignoreCase = true) ||
                        (queryNumber != null && (item.userTakenQuantity == queryNumber || item.oneItemPrice == queryNumber))
            }

            if (matchesUsername || matchesItem || matchesTotalPrice) {
                user // include this user in the result
            } else {
                null // filter out
            }
        }
    }
}


@Composable
fun UserBillCard(context: Context,
                 billDetails: MutableState<FullBillDetail?>,
                 user: UserItemGroup,
                 totalItemQuantities: MutableMap<String, Float>,
                 splitBillController: SplitBillController) {
    // Change background and text colors based on payment status
    val backgroundColor = if (user.status.value == UserPaymentStatus.PAID) Color.White else Color(0xFFFFE6E6)
    val statusColor = if (user.status.value == UserPaymentStatus.PAID) Color(0xFF388E3C) else Color.Red

    // Add to top of UserBillCard, near other states
    val userStatus = remember { mutableStateOf(user.status) } // <-- CHANGES
    val statusText = remember { mutableStateOf(if (userStatus.value.value == UserPaymentStatus.PAID) "Paid" else "Not Paid") } // <-- CHANGES

    billDetails.value?.billId.toString().let { Log.d("DEBUG BD CARD", it) }
    billDetails.value?.users?.forEach{ item ->
        Log.d("DEBUG BD CARD", item.toString())
    }
    Log.d("DEBUG BD CARD", "\n\n\n")


    // TODO: calculation time expensive due to hot-fix, need to refactor backend data classes
    // var userTotal = remember { mutableStateOf(0f) }
    // complete the totalQuality and user price share
    var completeItemDetail = user.items.map { itemDetail ->

        var itemTotalQuantity = totalItemQuantities[itemDetail.id]

        if (itemTotalQuantity != null && itemTotalQuantity != 0f){
            FullItemDetail(
                itemId = itemDetail.id,
                itemName = itemDetail.name,
                userPriceShare = itemDetail.oneItemPrice * itemDetail.userTakenQuantity,
                userItemQuantity = itemDetail.userTakenQuantity,
                itemTotalQuantity = itemTotalQuantity
            )
        }
        else{
            FullItemDetail(
                itemId = "",
                itemName = "ITEM ERROR",
                userPriceShare = 0f,
                userItemQuantity = 0f,
                itemTotalQuantity = 0f
            )
        }
    }

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

            // Row with user name, amount, and payment status
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                var userObject: User?

                runBlocking { userObject = UserController().searchUserByUserId(context = context, userId = user.userId) }

                userObject?.let {
                    Text(
                        text = it.username,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF388E3C),
                        modifier = Modifier.weight(1f)
                    )
                }

                Text(
                    // text = "$${"%.2f".format(user.items.fold(0f) {sum, item -> sum + item.price/item.qty })}",
                    text = "NTD ${"%.2f".format(completeItemDetail.fold(0f) {sum, item -> sum + item.userPriceShare})}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )

                var loggedInUser: User?

                runBlocking { loggedInUser = UserController().getOnboardUser(context) }


                Text(
                    text = statusText.value,
                    color = if (userStatus.value.value == UserPaymentStatus.PAID) Color(0xFF388E3C) else Color.Red, // <-- CHANGES
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f).clickable {

                        if ( (billDetails.value?.billOwnerId ?: "BILL DETAILS ERROR") == (loggedInUser?.id ?: "LOGGED IN USER ERROR") ) {
                            val currentUserId = user.userId
                            val currentGroup =
                                billDetails.value?.users?.find { it.userId == currentUserId }

                            if (currentGroup != null) {
                                val newStatus =
                                    if (currentGroup.status.value == UserPaymentStatus.PAID) {
                                        userStatus.value.value =
                                            UserPaymentStatus.UNPAID        // <-- CHANGES
                                        statusText.value =
                                            "Not Paid"                            // <-- CHANGES
                                        UserPaymentStatus.UNPAID
                                    } else {
                                        userStatus.value.value =
                                            UserPaymentStatus.PAID          // <-- CHANGES
                                        statusText.value =
                                            "Paid"                                // <-- CHANGES
                                        UserPaymentStatus.PAID
                                    }

                                currentGroup.status.value = newStatus

                                runBlocking {
                                    splitBillController.updateStatus(
                                        billId = billDetails.value!!.billId,
                                        userId = currentUserId,
                                        newStatus = newStatus
                                    )
                                }
                            }

                        }


                    },
                    textAlign = TextAlign.End
                )

            }

            // Divider line between user info and item list
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                thickness = 1.dp,
                color = Color(0xFFCCCCCC)
            )

            // List of purchased items
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                completeItemDetail.forEach { item ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item.itemName,
                            fontSize = 13.sp,
                            color = Color.DarkGray,
                            modifier = Modifier.weight(1f)
                        )

                        Text(
                            text = "x${item.userItemQuantity}",
                            fontSize = 13.sp,
                            color = Color.DarkGray,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1f)
                        )

//                        var itemTotalQuantity = totalItemQuantities[item.id]
//                        var pricePerUnit = if (itemTotalQuantity != null && itemTotalQuantity != 0f) {
//                            item.price / itemTotalQuantity
//                        } else {
//                            0f
//                        }
//                        var userShare = pricePerUnit * item.qty

                        Text(
                            text = "NTD ${"%.2f".format(item.userPriceShare)}",
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