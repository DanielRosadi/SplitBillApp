package com.ndhu.moneymanagementapp.ui.pages

import com.ndhu.moneymanagementapp.models.User

data class ItemOwnership(
    val owner: User,
    val itemQuantity: Int
)