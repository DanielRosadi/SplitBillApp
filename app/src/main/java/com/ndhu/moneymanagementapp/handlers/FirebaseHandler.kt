package com.ndhu.moneymanagementapp.handlers

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

object FirebaseHandler {
    val db: FirebaseFirestore by lazy { Firebase.firestore }

    suspend fun isConnected(): Boolean {
        return try {
            db.collection("test").limit(1).get().await()
            Log.d("FIREBASE", "Firestore is accessible")
            true
        } catch (e: Exception) {
            Log.e("FIREBASE_ERROR", "Firestore connection failed", e)
            false
        }
    }
}
