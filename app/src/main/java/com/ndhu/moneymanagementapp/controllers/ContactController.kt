package com.ndhu.moneymanagementapp.controllers

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.google.firebase.firestore.local.LocalStore
import com.ndhu.moneymanagementapp.enums.FriendStatus
import com.ndhu.moneymanagementapp.models.Contact
import com.ndhu.moneymanagementapp.models.User
import com.ndhu.moneymanagementapp.repositories.IContactRepository
import com.ndhu.moneymanagementapp.repositories.IUserRepository
import com.ndhu.moneymanagementapp.repositories.impl.ContactRepository
import com.ndhu.moneymanagementapp.repositories.impl.UserRepository
import com.ndhu.moneymanagementapp.utils.Util
import java.util.UUID

class ContactController {

    val contactRepository : IContactRepository = ContactRepository()
    val userRepository : IUserRepository = UserRepository()

    suspend fun addContact(context : Context, userId : String) {
        if(Util.getUserId(context).toString() == userId) {
            Log.d("ERROR", "TAK BISA ADD DIRI SEORANG WA")
            return
        }

        val userExist = userRepository.getById(userId)
        if (userExist == null) {
            Log.d("ERROR", "TAK BISA ADD USER YG GAK ADA")
            return
        }

        var requestBy = Util.getUserId(context).toString()
        makeFriend(Util.getUserId(context).toString(), userId, requestBy)
        makeFriend(userId, Util.getUserId(context).toString(), requestBy)
    }

    suspend fun getCurrUserLoginContact(context : Context) : List<Contact> {
        return contactRepository.getUserContact(Util.getUserId(context).toString())
    }

    suspend fun getPendingUserListContact(context: Context) : List<Contact> {
        return contactRepository.getPendingContact(Util.getUserId(context).toString())
    }

    suspend fun acceptFriend(context : Context, userId : String)  {
        acceptFriend(Util.getUserId(context).toString(), userId)
        acceptFriend(userId, Util.getUserId(context).toString())
    }

    suspend fun rejectFriend(context : Context, userId : String) {
        rejectFriend(Util.getUserId(context).toString(), userId)
        rejectFriend(userId, Util.getUserId(context).toString())
    }

    private suspend fun makeFriend(user1 : String, user2 : String, requestBy : String) {
        var contact = contactRepository.getContactFromUsers(user1, user2)
        if(contact != null && contact.friendStatus == FriendStatus.REJECT.name) {
            contactRepository.delete(contact.id)
        }
        contactRepository.add(Contact(
            id = UUID.randomUUID().toString(),
            userId1 = user1,
            userId2 = user2,
            friendStatus = FriendStatus.PENDING.name,
            requestedBy = requestBy
        ))
    }

    private suspend fun acceptFriend(user1 : String, user2 : String) {
        var contact = contactRepository.getContactFromUsers(user1, user2)
        if(contact.friendStatus == FriendStatus.PENDING.name) {
            contact.friendStatus = FriendStatus.FRIEND.name
            contactRepository.update(contact.id, contact)
            return
        }
    }

    private suspend fun rejectFriend(user1 : String, user2 : String) {
        var contact = contactRepository.getContactFromUsers(user1, user2)
        if(contact.friendStatus == FriendStatus.PENDING.name) {
            contact.friendStatus = FriendStatus.REJECT.name
            contactRepository.update(contact.id, contact)
            Log.w("INFO", "YAHAHAHAHHA KENA REJECT")
            return
        }
        Log.w("INFO", "ckptw sih gak ada contact")
    }
}