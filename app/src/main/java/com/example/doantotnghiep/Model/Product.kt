package com.example.doantotnghiep.Model

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class Product(
    var id : String? = null,
    var idShop : String?= null ,
    var nameShop : String?= null,
    var idCategory: String ?= null,
    var nameProduct : String? =null ,
    var price : Double =0.0,
    var number: Int? = 0,
    var description: String?= null ,
    var rate : Double = 0.0 ,
    var imgUrl: String?= null ,
    var isActive: Int?= null ,
) : Serializable
