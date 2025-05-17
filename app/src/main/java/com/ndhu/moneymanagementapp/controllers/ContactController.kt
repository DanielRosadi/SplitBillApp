package com.ndhu.moneymanagementapp.controllers

import android.content.Context
import android.util.Log
import com.ndhu.moneymanagementapp.enums.ContactAddResult
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

    suspend fun addContact(context: Context, userId: String): ContactAddResult {
        val currentUserId = Util.getUserId(context).toString()

        if (currentUserId == userId) {
            Log.w("ContactController", "Cannot send a friend request to yourself.")
            return ContactAddResult.SELF_ADD
        }

        val userExist = userRepository.getById(userId)
        if (userExist == null) {
            Log.w("ContactController", "User not found.")
            return ContactAddResult.USER_NOT_FOUND
        }

        return createFriendRequest(currentUserId, userId)
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

    private suspend fun createFriendRequest(sender: String, receiver: String): ContactAddResult {
        // Check if there is already a contact between the two users
        val existingContact = contactRepository.getContactFromUsers(sender, receiver)

        if (existingContact != null) {
            return when (existingContact.friendStatus) {
                // 1. request is already pending
                FriendStatus.PENDING.name -> {
                    Log.w("ContactController", "A pending request already exists between $sender and $receiver.")
                    ContactAddResult.REQUEST_ALREADY_PENDING
                }

                // 2. previous request was rejected
                FriendStatus.REJECT.name -> {
                    // Block if the same sender tries to resend
                    if (existingContact.senderId == sender) {
                        Log.w("ContactController", "Sender $sender cannot resend a rejected request to $receiver.")
                        return ContactAddResult.REQUEST_REJECTED_BLOCKED
                    }

                    // Allow the other user to reverse the request by deleting the old one and creating a new one
                    contactRepository.delete(existingContact.id)
                    contactRepository.add(
                        Contact(UUID.randomUUID().toString(), sender, receiver, FriendStatus.PENDING.name)
                    )
                    Log.i("ContactController", "Reversed friend request sent from $sender to $receiver.")
                    return ContactAddResult.SUCCESS
                }

                // 3. Users are already friends
                FriendStatus.FRIEND.name -> {
                    Log.w("ContactController", "You are already friends with this user: $receiver")
                    ContactAddResult.ALREADY_FRIENDS
                }

                // 4. Unexpected/unsupported status
                else -> {
                    Log.e("ContactController", "Unexpected friend status between $sender and $receiver: ${existingContact.friendStatus}")
                    ContactAddResult.ERROR
                }
            }
        }

        // 5. No existing relationship, create a new friend request
        val newContact = Contact(
            id = UUID.randomUUID().toString(),
            senderId = sender,
            receiverId = receiver,
            friendStatus = FriendStatus.PENDING.name
        )

        contactRepository.add(newContact)
        Log.i("ContactController", "Friend request sent from $sender to $receiver.")
        return ContactAddResult.SUCCESS
    }

}