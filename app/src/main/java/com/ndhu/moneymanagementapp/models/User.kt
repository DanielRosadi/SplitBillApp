package com.ndhu.moneymanagementapp.models

import com.google.firebase.Timestamp

data class User(
    val id : String = "",
    val username : String = "",
    val email : String = "",
    val password : String = "",
    val age : Number = 0,
    val createdAt : Timestamp
)
