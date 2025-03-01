package com.ndhu.moneymanagementapp.navigation

import java.lang.IllegalArgumentException

enum class AppScreens{
    HomepageScreen,
    ListStoresScreen;
    // List the App Screens above

    companion object{
        fun fromRoute(route: String?): AppScreens
        = when (route?.substringBefore("/")){
            HomepageScreen.name -> HomepageScreen
            ListStoresScreen.name -> ListStoresScreen
            null -> HomepageScreen
            else -> throw IllegalArgumentException("Route $route is not recognized")
        }
    }
}