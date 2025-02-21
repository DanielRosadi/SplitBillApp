package com.ndhu.moneymanagementapp.controllers

import android.os.Build
import androidx.annotation.RequiresApi
import com.ndhu.moneymanagementapp.models.User
import com.ndhu.moneymanagementapp.repositories.IUserRepository
import com.ndhu.moneymanagementapp.repositories.impl.UserRepository
import com.ndhu.moneymanagementapp.utils.Util
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID


class UserController {

    private val userRepo : IUserRepository = UserRepository()

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun registerUser(username : String, email : String, password : String) {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val current = LocalDateTime.now().format(formatter)

        userRepo.add(
            mapOf(
                "id" to UUID.randomUUID(),
                "username" to username,
                "email" to email,
                "password" to Util.passwordEncryption(password),
                "createdAt" to current,
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