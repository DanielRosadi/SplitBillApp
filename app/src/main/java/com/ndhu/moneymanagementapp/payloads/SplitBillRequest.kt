package com.ndhu.moneymanagementapp.payloads

data class ItemAndOwner (
    var itemId: String,
    var ownerId: String,
    var qty: Number
)

data class SplitBillRequest (
    var billOwnerId : String,
    var billContributorsId : List<String?>,
    var itemIdAndOwners: List<ItemAndOwner?>
)