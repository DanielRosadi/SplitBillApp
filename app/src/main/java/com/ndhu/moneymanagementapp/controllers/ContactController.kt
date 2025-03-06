package com.ndhu.moneymanagementapp.controllers

import android.annotation.SuppressLint
import com.google.firebase.firestore.local.LocalStore
import com.ndhu.moneymanagementapp.models.Contact
import com.ndhu.moneymanagementapp.repositories.IContactRepository
import com.ndhu.moneymanagementapp.repositories.impl.ContactRepository
import java.util.UUID

class ContactController {

    val contactRepository : IContactRepository = ContactRepository()

    suspend fun addContact(userId : String) {
        contactRepository.add(Contact(
            id = UUID.randomUUID().toString(),
            userId1 = "",
            userId2 = userId
        ))
    }

}