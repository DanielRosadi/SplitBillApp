package com.ndhu.moneymanagementapp.models

import com.google.firebase.Timestamp
import com.ndhu.moneymanagementapp.enums.UserStatus
import com.ndhu.moneymanagementapp.ui.pages.profilePictureRandomizer

data class User(
    var id : String = "",
    var username : String = "",
    var email : String = "",
    var status : UserStatus = UserStatus.ACTIVE,
    var password : String = "",
    var bio: String,
    var profilePictureId: Int? = profilePictureRandomizer
) {
    constructor() : this("", "", "", UserStatus.ACTIVE, "", "", profilePictureRandomizer)
}
