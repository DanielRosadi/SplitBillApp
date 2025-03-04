package com.ndhu.moneymanagementapp.navigation

import java.lang.IllegalArgumentException

enum class AppScreens{
    HomePageScreen,
    ContactScreen,
    AccountScreen,
    ListStoresScreen;
    // List the App Screens above

    companion object{
        fun fromRoute(route: String?): AppScreens
        = when (route?.substringBefore("/")){
            HomePageScreen.name -> HomePageScreen
            ListStoresScreen.name -> ListStoresScreen
            ContactScreen.name -> ContactScreen
            AccountScreen.name -> AccountScreen
            null -> HomePageScreen
            else -> throw IllegalArgumentException("Route $route is not recognized")
        }
    }
}