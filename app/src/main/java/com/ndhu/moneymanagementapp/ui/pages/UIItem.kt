package com.ndhu.moneymanagementapp.ui.pages

import com.ndhu.moneymanagementapp.models.Item

data class UIItem(
    val item: Item,
    var quantity: Int = 0
)