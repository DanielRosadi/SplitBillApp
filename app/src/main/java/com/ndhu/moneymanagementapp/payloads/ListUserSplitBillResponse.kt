package com.ndhu.moneymanagementapp.payloads

import com.ndhu.moneymanagementapp.enums.UserPaymentStatus

data class ItemDetail(
    var id : String = "",
    var name : String = "",
    var price : Number = 0,
    var qty: Number = 0
)

data class ListUserSplitBillResponse (
    var billId : String = "",
    var status : UserPaymentStatus = UserPaymentStatus.UNPAID,
    var itemList : MutableList<ItemDetail> = mutableListOf()
)