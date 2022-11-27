package com.example.doantotnghiep.Model

import java.io.Serializable

data class Order(
    var idOrder : String? = null ,
    var idUser  : String? = null ,
    var listProduct : Product? = null ,
    var totalPrice : Int = 0 ,
    var numberOptions     : Int = 0 ,
    var statusOrder : Int = 0 ,
    var timeStamp : String? = null
): Serializable
