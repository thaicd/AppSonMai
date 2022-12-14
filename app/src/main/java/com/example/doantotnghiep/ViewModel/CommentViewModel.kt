package com.example.doantotnghiep.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.doantotnghiep.Model.Comment
import com.example.doantotnghiep.Model.User
import com.example.doantotnghiep.Repository.Repository

class CommentViewModel : ViewModel() {
    lateinit var liveDataComments : MutableLiveData<List<Comment>>
    init {
        liveDataComments = MutableLiveData()
    }
    fun getLiveDataComment() : MutableLiveData<List<Comment>> {
        return liveDataComments
    }
    fun getListCommentLiveData(idShop: String, id : String) {
        Repository.getRepository().getCommentProductLiveData(idShop, id,liveDataComments)
    }
    fun getListComment( id : String) = liveData {
        emit(Repository.getRepository().getCommentProduct(id))
    }
    fun addComment(idShop : String , idProduct : String , idComment : String, comment : Comment )  = liveData <Int>{
        emit(Repository.getRepository().addComment(idShop, idProduct, idComment, comment))
    }

    //
}