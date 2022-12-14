package com.example.doantotnghiep

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.doantotnghiep.Admin.HomeAdminActivity
import com.example.doantotnghiep.Adminstrator.AdminShopActivity
import com.example.doantotnghiep.Customer.CustomerActivity
import com.example.doantotnghiep.Helper.CustomProgressBar
import com.example.doantotnghiep.Model.User
import com.example.doantotnghiep.Repository.ShareReference
import com.example.doantotnghiep.ViewModel.LoginViewModel
import com.example.doantotnghiep.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import es.dmoral.toasty.Toasty

class MainActivity : AppCompatActivity() {
    lateinit var viewBindingActivity : ActivityMainBinding
    lateinit var loginViewModel      : LoginViewModel
    lateinit var dialog              : CustomProgressBar
    var mAuth  = FirebaseAuth.getInstance().currentUser
    override fun onCreate(savedInstanceState: Bundle?) {
        dialog = CustomProgressBar(this@MainActivity)
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        //dialog.showProgressBar(this)
        super.onCreate(savedInstanceState)
        viewBindingActivity = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(viewBindingActivity.root)
        loginViewModel.getuserLoginLiveData().observe(this, object : Observer<User> {
            override fun onChanged(t: User?) {
                if (t != null)
                {
                    dialog.dismissDialog()
                    when(t.role_id) {
                        0->{
                            val intent = Intent(this@MainActivity, AdminShopActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                         1-> {

                             val intent = Intent(this@MainActivity, HomeAdminActivity::class.java)
                             val bundle = Bundle()
                             ShareReference.putUser(t)
                             bundle.putSerializable("user", t)
                             intent.putExtras(bundle)
                             startActivity(intent)
                             finish()
                        }
                        2 -> {
//                            dialog.dismissDialog()
                            ShareReference.putUser(t)
                            val intent = Intent(this@MainActivity, CustomerActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        else -> {
                           dialog.dismissDialog()
                        }
                    }
                }else {
                    dialog.dismissDialog()
                    Toasty.error(this@MainActivity,"Login isn't successful")
                }
            }

        })
        loginViewModel.getloginLiveData().observe(this, object : Observer<FirebaseUser> {
            override fun onChanged(t: FirebaseUser?) {
                if (t != null ) {
                    //loginViewModel.checkLoginSession(dialog)
                }else {
                    dialog?.dismissDialog()
                    Toast.makeText(this@MainActivity, " Tài khoản hoặc mật khẩu của bạn không đúng", Toast.LENGTH_SHORT).show()
                }
            }
        })

        viewBindingActivity.btnRegister.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                Toast.makeText(this@MainActivity,"Register", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@MainActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
        })
        viewBindingActivity.btnForgetPassword.setOnClickListener {
            startActivity(Intent(this@MainActivity, ResetPasswordActivity::class.java))
        }
        viewBindingActivity.btnLogin.setOnClickListener(object  : View.OnClickListener{
            override fun onClick(p0: View?) {
                val strUser = viewBindingActivity.inputUserName.text.toString().trim()

                if (strUser.length == 0 ) {
                    Toast.makeText(this@MainActivity, "Xin hãy nhập tài khoản ", Toast.LENGTH_SHORT).show()
                    return
                }
                val strPass = viewBindingActivity.inputPassWord.text.toString().trim()
                if (strUser.length == 0 ) {
                    Toast.makeText(this@MainActivity, "Xin hãy nhập mật khẩu ", Toast.LENGTH_SHORT).show()
                    return
                }
                dialog.showProgressBar(this@MainActivity)
                loginViewModel.login(strUser,strPass)

            }

        })

    }

    override fun onStart() {
        super.onStart()
        //loginViewModel.checkLoginSession(dialog)
    }

}