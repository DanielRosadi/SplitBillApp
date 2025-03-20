package com.ndhu.moneymanagementapp.repositories

import com.ndhu.moneymanagementapp.models.BillItemDetail

interface IBillItemDetailRepository {

    suspend fun add(data: BillItemDetail)
    suspend fun getList(): List<BillItemDetail>
    suspend fun update(documentId: String, data: BillItemDetail)
    suspend fun delete(documentId: String)
    suspend fun getBillListBasedOnUserId(userId : String) : List<BillItemDetail>
}