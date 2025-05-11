package com.ndhu.moneymanagementapp.ui.pages

import androidx.compose.runtime.MutableState
import com.ndhu.moneymanagementapp.models.User

data class ItemOwnership(
    val owner: User,
    val itemQuantity: MutableState<Float>
){
    val quantity: Float
        get() = itemQuantity.value
}