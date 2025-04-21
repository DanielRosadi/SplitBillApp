package com.ndhu.moneymanagementapp.ui.pages

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ndhu.moneymanagementapp.components.SearchBox
import com.ndhu.moneymanagementapp.components.UniformDesignScaffold
import com.ndhu.moneymanagementapp.controllers.SplitBillController
import com.ndhu.moneymanagementapp.models.User
import com.ndhu.moneymanagementapp.payloads.ItemDetail
import com.ndhu.moneymanagementapp.sharedviewmodels.SharedViewModel

@Composable
fun AssignSplitsScreen(context: Context, navController: NavController, viewModel: SharedViewModel){

    // TODO: need to save bill owner in view model?
    // viewModel.setBillOwner()

    val splitBillController = SplitBillController()

//    val listParticipants = List(7) { "User ${it + 1}" } // Example: 7 items
//    val listItems = List(5) { "Item ${it + 1}" } // Example: 5 items

    val listParticipants = viewModel.billParticipants
    val listItems = viewModel.itemDetailList


    ComposeAssignSplits(context, listParticipants, listItems, navController, splitBillController, viewModel)
}

@Composable
fun ComposeAssignSplits(context: Context, listParticipants: MutableList<User?>, listItems: MutableList<ItemDetail>, navController: NavController, splitBillController: SplitBillController, viewModel: SharedViewModel){
    val userColors = remember { mutableStateMapOf<User?, Color>() }
    var selectedUser by remember { mutableStateOf<User?>(null) }

    UniformDesignScaffold(
        topAppBar = {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
                }
                Spacer(modifier = Modifier.width(50.dp))
                Text("Assign Splits", fontSize = 30.sp, fontWeight = FontWeight.Bold)
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

                // update current user selected text
                Text(text = "Now Selecting: {${selectedUser?.username}}" ?: "No User Selected")

                Spacer(modifier = Modifier.height(10.dp))

                LazyColumn (modifier = Modifier.weight(1f), contentPadding = PaddingValues(5.dp)) {

                    // chunked groups items into sets of 3 for each row
                    // it also automatically handles when the list is not perfectly divisible by 3
                    items(listItems) { item ->
                        ListItemSplits(item)
                    }

                }

                Spacer(modifier = Modifier.height(30.dp))

                LazyRow () {
                    items(listParticipants) { participant ->

                        ListBillParticipants(participant = participant, userColors = userColors, selectedUser = selectedUser, onUserSelected = { selectedUser = it}, listParticipants = listParticipants)
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))



                Button(onClick = {
                    // TODO: if done, create list of itemIdAndOwners

                    // TODO: then, submit bill data to database (with list of itemIdAndOwners)
                    // val splitBillRequest = SplitBillRequest(billOwnerId = viewModel.getBillOwner(), itemIdAndOwners = )
                    /*
                    runBlocking {
                        splitBillController.addSplitBill(context = context, request = splitBillRequest)
                    }
                     */

                },
                    modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)) {
                    Text(text = "Done")
                }



                Spacer(modifier = Modifier.height(20.dp))

            }
        }

    )
}

@Composable
fun ListItemSplits(itemDetail: ItemDetail){
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(5.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
    ) {
        Row () {
            Text(
            text = itemDetail.name,
            fontSize = 18.sp,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyLarge)

            // TODO: implement selected users here

            Text(
                text = itemDetail.qty.toString(),
                fontSize = 18.sp,
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyLarge)

            Text(
                text = "x",
                fontSize = 18.sp,
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyLarge)

            Text(
                text = itemDetail.price.toString(),
                fontSize = 18.sp,
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyLarge)
        }

    }
}

@Composable
fun ListBillParticipants(participant: User?, userColors: SnapshotStateMap<User?, Color>, selectedUser: User?, onUserSelected: (User) -> Unit, listParticipants: MutableList<User?>){
    val index = listParticipants.indexOf(participant) // get participant index in the list
    val color = userColors.getOrPut(participant) {
        generateDistinctColor(index, listParticipants.size)
    }
    // update border color
    val borderColor = if (participant == selectedUser) Color.Black else Color.Transparent

    Card (
        modifier = Modifier
            .size(90.dp)
            .fillMaxWidth()
            .padding(5.dp)
            .clickable {
                if (participant != null) {
                    // apply selection, this function is required because we are passing state into a child function
                    // otherwise, error may occur
                    onUserSelected(participant)
                }
            },

        shape = CircleShape, // Makes it round
        elevation = CardDefaults.cardElevation(4.dp),
        border = BorderStroke(2.dp, borderColor),
        // colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
        // update card color based on user (according to distinct color generator)
        colors = CardDefaults.cardColors(
            containerColor = if (participant == selectedUser) color else Color.White
        )


    ) {
        Text(
            text = participant?.username ?: "Unknown User", // if participant is not null, execute participant.username else put "Unknown User"
            fontSize = 18.sp,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

fun generateDistinctColor(index: Int, total: Int): Color {
    // using list participants index and list size, we can generate distinct colors

    // use the Hue component in HSL (Hue-Saturation-Lightness) to evenly space colors.
    val hue = (index * (360f / total)) % 360f
    return Color.hsl(hue, 0.6f, 0.85f)
}


// Helper: Generate a random light color
fun randomLightColor(): Color {
    val red = (150..255).random()
    val green = (150..255).random()
    val blue = (150..255).random()
    return Color(red, green, blue)
}