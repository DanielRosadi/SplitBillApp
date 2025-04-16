package com.ndhu.moneymanagementapp.controllers

import com.ndhu.moneymanagementapp.models.Item
import com.ndhu.moneymanagementapp.repositories.IItemRepository
import com.ndhu.moneymanagementapp.repositories.IStoreRepository
import com.ndhu.moneymanagementapp.repositories.impl.ItemRepository
import com.ndhu.moneymanagementapp.repositories.impl.StoreRepository
import java.util.UUID

class ItemController {

    val itemRepo : IItemRepository = ItemRepository()
    val storeRepo : IStoreRepository = StoreRepository()

    suspend fun getItemList() : List<Item> {
        return this.itemRepo.getList()
    }

    suspend fun getItemListBasedStoreId(storeId : String) : List<Item> {
        return this.itemRepo.getItemsByStoreId(storeId)
    }

    suspend fun addItem(storeId : String, itemName : String, itemPrice : Float) {
        itemRepo.add(Item(
            id = UUID.randomUUID().toString(),
            name = itemName,
            price = itemPrice,
            storeId = storeId
        ))
    }

    suspend fun searchItem(searchItemName : String, storeId : String) : Item? {
        return itemRepo.searchByNameAndStore(storeId, searchItemName)
    }

    suspend fun updateItem(itemId : String, itemUpdate : Item) {
        var item: Item? = this.itemRepo.getById(itemId) ?: return

        if(itemUpdate.name != item?.name) {
            item?.name = itemUpdate.name
        }

        if(itemUpdate.price != item?.price) {
            item?.price = itemUpdate.price
        }

        if (item != null) {
            this.itemRepo.update(itemId, item)
        }

        return
    }

}