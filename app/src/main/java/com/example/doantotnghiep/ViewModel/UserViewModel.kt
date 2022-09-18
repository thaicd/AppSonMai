package com.example.doantotnghiep.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.doantotnghiep.Model.User
import com.example.doantotnghiep.Repository.Repository
import com.google.firebase.database.core.Repo
import kotlinx.coroutines.Dispatchers

class UserViewModel : ViewModel() {

    val getDataListUser = liveData<List<User>>(Dispatchers.IO) {
        emit(Repository.getRepository().getListUsersCoroutine())
    }
}