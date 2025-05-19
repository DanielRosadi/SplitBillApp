package com.ndhu.moneymanagementapp.repositories.impl

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.ndhu.moneymanagementapp.handlers.FirebaseHandler
import com.ndhu.moneymanagementapp.repositories.IRepositories
import kotlinx.coroutines.tasks.await

open class Repositories<T>(private val clazz: Class<T>) : IRepositories<T> {

    private val firestore: FirebaseFirestore = FirebaseHandler.db

    override suspend fun add(collectionName: String, data: T) {
        try {
            val documentReference = firestore.collection(collectionName).add(data as Any).await()
            Log.d("SUCCESS", "DocumentSnapshot added with ID: ${documentReference.id}")
        } catch (e: Exception) {
            Log.w("ERROR", "Error adding document", e)
        }
    }

    override suspend fun getList(collectionName: String): List<T> {
        return try {
            val querySnapshot = firestore.collection(collectionName).get().await()
            querySnapshot.documents.mapNotNull { it.toObject(clazz) }
        } catch (e: Exception) {
            Log.w("ERROR", "Error getting documents", e)
            emptyList()
        }
    }

    override suspend fun update(collectionName: String, id: String, data: T) {
        try {
            val firestore = FirebaseFirestore.getInstance()
            val querySnapshot = firestore.collection(collectionName)
                .whereEqualTo("id", id)
                .limit(1)
                .get()
                .await()

            if (!querySnapshot.isEmpty) {
                val dataMap = Gson().toJson(data) // Convert object to JSON string
                val updates: Map<String, Any> = Gson().fromJson(dataMap, Map::class.java) as Map<String, Any>

                for (document in querySnapshot.documents) {
                    document.reference.update(updates).await()
                }
            }
        } catch (e: Exception) {
            Log.w("ERROR", "Error updating document", e)
        }
    }

    override suspend fun delete(collectionName: String, documentId: String) {
        try {
            val firestore = FirebaseFirestore.getInstance()
            val querySnapshot = firestore.collection(collectionName)
                .whereEqualTo("id", documentId)
                .limit(1)
                .get()
                .await()

            if (!querySnapshot.isEmpty) {
                for (document in querySnapshot.documents) {
                    document.reference.delete().await()
                }
            }
        } catch (e: Exception) {
            Log.w("ERROR", "Error deleting document", e)
        }
    }

    override suspend fun <T> getById(collectionName : String, id : String, clazz: Class<T>) : T? {
        return try {
            val firestore = FirebaseFirestore.getInstance()
            val querySnapshot = firestore.collection(collectionName)
                .whereEqualTo("id", id)
                .limit(1)
                .get()
                .await()

            if (!querySnapshot.isEmpty) {

                // DEBUG
                val document = querySnapshot.documents.firstOrNull()
                Log.d("FIRESTORE", "Raw document data: ${document?.data}")
                val obj = document?.toObject(clazz)
                Log.d("FIRESTORE", "Deserialized object: $obj")
                // obj
                // DEBUG

                querySnapshot.documents.first().toObject(clazz)

            } else {
                null
            }
        } catch (e: Exception) {
            Log.w("ERROR", "Error while trying to get document", e)
            null
        }
    }

}