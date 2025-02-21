package com.ndhu.moneymanagementapp.repositories

import com.ndhu.moneymanagementapp.models.User

interface IUserRepository{
    suspend fun add(data: Map<String, Any>)
    suspend fun getList(): List<Map<String, Any>>
    suspend fun update(documentId: String, data: Map<String, Any>)
    suspend fun delete(documentId: String)
    suspend fun getUserByEmail(email : String) : User?

    suspend fun getUserByUsername(username : String) : User?

}