package com.ndhu.moneymanagementapp.ui.pages

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
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
import com.ndhu.moneymanagementapp.controllers.UserController
import com.ndhu.moneymanagementapp.enums.AppScreens
import com.ndhu.moneymanagementapp.models.Store
import com.ndhu.moneymanagementapp.models.User
import com.ndhu.moneymanagementapp.sharedviewmodels.SharedViewModel
import com.ndhu.moneymanagementapp.utils.Util
import kotlinx.coroutines.runBlocking

@Composable
fun SelectBillOwnerScreen(context: Context, navController: NavController, viewModel: SharedViewModel){
    var userController = UserController()

    // get friend list from previous page
    var listContacts = viewModel.getFriendList()

    // add the user himself/herself
    var listPossibleOwners = mutableListOf(runBlocking { userController.getOnboardUser(context) } )

    // populate listBillOwners with user's contacts
    if (listContacts != null) {
        for (contact in listContacts){
            val currentUserId = remember { Util.getUserId(context).toString() }
            val friendUserId = if (contact.senderId == currentUserId) contact.receiverId else contact.senderId

            runBlocking {
                listPossibleOwners.add(
                    userController.searchUserByUserId(context, friendUserId)
                )
            }
        }
    }

    // val listBillOwners = List(17) { "User ${it + 1}" } // Example: 11 items

    ComposeSelectBillOwner(context, listPossibleOwners, navController, viewModel)
}

@Composable
fun ComposeSelectBillOwner(context: Context, listPossibleOwners: MutableList<User?>, navController: NavController, viewModel: SharedViewModel) {
    var selectedIndex by remember { mutableStateOf(-1) }

    var searchQuery = remember { mutableStateOf("") }

    val filteredBillOwner = filterBillOwners(listPossibleOwners, searchQuery.value) //  no remember

    // chunked groups items into sets of 3 for each row
    // it also automatically handles when the list is not perfectly divisible by 3
    // val rows = listPossibleOwners.chunked(3)
    val rows = filteredBillOwner.chunked(3)

    //UniformDesignScaffold(
//        topAppBar = {
//            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
//                IconButton(onClick = { navController.popBackStack() }) {
//                    Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
//                }
//                Spacer(modifier = Modifier.width(50.dp))
//                Text("Select Bill Owner", fontSize = 30.sp, fontWeight = FontWeight.Bold)
//            }
//        },

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


            // White Box
        //body = {paddingValues->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .background(Color(0xFFD9D9D9))
                .padding(10.dp)
                //.padding(paddingValues)
                //.padding(10.dp),
            //verticalArrangement = Arrangement.Center,
            //horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Spacer(modifier = Modifier.height(10.dp))

            // Content Top Bar
            // TODO: search func
            ContentTopBar(navController = navController,
                topBarText = "Select Bill Owner",
                searchBox = { SearchBox(searchQuery = searchQuery) }
                )


            // Spacer(modifier = Modifier.height(10.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(7f)
                    .clip(RoundedCornerShape(20.dp))
                    //.background(darkGrey)
                    .background(Color(0xFFD9D9D9))

            ) {

                LazyColumn(modifier = Modifier.weight(1f), contentPadding = PaddingValues(5.dp)) {

                    itemsIndexed(rows) { rowIndex, rowItems ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            // iterate each item in list of 3 item
                            rowItems.forEachIndexed { columnIndex, user ->
                                // calculate globalIndex (original index from list of users/owners)
                                val globalIndex = rowIndex * 3 + columnIndex

                                if (user != null) {
                                    ListBillOwners(
                                        billOwner = user.username,
                                        isSelected = selectedIndex == globalIndex,
                                        onClick = {
                                            selectedIndex =
                                                if (selectedIndex == globalIndex) -1 else globalIndex
                                        } /* toggle logic */)
                                }
                            }
                            // Add empty spaces for the last row if it has less than 3 items
                            // need to add empty spaces for the remaining non-existing elements
                            // otherwise, Compose will make default Row alignment/behavior, which does not actually align like the other
                            // perfectly sets of 3 items (maintaining 3 column structure)
                            repeat(3 - rowItems.size) {
                                Spacer(modifier = Modifier.size(115.dp))
                            }
                        }

                    }

                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    //.fillMaxHeight()
                    .weight(1f)
                    .padding(2.dp)
                ,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

            Button(
                onClick = {
                    val selectedUser =
                        listPossibleOwners.getOrNull(selectedIndex) // take user which has been chosen as bill owner

                    if (selectedUser != null) {
                        // save bill owner data for the bill
                        viewModel.setBillOwner(selectedUser)
                        // save bill participants (bill owner + chosen friends/contacts (i.e possible owners/owner candidates)
                        viewModel.billParticipants = listPossibleOwners
                        // navigate to next page
                        navController.navigate(route = AppScreens.AssignSplitsScreen.name)
                    } else {
                        // feedback when user forgets to select a bill owner
                        Toast.makeText(
                            context,
                            "Please select a user for Bill Owner",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    //.height(60.dp)
                    .fillMaxHeight()
                ,
                shape = RoundedCornerShape(18.dp),
                colors = ButtonColors(
                    containerColor = primaryGreen,
                    contentColor = Color.White,
                    disabledContainerColor = Color.Gray,
                    disabledContentColor = Color.White
                )
            ) {
                Text(text = "Done",
                    fontSize = 24.sp,
                    //fontFamily = surannaFont,
            )
            }


        }
            Spacer(modifier = Modifier.height(10.dp))

        }

    }

}
        //}

    //)
}



