package com.example.doantotnghiep.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.doantotnghiep.Model.Comment
import com.example.doantotnghiep.Model.User
import com.example.doantotnghiep.Repository.Repository

class CommentViewModel : ViewModel() {

    fun getListComment( id : String) = liveData {
        emit(Repository.getRepository().getCommentProduct(id))
    }
    fun addComment(idProduct : String , idComment : String, comment : Comment )  = liveData <Int>{
        emit(Repository.getRepository().addComment(idProduct, idComment, comment))
    }

    //
}