package com.ndhu.moneymanagementapp.repositories.impl

import com.ndhu.moneymanagementapp.enums.Collection
import com.ndhu.moneymanagementapp.models.Store
import com.ndhu.moneymanagementapp.models.User
import com.ndhu.moneymanagementapp.repositories.IRepositories
import com.ndhu.moneymanagementapp.repositories.IStoreRepository

class StoreRepository : Repositories<Store>(Store::class.java), IStoreRepository{
    override suspend fun add(data: Store) {
        this.add(Collection.STORE.collection, data)
    }

    override suspend fun getList(): List<Store> {
        return this.getList(Collection.STORE.collection)
    }

    override suspend fun update(documentId: String, data: Store) {
        return this.update(Collection.STORE.collection, documentId, data)
    }

    override suspend fun delete(documentId: String) {
        return this.delete(Collection.STORE.collection, documentId)
    }

    override suspend fun get(id: String) : Store? {
        return this.getById(Collection.STORE.collection, id, Store::class.java)
    }


}