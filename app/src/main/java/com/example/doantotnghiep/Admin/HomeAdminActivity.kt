package com.example.doantotnghiep.Admin

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.doantotnghiep.Helper.Constanst
import com.example.doantotnghiep.Helper.CustomProgressBar
import com.example.doantotnghiep.MainActivity
import com.example.doantotnghiep.Model.Category
import com.example.doantotnghiep.Model.User
import com.example.doantotnghiep.R
import com.example.doantotnghiep.Repository.ShareReference
import com.example.doantotnghiep.ViewModel.CategoryViewModel
import com.example.doantotnghiep.databinding.ActivityHomeAdminBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class HomeAdminActivity : AppCompatActivity() {

    private lateinit var viewBindingAdmin : ActivityHomeAdminBinding
    lateinit var viewModelCategory : CategoryViewModel
    lateinit var loading : CustomProgressBar
    lateinit var mData : User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mData = ShareReference.getUser()

        viewBindingAdmin = ActivityHomeAdminBinding.inflate(LayoutInflater.from(this))
        setContentView(viewBindingAdmin.root)
        val data = intent.extras
        data?.apply {
            mData = this.get("user") as User
        }
        //if (mData == null ) {
       // }
        loading = CustomProgressBar(this)

//        viewBindingAdmin.btnAddCategory.setOnClickListener {
//            _->
//            _showDialog(Gravity.BOTTOM)
//        }
        viewBindingAdmin.toolbar.setTitle(mData.name)
        viewBindingAdmin.txtNameShop.text = mData.name
        viewBindingAdmin.btnExit.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        viewBindingAdmin.btnAddProduct.setOnClickListener { _ ->
                val intent = Intent(this@HomeAdminActivity, AddProductActivity::class.java)
                startActivity(intent)

        }
        viewBindingAdmin.btnListUser.setOnClickListener {
            _->

            val intent = Intent(this@HomeAdminActivity, ListUserActivity::class.java)
            startActivity(intent)
        }
        viewBindingAdmin.btnWatchOrder.setOnClickListener {
            val intent = Intent(this@HomeAdminActivity, OrderProductAdminActivity::class.java)
            startActivity(intent)
        }
        viewBindingAdmin.btnOrdrProductSold.setOnClickListener {
            val intent = Intent(this@HomeAdminActivity, OrderSoldAdminActivity::class.java)
            startActivity(intent)
        }
        viewBindingAdmin.btnListProduct.setOnClickListener {
            _ ->
            val intent = Intent(this@HomeAdminActivity, ListProductActivity::class.java)
            startActivity(intent)
        }

    }

    private fun _showDialog(gravity: Int ) {
        val dialog = Dialog(this)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.custom_dialog)

        val window = dialog.window
        if (window == null ) return
        window?.apply {
            setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT)
            setGravity(gravity)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        val layout = dialog.findViewById<TextInputLayout>(R.id.layout)
        val edtCategory = dialog.findViewById<TextInputEditText>(R.id.item_edt_category)
        val btnAdd = dialog.findViewById<Button>(R.id.btn_add)
        btnAdd.setOnClickListener {
            _ ->
            loading.showProgressBar(this)
            val cat = edtCategory.text.toString()
            viewModelCategory = ViewModelProvider(this)[CategoryViewModel::class.java]
            viewModelCategory.addCategory(Category(System.currentTimeMillis().toString(), cat)).observe(this,
            Observer {
                loading.dismissDialog()
                dialog.dismiss()
            })
        }
        dialog.setCancelable(true)
        dialog.show()
    }
}