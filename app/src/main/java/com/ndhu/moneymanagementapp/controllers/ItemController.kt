package com.ndhu.moneymanagementapp.controllers

import com.ndhu.moneymanagementapp.models.Item
import com.ndhu.moneymanagementapp.repositories.IItemRepository
import com.ndhu.moneymanagementapp.repositories.IStoreRepository
import com.ndhu.moneymanagementapp.repositories.impl.ItemRepository
import com.ndhu.moneymanagementapp.repositories.impl.StoreRepository

class ItemController {

    val itemRepo : IItemRepository = ItemRepository()
    val storeRepo : IStoreRepository = StoreRepository()

    suspend fun getItemList() : List<Item> {
        return this.itemRepo.getList()
    }

    suspend fun getItemListBasedStoreId(storeId : String) : List<Item> {
        return emptyList()
    }

}