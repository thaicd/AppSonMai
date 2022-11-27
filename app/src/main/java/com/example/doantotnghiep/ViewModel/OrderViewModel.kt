package com.example.doantotnghiep.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.doantotnghiep.Model.Order
import com.example.doantotnghiep.Repository.Repository

class OrderViewModel : ViewModel() {
    fun addOrdersProduct(idUser: String, idOrder : String, idProduct: String, order : Order, price : Int) = liveData {
        emit(Repository.getRepository().addOrders(idUser,idOrder,idProduct,order,price))
    }
    fun getListOrders(idUser: String) = liveData {
        emit(Repository.getRepository().getListOrders(idUser))
    }
}