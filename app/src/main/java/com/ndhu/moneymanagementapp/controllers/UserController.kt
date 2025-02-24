package com.ndhu.moneymanagementapp.controllers

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.Timestamp
import com.ndhu.moneymanagementapp.models.User
import com.ndhu.moneymanagementapp.repositories.IUserRepository
import com.ndhu.moneymanagementapp.repositories.impl.UserRepository
import com.ndhu.moneymanagementapp.utils.Util
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID


class UserController {

    private val userRepo : IUserRepository = UserRepository()

    suspend fun registerUser(username : String, email : String, password : String) {

        userRepo.add(
            User(
                UUID.randomUUID().toString(),
                username,
                0,
                email,
                Util.passwordEncryption(password),
                0,
                Timestamp.now(),
            )
        )

    }

    suspend fun loginUser(identify : String, password : String) : User? {
        var user : User?
        if(identify.contains("@")) {
            user = userRepo.getUserByEmail(identify)
        }else {
            user = userRepo.getUserByUsername(identify)
        }
        val pass = user?.let { Util.verifyPassword(password, it.password) }
        if(pass==true) {
            return user
        }
        return null
    }

}