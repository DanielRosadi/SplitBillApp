package com.ndhu.moneymanagementapp.models

import com.google.firebase.Timestamp
import com.ndhu.moneymanagementapp.enums.UserPaymentStatus

data class BillItemDetail (
    val id : String = "",
    var billId : String = "",
    var itemId : String = "",
    var qty : Float = 0f,
    var userId: String = "",
    var status: UserPaymentStatus = UserPaymentStatus.UNPAID,
    var createdAt : Timestamp = Timestamp.now(),
    var updatedAt : Timestamp = Timestamp.now()
)