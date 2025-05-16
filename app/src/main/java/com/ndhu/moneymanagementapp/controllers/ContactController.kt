package com.ndhu.moneymanagementapp.controllers

import android.content.Context
import android.util.Log
import com.ndhu.moneymanagementapp.enums.FriendStatus
import com.ndhu.moneymanagementapp.models.Contact
import com.ndhu.moneymanagementapp.repositories.IContactRepository
import com.ndhu.moneymanagementapp.repositories.IUserRepository
import com.ndhu.moneymanagementapp.repositories.impl.ContactRepository
import com.ndhu.moneymanagementapp.repositories.impl.UserRepository
import com.ndhu.moneymanagementapp.utils.Util
import java.util.UUID

class ContactController {

    val contactRepository: IContactRepository = ContactRepository()
    val userRepository: IUserRepository = UserRepository()

    suspend fun addContact(context: Context, userId: String) {
        val currentUserId = Util.getUserId(context).toString()

        if (currentUserId == userId) {
            Log.w("ContactController", "Cannot send a friend request to yourself.")
            return
        }

        val userExist = userRepository.getById(userId)
        if (userExist == null) {
            Log.w("ContactController", "User not found.")
            return
        }

        createFriendRequest(currentUserId, userId)
    }

    suspend fun getCurrUserLoginContact(context: Context): List<Contact> {
        return contactRepository.getUserContact(Util.getUserId(context).toString())
    }

    suspend fun getPendingUserListContact(context: Context): List<Contact> {
        return contactRepository.getPendingContact(Util.getUserId(context).toString())
    }

    suspend fun getRejectContact(context: Context): List<Contact> {
        return contactRepository.getRejectContacts(Util.getUserId(context).toString())
    }

    suspend fun acceptFriend(context: Context, senderId: String) {
        val currentUserId = Util.getUserId(context).toString()
        val contact = contactRepository.getContactFromUsers(senderId, currentUserId)
        if (contact?.friendStatus == FriendStatus.PENDING.name) {
            contact.friendStatus = FriendStatus.FRIEND.name
            contactRepository.update(contact.id, contact)
            Log.i("ContactController", "Friend request accepted from $senderId")
        } else {
            Log.w("ContactController", "No pending request from $senderId to accept")
        }
    }

    suspend fun rejectFriend(context: Context, senderId: String) {
        val currentUserId = Util.getUserId(context).toString()
        val contact = contactRepository.getContactFromUsers(senderId, currentUserId)
        if (contact?.friendStatus == FriendStatus.PENDING.name) {
            contact.friendStatus = FriendStatus.REJECT.name
            contactRepository.update(contact.id, contact)
            Log.i("ContactController", "Friend request rejected from $senderId")
        } else {
            Log.w("ContactController", "No pending request from $senderId to reject")
        }
    }

    suspend fun cancelFriendRequest(context: Context, receiverId: String) {
        val currentUserId = Util.getUserId(context).toString()
        val contact = contactRepository.getContactFromUsers(currentUserId, receiverId)
        if (contact?.friendStatus == FriendStatus.PENDING.name) {
            contactRepository.delete(contact.id)
            Log.i("ContactController", "Friend request to $receiverId cancelled.")
        } else {
            Log.w("ContactController", "No pending request to $receiverId to cancel.")
        }
    }

    private suspend fun createFriendRequest(sender: String, receiver: String) {
        val existingContact = contactRepository.getContactFromUsers(sender, receiver)

        if (existingContact != null) {
            when (existingContact.friendStatus) {
                FriendStatus.PENDING.name -> {
                    // Block both sender and receiver from re-sending
                    Log.w("ContactController", "There is already a pending friend request between you and this user.")
                    return
                }

                FriendStatus.REJECT.name -> {
                    // Allow only the *other* user (not the original sender) to send a new request
                    if (existingContact.senderId == sender) {
                        Log.w("ContactController", "You cannot resend a friend request after rejection.")
                        return
                    }

                    contactRepository.delete(existingContact.id)
                    Log.i("ContactController", "Old rejected request deleted to allow reversed request.")
                }

                FriendStatus.FRIEND.name -> {
                    Log.w("ContactController", "You are already friends with this user.")
                    return
                }
            }
        }

        // Create new request
        val newContact = Contact(
            id = UUID.randomUUID().toString(),
            senderId = sender,
            receiverId = receiver,
            friendStatus = FriendStatus.PENDING.name
        )

        contactRepository.add(newContact)
        Log.i("ContactController", "Friend request sent from $sender to $receiver.")
    }

}