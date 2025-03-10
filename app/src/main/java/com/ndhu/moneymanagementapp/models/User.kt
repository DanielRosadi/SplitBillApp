package com.ndhu.moneymanagementapp.models

import com.google.firebase.Timestamp

data class User(
    var id : String = "",
    var username : String = "",
    var balance : Int = 0,
    var email : String = "",
    var password : String = "",
    var createdAt : Timestamp
) {
    constructor() : this("", "", 0, "", "", Timestamp.now())
}
