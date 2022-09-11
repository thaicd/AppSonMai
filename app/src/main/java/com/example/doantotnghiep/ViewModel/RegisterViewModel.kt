package com.example.doantotnghiep.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.doantotnghiep.Model.User
import com.example.doantotnghiep.Repository.Repository
import com.google.firebase.auth.FirebaseUser

class RegisterViewModel :ViewModel() {

    lateinit var registerLiveData: MutableLiveData<FirebaseUser>

    init {
        registerLiveData = MutableLiveData()
    }

    fun getregisterLiveData() : MutableLiveData<FirebaseUser> {
//        Repository.getRepository().RegisterUser(user,registerLiveData )
        return registerLiveData
    }

    fun register(user : User) {
        Repository.getRepository().RegisterUser(user,registerLiveData )
    }
}