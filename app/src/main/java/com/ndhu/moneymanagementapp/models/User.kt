package com.ndhu.moneymanagementapp.models

import com.google.firebase.Timestamp
import com.ndhu.moneymanagementapp.enums.UserStatus

data class User(
    var id : String = "",
    var username : String = "",
    var email : String = "",
    var status : UserStatus = UserStatus.ACTIVE,
    var password : String = "",
    var bio: String,
    var profilePictureId: Int
) {
    constructor() : this("", "", "", UserStatus.ACTIVE, "", "", (2..7).random())
}
