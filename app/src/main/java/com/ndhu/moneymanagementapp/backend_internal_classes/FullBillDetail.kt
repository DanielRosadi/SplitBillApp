package com.ndhu.moneymanagementapp.backend_internal_classes

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.google.firebase.Timestamp
import com.ndhu.moneymanagementapp.enums.UserPaymentStatus
import com.ndhu.moneymanagementapp.payloads.ItemDetail

// ADDED
// Internal usage in backend

// New data classes for grouped response
data class UserItemGroup(
    val userId: String,
    var status: MutableState<UserPaymentStatus>,
    val items: List<ItemDetail>
)

data class FullBillDetail(
    val billId: String,
    val billOwnerId: String,
    val createdAt: Timestamp,
    val storeId: String,
    val users: SnapshotStateList<UserItemGroup>
)
