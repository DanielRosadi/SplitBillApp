package com.ndhu.moneymanagementapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ndhu.moneymanagementapp.ui.pages.Homepage
import com.ndhu.moneymanagementapp.ui.pages.ListStores
import com.ndhu.moneymanagementapp.ui.pages.ListStoresPage

@Composable
fun AppNavigation(){
    // this defines nav controller, that controls/instruct the navigation to screens
    // when the user needs to navigate, they should use nav controller to do so
    val navController = rememberNavController()

    // the Nav Host, act as a 'Host', in which they contains the screens where
    // navigation will be performed onto
    // starting page/screen will be the Homepage
    NavHost(navController = navController,
        startDestination = AppScreens.HomepageScreen.name){
        // inside the NavHost, we contains the NavGraph
        // NavGraph will be our collection/list of composable screens of the application

        // Homepage screen
        composable(AppScreens.HomepageScreen.name){
            Homepage(navController = navController)
        }

        // ListStores screen
        composable(AppScreens.ListStoresScreen.name){
            ListStoresPage(navController = navController)
        }
    }
}