package com.ndhu.moneymanagementapp.repositories.impl

import com.ndhu.moneymanagementapp.enums.Collection
import com.ndhu.moneymanagementapp.models.Contact
import com.ndhu.moneymanagementapp.repositories.IContactRepository
import com.ndhu.moneymanagementapp.repositories.IRepositories

class ContactRepository : Repositories<Contact>(Contact::class.java), IContactRepository{
    override suspend fun add(data: Contact) {
        this.add(Collection.CONTACT.collection, data)
    }

    override suspend fun getList(): List<Contact> {
        return this.getList(Collection.CONTACT.collection)
    }

    override suspend fun update(documentId: String, data: Contact) {
        return this.update(Collection.CONTACT.collection, documentId, data)
    }

    override suspend fun delete(documentId: String) {
        return this.delete(Collection.CONTACT.collection, documentId)
    }


}