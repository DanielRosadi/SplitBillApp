package com.ndhu.moneymanagementapp.repositories.impl

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ndhu.moneymanagementapp.handlers.FirebaseHandler
import com.ndhu.moneymanagementapp.repositories.IRepositories
import kotlinx.coroutines.tasks.await

class Repositories : IRepositories {

    private val firestore: FirebaseFirestore = FirebaseHandler.db

    override suspend fun add(collectionName: String, data: Map<String, Any>) {
        try {
            val documentReference = firestore.collection(collectionName).add(data).await()
            Log.d("SUCCESS", "DocumentSnapshot added with ID: ${documentReference.id}")
        } catch (e: Exception) {
            Log.w("ERROR", "Error adding document", e)
        }
    }

    override suspend fun getList(collectionName: String): List<Map<String, Any>> {
        return try {
            val querySnapshot = firestore.collection(collectionName).get().await()
            querySnapshot.documents.map { it.data ?: emptyMap() }
        } catch (e: Exception) {
            Log.w("ERROR", "Error getting documents", e)
            emptyList()
        }
    }

    override suspend fun update(collectionName: String, documentId: String, data: Map<String, Any>) {
        try {
            firestore.collection(collectionName).document(documentId).update(data).await()
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