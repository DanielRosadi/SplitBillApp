package com.ndhu.moneymanagementapp.repositories

import com.ndhu.moneymanagementapp.models.History

interface IHistoryRepository {
    suspend fun add(data: History)
    suspend fun getList(): List<History>
    suspend fun update(documentId: String, data: History)
    suspend fun delete(documentId: String)
}