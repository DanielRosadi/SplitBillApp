package com.ndhu.moneymanagementapp.controllers

import android.content.Context
import android.util.Log
import com.ndhu.moneymanagementapp.enums.UserStatus
import com.ndhu.moneymanagementapp.models.User
import com.ndhu.moneymanagementapp.repositories.IUserRepository
import com.ndhu.moneymanagementapp.repositories.impl.UserRepository
import com.ndhu.moneymanagementapp.utils.Util
import java.util.UUID


class UserController {

    private val userRepo : IUserRepository = UserRepository()

    suspend fun registerUser(username: String, email: String, password: String, bio: String): String? {
        val existingUserByUsername = userRepo.getUserByUsername(username)
        if (existingUserByUsername != null) {
            return "Username is already in use."
        }

        val existingUserByEmail = userRepo.getUserByEmail(email)
        if (existingUserByEmail != null) {
            return "Email is already in use."
        }

        userRepo.add(
            User(
                UUID.randomUUID().toString(),
                username,
                email,
                UserStatus.ACTIVE,
                Util.passwordEncryption(password),
                bio,
                (2..7).random()
            )
        )
        return null
    }

    suspend fun loginUser(context : Context, identify : String, password : String) : User? {
        var user : User?
        if(identify.contains("@")) {
            user = userRepo.getUserByEmail(identify)
        }else {
            user = userRepo.getUserByUsername(identify)
        }
        val pass = user?.let { Util.verifyPassword(password, it.password) }
        if(pass==true && user?.status==UserStatus.ACTIVE) {
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

    suspend fun deleteCurrUser(context: Context) {
        val userId = Util.getUserId(context)
        if(userId != null) {
            val user = userRepo.getById(userId)
            if (user != null) {
                user.status = UserStatus.DELETED
                Util.clearUserId(context)
                userRepo.update(user.id, user)
                return
            }
        }
        Log.d("INFO", "USER NOT FOUND")
        return
    }

    suspend fun updateUserData(
        context: Context,
        newUsername: String,
        newEmail: String,
        newBio: String,
    ): String? {
        val user = getOnboardUser(context)
        if (user != null) {
            // Check if new username is taken by someone else
            val userByUsername = userRepo.getUserByUsername(newUsername)
            if (userByUsername != null && userByUsername.id != user.id) {
                return "Username is already in use."
            }

            // Check if new email is taken by someone else
            val userByEmail = userRepo.getUserByEmail(newEmail)
            if (userByEmail != null && userByEmail.id != user.id) {
                return "Email is already in use."
            }

            // If everything valid, proceed to update
            val updatedUser = user.copy(
                username = newUsername,
                email = newEmail,
                bio = newBio,
            )
            userRepo.update(updatedUser.id, updatedUser)
            Log.d("INFO", "User data updated successfully")
            return null
        } else {
            Log.w("ERROR", "No onboard user found to update")
            return "User not found."
        }
    }

    suspend fun updateUserPassword(
        context: Context,
        newPassword: String
    ) {
        val user = getOnboardUser(context)
        if (user != null) {
            val updatedUser = user.copy(
                password = Util.passwordEncryption(newPassword)
            )
            userRepo.update(updatedUser.id, updatedUser)
            Log.d("INFO", "User password updated successfully")
        } else {
            Log.w("ERROR", "No onboard user found to update")
        }
    }

    suspend fun updateUserProfile(
        context: Context,
        newProfilePictureId: Int
    ){

    }
}