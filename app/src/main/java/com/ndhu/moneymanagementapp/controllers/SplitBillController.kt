package com.ndhu.moneymanagementapp.controllers

import android.content.Context
import com.google.firebase.Timestamp
import com.google.firebase.installations.Utils
import com.ndhu.moneymanagementapp.enums.BillStatus
import com.ndhu.moneymanagementapp.models.Bill
import com.ndhu.moneymanagementapp.models.BillItemDetail
import com.ndhu.moneymanagementapp.payloads.ItemDetail
import com.ndhu.moneymanagementapp.payloads.ListUserSplitBillResponse
import com.ndhu.moneymanagementapp.payloads.SplitBillRequest
import com.ndhu.moneymanagementapp.repositories.IBillItemDetailRepository
import com.ndhu.moneymanagementapp.repositories.IBillRepository
import com.ndhu.moneymanagementapp.repositories.IItemRepository
import com.ndhu.moneymanagementapp.repositories.impl.BillDetailRepository
import com.ndhu.moneymanagementapp.repositories.impl.BillRepository
import com.ndhu.moneymanagementapp.repositories.impl.ItemRepository
import com.ndhu.moneymanagementapp.utils.Util
import java.util.UUID

class SplitBillController {

    var billRepo : IBillRepository = BillRepository()
    var billDetailRepo : IBillItemDetailRepository = BillDetailRepository()
    var itemRepo : IItemRepository = ItemRepository()

    suspend fun addSplitBill(context : Context, request : SplitBillRequest) : Boolean {
        var success = false
        val bill = Bill(
            id = UUID.randomUUID().toString(),
            ownerId = request.billOwnerId,
            createdAt = Timestamp.now(),
            updatedAt = Timestamp.now()
        )

        billRepo.add(bill)

        request.itemIdAndOwners.forEach {
            var billDetail = it?.let { it1 ->
                BillItemDetail(
                    id = UUID.randomUUID().toString(),
                    billId = bill.id,
                    itemId = it1.itemId,
                    userId = it1.ownerId,
                    qty = it1.qty,
                    status = BillStatus.UNPAID,
                    createdAt = Timestamp.now(),
                    updatedAt = Timestamp.now()
                )
            }
            if (billDetail != null) {
                billDetailRepo.add(billDetail)
            }
        }
        success = true
        return success
    }

    suspend fun getLoginUserListOfBills(context : Context) : List<ListUserSplitBillResponse> {

        var billList : MutableList<ListUserSplitBillResponse> = mutableListOf()
        var billItemDetailList = billDetailRepo.getBillListBasedOnUserId(Util.getUserId(context).toString())

        for (billItem in billItemDetailList) {
            val index = getListUserResponseFromBillId(billList, billItem.billId)

            if (index == -1) {
                billList.add(
                    ListUserSplitBillResponse(
                        billId = billItem.billId,
                        status = billItem.status,
                        itemList = mutableListOf()
                    )
                )
            }

            val updatedIndex = getListUserResponseFromBillId(billList, billItem.billId)
            if (updatedIndex != -1) {
                var item = itemRepo.get(billItem.itemId)
                if(item  != null){
                    billList[updatedIndex].itemList.add(0, ItemDetail(
                        id = billItem.itemId,
                        name = item.name,
                        price = item.price,
                        qty = billItem.qty
                    ))
                }
            }
        }


        return billList
    }

    private fun getListUserResponseFromBillId(list : List<ListUserSplitBillResponse>, id : String) : Int {
        for (i in 0 until list.size) {
            if(list.get(i).billId == id) {
                return i
            }
        }
        return -1
    }

}