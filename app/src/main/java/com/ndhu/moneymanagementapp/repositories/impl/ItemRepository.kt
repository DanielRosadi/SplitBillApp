package com.ndhu.moneymanagementapp.repositories.impl

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ndhu.moneymanagementapp.enums.Collection
import com.ndhu.moneymanagementapp.models.BillItemDetail
import com.ndhu.moneymanagementapp.models.Item
import com.ndhu.moneymanagementapp.repositories.IItemRepository
import kotlinx.coroutines.tasks.await

class ItemRepository : Repositories<Item>(Item::class.java), IItemRepository{

    private val firestore: FirebaseFirestore = Firebase.firestore

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

    override suspend fun searchByNameAndStore(storeId : String, itemName : String) : Item? {
        return try {
            val querySnapshot = firestore.collection(Collection.ITEM.collection)
                .whereEqualTo("storeId", storeId)
                .whereEqualTo("name", itemName)
                .get()
                .await()

            querySnapshot.documents.firstOrNull()?.toObject(Item::class.java)
        } catch (e: Exception) {
            Log.w("ERROR", "Error getting user by username", e)
            null
        }
    }

    override suspend fun getById(id : String) : Item? {
        return this.getById(Collection.ITEM.collection, id, Item::class.java)
    }

    override suspend fun getItemsByStoreId(storeId : String) : List<Item> {
        var itemList = mutableListOf<Item>()
        val querySnapshot = firestore.collection(Collection.ITEM.collection)
            .whereEqualTo("storeId", storeId)
            .get()
            .await()

        if(!querySnapshot.isEmpty){
            for(document in querySnapshot.documents) {
                document.toObject(Item::class.java)?.let {
                    itemList.add(it)
                }
            }
        }

        return itemList
    }
}