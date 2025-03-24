package com.ndhu.moneymanagementapp.sharedviewmodels

import androidx.lifecycle.ViewModel
import com.ndhu.moneymanagementapp.models.Bill
import com.ndhu.moneymanagementapp.models.Contact
import com.ndhu.moneymanagementapp.models.Item
import com.ndhu.moneymanagementapp.models.Store

// ignore this for now
class SharedViewModels {
}

/*
This file will contains SharedViewModel of every database table/collection.
The purpose is for data distribution between screens.
These SharedViewModels will be global across screens
And developer only need to pass it onto the screen via AppNavigation.kt
And pass respective dataId to find specific database record/row/document
 */

class SharedStoreViewModel : ViewModel() {

    var storeList: MutableList<Store>? = null

    // selectedStore for the bill
    var selectedStore: Store? = null

    // list of items for the bill
    var itemList: List<Item>? = null

    // list of friends/contacts participating in the bill
    var friendList: List<Contact>? = null

    // bill owner of the bill
    var billOwner: Contact? = null



    // to contains information of one bill (1 store, items, friends, 1 bill owner, item assigns)
    var bill: Bill? = null





    fun getStoreById(id: String): Store? {
        return storeList?.find { it.id == id }
    }

    fun addStore(store: Store){
        storeList?.add(store)
    }

    fun setSelectedStore(store: Store?){
        selectedStore = store
    }

    fun setItemList(listOfItems: List<Item>?){
        itemList = listOfItems
    }

    fun setFriendList(listOfFriends: List<Contact>?){
        friendList = listOfFriends
    }

    fun setBillOwner(contact: Contact?){
        billOwner = contact
    }


}