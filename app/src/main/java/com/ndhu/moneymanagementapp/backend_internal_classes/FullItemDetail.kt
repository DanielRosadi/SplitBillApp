package com.ndhu.moneymanagementapp.backend_internal_classes

// ADDED
// Internal usage in Backend

data class FullItemDetail(
    var itemId : String = "",
    var itemName : String = "",
    var userPriceShare : Float = 0f,
    var userItemQuantity: Float = 0f,
    var itemTotalQuantity: Float = 0f
)