// filter function for search box
fun filterBillOwners(billOwnerList: MutableList<User?>, query: String): MutableList<User?> {
    val result = if (query.isBlank()) {
        billOwnerList
    } else {
        billOwnerList.filter {
            it?.username?.contains(query, ignoreCase = true) ?: false
        }.toMutableList() // Convert result to SnapshotStateList
    }

    // println("Filtered ${result.size} items for query: '$query'") // <-- DEBUG
    return result
}

@Composable
fun ListBillOwners(billOwner: String, isSelected: Boolean, onClick: ()-> Unit) {

    // val correctTransparentGreen = Color(0xAA4CAF50)
    // val backgroundColor = if (isSelected) Color.Red else Color.White
    val backgroundColor = if (isSelected) overlayColor else Color.White
    // val borderColor = if (isSelected) Color.Red else Color.Gray
    // val borderColor = if (isSelected) correctTransparentGreen else Color.Gray

    // TODO: omit the square shadow when user performing click on cards
    Column( //  Wrapped everything in Column to stack Card & Text
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(120.dp) // Fixed width for consistent sizing
            .padding(1.dp)
    ) {

    Card(
        modifier = Modifier
            .size(115.dp)
            //.fillMaxWidth()
            .padding(3.dp)
            .clickable { onClick() /* changes selectedIndex to globalIndex, therefore making it currently selected user/owner because (selectedIndex == globalIndex) for isSelected condition */ },
        shape = CircleShape, // Makes it round
        // border = BorderStroke(2.dp, borderColor),
        // colors = CardDefaults.cardColors(containerColor = backgroundColor),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp),

        ) {
        // Removed Text from inside the Card
        // TODO: You can put an image/icon here if needed later

        Box(modifier = Modifier.fillMaxSize()) { //  Added Box for layering

            /*
            // TODO: Your image here
            Image(
                painter = painterResource(id = R.drawable.your_image),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            */

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor) // Green transparent overlay (50% opacity)
            )
        }

    }

        Spacer(modifier = Modifier.height(1.dp)) // Added spacing between Card and Text

        Text(
            text = billOwner,
            // color = if (isSelected) Color.White else Color.Black,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            fontFamily = surannaFont,
            modifier = Modifier.padding(1.dp),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center // Center the text below the Card
        )

    }

}

