package com.ndhu.moneymanagementapp.training

class ProductList(var products: List<Product>) {

    fun applyDiscount(discount: Int): Unit{
        for (index in products.indices){
            // apply discount to every Product object in list
            products[index].productPrice = products[index].productPrice / discount
        }
        // using the class attribute, not returning new list or anything
    }

    fun filterExpensive(minimumPrice: Int): List<Product>{
        var result = mutableListOf<Product>() // can change list content/element
        for (product in products){
            // filter based on minimum price
            if (product.productPrice <= minimumPrice){
                result.add(product)
            }
        }
        return result // return new list
    }

    fun getProductById(idProductToFind: Int): Product?{
        for (product in products){
            // find using the product id
            if(product.productId == idProductToFind){
                return product // return the Product object
            }
        }
        return null
    }


}

fun main(){
    // list of Product creation
    var listSmartphones = ProductList(
        listOf(
        Product(1, 20000,"Samsung A8"),
        Product(2, 15000, "Xiaomi 15T"),
        Product(3, 100000, "iPhone 20"))
    )

    // apply discount
    listSmartphones.applyDiscount(90/100) // discount by 10%, that is 90% price now. 90% (90/100)
    println(listSmartphones)

    // filter based on price
    var cheaperSmartphones = listSmartphones.filterExpensive(50000)
    println(cheaperSmartphones)

    // change product name and price based on product id
    var xiaomi_15t = listSmartphones.getProductById(2)
    xiaomi_15t?.changeName(2, "Xiaomi 15T PLUS")
    xiaomi_15t?.changePrice(2, 18000)

}