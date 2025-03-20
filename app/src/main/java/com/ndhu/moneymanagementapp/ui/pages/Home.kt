package com.ndhu.moneymanagementapp.ui.pages

import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ndhu.moneymanagementapp.controllers.UserController
import com.ndhu.moneymanagementapp.enums.AppScreens
import kotlinx.coroutines.runBlocking

@Composable
fun Home(context: Context, navController: NavController) {

    var loginUser = "{NOT-registered}"
    var userController =  UserController()
    loginUser = runBlocking { userController.getOnboardUser(context = context)?.username ?: "{FAIL-LOGIN}" }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(
            text = "Split Bill App",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Hello ${loginUser}, Welcome!",
            fontSize = 20.sp,
            modifier = Modifier
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row (modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = { navController.navigate(route = AppScreens.SignUpScreen.name) },
                modifier = Modifier.width(180.dp).height(70.dp)){
                Text(text = "Sign Up (Temp)")
            }
            Button(onClick = { navController.navigate(route = AppScreens.LogInScreen.name) },
                modifier = Modifier.width(180.dp).height(70.dp)){
                Text(text = "Log In (Temp)")
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth().fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { navigateToSplitBillScreen(navController) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .border(1.dp, Color.Black, RoundedCornerShape(8.dp)),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Text(text = "Split Bill")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navigateToBillHistoryScreen(navController) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .border(1.dp, Color.Black, RoundedCornerShape(8.dp)),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Text(text = "Bill History")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navigateToContactScreen(navController) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .border(1.dp, Color.Black, RoundedCornerShape(8.dp)),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Text(text = "Contacts")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navigateToAccountScreen(navController) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .border(1.dp, Color.Black, RoundedCornerShape(8.dp)),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Text(text = "Account")
            }
        } // Button Column
    } // Outer Column
}

fun navigateToSplitBillScreen(navController: NavController) {
    navController.navigate(route = AppScreens.ListStoresScreen.name)
}

//TODO: Make the navigation to one function only, as this function can be reused
fun navigateToBillHistoryScreen(navController: NavController) {
    navController.navigate(route = AppScreens.BillHistoryScreen.name)
}

fun navigateToContactScreen(navController: NavController) {
    navController.navigate(route = AppScreens.ContactScreen.name)
}

fun navigateToAccountScreen(navController: NavController) {
    navController.navigate(route = AppScreens.AccountScreen.name)
}