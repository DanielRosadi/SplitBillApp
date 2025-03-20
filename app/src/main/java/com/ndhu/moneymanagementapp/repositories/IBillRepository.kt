package com.ndhu.moneymanagementapp.repositories

import com.ndhu.moneymanagementapp.models.Bill
import com.ndhu.moneymanagementapp.models.User

interface IBillRepository {
    suspend fun add(data: Bill)
    suspend fun getList(): List<Bill>
    suspend fun update(documentId: String, data: Bill)
    suspend fun delete(documentId: String)
    suspend fun getBillListBasedOnOwnerId(ownerId : String) : List<Bill>

}