package com.ndhu.moneymanagementapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ndhu.moneymanagementapp.ui.pages.AccountPage
import com.ndhu.moneymanagementapp.ui.pages.ContactPage
import com.ndhu.moneymanagementapp.ui.pages.HomePage
import com.ndhu.moneymanagementapp.ui.pages.ListItemPage
import com.ndhu.moneymanagementapp.ui.pages.ListStorePage

@Composable
fun AppNavigation(){
    // this defines nav controller, that controls/instruct the navigation to screens
    // when the user needs to navigate, they should use nav controller to do so
    val navController = rememberNavController()

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
            ListStorePage(navController = navController)
        }

        // ListOfItem screen
        composable(AppScreens.ListItemsScreen.name){
            ListItemPage(navController = navController)
        }

        // Contact screen
        composable(AppScreens.ContactScreen.name){
            ContactPage(navController = navController)
        }

        // Account screen
        composable(AppScreens.AccountScreen.name){
            AccountPage(navController = navController)
        }

    }
}