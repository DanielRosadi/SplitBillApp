package com.ndhu.moneymanagementapp.repositories.impl

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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

    override suspend fun update(collectionName: String, documentId: String, data: T) {
        try {
            firestore.collection(collectionName).document(documentId).set(data as Any).await()
            Log.d("SUCCESS", "DocumentSnapshot successfully updated!")
        } catch (e: Exception) {
            Log.w("ERROR", "Error updating document", e)
        }
    }

    override suspend fun delete(collectionName: String, documentId: String) {
        try {
            firestore.collection(collectionName).document(documentId).delete().await()
            Log.d("SUCCESS", "DocumentSnapshot successfully deleted!")
        } catch (e: Exception) {
            Log.w("ERROR", "Error deleting document", e)
        }
    }
}