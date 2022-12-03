package com.example.doantotnghiep.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.doantotnghiep.Model.Rating
import com.example.doantotnghiep.Repository.Repository

class RatingViewModel : ViewModel() {
    lateinit var liveDataRating : MutableLiveData<Float>
    lateinit var liveDataRatingAvarage :  MutableLiveData<Float>

    init {
        liveDataRating = MutableLiveData()
        liveDataRatingAvarage = MutableLiveData()
    }

    fun getLiveDataRating1() : MutableLiveData<Float> {
        return liveDataRating
    }
    fun liveDataRatingAvarage1() : MutableLiveData<Float> {
        return liveDataRatingAvarage
    }

    fun addGetRating(idProduct: String , rate : Rating) {
        Repository.getRepository().addRating(idProduct,rate,liveDataRating)
    }
    fun getRating(idProduct: String) {
        Repository.getRepository().getRating(idProduct,liveDataRatingAvarage)
    }
}