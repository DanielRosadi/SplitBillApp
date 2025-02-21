package com.ndhu.moneymanagementapp.repositories.impl

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ndhu.moneymanagementapp.models.User
import com.ndhu.moneymanagementapp.repositories.IRepositories
import com.ndhu.moneymanagementapp.repositories.IUserRepository
import kotlinx.coroutines.tasks.await

class UserRepository : IUserRepository {

    private val iRepositories : IRepositories = Repositories()

    private val firestore: FirebaseFirestore = Firebase.firestore

    override suspend fun getUserByEmail(email: String): User? {
        return try {
            val querySnapshot = firestore.collection("users")
                .whereEqualTo("email", email)
                .get()
                .await()

            val document = querySnapshot.documents.firstOrNull()
            document?.toObject(User::class.java)
        } catch (e: Exception) {
            Log.w("ERROR", "Error getting user by email", e)
            null
        }
    }

    override suspend fun getUserByUsername(username: String): User? {
        return try {
            val querySnapshot = firestore.collection("users")
                .whereEqualTo("username", username)
                .get()
                .await()

            val document = querySnapshot.documents.firstOrNull()
            document?.toObject(User::class.java)
        } catch (e: Exception) {
            Log.w("ERROR", "Error getting user by username", e)
            null
        }
    }


    override suspend fun add(data: Map<String, Any>) {
        this.iRepositories.add("users", data)
    }

    override suspend fun getList(): List<Map<String, Any>> {
        return this.iRepositories.getList("users")
    }

    override suspend fun update(documentId: String, data: Map<String, Any>) {
        return this.iRepositories.update("users", documentId, data)
    }

    override suspend fun delete(documentId: String) {
        return this.iRepositories.delete("users", documentId)
    }
}