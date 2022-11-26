package com.example.doantotnghiep.Model

import com.example.doantotnghiep.Adapter.ProductAdapter

data class Cart(
    var prod : Product?= null ,
    var totalPrice : Int = 0 ,
    var numberChoice : Int = 0 ,
    var timeStamp : String?= null

)
