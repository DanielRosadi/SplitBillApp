package com.ndhu.moneymanagementapp.enums

import java.lang.IllegalArgumentException

enum class AppScreens {
    WelcomeScreen,
    HomeScreen,
    BillHistoryScreen,
    BillDetailsScreen,
    ListStoresScreen,
    DeleteStoresScreen,
    ListItemsScreen,
    SelectFriendsScreen,
    SelectBillOwnerScreen,
    AssignSplitsScreen,
    SplitItemScreen,
    ContactScreen,
    PendingRequestScreen,
    AccountScreen,
    EditProfileScreen,
    FirstLoginScreen,
    LogInScreen,
    SignUpScreen,
    TestingScreen
    ;
    // List the App Screens above

    companion object {
        fun fromRoute(route: String?): AppScreens = when (route?.substringBefore("/")) {
            WelcomeScreen.name -> WelcomeScreen
            HomeScreen.name -> HomeScreen
            BillHistoryScreen.name -> BillHistoryScreen
            BillDetailsScreen.name -> BillDetailsScreen
            ListStoresScreen.name -> ListStoresScreen
            DeleteStoresScreen.name -> DeleteStoresScreen
            ListItemsScreen.name -> ListItemsScreen
            SelectFriendsScreen.name -> SelectFriendsScreen
            SelectBillOwnerScreen.name -> SelectBillOwnerScreen
            AssignSplitsScreen.name -> AssignSplitsScreen
            SplitItemScreen.name -> SplitItemScreen
            ContactScreen.name -> ContactScreen
            PendingRequestScreen.name -> PendingRequestScreen
            AccountScreen.name -> AccountScreen
            EditProfileScreen.name -> EditProfileScreen
            FirstLoginScreen.name -> FirstLoginScreen
            LogInScreen.name -> LogInScreen
            SignUpScreen.name -> SignUpScreen
            TestingScreen.name -> TestingScreen
            null -> HomeScreen
            else -> throw IllegalArgumentException("Route $route is not recognized")
        }
    }
}