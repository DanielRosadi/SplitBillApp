package com.ndhu.moneymanagementapp.enums

enum class Collection(val collection : String) {
    USER("users"),
    STORE("stores"),
    ITEM("items"),
    HISTORY("histories"),
    CONTACT("contacts"),
    BILL("bills"),
    BILLITEMDETAIL("bill_item_details")
}