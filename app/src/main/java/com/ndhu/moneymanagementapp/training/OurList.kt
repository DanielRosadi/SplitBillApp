package com.ndhu.moneymanagementapp.training

class OurList(var list: List<Int>){

    fun map(operation: (Int) -> Unit){
        for (element in list) {
            // 1, 2, 3
            operation(element)
        }
    }
}

val multiplyByTwo: (Int) -> Unit = { angka -> angka * 2 }
val multiplyByThree: (Int) -> Unit = { angka -> angka * 3 }

// Int angka, Int N -> Unit
val multiplyByN: (Int, Int) -> Unit = { angka, N -> angka * N }


fun main(){
    var myList = OurList(listOf(1, 2, 3))

    myList.map(
        {angka->
            angka * 2
        }
    )

    print(myList)

}