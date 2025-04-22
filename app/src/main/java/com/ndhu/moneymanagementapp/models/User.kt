package com.ndhu.moneymanagementapp.models

import com.google.firebase.Timestamp

data class User(
    var id : String = "",
    var username : String = "",
    var email : String = "",
    var password : String = "",
    var bio: String
) {
    constructor() : this("", "", "", "", "")
}
