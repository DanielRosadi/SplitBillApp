package com.ndhu.moneymanagementapp.models

import java.util.UUID

data class ProfilePicture (
    val id : String = UUID.randomUUID().toString(),
    var profilePicture : String = ""
)