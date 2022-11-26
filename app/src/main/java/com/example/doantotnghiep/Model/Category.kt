package com.example.doantotnghiep.Model

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties

data class Category  (
        var id : String? = "",
        var name : String? = "",
):Serializable
