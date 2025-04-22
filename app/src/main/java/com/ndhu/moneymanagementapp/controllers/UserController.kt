package com.ndhu.moneymanagementapp.controllers

import android.content.Context
import android.util.Log
import com.google.firebase.Timestamp
import com.ndhu.moneymanagementapp.models.User
import com.ndhu.moneymanagementapp.repositories.IUserRepository
import com.ndhu.moneymanagementapp.repositories.impl.UserRepository
import com.ndhu.moneymanagementapp.utils.Util
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
                Timestamp.now(),
            )
        )

    }

    suspend fun updateUserInformation(context: Context, username: String, email: String, bio: String){
        val user = getOnboardUser(context)
        Log.d("DEBUG", "Updating user with ID: ${user?.id}")
        userRepo.update(
            user!!.id,
            User(
                user.id,
                username,
                user.balance,
                email,
                user.password,
                user.createdAt,
            )
        )
    }

    suspend fun loginUser(context : Context, identify : String, password : String) : User? {
        var user : User?
        if(identify.contains("@")) {
            user = userRepo.getUserByEmail(identify)
        }else {
            user = userRepo.getUserByUsername(identify)
        }
        val pass = user?.let { Util.verifyPassword(password, it.password) }
        if(pass==true) {
            if(user != null){
                Util.saveUserId(context, user.id)
            }
            return user
        }
        return null
    }

    suspend fun getOnboardUser(context : Context) : User? {
        val userId = Util.getUserId(context)
        if(userId != null) {
            val user = userRepo.getById(userId)
            if (user != null) {
                return user
            }
        }
        return null
    }

    suspend fun logout(context : Context) {
        Util.clearUserId(context)
    }

    suspend fun searchUserByUserName(context : Context, userName : String) : User? {
        return userRepo.getUserByUsername(userName)
    }

    suspend fun searchUserByUserId(context: Context, userId: String): User?{
        return userRepo.getById(userId)
    }


}