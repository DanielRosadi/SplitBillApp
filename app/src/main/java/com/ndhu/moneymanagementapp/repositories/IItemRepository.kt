package com.ndhu.moneymanagementapp.repositories

import com.ndhu.moneymanagementapp.models.Item

interface IItemRepository  {
    suspend fun add(data: Item)
    suspend fun getList(): List<Item>
    suspend fun update(documentId: String, data: Item)
    suspend fun delete(documentId: String)
    suspend fun searchByNameAndStore(storeId : String, itemName : String) : Item?
}