package com.example.doantotnghiep.Customer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.doantotnghiep.R
import com.example.doantotnghiep.databinding.ActivityCustomerBinding

class CustomerActivity : AppCompatActivity() {

    lateinit var viewBindingCustomer : ActivityCustomerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBindingCustomer = ActivityCustomerBinding.inflate(LayoutInflater.from(this))
        setContentView(viewBindingCustomer.root)
    }
}