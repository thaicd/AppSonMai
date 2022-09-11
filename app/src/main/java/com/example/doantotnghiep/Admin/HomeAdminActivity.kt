package com.example.doantotnghiep.Admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.doantotnghiep.R
import com.example.doantotnghiep.databinding.ActivityHomeAdminBinding

class HomeAdminActivity : AppCompatActivity() {

    private lateinit var viewBindingAdmin : ActivityHomeAdminBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBindingAdmin = ActivityHomeAdminBinding.inflate(LayoutInflater.from(this))
        setContentView(viewBindingAdmin.root)


    }
}