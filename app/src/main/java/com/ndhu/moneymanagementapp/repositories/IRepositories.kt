package com.ndhu.moneymanagementapp.repositories

interface IRepositories<T> {
    suspend fun add(collectionName: String, data: T)
    suspend fun getList(collectionName: String): List<T>
    suspend fun update(collectionName: String, documentId: String, data: T)
    suspend fun delete(collectionName: String, documentId: String)
}