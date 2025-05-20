package com.ndhu.moneymanagementapp.controllers

import com.ndhu.moneymanagementapp.models.Store
import com.ndhu.moneymanagementapp.repositories.IStoreRepository
import com.ndhu.moneymanagementapp.repositories.impl.StoreRepository
import java.util.UUID

class StoreController {

    val storeRepository : IStoreRepository = StoreRepository()

    suspend fun getListOfStores() : List<Store> {
        return this.storeRepository.getList()
    }

    suspend fun addStore(name : String, address : String, description : String) : Store {
        val store = Store(
            id = UUID.randomUUID().toString(),
            name= name,
            address = address,
            description = description
        )
        this.storeRepository.add(store)
        return store
    }

    suspend fun updateStore(id : String, newName : String, newAddress : String, newDescription : String) : Store? {
        var oldStoreData = this.storeRepository.getById(id)

        if(oldStoreData != null) {
            if(newName != "") {
                oldStoreData?.name = newName
            }

            if(newAddress != "") {
                oldStoreData?.address = newAddress
            }

            if(newDescription != "") {
                oldStoreData?.description = newDescription
            }

            this.storeRepository.update(id, oldStoreData)
        }


        return null
    }

    suspend fun deleteStore(id : String) {
        this.storeRepository.delete(id)
    }

    suspend fun getStore(storeId: String): Store?{
        return this.storeRepository.getById(storeId)
    }

}