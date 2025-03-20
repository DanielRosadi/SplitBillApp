package com.ndhu.moneymanagementapp.controllers

import android.content.Context
import com.ndhu.moneymanagementapp.payloads.SplitBillRequest

class SplitBillController {

    suspend fun addSplitBill(context : Context, request : SplitBillRequest) {
        //1. check bill owner id
        //2. insert to transaction header
        //3. insert to transaction detail
    }

}