package com.example.doantotnghiep.Admin

import android.content.Intent
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

        viewBindingAdmin.btnAddProduct.setOnClickListener { _ ->
                val intent = Intent(this@HomeAdminActivity, AddProductActivity::class.java)
                startActivity(intent)

        }
        viewBindingAdmin.btnListUser.setOnClickListener {
            _->

            val intent = Intent(this@HomeAdminActivity, ListUserActivity::class.java)
            startActivity(intent)
        }
        viewBindingAdmin.btnListProduct.setOnClickListener {
            _ ->
            val intent = Intent(this@HomeAdminActivity, ListProductActivity::class.java)
            startActivity(intent)
        }

    }
}