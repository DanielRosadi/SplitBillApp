package com.ndhu.moneymanagementapp.repositories

interface IRepositories {
    suspend fun add(collectionName: String, data: Map<String, Any>)
    suspend fun getList(collectionName: String): List<Map<String, Any>>
    suspend fun update(collectionName: String, documentId: String, data: Map<String, Any>)
    suspend fun delete(collectionName: String, documentId: String)
}