package com.ndhu.moneymanagementapp.repositories;

import com.ndhu.moneymanagementapp.models.Store

interface IStoreRepository {
    suspend fun add(data: Store)
    suspend fun getList(): List<Store>
    suspend fun update(documentId: String, data: Store)
    suspend fun delete(documentId: String)
}
