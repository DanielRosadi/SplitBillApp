package com.ndhu.moneymanagementapp.controllers

import com.ndhu.moneymanagementapp.models.Store
import com.ndhu.moneymanagementapp.repositories.IStoreRepository
import com.ndhu.moneymanagementapp.repositories.impl.StoreRepository

class StoreController {

    val storeRepository : IStoreRepository = StoreRepository()

    suspend fun getListOfStores() : List<Store> {
        return this.storeRepository.getList()
    }

}