package com.example.doantotnghiep.Repository

import java.text.SimpleDateFormat
import java.util.*

object Time {

    fun convertTimstampToDate() : String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val timeNow = sdf.format(System.currentTimeMillis())
        return timeNow
    }
    fun convertTimeDate(time : Long) : String {
        val sdf = SimpleDateFormat("yyyyMMdd")
        val timeNow = sdf.format(time)
        return timeNow
    }
}