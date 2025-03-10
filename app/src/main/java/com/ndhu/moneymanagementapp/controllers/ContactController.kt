package com.ndhu.moneymanagementapp.controllers

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.google.firebase.firestore.local.LocalStore
import com.ndhu.moneymanagementapp.enums.FriendStatus
import com.ndhu.moneymanagementapp.models.Contact
import com.ndhu.moneymanagementapp.repositories.IContactRepository
import com.ndhu.moneymanagementapp.repositories.impl.ContactRepository
import com.ndhu.moneymanagementapp.utils.Util
import java.util.UUID

class ContactController {

    val contactRepository : IContactRepository = ContactRepository()

    suspend fun addContact(context : Context, userId : String) {
        contactRepository.add(Contact(
            id = UUID.randomUUID().toString(),
            userId1 = Util.getUserId(context).toString(),
            userId2 = userId,
            friendStatus = FriendStatus.PENDING.name
        ))
    }

    suspend fun getCurrUserLoginContact(context : Context) : List<Contact> {
        return contactRepository.getUserContact(Util.getUserId(context).toString())
    }

    suspend fun getPendingUserListContact(context: Context) : List<Contact> {
        return contactRepository.getPendingContact(Util.getUserId(context).toString())
    }

    suspend fun acceptFriend(context : Context, userId : String)  {
        val userId2 = Util.getUserId(context).toString()
        var contact = contactRepository.getContactFromUsers(userId, userId2)
        if(contact.friendStatus == FriendStatus.PENDING.name) {
            //update status from userId1
            contact.friendStatus = FriendStatus.FRIEND.name
            contactRepository.update(contact.id, contact)

            var contact2 = Contact(
                id = UUID.randomUUID().toString(),
                userId1 = userId2,
                userId2 = userId2,
                friendStatus = FriendStatus.FRIEND.name
            )
            contactRepository.add(contact2)
            return
        }
        Log.w("INFO", "ckptw sih gak ada contact")
    }

    suspend fun rejectFriend(context : Context, userId : String) {
        val userId2 = Util.getUserId(context).toString()
        var contact = contactRepository.getContactFromUsers(userId, userId2)
        if(contact.friendStatus == FriendStatus.PENDING.name) {
            //update status from userId1
            contact.friendStatus = FriendStatus.REJECT.name
            contactRepository.update(contact.id, contact)
            Log.w("INFO", "YAHAHAHAHHA KENA REJECT")
            return
        }
        Log.w("INFO", "ckptw sih gak ada contact")
    }
}