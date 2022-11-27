package com.example.doantotnghiep.ViewModel

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.recyclerview.widget.RecyclerView
import com.example.doantotnghiep.Model.Cart
import com.example.doantotnghiep.Repository.Repository

class CartViewModel : ViewModel() {
    fun addCartProduct(idUser : String, total : Int , idProduct : String , cart : Cart) = liveData {
        emit(Repository.getRepository().addProductCart(idUser,total,idProduct,cart))
    }
    fun getListCartProduct(idUser: String)  = liveData {
        emit(Repository.getRepository().getListCart(idUser))
    }
}