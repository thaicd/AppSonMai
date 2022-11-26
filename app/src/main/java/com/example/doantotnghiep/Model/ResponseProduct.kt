package com.example.doantotnghiep.Model

sealed class ResponseProduct<T> (val data: T? = null, val messsage :String? = null){
    class SUCCESS<T>(data: T?) : ResponseProduct<T>(data)
    class ERROR<T>(message: String,data: T? = null) : ResponseProduct<T>(data,message)
}