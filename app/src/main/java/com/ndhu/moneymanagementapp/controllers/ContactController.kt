package com.ndhu.moneymanagementapp.controllers

import android.annotation.SuppressLint
import android.content.Context
import com.google.firebase.firestore.local.LocalStore
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
            userId2 = userId
        ))
    }

}