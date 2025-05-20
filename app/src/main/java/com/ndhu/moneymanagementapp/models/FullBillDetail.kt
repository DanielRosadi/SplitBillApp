package com.ndhu.moneymanagementapp.models

import com.google.firebase.Timestamp
import com.ndhu.moneymanagementapp.enums.UserPaymentStatus
import com.ndhu.moneymanagementapp.payloads.ItemDetail

// New data classes for grouped response
data class UserItemGroup(
    val userId: String,
    val status: UserPaymentStatus,
    val items: List<ItemDetail>
)

data class FullBillDetail(
    val billId: String,
    val billOwnerId: String,
    val createdAt: Timestamp,
    val storeId: String,
    val users: List<UserItemGroup>
)
