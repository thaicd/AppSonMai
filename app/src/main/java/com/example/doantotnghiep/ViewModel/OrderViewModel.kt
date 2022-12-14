package com.example.doantotnghiep.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.doantotnghiep.Model.Order
import com.example.doantotnghiep.Model.OrderDetails
import com.example.doantotnghiep.Model.Product
import com.example.doantotnghiep.Repository.Repository

class OrderViewModel : ViewModel() {
    lateinit var liveDataOrder : MutableLiveData<Boolean>
    init {
        liveDataOrder = MutableLiveData()
    }
    fun getLiveDataOrder1() : MutableLiveData<Boolean> {
        return liveDataOrder
    }
    fun addOrdersProduct(idUser: String, idOrder : String, order : Order, price : Int) = liveData {
        emit(Repository.getRepository().addOrders(idUser,idOrder,order,price))
    }
    fun addOrdersDetailProduct(idUser: String, idOrder : String, mProduct: Product, order : OrderDetails) = liveData {
        emit(Repository.getRepository().addOrdersDetailProduct(idUser,idOrder,mProduct,order))
    }
    fun getListOrdersDetail(idUser: String, idOrder: String) = liveData {
        emit(Repository.getRepository().getListOrdersDetail(idUser,idOrder))
    }
    fun getListOrders(idUser: String) = liveData {
        emit(Repository.getRepository().getListOrders(idUser))
    }
    fun updateStatusOrder(idUser: String, idOrder: String, data: Int )  {
        Repository.getRepository().updateStatusOrder(idUser,idOrder, data,liveDataOrder)
    }
    fun getListOrderUsers() = liveData {
        emit(Repository.getRepository().getListUserOrders())
    }
    fun getListIDOrders(idUser: String) = liveData {
        emit(Repository.getRepository().getListIDOrder(idUser))
    }
}