package com.ndhu.moneymanagementapp.ui.pages

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ndhu.moneymanagementapp.controllers.ContactController
import com.ndhu.moneymanagementapp.controllers.UserController
import com.ndhu.moneymanagementapp.enums.AppScreens
import kotlinx.coroutines.runBlocking

@Composable
fun TestingScreen(context: Context, navController: NavController){

    Column (modifier = Modifier.fillMaxWidth().fillMaxHeight()) {

        // define your desired contact to add, mind that they will be in PENDING status
        // might need to change to FRIEND manually in firebase
        // uncomment when not used
        // var contactsName = listOf("user9", "user8")
        var contactsName = emptyList<String>()
        Button(onClick = { populateContact(context, contactsName) },
            modifier = Modifier.width(180.dp).height(70.dp)){
            Text(text = "Populate My Contact")
        }
    }
}

fun populateContact(context: Context, contactsName: List<String>){
    var contactController = ContactController()
    var userController = UserController()

    for (contact in contactsName) {
        runBlocking {
            userController.searchUserByUserName(context, contact)
                ?.let { contactController.addContact(context, it.id) }
        }
    }


}