package com.example.doantotnghiep.Customer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.doantotnghiep.databinding.ActivityOrderCustomerBinding

class OrderCustomerActivity : AppCompatActivity() {
    lateinit var viewBinding : ActivityOrderCustomerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityOrderCustomerBinding.inflate(LayoutInflater.from(this))
        setContentView(viewBinding.root)
    }
}