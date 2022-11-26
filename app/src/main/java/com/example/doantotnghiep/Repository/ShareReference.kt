package com.example.doantotnghiep.Repository

import android.content.Context
import android.content.SharedPreferences
import com.example.doantotnghiep.Model.User
import com.google.gson.Gson


object ShareReference {
    lateinit var myReference: SharedPreferences

        val USER = "user"
        val USERNAME = "key_user"



    fun init( mContent : android.content.Context) {
        myReference = mContent.getSharedPreferences(USER, Context.MODE_PRIVATE) as SharedPreferences
    }

    fun putUser(user : User) {
        val json = Gson()
        val strUser = json.toJson(user)
        val editor : SharedPreferences.Editor = myReference.edit()
        editor.putString(USERNAME, strUser)
        editor.apply()
    }
    fun getUser() : User {
        val json = Gson()

        val strUser = myReference.getString(USERNAME, "").toString()
        val res = json.fromJson(strUser, User::class.java)
        return  res ;
    }
}