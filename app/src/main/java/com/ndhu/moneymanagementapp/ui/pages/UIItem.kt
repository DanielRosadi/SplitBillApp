package com.ndhu.moneymanagementapp.ui.pages

import com.ndhu.moneymanagementapp.models.Item

// UIItem is used for ListOfItemPage.kt only (internal usage)
// It is required that item quantity to be of type Integer
// because in ListOfItem, we encourage the user to input an integer item quantity
// for partial portion, user can add new item instead (e.g 1/2 portion of fried rice with integer item quantity )
data class UIItem(
    val item: Item,
    var quantity: Int = 0
)