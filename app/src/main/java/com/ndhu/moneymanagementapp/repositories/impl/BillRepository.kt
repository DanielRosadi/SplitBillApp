package com.ndhu.moneymanagementapp.repositories.impl

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ndhu.moneymanagementapp.enums.Collection
import com.ndhu.moneymanagementapp.handlers.FirebaseHandler
import com.ndhu.moneymanagementapp.models.Bill
import com.ndhu.moneymanagementapp.repositories.IBillRepository
import kotlinx.coroutines.tasks.await

class BillRepository() : Repositories<Bill>(Bill::class.java), IBillRepository {

    private val firestore: FirebaseFirestore = Firebase.firestore

    override suspend fun add(data: Bill) {
        this.add(Collection.BILL.collection, data)
    }

    override suspend fun getList(): List<Bill> {
        return this.getList(Collection.BILL.collection)
    }

    override suspend fun update(id: String, data: Bill) {
        return this.update(Collection.BILL.collection, id, data)
    }

    override suspend fun delete(documentId: String) {
        this.delete(Collection.BILL.collection, documentId)
    }

    override suspend fun getBillListBasedOnOwnerId(ownerId: String): List<Bill> {
        var billList = mutableListOf<Bill>()
        val querySnapshot = firestore.collection(Collection.BILL.collection)
            .whereEqualTo("ownerId", ownerId)
            .get()
            .await()

        if(!querySnapshot.isEmpty){
            for(document in querySnapshot.documents) {
                var bill = document.toObject(Bill::class.java)?.let {
                    billList.add(it)
                }
            }
        }
        return billList
    }


    override suspend fun getById(id: String): Bill? {
        return this.getById(Collection.BILL.collection, id, Bill::class.java)
    }
}