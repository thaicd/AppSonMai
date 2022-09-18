package com.example.doantotnghiep.Model

data class Product(
    var id : String? = null,
    var nameProduct : String? =null ,
    var price : Double? =0.0,
    var number: Int? = 0,
    var description: String?= null ,
    var imgUrl: String?= null ,
    var isActive: Int?= null ,

)
