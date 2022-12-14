package com.example.doantotnghiep.Model

import java.io.Serializable

data class OrderDetails(var idOrder : String?= null,
                        var numberChoice: Int? = 0,
                        var totalPrice  : Int? = 0,
                        var idUser  : String? = null ,
                        var product : Product?= null,
                        var timeStamp : String?= null

):Serializable
