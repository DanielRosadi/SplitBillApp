package com.ndhu.moneymanagementapp.payloads

data class ItemAndOwner (
    var itemId: String = "",
    var ownerId: String = "",
    var qty: Float = 0f
)

data class SplitBillRequest (
    var billOwnerId : String = "",
    var itemIdAndOwners: List<ItemAndOwner?> = emptyList(),
    val storeId: String                             // added
)