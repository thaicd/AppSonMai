package com.example.doantotnghiep.ViewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import androidx.lifecycle.liveData
import com.example.doantotnghiep.Model.Product
import com.example.doantotnghiep.Repository.Repository

class ProductViewModel : ViewModel() {

    fun responseLiveData(idShop : String) = liveData<List<Product>>(Dispatchers.IO){
        emit(Repository.getRepository().getListProductCoroutine(idShop))
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
    fun getMyFavoriteProduct(idShop: String, id : String ) = liveData {
        emit(Repository.getRepository().getListMyFavoriteProduct(idShop,id))
    }
    fun removeMyFavoriteProduct(idUser: String, idProduct : String ) = liveData {
        emit(Repository.getRepository().removeFavoriteProduct(idUser, idProduct))
    }
    fun addMyFavoriteProduct(idUser: String, idProduct: String, prod : Product) = liveData{
        emit(Repository.getRepository().addFavoriteProduct(idUser,idProduct,prod))
    }
    fun getStatusFavorite(idUser: String, idProduct: String, idShop: String) = liveData {
        emit(Repository.getRepository().getStatusFavoriteProduct(idUser,idProduct,idShop))
    }
    fun editRatingProduct(idShop: String, idProduct: String , data : Double) = liveData {
        emit(Repository.getRepository().editRatingProduct(idShop,idProduct,data))
    }
    fun editNumberProduct(idShop: String,idProduct: String, number: Int) = liveData {
        emit(Repository.getRepository().editNumberProduct(idShop,idProduct,number))
    }
    fun getNumberProduct(idProduct: String, idShop: String) = liveData {
        emit(Repository.getRepository().getNumberProduct(idProduct,idShop))
    }
}