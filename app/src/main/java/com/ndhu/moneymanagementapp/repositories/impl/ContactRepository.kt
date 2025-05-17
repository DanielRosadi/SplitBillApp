package com.ndhu.moneymanagementapp.repositories.impl

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ndhu.moneymanagementapp.enums.Collection
import com.ndhu.moneymanagementapp.enums.FriendStatus
import com.ndhu.moneymanagementapp.models.Contact
import com.ndhu.moneymanagementapp.repositories.IContactRepository
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

    // Get all contacts where the user is either sender or receiver and the status is FRIEND
    override suspend fun getUserContact(userId: String): List<Contact> {
        val contacts = mutableSetOf<Contact>()

        val db = Firebase.firestore
        val collectionRef = db.collection(Collection.CONTACT.collection)

        // Query for user as sender
        val query1 = collectionRef
            .whereEqualTo("senderId", userId)
            .whereEqualTo("friendStatus", FriendStatus.FRIEND.name)
            .get()
            .await()

        // Query for user as receiver
        val query2 = collectionRef
            .whereEqualTo("receiverId", userId)
            .whereEqualTo("friendStatus", FriendStatus.FRIEND.name)
            .get()
            .await()

        // Convert documents to Contact objects
        query1.documents.forEach { doc ->
            doc.toObject(Contact::class.java)?.let { contacts.add(it) }
        }

        query2.documents.forEach { doc ->
            doc.toObject(Contact::class.java)?.let { contacts.add(it) }
        }

        return contacts.toList()
    }

    override suspend fun getPendingContact(userId: String): List<Contact> {
        val querySnapshot = Firebase.firestore.collection(Collection.CONTACT.collection)
            .whereEqualTo("receiverId", userId)
            .whereEqualTo("friendStatus", FriendStatus.PENDING.name)
            .get()
            .await()

        return querySnapshot.documents.mapNotNull { it.toObject(Contact::class.java) }
    }

    // Get a specific contact between two users
    override suspend fun getContactFromUsers(userId1: String, userId2: String): Contact? {
        val collectionRef = Firebase.firestore.collection(Collection.CONTACT.collection)

        // Try direction 1 → 2
        val query1 = collectionRef
            .whereEqualTo("senderId", userId1)
            .whereEqualTo("receiverId", userId2)
            .limit(1)
            .get()
            .await()

        if (!query1.isEmpty) {
            return query1.first().toObject(Contact::class.java)
        }

        // Try direction 2 → 1
        val query2 = collectionRef
            .whereEqualTo("senderId", userId2)
            .whereEqualTo("receiverId", userId1)
            .limit(1)
            .get()
            .await()

        return if (!query2.isEmpty) query2.first().toObject(Contact::class.java) else null
    }

    // Get all contacts by userId with a specific friend status
    override suspend fun getUserContactsByStatus(userId1 : String, friendStatus : FriendStatus) : List<Contact> {
        var contacts = mutableListOf<Contact>()
        val querySnapshot = Firebase.firestore.collection(Collection.CONTACT.collection).
        whereEqualTo("senderId", userId1).
        whereEqualTo("friendStatus", friendStatus.name).
        get().await()

        // Convert documents to Contact objects
        if(!querySnapshot.isEmpty) {
            for (document in querySnapshot.documents) {
                document.toObject(Contact::class.java)?.let { contacts.add(it) }
            }
        }
        return contacts
    }

    // Get all rejected contact requests where the user is the receiver
    override suspend fun getRejectContacts(userId : String) : List<Contact> {
        var contacts = mutableListOf<Contact>()
        val querySnapshot = Firebase.firestore.collection(Collection.CONTACT.collection).
        whereEqualTo("receiverId", userId).
        whereEqualTo("friendStatus", FriendStatus.REJECT.name).
        get().await()

        // Convert documents to Contact objects
        if(!querySnapshot.isEmpty) {
            for (document in querySnapshot.documents) {
                document.toObject(Contact::class.java)?.let { contacts.add(it) }
            }
        }
        return contacts
    }
}