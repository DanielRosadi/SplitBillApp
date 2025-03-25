package com.ndhu.moneymanagementapp.sharedviewmodels

import androidx.lifecycle.ViewModel
import com.ndhu.moneymanagementapp.models.Bill
import com.ndhu.moneymanagementapp.models.Contact
import com.ndhu.moneymanagementapp.models.Item
import com.ndhu.moneymanagementapp.models.Store

// ignore this for
/*
class SharedViewModels {
}

 */

/*
This file will contains SharedViewModel of every database table/collection.
The purpose is for data distribution between screens.
These SharedViewModels will be global across screens
And developer only need to pass it onto the screen via AppNavigation.kt
And pass respective dataId to find specific database record/row/document
 */

class SharedViewModel : ViewModel() {

    var storeList: MutableList<Store>? = null

    // selectedStore for the bill
    private var selectedStore: Store? = null

    // list of items for the bill
    private var itemList: List<Item>? = emptyList()

    // list of friends/contacts participating in the bill
    private var friendList: List<Contact>? = emptyList()

    // bill owner of the bill
    private var billOwner: Contact? = null


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

    fun getBillOwner(): String{
        return billOwner?.id ?: "Unknown Bill Owner"
    }

    fun setBillOwner(contact: Contact?){
        billOwner = contact
    }

    fun setBill(){
        bill = Bill()
    }


}