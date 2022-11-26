package com.example.doantotnghiep.Customer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.doantotnghiep.databinding.ActivityMyFavoriteBinding

class MyFavoriteActivity : AppCompatActivity() {
    lateinit var viewBinding : ActivityMyFavoriteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMyFavoriteBinding.inflate(LayoutInflater.from(this))
        setContentView(viewBinding.root)
    }
}