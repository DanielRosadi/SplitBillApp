package com.ndhu.moneymanagementapp.repositories

import com.ndhu.moneymanagementapp.models.User

interface IUserRepository{
    suspend fun add(data: User)
    suspend fun getList(): List<User>
    suspend fun update(documentId: String, data: User)
    suspend fun delete(documentId: String)
    suspend fun getUserByEmail(email : String) : User?
    suspend fun getUserByUsername(username : String) : User?
    suspend fun getUserByUserId(userId: String): User?
    suspend fun get(id : String) : User?
}