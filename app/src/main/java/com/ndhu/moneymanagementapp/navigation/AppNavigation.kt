package com.ndhu.moneymanagementapp.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ndhu.moneymanagementapp.enums.AppScreens
import com.ndhu.moneymanagementapp.sharedviewmodels.SharedStoreViewModel
import com.ndhu.moneymanagementapp.ui.pages.AccountPage
import com.ndhu.moneymanagementapp.ui.pages.BillDetails
import com.ndhu.moneymanagementapp.ui.pages.BillHistory
import com.ndhu.moneymanagementapp.ui.pages.ContactPage
import com.ndhu.moneymanagementapp.ui.pages.DeleteStoreScreen
import com.ndhu.moneymanagementapp.ui.pages.EditProfile
import com.ndhu.moneymanagementapp.ui.pages.FirstLogin
import com.ndhu.moneymanagementapp.ui.pages.Home
import com.ndhu.moneymanagementapp.ui.pages.ListOfItemPage
import com.ndhu.moneymanagementapp.ui.pages.ListStorePage
import com.ndhu.moneymanagementapp.ui.pages.LogIn
import com.ndhu.moneymanagementapp.ui.pages.SelectBillOwnerScreen
import com.ndhu.moneymanagementapp.ui.pages.SelectFriendsPage
import com.ndhu.moneymanagementapp.ui.pages.SignUp

@Composable
fun AppNavigation(context : Context){
    // this defines nav controller, that controls/instruct the navigation to screens
    // when the user needs to navigate, they should use nav controller to do so
    val navController = rememberNavController()
    val sharedViewModel: SharedStoreViewModel = viewModel()

    // the Nav Host, act as a 'Host', in which they contains the screens where
    // navigation will be performed onto
    // starting page/screen will be the Homepage
    NavHost(navController = navController,
        startDestination = AppScreens.HomeScreen.name){
        // inside the NavHost, we contains the NavGraph
        // NavGraph will be our collection/list of composable screens of the application

        // Home screen
        composable(AppScreens.HomeScreen.name){
            Home(navController = navController, context = context)
        }

        // BillHistory screen
        composable(AppScreens.BillHistoryScreen.name){
            BillHistory(context, navController = navController)
        }

        // BillDetails screen
        composable(AppScreens.BillDetailsScreen.name){
            BillDetails(context, navController = navController)
        }

        // ListOfStore screen
        composable(AppScreens.ListStoresScreen.name){
            ListStorePage(context, navController = navController, sharedViewModel)
        }

        // DeleteStoreScreen
        composable(AppScreens.DeleteStoresScreen.name){
            DeleteStoreScreen(context, navController = navController, viewModel = sharedViewModel)
        }

        // ListOfItem screen
        composable(AppScreens.ListItemsScreen.name+"/{storeId}",){backStackEntry ->
            val storeId = backStackEntry.arguments?.getString("storeId")
            ListOfItemPage(context, navController = navController, storeId = storeId, sharedViewModel)
        }

        // SelectFriends screen
        composable(AppScreens.SelectFriendsScreen.name){
            SelectFriendsPage(context, navController = navController)
        }

        // SelectBillOwner screen
        composable(AppScreens.SelectBillOwnerScreen.name){
            SelectBillOwnerScreen(context,navController = navController)
        }

        // Contact screen
        composable(AppScreens.ContactScreen.name){
            ContactPage(context, navController = navController)
        }

        // Account screen
        composable(AppScreens.AccountScreen.name){
            AccountPage(context, navController = navController)
        }

        // EditAccount screen
        composable(AppScreens.EditProfileScreen.name){
            EditProfile(context, navController = navController)
        }

        // First Login screen
        composable(AppScreens.FirstLoginScreen.name){
            FirstLogin(context, navController = navController)
        }

        // LogIn screen
        composable(AppScreens.LogInScreen.name){
            LogIn(context, navController = navController)
        }

        // SignUp screen
        composable(AppScreens.SignUpScreen.name){
            SignUp(context, navController = navController)
        }

    }
}
