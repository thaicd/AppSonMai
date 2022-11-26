package com.example.doantotnghiep.Model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Comment(
    var idComment: String? = null,
    var nameUser: String? = null ,
    var message : String? = null ,
    var timeStamp : String?= null
)
