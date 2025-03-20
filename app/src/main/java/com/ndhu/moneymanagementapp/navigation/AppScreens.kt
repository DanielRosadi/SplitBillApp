package com.ndhu.moneymanagementapp.navigation

import java.lang.IllegalArgumentException

enum class AppScreens{
    HomeScreen,
    BillHistoryScreen,
    BillDetailsScreen,
    ListStoresScreen,
    ListItemsScreen,
    SelectFriendsScreen,
    SelectBillOwnerScreen,
    ContactScreen,
    AccountScreen,
    EditProfileScreen,
    FirstLoginScreen,
    LogInScreen,
    SignUpScreen
    ;
    // List the App Screens above

    companion object{
        fun fromRoute(route: String?): AppScreens
        = when (route?.substringBefore("/")){
            HomeScreen.name -> HomeScreen
            BillHistoryScreen.name -> BillHistoryScreen
            BillDetailsScreen.name -> BillDetailsScreen
            ListStoresScreen.name -> ListStoresScreen
            ListItemsScreen.name -> ListItemsScreen
            SelectFriendsScreen.name -> SelectFriendsScreen
            SelectBillOwnerScreen.name -> SelectBillOwnerScreen
            ContactScreen.name -> ContactScreen
            AccountScreen.name -> AccountScreen
            EditProfileScreen.name -> EditProfileScreen
            FirstLoginScreen.name -> FirstLoginScreen
            LogInScreen.name -> LogInScreen
            SignUpScreen.name -> SignUpScreen
            null -> HomeScreen
            else -> throw IllegalArgumentException("Route $route is not recognized")
        }
    }
}