package com.ndhu.moneymanagementapp.repositories

import com.ndhu.moneymanagementapp.models.Contact

interface IContactRepository {
    suspend fun add(data: Contact)
    suspend fun getList(): List<Contact>
    suspend fun update(documentId: String, data: Contact)
    suspend fun delete(documentId: String)
    suspend fun getUserContact(userId : String) : List<Contact>
    suspend fun getPendingContact(userId : String) : List<Contact>
    suspend fun getContactFromUsers(userId1 : String, userId2 : String) : Contact
}