package com.ndhu.moneymanagementapp.utils

import androidx.compose.ui.input.key.Key
import at.favre.lib.crypto.bcrypt.BCrypt

object Util {

    fun identifyBy(identify : String) : String {
        if(identify.contains("@")) {
            return "query by email"
        }
        return "query by username"
    }

    fun passwordEncryption(password : String) : String {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray())
    }

    fun verifyPassword(password : String, hashedPassword : String) : Boolean {
        return BCrypt.verifyer().verify(password.toCharArray(), hashedPassword).verified
    }
}