package com.example.doantotnghiep.Adminstrator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.doantotnghiep.Adapter.UserAdapter
import com.example.doantotnghiep.InterfaceProcess.IClickItem
import com.example.doantotnghiep.InterfaceProcess.IClickUser
import com.example.doantotnghiep.Model.User
import com.example.doantotnghiep.ViewModel.UserViewModel
import com.example.doantotnghiep.databinding.ActivityListShopAndUserBinding

class ListShopAndUserActivity : AppCompatActivity(), IClickUser {
    lateinit var binding : ActivityListShopAndUserBinding
    lateinit var viewModelUser : UserViewModel
    lateinit var adapterUser : UserAdapter
    lateinit var adapterShop : UserAdapter

    var mListUser = mutableListOf<User>()
    var mListShop = mutableListOf<User>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapterShop = UserAdapter(this)
        adapterUser = UserAdapter(this)
        binding = ActivityListShopAndUserBinding.inflate(layoutInflater)
        viewModelUser = ViewModelProvider(this)[UserViewModel::class.java]
        setContentView(binding.root)
        binding.toolbar.setTitle("List Person")
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            this.setDisplayHomeAsUpEnabled(true)
            this.setDisplayShowHomeEnabled(true)
        }
        binding?.apply {

            viewModelUser.getListShop()
            viewModelUser.getLiveDataListUser1().observe(this@ListShopAndUserActivity, Observer {
                if (it != null && it.size > 0) {
                    var mListU = mutableListOf<User>()
                    var mListS = mutableListOf<User>()
                    for (data in it ) {
                        if (data.role_id == 1 ) {
                            mListS.add(data)
                        }else if(data.role_id == 2) {
                            mListU.add(data)
                        }
                    }
                    adapterUser.differ.submitList(mListU)
                    adapterShop.differ.submitList(mListS)
                    adapterUser.notifyDataSetChanged()
                    adapterShop.notifyDataSetChanged()
                }
            })

            recyclerShop?.apply {
                adapter = adapterShop
                hasFixedSize()
                layoutManager = LinearLayoutManager(this@ListShopAndUserActivity, RecyclerView.VERTICAL,false)
            }

            recyclerUser?.apply {
                adapter = adapterUser
                hasFixedSize()
                layoutManager = LinearLayoutManager(this@ListShopAndUserActivity, RecyclerView.VERTICAL, false)
            }
        }
    }

    override fun getPosition(user: User) {

    }

}