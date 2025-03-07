package com.ndhu.moneymanagementapp.sharedviewmodels

import androidx.lifecycle.ViewModel
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
    var storeList: List<Store>? = null

    fun getStoreById(id: String): Store? {
        return storeList?.find { it.id == id }
    }
}