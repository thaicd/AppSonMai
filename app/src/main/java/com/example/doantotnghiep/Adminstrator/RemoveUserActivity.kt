package com.example.doantotnghiep.Adminstrator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.doantotnghiep.Adapter.RemoveUserAdapter
import com.example.doantotnghiep.InterfaceProcess.IClickUser
import com.example.doantotnghiep.Model.User
import com.example.doantotnghiep.ViewModel.UserViewModel
import com.example.doantotnghiep.databinding.ActivityRemoveUserBinding

class RemoveUserActivity : AppCompatActivity() , IClickUser{
    lateinit var viewBinding : ActivityRemoveUserBinding
    lateinit var viewModel   : UserViewModel
    lateinit var adapterUser    : RemoveUserAdapter
    var mListUser = mutableListOf<User>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel   = ViewModelProvider(this)[UserViewModel::class.java]
        adapterUser = RemoveUserAdapter(mListUser, this)
        viewBinding = ActivityRemoveUserBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        viewBinding.toolbar.setTitle("Remove User")
        setSupportActionBar(viewBinding.toolbar)
        supportActionBar?.apply {
            this.setDisplayHomeAsUpEnabled(true)
            this.setDisplayShowHomeEnabled(true)
        }
        viewModel.getListShop()
        viewModel.getLiveDataListUser1().observe(this, Observer {
            it?.apply {
                mListUser.addAll(this)
                adapterUser.notifyDataSetChanged()
            }

        })
        viewBinding?.apply {
            this.recyclerUser?.apply {
                hasFixedSize()
                adapter = adapterUser
                layoutManager = LinearLayoutManager(this@RemoveUserActivity, RecyclerView.VERTICAL, false)
            }
        }
    }

    override fun getPosition(user: User) {
        viewModel.deleteUser(user)
    }
}