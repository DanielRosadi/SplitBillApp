package com.ndhu.moneymanagementapp.repositories.impl

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ndhu.moneymanagementapp.enums.Collection
import com.ndhu.moneymanagementapp.models.User
import com.ndhu.moneymanagementapp.repositories.IUserRepository
import kotlinx.coroutines.tasks.await

class UserRepository : Repositories<User>(User::class.java), IUserRepository  {


    private val firestore: FirebaseFirestore = Firebase.firestore
    override suspend fun add(data: User) {
        this.add(Collection.USER.collection, data)
    }

    override suspend fun getList(): List<User> {
        return this.getList(Collection.USER.collection)
    }

    override suspend fun update(documentId: String, data: User) {
        return this.update(Collection.USER.collection, documentId, data)
    }

    override suspend fun delete(documentId: String) {
        return this.delete(Collection.USER.name, documentId)
    }


    override suspend fun getUserByEmail(email: String): User? {
        return try {
            val querySnapshot = firestore.collection(Collection.USER.collection)
                .whereEqualTo("email", email)
                .get()
                .await()

            querySnapshot.documents.firstOrNull()?.toObject(User::class.java)
        } catch (e: Exception) {
            Log.w("ERROR", "Error getting user by email", e)
            null
        }
    }

    override suspend fun getUserByUsername(username: String): User? {
        return try {
            val querySnapshot = firestore.collection(Collection.USER.collection)
                .whereEqualTo("username", username)
                .get()
                .await()

            querySnapshot.documents.firstOrNull()?.toObject(User::class.java)
        } catch (e: Exception) {
            Log.w("ERROR", "Error getting user by username", e)
            null
        }
    }

    override suspend fun getById(id : String) : User? {
        return this.getById(Collection.USER.collection, id, User::class.java)
    }
}