package com.example.doantotnghiep.Admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.doantotnghiep.Adapter.UserAdapter
import com.example.doantotnghiep.InterfaceProcess.IClickItem
import com.example.doantotnghiep.InterfaceProcess.IClickUser
import com.example.doantotnghiep.Model.User
import com.example.doantotnghiep.ViewModel.UserViewModel
import com.example.doantotnghiep.databinding.ActivityListUserBinding

class ListUserActivity : AppCompatActivity(), IClickUser {
    lateinit var viewBinding : ActivityListUserBinding
    lateinit var _adapter: UserAdapter

    lateinit var viewModel : UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityListUserBinding.inflate(LayoutInflater.from(this))
        setContentView(viewBinding.root)
        supportActionBar?.setTitle("List User")
        supportActionBar?.setDisplayShowHomeEnabled(true)
        viewModel = ViewModelProvider(this)[UserViewModel::class.java]
        _adapter = UserAdapter(this)
        viewBinding.toolbar.setTitle("List User")
        setSupportActionBar(viewBinding.toolbar)
        supportActionBar?.apply {
            this.setDisplayShowHomeEnabled(true)
            this.setDisplayHomeAsUpEnabled(true);
        }
        viewBinding.recyclerUser?.apply {
            adapter = _adapter
            hasFixedSize()
            layoutManager = LinearLayoutManager(this@ListUserActivity,LinearLayoutManager.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(this@ListUserActivity, LinearLayoutManager.HORIZONTAL))
        }

        viewModel.getDataListUser.observe(this, object : Observer<List<User>>{
            override fun onChanged(t: List<User>?) {
                t?.apply {
                    var listUser = mutableListOf<User>()
                    for (data in this ) {
                        if (data.role_id == 2 ) {
                            listUser.add(data)
                        }
                    }
                    _adapter.differ.submitList(listUser)
                }
            }

        })


    }
    override fun getPosition(user: User) {
    }
}