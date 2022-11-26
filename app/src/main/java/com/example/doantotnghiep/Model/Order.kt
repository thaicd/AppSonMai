package com.example.doantotnghiep.Model

data class Order(
    var idOrder : String? = null ,
    var idUser  : String? = null ,
    var listProduct : List<Product>? = null ,
    var totalPrice : Int = 0 ,
    var statusOrder : String? = null ,
    var timeStamp : String? = null
)
