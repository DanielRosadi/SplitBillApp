package com.ndhu.moneymanagementapp.seeds

import com.google.firebase.Timestamp
import com.ndhu.moneymanagementapp.models.Item
import com.ndhu.moneymanagementapp.models.Store
import com.ndhu.moneymanagementapp.models.User
import com.ndhu.moneymanagementapp.repositories.IItemRepository
import com.ndhu.moneymanagementapp.repositories.IStoreRepository
import com.ndhu.moneymanagementapp.repositories.IUserRepository
import com.ndhu.moneymanagementapp.repositories.impl.ItemRepository
import com.ndhu.moneymanagementapp.repositories.impl.StoreRepository
import com.ndhu.moneymanagementapp.repositories.impl.UserRepository
import com.ndhu.moneymanagementapp.utils.Util
import java.util.UUID
import kotlin.random.Random

object Seeders {

    val userRepo : IUserRepository = UserRepository()
    val storeRepo : IStoreRepository = StoreRepository()
    val itemRepo : IItemRepository = ItemRepository()

    suspend fun seedUsersData() {
        val users = List(10) { index ->
            User(
                id = UUID.randomUUID().toString(),
                username = "user${index + 1}",
                balance = (1000..5000).random(),
                email = "user${index + 1}@example.com",
                password = Util.passwordEncryption("test_1234"),
                age = (18..50).random(),
                createdAt = Timestamp.now()
            )
        }

        for(user in users) {
            this.userRepo.add(user)
        }
    }

    suspend fun seedStoresData() {
        val stores = List(10) { index ->
            Store(
                id = UUID.randomUUID().toString(),
                name = "Store ${index + 1}",
                description = "This is store ${index + 1}"
            )
        }

        for(store in stores) {
            this.storeRepo.add(store)
        }
    }

    suspend fun seedItemsData() {
        val stores = this.storeRepo.getList()

        for(store in stores) {
            val items = List(10) { index ->
                Item(
                    id = UUID.randomUUID().toString(),
                    name = "Item ke ${index+1} from ${store.name}",
                    price = Random.nextFloat() * (5000 - 1000) + 1000,
                    storeId = store.id
                )
            }

            for(item in items) {
                this.itemRepo.add(item)
            }
        }
    }


    suspend fun runSeeder() {
//        seedUsersData()
//        seedStoresData()
//        seedItemsData()
    }
}