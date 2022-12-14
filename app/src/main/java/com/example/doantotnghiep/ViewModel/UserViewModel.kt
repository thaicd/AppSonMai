package com.example.doantotnghiep.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.doantotnghiep.Model.User
import com.example.doantotnghiep.Repository.Repository
import com.google.firebase.database.core.Repo
import kotlinx.coroutines.Dispatchers

class UserViewModel : ViewModel() {

    lateinit var liveDataListUser : MutableLiveData<MutableList<User>>
    lateinit var liveDataRegister : MutableLiveData<Boolean>
    lateinit var liveDataReset    : MutableLiveData<Boolean>
    init {
        liveDataListUser = MutableLiveData()
        liveDataRegister = MutableLiveData()
        liveDataReset    = MutableLiveData()
    }
    fun getLiveDataReset1() : MutableLiveData<Boolean> {
        return  liveDataReset
    }
    val getDataListUser = liveData<List<User>>(Dispatchers.IO) {
        emit(Repository.getRepository().getListUsersCoroutine())
    }
    fun getLiveDataListUser1() : MutableLiveData<MutableList<User>> {
        return  liveDataListUser
    }
    fun getLiveDataRegister1() : MutableLiveData<Boolean> {
        return liveDataRegister
    }

    fun addShop(data : User) {
        Repository.getRepository().addOwnerShop(data, liveDataRegister)
    }

    fun deleteUser ( data: User) {
        Repository.getRepository().removeUser(data)
    }
    fun getListShop() {
        Repository.getRepository().getListShop(liveDataListUser);
    }
    fun resetPassword(email : String) {
        Repository.getRepository().resetEmail(email,liveDataReset)
    }

}