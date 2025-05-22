package com.ndhu.moneymanagementapp.ui.pages

import androidx.compose.runtime.MutableState
import com.ndhu.moneymanagementapp.models.User

// ADDED
// Internal usage in Frontend

data class ItemOwnership(
    val owner: User,
    val itemQuantity: MutableState<Float> // // Added MutableState to itemQuantity of ItemOwnership class required for correct data observation
){
    val quantity: Float
        get() = itemQuantity.value
}