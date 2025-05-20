package com.ndhu.moneymanagementapp.payloads

import com.ndhu.moneymanagementapp.enums.UserPaymentStatus
import com.google.firebase.Timestamp

data class ItemDetail(
    var id : String = "",
    var name : String = "",
    var price : Float = 0f,
    var qty: Float = 0f
)

data class ListUserSplitBillResponse(
    var billId: String = "",
    var status: UserPaymentStatus = UserPaymentStatus.UNPAID,
    var itemList: MutableList<ItemDetail> = mutableListOf(),
    var storeId: String = "",                     // added
    val createdAt: Timestamp,      // <-- added
    val billOwnerId: String          // <-- added
)