package com.ndhu.moneymanagementapp.payloads

import com.ndhu.moneymanagementapp.enums.BillStatus

data class ItemDetail(
    var id : String = "",
    var name : String = "",
    var price : Number = 0,
    var qty: Number = 0
)

data class ListUserSplitBillResponse (
    var billId : String = "",
    var status : BillStatus = BillStatus.UNPAID,
    var itemList : MutableList<ItemDetail> = mutableListOf()
)