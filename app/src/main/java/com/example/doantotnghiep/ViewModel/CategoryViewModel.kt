package com.example.doantotnghiep.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.doantotnghiep.Model.Category
import com.example.doantotnghiep.Repository.Repository

class CategoryViewModel : ViewModel() {
    fun addCategory(cat : Category) = liveData<Unit> {
        emit(Repository.getRepository().addCategory(cat))
    }
    fun getListCategory() = liveData {
        emit(Repository.getRepository().getListCategoryFirebase())
    }
    fun checkExistCategory(id:String) = liveData<Boolean> {
        emit(Repository.getRepository().checkExistCategoryFirebase(id))
    }
}