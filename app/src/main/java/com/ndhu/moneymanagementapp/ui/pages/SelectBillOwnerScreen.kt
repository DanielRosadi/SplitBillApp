package com.ndhu.moneymanagementapp.ui.pages

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ndhu.moneymanagementapp.components.SearchBox
import com.ndhu.moneymanagementapp.components.UniformDesignScaffold
import com.ndhu.moneymanagementapp.controllers.UserController
import com.ndhu.moneymanagementapp.enums.AppScreens
import com.ndhu.moneymanagementapp.models.User
import com.ndhu.moneymanagementapp.sharedviewmodels.SharedViewModel
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
            runBlocking {
                listPossibleOwners.add(
                    userController.searchUserByUserId(context, contact.userId2)
                )
            }
        }
    }

    // val listBillOwners = List(17) { "User ${it + 1}" } // Example: 11 items

    ComposeSelectBillOwner(context, listPossibleOwners, navController, viewModel)
}

@Composable
fun ComposeSelectBillOwner(context: Context, listPossibleOwners: MutableList<User?>, navController: NavController, viewModel: SharedViewModel){
    var selectedIndex by remember { mutableStateOf(-1) }

    val rows = listPossibleOwners.chunked(3)

    UniformDesignScaffold(
        topAppBar = {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
                }
                Spacer(modifier = Modifier.width(50.dp))
                Text("Select Bill Owner", fontSize = 30.sp, fontWeight = FontWeight.Bold)
            }
        },

        body = {paddingValues->
            Column (modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(paddingValues)
                .padding(10.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {

                Spacer(modifier = Modifier.height(10.dp))

                SearchBox()

                Spacer(modifier = Modifier.height(10.dp))

                // TODO: implement single selection for bill owner here
                LazyColumn (modifier = Modifier.weight(1f), contentPadding = PaddingValues(5.dp)) {

                    // chunked groups items into sets of 3 for each row
                    // it also automatically handles when the list is not perfectly divisible by 3
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
                                        onClick = { selectedIndex = if (selectedIndex == globalIndex) -1 else globalIndex } /* toggle logic */)
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

                Spacer(modifier = Modifier.height(30.dp))

                Button(
                    onClick = {
                        navController.navigate(route = AppScreens.AssignSplitsScreen.name)
                        // TODO: save bill owner data here
                        // viewModel.setBillOwner()
                              },
                    modifier = Modifier.fillMaxWidth().height(60.dp)) {
                    Text(text = "Done")
                }

                Spacer(modifier = Modifier.height(20.dp))

            }
        }

    )
}

@Composable
fun ListBillOwners(billOwner: String, isSelected: Boolean, onClick: ()-> Unit){
    val backgroundColor = if (isSelected) Color.Red else Color.White
    val borderColor = if (isSelected) Color.Red else Color.Gray

    Card (
        modifier = Modifier
            .size(115.dp)
            .fillMaxWidth()
            .padding(5.dp)
            .clickable { onClick() /* changes selectedIndex to globalIndex, therefore making it currently selected user/owner because (selectedIndex == globalIndex) for isSelected condition */ },
        shape = CircleShape, // Makes it round
        border = BorderStroke(2.dp, borderColor),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(4.dp),

    ) {
        Text(
            text = billOwner,
            color = if (isSelected) Color.White else Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

