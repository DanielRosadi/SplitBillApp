package com.ndhu.moneymanagementapp.repositories.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ndhu.moneymanagementapp.enums.Collection
import com.ndhu.moneymanagementapp.models.Bill
import com.ndhu.moneymanagementapp.models.BillItemDetail
import com.ndhu.moneymanagementapp.repositories.IBillItemDetailRepository
import kotlinx.coroutines.tasks.await

class BillDetailRepository : Repositories<BillItemDetail>(BillItemDetail::class.java), IBillItemDetailRepository {
    private val firestore: FirebaseFirestore = Firebase.firestore
    override suspend fun add(data: BillItemDetail) {
        this.add(Collection.BILLITEMDETAIL.collection, data)
    }

    override suspend fun getList(): List<BillItemDetail> {
        return this.getList(Collection.BILLITEMDETAIL.collection)
    }

    override suspend fun update(id: String, data: BillItemDetail) {
        return this.update(Collection.BILLITEMDETAIL.collection, id, data)
    }

    override suspend fun delete(id: String) {
        this.delete(Collection.BILLITEMDETAIL.collection, id)
    }

    override suspend fun getBillListBasedOnUserId(userId: String): List<BillItemDetail> {
        var billItemList = mutableListOf<BillItemDetail>()
        val querySnapshot = firestore.collection(Collection.BILLITEMDETAIL.collection)
            .whereEqualTo("userId", userId)
            .get()
            .await()

        if(!querySnapshot.isEmpty){
            for(document in querySnapshot.documents) {
                var bill = document.toObject(BillItemDetail::class.java)?.let {
                    billItemList.add(it)
                }
            }
        }
        return billItemList
    }

    override suspend fun getByBillId(billId: String) : List<BillItemDetail> {
        var billItemList = mutableListOf<BillItemDetail>()
        val querySnapshot = firestore.collection(Collection.BILLITEMDETAIL.collection)
            .whereEqualTo("billId", billId)
            .get()
            .await()

        if(!querySnapshot.isEmpty){
            for(document in querySnapshot.documents) {
                var bill = document.toObject(BillItemDetail::class.java)?.let {
                    billItemList.add(it)
                }
            }
        }
        return billItemList
    }
}