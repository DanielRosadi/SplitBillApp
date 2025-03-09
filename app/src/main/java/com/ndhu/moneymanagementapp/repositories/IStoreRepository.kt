package com.ndhu.moneymanagementapp.repositories;

import com.ndhu.moneymanagementapp.models.Store

interface IStoreRepository {
    suspend fun add(data: Store)
    suspend fun getList(): List<Store>
    suspend fun update(id: String, data: Store)
    suspend fun delete(id: String)
    suspend fun get(id : String) : Store?
}
