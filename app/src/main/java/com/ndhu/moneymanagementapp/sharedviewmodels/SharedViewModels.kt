package com.ndhu.moneymanagementapp.sharedviewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.ndhu.moneymanagementapp.models.Bill
import com.ndhu.moneymanagementapp.models.Contact
import com.ndhu.moneymanagementapp.models.Item
import com.ndhu.moneymanagementapp.models.ProfilePicture
import com.ndhu.moneymanagementapp.models.Store
import com.ndhu.moneymanagementapp.models.User
import com.ndhu.moneymanagementapp.payloads.ItemDetail
import com.ndhu.moneymanagementapp.ui.pages.ItemOwnership
import com.ndhu.moneymanagementapp.ui.pages.ItemWithUsersList

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

    // --------------- class variables/fields

    var storeList: MutableList<Store>? = null

    // selectedStore for the bill
    var selectedStore: Store = Store("", "INIT STORE", "", "")

    // list of items for the bill
    private var itemList: MutableList<Item>? = null

    // list of items for bill with quantity
    var itemDetailList : MutableList<ItemDetail> = mutableListOf()

    // list of friends/contacts participating in the bill
    private var friendList: List<Contact>? = null

    // bill owner of the bill
    private var billOwner: User? = null

    // bill owner + chosen friends/contacts
    var billParticipants: MutableList<User?> = mutableListOf()

    // to contains information of one bill (1 store, items, friends, 1 bill owner, item assigns)
    var bill: Bill? = null

    // act as a data backup/"cache" between AssignSplitsScreen and SplitItemScreen
    var listItemAndUserQuantities: MutableList<ItemWithUsersList> = mutableListOf()

    // current item with users/owners list
    var currentItemAndUserQuantities: ItemWithUsersList = ItemWithUsersList(ItemDetail(), mutableStateListOf())
        /*
    var currentItemAndUserQuantities: ItemWithUsersList = ItemWithUsersList(ItemDetail(), mutableStateListOf(ItemOwnership(
        User(id = "xxxxxxxxx", username = "user_debug", "email", "lol")
        , 1.0f)))

         */

    // current item highlighted for SplitItemScreen
    var currentItem: ItemDetail = ItemDetail()

    // copy of current item with users/owners list (temp)
    // this is used to save data and only applies if the user really saves the data. otherwise, if user press cancels, we will not use this
    var tempItemAndUserQuantities: ItemWithUsersList = ItemWithUsersList(ItemDetail(), mutableStateListOf())

    // to save user profile pic in case its need to be fetch again (save time)
    // var currentUserProfilePic: ProfilePicture? = null

    // --------------- functions (getters, setters)

    fun getStoreById(id: String): Store? {
        return storeList?.find { it.id == id }
    }

    fun addStore(store: Store){
        storeList?.add(store)
    }

    fun addItem(item: Item){
        itemList?.add(item)
    }

    fun getItemList(): MutableList<Item>?{
        return itemList
    }

    fun setItemList(listOfItems: MutableList<Item>?){
        itemList = listOfItems
    }

    fun setFriendList(listOfFriends: List<Contact>?){
        friendList = listOfFriends
    }

    fun getFriendList(): List<Contact>?{
        return friendList
    }

    fun getBillOwnerId(): String{
        return billOwner?.id ?: "Unknown Bill Owner"
    }

    fun setBillOwner(user: User){
        billOwner = user
    }

    fun setBill(){
        bill = Bill()
    }


}