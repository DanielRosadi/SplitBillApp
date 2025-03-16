package com.ndhu.moneymanagementapp.training

// class Produk
class Product(var productId: Int, var productPrice: Int, var productName: String) {

    fun changePrice(productId: Int, newPrice: Int){
        productPrice = newPrice
    }

    fun changeName(productId: Int, newName: String){
        productName = newName
    }
}

/*
CPP

class Product{

// class attributes
int price;
String name;

}


 */


fun main(){
    var gelas = Product(1, 1000, "Gelas A")
    gelas.changeName(1, "Piring")
    gelas.changePrice(1, 2000)

    var anjing = Product(2, 10000, "Kevin")
    anjing.changePrice(2, 20000)

    var smartphone = Product(3, 9999, "Xiaomi 11T")
    smartphone.changePrice(3, 15000)

}




