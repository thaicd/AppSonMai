package com.example.doantotnghiep.Model

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class Rating(var idRate : String?= null ,
                  var rateNumber : Float?= (0).toFloat() ,
                  var idUser : String?= null,
) : Serializable
