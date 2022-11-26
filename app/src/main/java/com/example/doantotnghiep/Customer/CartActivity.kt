package com.example.doantotnghiep.Customer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.doantotnghiep.databinding.ActivityCartBinding
import com.example.doantotnghiep.databinding.ActivityCustomerBinding

class CartActivity : AppCompatActivity() {
    lateinit var viewBindingCustomer : ActivityCartBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBindingCustomer = ActivityCartBinding.inflate(LayoutInflater.from(this))
        setContentView(viewBindingCustomer.root)
    }
}