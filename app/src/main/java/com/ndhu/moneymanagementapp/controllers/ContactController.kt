package com.ndhu.moneymanagementapp.controllers

import android.annotation.SuppressLint
import android.content.Context
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
}