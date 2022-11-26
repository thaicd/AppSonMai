package com.example.doantotnghiep.Admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.doantotnghiep.Adapter.ProductAdapter
import com.example.doantotnghiep.IClickItem
import com.example.doantotnghiep.Model.Product
import com.example.doantotnghiep.R
import com.example.doantotnghiep.ViewModel.ProductViewModel
import com.example.doantotnghiep.ViewModel.UserViewModel
import com.example.doantotnghiep.databinding.ActivityListProductBinding

class ListProductActivity : AppCompatActivity() , IClickItem{

    lateinit var viewBinding : ActivityListProductBinding
    lateinit var viewModel: ProductViewModel
    lateinit var adapterPro : ProductAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityListProductBinding.inflate(LayoutInflater.from(this))
        setContentView(viewBinding.root)
        supportActionBar?.apply {
            title = "List Product"
            setDisplayHomeAsUpEnabled(true)
        }
        adapterPro = ProductAdapter(this@ListProductActivity, this)


        viewBinding.recyclerProduct.apply {
            hasFixedSize()
            adapter = adapterPro
            layoutManager = LinearLayoutManager(this@ListProductActivity)
            addItemDecoration(DividerItemDecoration(this@ListProductActivity, LinearLayoutManager.HORIZONTAL))

        }

        viewModel = ViewModelProvider(this)[ProductViewModel::class.java]
        viewModel.responseLiveData.observe(this, object : Observer<List<Product>>{
            override fun onChanged(t: List<Product>?) {
                t?.apply {
                    adapterPro.differ.submitList(t)
                }
            }
        })
    }

    override fun getPosition(index: Int) {

    }
}