package com.ndhu.moneymanagementapp.repositories.impl

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.ndhu.moneymanagementapp.enums.Collection
import com.ndhu.moneymanagementapp.enums.FriendStatus
import com.ndhu.moneymanagementapp.models.Contact
import com.ndhu.moneymanagementapp.repositories.IContactRepository
import com.ndhu.moneymanagementapp.repositories.IRepositories
import kotlinx.coroutines.tasks.await

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

    override suspend fun getUserContact(userId : String) : List<Contact> {
        val contacts = mutableListOf<Contact>()
        val querySnapshot = Firebase.firestore.collection(Collection.CONTACT.collection).whereEqualTo("userId1", userId).get().await()
        if(!querySnapshot.isEmpty) {
            for (document in querySnapshot.documents) {
                document.toObject(Contact::class.java)?.let { contacts.add(it) }
            }
        }
        return contacts
    }

    override suspend fun getPendingContact(userId : String) : List<Contact> {
        val contacts = mutableListOf<Contact>()
        val querySnapshot = Firebase.firestore.collection(Collection.CONTACT.collection).
            whereEqualTo("userId2", userId).
            whereEqualTo("friendStatus", FriendStatus.PENDING).
            get().await()
        if(!querySnapshot.isEmpty) {
            for (document in querySnapshot.documents) {
                document.toObject(Contact::class.java)?.let { contacts.add(it) }
            }
        }
        return contacts
    }

    override suspend fun getContactFromUsers(userId1 : String, userId2 : String) : Contact {
        var contact = Contact()

        val querySnapshot = Firebase.firestore.collection(Collection.CONTACT.collection).
            whereEqualTo("userId1", userId1).
            whereEqualTo("userId2", userId2).
            limit(1).
            get().await()

        if(!querySnapshot.isEmpty) {
            contact = querySnapshot.first().toObject(Contact::class.java)
        }

        return contact
    }
}