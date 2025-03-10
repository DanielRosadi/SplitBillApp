package com.ndhu.moneymanagementapp.ui.pages

import android.content.Context
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ndhu.moneymanagementapp.navigation.AppScreens
import com.ndhu.moneymanagementapp.reusable_composables.SearchBox
import com.ndhu.moneymanagementapp.reusable_composables.UniformDesignScaffold

@Composable
fun SelectFriendsPage(context : Context, navController: NavController){

    // getContacts() here

    val listFriends = listOf("Friend 1", "Friend 2", "Friend 3")

    ComposeSelectFriends(context, listFriends, navController)
}


@Composable
fun ComposeSelectFriends(context : Context, listFriends: List<String>, navController: NavController){
    UniformDesignScaffold(
        topAppBar = {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
                }
                Spacer(modifier = Modifier.width(50.dp))
                Text("Select Friends", fontSize = 40.sp, fontWeight = FontWeight.Bold)
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

                LazyColumn (modifier = Modifier.weight(1f), contentPadding = PaddingValues(5.dp)) {

                    items(listFriends) { item ->
                        ListFriends(item)
                    }

                }

                Spacer(modifier = Modifier.height(30.dp))

                Button(onClick = {}, modifier = Modifier.fillMaxWidth().height(60.dp)) {
                    Text(text = "Add Friend")
                    }

                Spacer(modifier = Modifier.height(20.dp))

                Button(onClick = { navController.navigate(route = AppScreens.SelectBillOwnerScreen.name)}, modifier = Modifier.fillMaxWidth().height(60.dp)) {
                    Text(text = "Done")
                }

                Spacer(modifier = Modifier.height(20.dp))

            }
        })
}

@Composable
fun ListFriends(friends: String){
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
    ) {
        Text(
            text = friends,
            fontSize = 30.sp,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}