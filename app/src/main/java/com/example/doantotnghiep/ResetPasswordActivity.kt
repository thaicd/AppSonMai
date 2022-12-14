package com.example.doantotnghiep

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.doantotnghiep.ViewModel.UserViewModel
import com.example.doantotnghiep.databinding.ActivityResetPasswordBinding
import es.dmoral.toasty.Toasty

class ResetPasswordActivity : AppCompatActivity() {
    lateinit var viewBinding : ActivityResetPasswordBinding
    lateinit var userViewModel: UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        viewBinding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        userViewModel.getLiveDataReset1().observe(this, Observer {
            if (it == true) {
                Toasty.success(this,"Sent email successfully. Please check email to reset password", Toasty.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
            }else {
                Toasty.error(this,"Sent email Failure", Toasty.LENGTH_SHORT).show()
            }
        })
        viewBinding.btnResendMail.setOnClickListener {
            var strEmail = viewBinding.edtPasswordRetry.toString().trim()
            if (Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()) {
                userViewModel.resetPassword(strEmail)
            }else {
                Toast.makeText(this," Không đúng định dạng email", Toast.LENGTH_SHORT).show()
            }
        }
    }
}