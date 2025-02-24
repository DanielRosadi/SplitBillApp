package com.ndhu.moneymanagementapp.repositories.impl

import com.ndhu.moneymanagementapp.enums.Collection
import com.ndhu.moneymanagementapp.models.Item
import com.ndhu.moneymanagementapp.repositories.IItemRepository
import com.ndhu.moneymanagementapp.repositories.IRepositories

class ItemRepository : Repositories<Item>(Item::class.java), IItemRepository{
    override suspend fun add(data: Item) {
        this.add(Collection.ITEM.collection, data)
    }

    override suspend fun getList(): List<Item> {
        return this.getList(Collection.ITEM.collection)
    }

    override suspend fun update(documentId: String, data: Item) {
        return this.update(Collection.ITEM.collection, documentId, data)
    }

    override suspend fun delete(documentId: String) {
        this.delete(Collection.ITEM.collection, documentId)
    }


}