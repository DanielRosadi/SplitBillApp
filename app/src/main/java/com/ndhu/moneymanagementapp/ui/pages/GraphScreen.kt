package com.ndhu.moneymanagementapp.ui.pages

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ndhu.moneymanagementapp.controllers.SplitBillController
import com.ndhu.moneymanagementapp.controllers.UserController
import com.ndhu.moneymanagementapp.enums.UserPaymentStatus
import com.ndhu.moneymanagementapp.graphs.DonutChart
import com.ndhu.moneymanagementapp.graphs.PieChart
import com.ndhu.moneymanagementapp.models.FullBillDetail
import com.ndhu.moneymanagementapp.payloads.ListUserSplitBillResponse
import com.ndhu.moneymanagementapp.sharedviewmodels.SharedViewModel
import kotlinx.coroutines.runBlocking

@Composable
fun GraphingScreen(context: Context, navController: NavController, viewModel: SharedViewModel){

    // GraphExample()

    var splitBillController = SplitBillController()
    var userController = UserController()

    var billResponseList = remember { mutableStateListOf<ListUserSplitBillResponse>() }

    runBlocking {
        Log.d("LaunchTag", "DEBUG. Accessing Debt Listing Page.")  // Prints in Logcat

        // clear stores list and add data from database into the stores list
        billResponseList.clear()
        billResponseList.addAll( splitBillController.getLoginUserListOfBills(context) )
        // TODO: add view model if needed
        // viewModel.billHistory = storeController.getListOfStores().toMutableList()

    }

    var billIdList = billResponseList.map {
        billResponse -> billResponse.billId
    }

    FetchBill(context, billIdList, splitBillController, userController)

    // ComposeDebtListing(context, billIdList, splitBillController, userController)

}

@Composable
fun FetchBill(context: Context, billResponseList: List<String>, splitBillController: SplitBillController, userController: UserController){


    var billDetailsList = remember { mutableStateListOf<FullBillDetail?>(null) }


    runBlocking {
        Log.d("LaunchTag", "DEBUG. Accessing FetchBill().")  // Prints in Logcat

        // clear stores list and add data from database into the stores list
        // billDetail.clear()
        // billDetail.addAll( splitBillController.getLoginUserListOfBills(context) )

        billDetailsList.clear()
        billDetailsList=
            billResponseList.map{
                billId -> splitBillController.getBillDetailGroupedByUser(billId)
            }.toMutableStateList()

        // TODO: add view model if needed
        // viewModel.billHistory = storeController.getListOfStores().toMutableList()
    }

    ComposeDebtListing(context, billDetailsList, splitBillController, userController)
}

@Composable
fun ComposeDebtListing(
    context: Context,
    billDetailsList:  SnapshotStateList<FullBillDetail?>,
    splitBillController: SplitBillController,
    userController: UserController){

    // in float type
    var currentLoggedInUserDebt = currentLoggedInUserDebt(context, billDetailsList, splitBillController, userController)

    ChartDebt(context, billDetailsList, splitBillController, userController, currentLoggedInUserDebt)

}

@Composable
fun ChartDebt(
    context: Context,
    billDetailsList:  SnapshotStateList<FullBillDetail?>,
    splitBillController: SplitBillController,
    userController: UserController,
    currentLoggedInUserDebt: Float
){
    Column (
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val data = listOf(currentLoggedInUserDebt, 2000f, /* 50f*/)
        val colors = listOf(Color.Red, Color.Green, /* Color.Blue */)

        Text("Pie Chart", style = MaterialTheme.typography.titleLarge)
        PieChart(
            entries = data,
            colors = colors,
            modifier = Modifier.size(200.dp)
        )
    }
}

fun currentLoggedInUserDebt(
    context: Context,
    billDetailsList: SnapshotStateList<FullBillDetail?>,
    splitBillController: SplitBillController,
    userController: UserController)
: Float{

    var currentLoggedInUser = runBlocking { userController.getOnboardUser(context) }
    var currentLoggedInUserDebt: Float = 0f

    if (currentLoggedInUser != null) {
        billDetailsList.filter {
            // current user debt only from when current user is not the bill owner
                fullBillDetail ->
            (fullBillDetail?.billOwnerId ?: "NULL ERROR") != currentLoggedInUser.id
        }.forEach {
        filteredBillResponse ->
            if (filteredBillResponse != null) {
                filteredBillResponse.users.forEach{ itemGroup ->
                    if ( itemGroup.userId == currentLoggedInUser.id ) {
                        if (itemGroup.status.value == UserPaymentStatus.UNPAID){
                            currentLoggedInUserDebt += itemGroup.items.fold(0f) { sum, item -> sum + item.oneItemPrice * item.userTakenQuantity }
                        }
                    }
                }
            }
    }
    }

    return currentLoggedInUserDebt

}

@Composable
fun GraphExample(){
    val data = listOf(30f, 20f, 50f)
    val colors = listOf(Color.Red, Color.Green, Color.Blue)

    Column (
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Donut Chart", style = MaterialTheme.typography.titleLarge)
        DonutChart(
            entries = data,
            colors = colors,
            modifier = Modifier.size(200.dp)
        )

        Text("Pie Chart", style = MaterialTheme.typography.titleLarge)
        PieChart(
            entries = data,
            colors = colors,
            modifier = Modifier.size(200.dp)
        )
    }
}