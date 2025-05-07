package com.ndhu.moneymanagementapp.ui.pages

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.ndhu.moneymanagementapp.payloads.ItemDetail

data class ItemWithUsersList(
    val item: ItemDetail,
    val ownership: SnapshotStateList<ItemOwnership>
)
