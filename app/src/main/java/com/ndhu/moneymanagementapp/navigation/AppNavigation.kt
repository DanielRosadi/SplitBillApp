package com.ndhu.moneymanagementapp.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ndhu.moneymanagementapp.models.Store
import com.ndhu.moneymanagementapp.sharedviewmodels.SharedStoreViewModel
import com.ndhu.moneymanagementapp.ui.pages.AccountPage
import com.ndhu.moneymanagementapp.ui.pages.ContactPage
import com.ndhu.moneymanagementapp.ui.pages.FirstLogin
import com.ndhu.moneymanagementapp.ui.pages.HomePage
import com.ndhu.moneymanagementapp.ui.pages.ListOfItemPage
import com.ndhu.moneymanagementapp.ui.pages.ListStorePage
import com.ndhu.moneymanagementapp.ui.pages.LogIn
import com.ndhu.moneymanagementapp.ui.pages.SignUp

@Composable
fun AppNavigation(){
    // this defines nav controller, that controls/instruct the navigation to screens
    // when the user needs to navigate, they should use nav controller to do so
    val navController = rememberNavController()
    val sharedViewModel: SharedStoreViewModel = viewModel()

    // the Nav Host, act as a 'Host', in which they contains the screens where
    // navigation will be performed onto
    // starting page/screen will be the Homepage
    NavHost(navController = navController,
        startDestination = AppScreens.HomePageScreen.name){
        // inside the NavHost, we contains the NavGraph
        // NavGraph will be our collection/list of composable screens of the application

        // HomePage screen
        composable(AppScreens.HomePageScreen.name){
            HomePage(navController = navController)
        }

        // ListOfStore screen
        composable(AppScreens.ListStoresScreen.name){
            ListStorePage(navController = navController, sharedViewModel)
        }

        // ListOfItem screen
        composable(AppScreens.ListItemsScreen.name+"/{storeId}",){backStackEntry ->
            val storeId = backStackEntry.arguments?.getString("storeId")
            ListOfItemPage(navController = navController, storeId = storeId, sharedViewModel)
        }

        // Contact screen
        composable(AppScreens.ContactScreen.name){
            ContactPage(navController = navController)
        }

        // Account screen
        composable(AppScreens.AccountScreen.name){
            AccountPage(navController = navController)
        }

        // First Login screen
        composable(AppScreens.FirstLoginScreen.name){
            FirstLogin(navController = navController)
        }

        // LogIn screen
        composable(AppScreens.LogInScreen.name){
            LogIn(navController = navController)
        }

        // SignUp screen
        composable(AppScreens.SignUpScreen.name){
            SignUp(navController = navController)
        }

    }
}
