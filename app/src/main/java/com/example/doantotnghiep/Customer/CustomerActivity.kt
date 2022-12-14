package com.example.doantotnghiep.Customer

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import com.example.doantotnghiep.Adapter.CategoryAdapter
import com.example.doantotnghiep.Adapter.ImageSliderAdapter
import com.example.doantotnghiep.Customer.Fragment.CartFragment
import com.example.doantotnghiep.Customer.Fragment.FavoriteFragment
import com.example.doantotnghiep.Customer.Fragment.HomeFragment
import com.example.doantotnghiep.Customer.Fragment.OrderFragment
import com.example.doantotnghiep.Helper.CustomProgressBar
import com.example.doantotnghiep.MainActivity
import com.example.doantotnghiep.Model.Category
import com.example.doantotnghiep.Model.Order
import com.example.doantotnghiep.Model.Product
import com.example.doantotnghiep.R
import com.example.doantotnghiep.ViewModel.CategoryViewModel
import com.example.doantotnghiep.databinding.ActivityCustomerBinding
import com.google.firebase.auth.FirebaseAuth

class CustomerActivity : AppCompatActivity() {
    companion object {
        var mCategory : Category = Category()
        var mProduct  : Product  = Product()
        var mOrder    : Order    = Order()
    }

    lateinit var viewBindingCustomer : ActivityCustomerBinding
    lateinit var adapterCate : CategoryAdapter
    lateinit var viewModelCate : CategoryViewModel
    lateinit var progress : CustomProgressBar
    lateinit var adapterViewPager : ImageSliderAdapter
    var mListCategory : MutableList<Category> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        progress = CustomProgressBar(this)
//        progress.showProgressBar(this)

        viewBindingCustomer = ActivityCustomerBinding.inflate(LayoutInflater.from(this))
        setContentView(viewBindingCustomer.root)
        supportActionBar?.apply {
            title = "List Category"

        }

        viewBindingCustomer.bottomBar.setOnItemSelectedListener {
            when(it) {
                0 -> {
                    supportActionBar?.apply {
                        title = "Production"

                    }
                    replaceFragment(HomeFragment())
                   // Toasty.info(this, "Home", Toasty.LENGTH_SHORT).show()
                }
                1 -> {
                    //Toasty.info(this, "Favorite", Toasty.LENGTH_SHORT).show()
                    supportActionBar?.apply {
                        title = "My Favorite"

                    }
                    replaceFragment(FavoriteFragment())

                }
                2 -> {
                   // Toasty.info(this, "Cart", Toasty.LENGTH_SHORT).show()
                    supportActionBar?.apply {
                        title = "My Cart"

                    }
                    replaceFragment(CartFragment())
                }
                3 -> {
                  //  Toasty.info(this, "Order", Toasty.LENGTH_SHORT).show()
                    supportActionBar?.apply {
                        title = "My Order"

                    }
                    replaceFragment(OrderFragment())
                }
                else ->{
                    showDialog()
                }
            }
        }
        replaceFragment(HomeFragment())

    }
    fun showDialog() {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Log out")
            .setCancelable(true)
            .setMessage("Do you want to log out ?")
            .setNegativeButton("Yes", {
               i1, i2 ->
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
                i1.dismiss()

            })
            .setPositiveButton("No" , {
                i1, i2 ->
                i1.dismiss()
            })

        dialog.show()
    }
    fun replaceFragment(fragment : Fragment,) {
        val frag = supportFragmentManager.beginTransaction()
        frag.replace(R.id.fragment_container, fragment)
        frag.commit()
    }
}