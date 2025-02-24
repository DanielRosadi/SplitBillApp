package com.ndhu.moneymanagementapp.repositories.impl

import com.ndhu.moneymanagementapp.enums.Collection
import com.ndhu.moneymanagementapp.models.History
import com.ndhu.moneymanagementapp.repositories.IHistoryRepository
import com.ndhu.moneymanagementapp.repositories.IRepositories

class HistoryRepository : Repositories<History>(History::class.java), IHistoryRepository{
    override suspend fun add(data: History) {
        this.add(Collection.HISTORY.collection, data)
    }

    override suspend fun getList(): List<History> {
        return this.getList(Collection.HISTORY.collection)
    }

    override suspend fun update(documentId: String, data: History) {
        this.update(Collection.HISTORY.collection, documentId, data)
    }

    override suspend fun delete(documentId: String) {
        this.delete(Collection.HISTORY.collection, documentId)
    }

}