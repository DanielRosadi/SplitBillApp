package com.ndhu.moneymanagementapp.controllers

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import com.google.firebase.Timestamp
import com.ndhu.moneymanagementapp.enums.UserPaymentStatus
import com.ndhu.moneymanagementapp.models.Bill
import com.ndhu.moneymanagementapp.models.BillItemDetail
import com.ndhu.moneymanagementapp.models.FullBillDetail
import com.ndhu.moneymanagementapp.models.UserItemGroup
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
            updatedAt = Timestamp.now(),
            storeId = request.storeId                      // added
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
                    status = UserPaymentStatus.UNPAID,
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
                val bill = billRepo.getById(billItem.billId)  // added
                if (bill != null) {
                    billList.add(
                        ListUserSplitBillResponse(
                            billId = billItem.billId,
                            status = billItem.status,
                            itemList = mutableListOf(),
                            storeId = bill.storeId, // ?: "Store Error",          // added
                            createdAt = bill.createdAt,             // <-- ADDED
                            billOwnerId = bill.ownerId                // <-- ADDED
                        )
                    )
                }
            }

            val updatedIndex = getListUserResponseFromBillId(billList, billItem.billId)
            if (updatedIndex != -1) {
                var item = itemRepo.getById(billItem.itemId)
                if(item  != null){
                    billList[updatedIndex].itemList.add(0, ItemDetail(
                        id = billItem.itemId,
                        name = item.name,
                        oneItemPrice = item.price,
                        userTakenQuantity = billItem.qty
                    ))
                }
            }
        }


        return billList
    }


    // New method for getting bill details grouped by user
    suspend fun getBillDetailGroupedByUser(billId: String): FullBillDetail? {
        val bill = billRepo.getById(billId) ?: return null
        val billDetails = billDetailRepo.getByBillId(billId)

        val userToItems: MutableMap<String, MutableList<ItemDetail>> = mutableMapOf()
        val userToStatus: MutableMap<String, UserPaymentStatus> = mutableMapOf()

        for (billItem in billDetails) {
            val item = itemRepo.getById(billItem.itemId) ?: continue

            val itemDetail = ItemDetail(
                id = billItem.itemId,
                name = item.name,
                oneItemPrice = item.price,
                userTakenQuantity = billItem.qty
            )

            userToItems.getOrPut(billItem.userId) { mutableListOf() }.add(itemDetail)
            userToStatus[billItem.userId] = billItem.status
        }

        val userItemGroups = userToItems.map { (userId, items) ->
            UserItemGroup(
                userId = userId,
                status = mutableStateOf( userToStatus[userId] ?: UserPaymentStatus.UNPAID ) ,
                items = items
            )
        }

        return FullBillDetail(
            billId = bill.id,
            billOwnerId = bill.ownerId,
            createdAt = bill.createdAt,
            storeId = bill.storeId,
            users = userItemGroups.toMutableStateList()
        )
    }

    suspend fun getBillDetail(billId: String) : List<ListUserSplitBillResponse> {
        var billList : MutableList<ListUserSplitBillResponse> = mutableListOf()
        var bill: Bill? = billRepo.getById(billId)
        if (bill == null) {
            return billList
        }
        var billDetails = billDetailRepo.getByBillId(bill.id)
        for(billItem in billDetails) {
            val index = getListUserResponseFromBillId(billList, billItem.billId)

            if (index == -1) {
                billList.add(
                    ListUserSplitBillResponse(
                        billId = billItem.billId,
                        status = billItem.status,
                        itemList = mutableListOf(),
                        storeId = bill.storeId, // ?: "Store Error",          // added
                        createdAt = bill.createdAt,             // <-- ADDED
                        billOwnerId = bill.ownerId                // <-- ADDED
                    )
                )
            }

            val updatedIndex = getListUserResponseFromBillId(billList, billItem.billId)
            if (updatedIndex != -1) {
                var item = itemRepo.getById(billItem.itemId)
                if(item  != null){
                    billList[updatedIndex].itemList.add(0, ItemDetail(
                        id = billItem.itemId,
                        name = item.name,
                        oneItemPrice = item.price,
                        userTakenQuantity = billItem.qty
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

    suspend fun updateStatus(billId: String, userId: String, newStatus: UserPaymentStatus): Boolean {
        val billDetails = billDetailRepo.getByBillId(billId)

        val userItems = billDetails.filter { it.userId == userId }

        if (userItems.isEmpty()) return false

        for (billItem in userItems) {
//            val updatedBillItem = billItem.copy(
//                status = newStatus,
//                // TODO: update date safely, this causes error due to Timestamp() type
//                updatedAt = Timestamp.now()
//            )
            val updatedBillItem = BillItemDetail(
                id = billItem.id,
                billId = billItem.billId,
                itemId = billItem.itemId,
                userId = billItem.userId,
                qty = billItem.qty,
                status = newStatus,
                createdAt = billItem.createdAt,
                updatedAt = Timestamp.now()
            )

            // Use the document ID to update
            billDetailRepo.updateWithDateFields(billItem.id, updatedBillItem)
        }

        return true
    }


}