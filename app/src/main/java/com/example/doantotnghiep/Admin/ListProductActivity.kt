package com.example.doantotnghiep.Admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.doantotnghiep.Adapter.ProductAdapter
import com.example.doantotnghiep.InterfaceProcess.IClickItem
import com.example.doantotnghiep.Model.Product
import com.example.doantotnghiep.Repository.ShareReference
import com.example.doantotnghiep.ViewModel.ProductViewModel
import com.example.doantotnghiep.databinding.ActivityListProductBinding

class ListProductActivity : AppCompatActivity() , IClickItem {

    lateinit var viewBinding : ActivityListProductBinding
    lateinit var viewModel: ProductViewModel
    lateinit var adapterPro : ProductAdapter
    var mListProduct = mutableListOf<Product>()
    var user = ShareReference.getUser()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityListProductBinding.inflate(LayoutInflater.from(this))
        setContentView(viewBinding.root)
        supportActionBar?.apply {
            title = "List Product"
            setDisplayHomeAsUpEnabled(true)
        }
        adapterPro = ProductAdapter(mListProduct,this, this)

        viewBinding.toolbar.setTitle("List Product")
        setSupportActionBar(viewBinding.toolbar)
        supportActionBar?.apply {
            this.setDisplayShowHomeEnabled(true)
            this.setDisplayHomeAsUpEnabled(true);
        }
        viewBinding.recyclerProduct.apply {
            hasFixedSize()
            adapter = adapterPro
            layoutManager = LinearLayoutManager(this@ListProductActivity)
           // addItemDecoration(DividerItemDecoration(this@ListProductActivity, LinearLayoutManager.HORIZONTAL))

        }

        viewModel = ViewModelProvider(this)[ProductViewModel::class.java]
        viewModel.responseLiveData(user.id!!).observe(this, object : Observer<List<Product>>{
            override fun onChanged(t: List<Product>?) {
                t?.apply {
                    //adapterPro.differ.submitList(t)
                    mListProduct.addAll(this)
                    adapterPro.notifyDataSetChanged()
                }
                viewBinding.layoutShimmer.startShimmer()
                viewBinding.layoutShimmer.visibility = View.GONE
                viewBinding.loadingProduct.visibility = View.GONE
                if (t == null)
                {
                    viewBinding.labelData.visibility = View.VISIBLE
                    viewBinding.labelData.text = "Production is empty"
                }else if(t.size == 0) {
                    viewBinding.labelData.visibility = View.VISIBLE
                    viewBinding.labelData.text = "Production is empty"
                }
            }
        })

        viewBinding.layoutShimmer.startShimmer()
    }

    override fun getPosition(index: Int) {

    }
}