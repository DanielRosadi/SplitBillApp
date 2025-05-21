package com.ndhu.moneymanagementapp.payloads

import com.ndhu.moneymanagementapp.enums.UserPaymentStatus
import com.google.firebase.Timestamp

// WARNING
// careful when changing this!
// ListOfItem accidentally uses this data class!!!
// so refactor will break the features, unless you also fix it
data class ItemDetail(
    var id : String = "",
    var name : String = "",
    var oneItemPrice : Float = 0f, // price of one item quantity bought
    var userTakenQuantity: Float = 0f // quantity taken by user
    // so, user share price = price * qty
    // TODO: missing corresponding user, need to refactor with existing complement data classes
)

data class ListUserSplitBillResponse(
    var billId: String = "",
    var status: UserPaymentStatus = UserPaymentStatus.UNPAID,
    var itemList: MutableList<ItemDetail> = mutableListOf(),
    var storeId: String = "",                     // added
    val createdAt: Timestamp,      // <-- added
    val billOwnerId: String          // <-- added
)