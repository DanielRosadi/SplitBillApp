package com.ndhu.moneymanagementapp.ui.pages

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.ndhu.moneymanagementapp.components.SearchBox
import com.ndhu.moneymanagementapp.controllers.SplitBillController
import com.ndhu.moneymanagementapp.controllers.UserController
import com.ndhu.moneymanagementapp.enums.UserPaymentStatus
import com.ndhu.moneymanagementapp.graphs.DonutChart
import com.ndhu.moneymanagementapp.graphs.PieChart
import com.ndhu.moneymanagementapp.graphs.TwoSlicePieChart
import com.ndhu.moneymanagementapp.models.FullBillDetail
import com.ndhu.moneymanagementapp.payloads.ListUserSplitBillResponse
import com.ndhu.moneymanagementapp.sharedviewmodels.SharedViewModel
import kotlinx.coroutines.runBlocking

@Composable
fun DebtListingScreen(context: Context, navController: NavController, viewModel: SharedViewModel){

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

    FetchBill(context, navController, billIdList, splitBillController, userController)

    // ComposeDebtListing(context, billIdList, splitBillController, userController)

}

@Composable
fun FetchBill(context: Context, navController: NavController, billResponseList: List<String>, splitBillController: SplitBillController, userController: UserController){


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

    ComposeDebtListing(context, navController , billDetailsList, splitBillController, userController)
}

@Composable
fun ComposeDebtListing(
    context: Context,
    navController: NavController,
    billDetailsList:  SnapshotStateList<FullBillDetail?>,
    splitBillController: SplitBillController,
    userController: UserController
) {

    // in float type
    var currentLoggedInUserDebt =
        currentLoggedInUserDebt(context, billDetailsList, splitBillController, userController)

    var searchQuery = remember { mutableStateOf("") }

    val filteredDebts =
    // val filteredDebts = filterStores(data, searchQuery.value) //  no remember


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        // Background Image
        BackgroundImage()

        // Full Page
        Column(
            modifier = Modifier
                .fillMaxWidth()
            //.fillMaxHeight()
            // .padding(paddingValues)
            //.padding(10.dp)
            ,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(30.dp))

            TitleText()

            Spacer(modifier = Modifier.height(30.dp))

            ChartDebt(
                context,
                Modifier.weight(1f).padding(16.dp),
                billDetailsList,
                splitBillController,
                userController,
                currentLoggedInUserDebt
            )

            // White Box
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    //.fillMaxHeight()
                    .weight(1f)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(Color(0xFFD9D9D9))
                    .padding(10.dp)
            )
            {
                // Content Top Bar
                // TODO: search func
                ContentTopBar(navController = navController,
                    topBarText = "Debt Listing",
                    // searchBox = { SearchBox(searchQuery = searchQuery) }
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(7f)
                        .clip(RoundedCornerShape(20.dp))
                        .background(darkGreyContainer)

                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentPadding = PaddingValues(5.dp)
                    ) {
                        items(listOf(10f, 20f, -30f, -50f, 70f)) { debt ->
                            ListDebt(context, navController , debt, splitBillController, userController)
                        }
                    }

                }

                Spacer(modifier = Modifier.height(15.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        //.fillMaxHeight()
                        .weight(1f)
                        .padding(2.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // if any, bottomPage

                }



            }

        }

    }
}

@Composable
fun ListDebt(
    context: Context,
    navController: NavController,
    debt:  Float,
    splitBillController: SplitBillController,
    userController: UserController
){

}

@Composable
fun ChartDebt(
    context: Context,
    modifier: Modifier,
    billDetailsList:  SnapshotStateList<FullBillDetail?>,
    splitBillController: SplitBillController,
    userController: UserController,
    currentLoggedInUserDebt: Float
){
    Column (
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val oldLightRedDebtScreen = Color(0xfffd594d)
        val oldLightGreenDebtScreen = Color(0xff85d479)

        val lightRedDebtScreen = Color(0xFFF44336)
        val lightGreenDebtScreen = Color(0xFF4CAF50)

        // TODO: currentLoggedInUserDebt is not correctly fetched?
        // val data = listOf(currentLoggedInUserDebt, 2000f, /* 50f*/)
        // /Log.d("Debt Screen", currentLoggedInUserDebt)

        // val data = listOf(30f, 20f)
        var toGet = 20f
        var toPay = 30f
        val colors = listOf(lightRedDebtScreen, lightGreenDebtScreen)
        // val colors = listOf(Color.Red, Color.Green, /* Color.Blue */)

        Text("Debt Chart", style = MaterialTheme.typography.titleLarge)
        TwoSlicePieChart(
            toGet = toGet,
            toPay = toPay,
            modifier = Modifier.size(200.dp),
            getColor = lightGreenDebtScreen, // green
            payColor = lightRedDebtScreen // red
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