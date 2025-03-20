package com.ndhu.moneymanagementapp.models

import com.google.firebase.Timestamp
import com.ndhu.moneymanagementapp.enums.BillStatus

data class BillItemDetail (
    val id : String = "",
    var billId : String = "",
    var itemId : String = "",
    var qty : Number = 0,
    var userId: String = "",
    var status: BillStatus = BillStatus.UNPAID,
    var createdAt : Timestamp = Timestamp.now(),
    var updatedAt : Timestamp = Timestamp.now()
)