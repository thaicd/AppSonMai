package com.example.doantotnghiep.Model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User constructor(var id : String?= null,
                var email : String?= null,
                var name : String?= null,
                var phone : String?= null,
                var role_id : Int = 2,
                var password : String?= null,
                var address : String?= null
)
