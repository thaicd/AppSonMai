package com.example.doantotnghiep.Adminstrator

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
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.doantotnghiep.MainActivity
import com.example.doantotnghiep.Model.Category
import com.example.doantotnghiep.Model.User
import com.example.doantotnghiep.R
import com.example.doantotnghiep.ViewModel.CategoryViewModel
import com.example.doantotnghiep.ViewModel.UserViewModel
import com.example.doantotnghiep.databinding.ActivityAdminShopBinding
import com.example.doantotnghiep.databinding.ActivityHomeAdminBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import es.dmoral.toasty.Toasty

class AdminShopActivity : AppCompatActivity() {
    private lateinit var viewBindingAdmin : ActivityAdminShopBinding
    lateinit var viewModelUser : UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelUser = ViewModelProvider(this)[UserViewModel::class.java]
        viewBindingAdmin = ActivityAdminShopBinding.inflate(LayoutInflater.from(this))
        setContentView(viewBindingAdmin.root)
        viewBindingAdmin.toolbar.setTitle("Administator")

        viewBindingAdmin.btnAddShop.setOnClickListener {
            _showDialog(Gravity.BOTTOM)
        }
        viewBindingAdmin.btnShopRegister.setOnClickListener {
            startActivity(Intent(this, ListShopAndUserActivity::class.java))
        }
        viewBindingAdmin.btnRemove.setOnClickListener {
            startActivity(Intent(this, RemoveUserActivity::class.java))
        }
        viewModelUser.getLiveDataRegister1().observe(this, Observer {
            if (it == true) {
                Toasty.success(this, "Owner Shop is created successfully !", Toasty.LENGTH_SHORT).show()
            }else {
                Toasty.error(this, "Failure is creating owner shop",Toasty.LENGTH_SHORT).show()
            }
        })
        viewBindingAdmin.btnExit.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
    private fun _showDialog(gravity: Int ) {
        val dialog = Dialog(this)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.custom_dialog_shop)

        val window = dialog.window
        if (window == null ) return
        window?.apply {
            setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT)
            setGravity(gravity)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        val layout = dialog.findViewById<TextInputLayout>(R.id.layout)
        val edtUserName = dialog.findViewById<EditText>(R.id.edt_user_shop)
        val edtPass = dialog.findViewById<EditText>(R.id.edt_pass_shop)
        val edtPhone = dialog.findViewById<EditText>(R.id.edt_phone_shop)
        val edtAddress = dialog.findViewById<EditText>(R.id.edt_address_shop)
        val edtNameShop = dialog.findViewById<EditText>(R.id.edt_name_shop)

        val btnAdd = dialog.findViewById<Button>(R.id.btn_add)
        val btnCancel = dialog.findViewById<Button>(R.id.btn_cancel)
        btnAdd.setOnClickListener {
                _ ->
            //val strID       = System.currentTimeMillis().toString()
            val strUserName = edtUserName.text.toString().trim()
            val strNameShop = edtNameShop.text.toString().trim()
            val strPhone    = edtPhone.text.toString().trim()
            val strPass     = edtPass.text.toString().trim()
            val strAddress  = edtAddress.text.toString().trim()
            val user = User("",strUserName,strNameShop,strPhone,1,strPass,strAddress)
            viewModelUser.addShop(user)
            dialog.dismiss()
        }
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setCancelable(true)
        dialog.show()
    }
}