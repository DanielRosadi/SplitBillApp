package com.ndhu.moneymanagementapp.navigation

import java.lang.IllegalArgumentException

enum class AppScreens{
    HomePageScreen,
    ListStoresScreen,
    ListItemsScreen,
    SelectFriendsScreen,
    SelectBillOwnerScreen,
    AssignSplitsScreen,
    ContactScreen,
    AccountScreen,
    FirstLoginScreen,
    LogInScreen,
    SignUpScreen
    ;
    // List the App Screens above

    companion object{
        fun fromRoute(route: String?): AppScreens
        = when (route?.substringBefore("/")){
            HomePageScreen.name -> HomePageScreen
            ListStoresScreen.name -> ListStoresScreen
            ListItemsScreen.name -> ListItemsScreen
            SelectFriendsScreen.name -> SelectFriendsScreen
            SelectBillOwnerScreen.name -> SelectBillOwnerScreen
            AssignSplitsScreen.name -> AssignSplitsScreen
            ContactScreen.name -> ContactScreen
            AccountScreen.name -> AccountScreen
            FirstLoginScreen.name -> FirstLoginScreen
            LogInScreen.name -> LogInScreen
            SignUpScreen.name -> SignUpScreen
            null -> HomePageScreen
            else -> throw IllegalArgumentException("Route $route is not recognized")
        }
    }
}