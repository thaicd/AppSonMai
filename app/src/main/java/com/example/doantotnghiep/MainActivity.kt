package com.example.doantotnghiep

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.doantotnghiep.Admin.HomeAdminActivity
import com.example.doantotnghiep.Customer.CustomerActivity
import com.example.doantotnghiep.Helper.CustomProgressBar
import com.example.doantotnghiep.Model.User
import com.example.doantotnghiep.Repository.ShareReference
import com.example.doantotnghiep.ViewModel.LoginViewModel
import com.example.doantotnghiep.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {
    lateinit var viewBindingActivity : ActivityMainBinding
    lateinit var loginViewModel      : LoginViewModel
    lateinit var dialog              : CustomProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        dialog = CustomProgressBar(this@MainActivity)
        dialog.showProgressBar(this)
        super.onCreate(savedInstanceState)


        viewBindingActivity = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(viewBindingActivity.root)

        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        loginViewModel.checkLoginSession(dialog)

        loginViewModel.getuserLoginLiveData().observe(this, object : Observer<User> {
            override fun onChanged(t: User?) {
                if (t != null)
                {
                    dialog.dismissDialog()
                    when(t.role_id) {
                        0 -> {

                            val intent = Intent(this@MainActivity, HomeAdminActivity::class.java)
                            startActivity(intent)
                        }
                        2 -> {
//                            dialog.dismissDialog()
                            ShareReference.putUser(t)
                            val intent = Intent(this@MainActivity, CustomerActivity::class.java)
                            startActivity(intent)
                        }
                        else -> {
//                            dialog.dismissDialog()

                        }
                    }
                }
            }

        })
        loginViewModel.getloginLiveData().observe(this, object : Observer<FirebaseUser> {
            override fun onChanged(t: FirebaseUser?) {
                if (t != null ) {
                    loginViewModel.checkLoginSession(dialog)
                }else {
                    Toast.makeText(this@MainActivity, " Tài khoản hoặc mật khẩu của bạn không đúng", Toast.LENGTH_SHORT).show()
                }
            }
        })
        viewBindingActivity.btnRegister.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                val intent = Intent(this@MainActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
        })
        viewBindingActivity.btnLogin.setOnClickListener(object  : View.OnClickListener{
            override fun onClick(p0: View?) {
                val strUser = viewBindingActivity.inputUsername.text.toString().trim()

                if (strUser.length == 0 ) {
                    Toast.makeText(this@MainActivity, "Xin hãy nhập tài khoản ", Toast.LENGTH_SHORT).show()
                    return
                }
                val strPass = viewBindingActivity.inputPassword.text.toString().trim()
                if (strUser.length == 0 ) {
                    Toast.makeText(this@MainActivity, "Xin hãy nhập mật khẩu ", Toast.LENGTH_SHORT).show()
                    return
                }
                dialog.showProgressBar(this@MainActivity)
                loginViewModel.login(strUser,strPass) ;

            }

        })

    }

    override fun onStart() {
        super.onStart()
        loginViewModel.checkLoginSession(dialog)
    }

}