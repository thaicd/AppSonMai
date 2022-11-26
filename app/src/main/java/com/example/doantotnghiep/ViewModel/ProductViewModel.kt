package com.example.doantotnghiep.ViewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import androidx.lifecycle.liveData
import com.example.doantotnghiep.Model.Product
import com.example.doantotnghiep.Repository.Repository

class ProductViewModel : ViewModel() {

    val responseLiveData = liveData<List<Product>>(Dispatchers.IO){
        emit(Repository.getRepository().getListProductCoroutine())
    }
    fun removeData(id: String) = liveData<Boolean> (Dispatchers.IO){
        emit(Repository.getRepository().removeProduct(id))
    }
    fun editData(id: String , p : Product) = liveData<Boolean>(Dispatchers.IO  ) {
        emit(Repository.getRepository().editProduct(id, p ))
    }
    fun addData(p : Product) = liveData<Boolean> {
        emit(Repository.getRepository().addProduct(p))
    }
    fun getMyFavoriteProduct(id : String ) = liveData {
        emit(Repository.getRepository().getListMyFavoriteProduct(id))
    }
    fun removeMyFavoriteProduct(idUser: String, idProduct : String ) = liveData {
        emit(Repository.getRepository().removeFavoriteProduct(idUser, idProduct))
    }
    fun addMyFavoriteProduct(idUser: String, idProduct: String, prod : Product) = liveData{
        emit(Repository.getRepository().addFavoriteProduct(idUser,idProduct,prod))
    }
    fun getStatusFavorite(idUser: String, idProduct: String) = liveData {
        emit(Repository.getRepository().getStatusFavoriteProduct(idUser,idProduct))
    }
}