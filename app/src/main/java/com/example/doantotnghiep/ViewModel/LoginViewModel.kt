package com.example.doantotnghiep.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.doantotnghiep.Helper.CustomProgressBar
import com.example.doantotnghiep.Model.User
import com.example.doantotnghiep.Repository.Repository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlin.math.log

class LoginViewModel : ViewModel() {
    lateinit var loginLiveData: MutableLiveData<FirebaseUser>
    lateinit var userLoginLiveData: MutableLiveData<User>

    init {
        loginLiveData = MutableLiveData()
        userLoginLiveData = MutableLiveData()
    }

    fun getloginLiveData() : MutableLiveData<FirebaseUser> {
        return loginLiveData
    }
    fun getuserLoginLiveData() : MutableLiveData<User> {
        return userLoginLiveData
    }
    fun login(strUser : String , strPass : String) {
        Repository.getRepository().Login(strUser,strPass,loginLiveData)
    }

    fun checkLoginSession(dialog : CustomProgressBar? = null) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val uid = user.uid
            Repository.getRepository().checkLoginAccount(uid,userLoginLiveData)
        }else {
            dialog?.dismissDialog()
        }
    }

}