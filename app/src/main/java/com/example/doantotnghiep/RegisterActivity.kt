package com.example.doantotnghiep

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.doantotnghiep.Customer.CustomerActivity
import com.example.doantotnghiep.Model.User
import com.example.doantotnghiep.ViewModel.RegisterViewModel
import com.example.doantotnghiep.databinding.ActivityMainBinding
import com.example.doantotnghiep.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseUser

class RegisterActivity : AppCompatActivity() {

    private lateinit var activityBinding : ActivityRegisterBinding
    private lateinit var viewModel       : RegisterViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityBinding = ActivityRegisterBinding.inflate(LayoutInflater.from(this))
        setContentView(activityBinding.root)
        // init viewmodel
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        viewModel.getregisterLiveData().observe(this, object : Observer<FirebaseUser>{
            override fun onChanged(t: FirebaseUser?) {
                if(t != null ) {
                    // transfer to LoginActivity
                    Log.d( "onChanged: ", "Đang ki Oke - ${t.email}")
                    val intent = Intent(this@RegisterActivity, CustomerActivity::class.java)
                    startActivity(intent)
                    finish()
                }else {
                    // logic transfer to LoginActivity
                    Log.d( "onChanged: ", "Đang ki thất bại")
                }
            }

        })

        activityBinding.btnRegister.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                val email = activityBinding.edtEmail.text.toString().trim()
                if (email.length == 0 ){
//                    Toast.makeText().show()
                    Toast.makeText(this@RegisterActivity,"Xin hãy nhập email trước khi đăng kí",Toast.LENGTH_SHORT).show()
                }
                val name = activityBinding.edtName.text.toString().trim()
                if (name.length == 0 ){
//                    Toast.makeText().show()
                    Toast.makeText(this@RegisterActivity,"Xin hãy nhập tên trước khi đăng kí",Toast.LENGTH_SHORT).show()
                }

                val pass = activityBinding.edtPass.text.toString().trim()
                if (name.length == 0 ){
//                    Toast.makeText().show()
                    Toast.makeText(this@RegisterActivity,"Xin hãy nhập mật khẩu trước khi đăng kí",Toast.LENGTH_SHORT).show()
                }

                val phone = activityBinding.edtPhone.text.toString().trim()
                if (phone.length == 0 ){
//                    Toast.makeText().show()
                    Toast.makeText(this@RegisterActivity,"Xin hãy nhập điện thoại trước khi đăng kí",Toast.LENGTH_SHORT).show()
                }
                val address = activityBinding.edtAddress.text.toString().trim()
                if (address.length == 0 ){
//                    Toast.makeText().show()
                    Toast.makeText(this@RegisterActivity,"Xin hãy nhập địa chỉ trước khi đăng kí",Toast.LENGTH_SHORT).show()
                }

                viewModel.register(User("", email,name,phone,2,pass,address))
            }

        })
    }
}