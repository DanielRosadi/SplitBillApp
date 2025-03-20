package com.ndhu.moneymanagementapp.models

import com.google.firebase.Timestamp

data class Bill (
    val id : String = "",
    var ownerId : String = "",
    var billDate: Timestamp = Timestamp.now(),
    var createdAt : Timestamp = Timestamp.now(),
    var updatedAt : Timestamp = Timestamp.now()
)