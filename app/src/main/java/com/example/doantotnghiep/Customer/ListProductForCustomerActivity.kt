package com.example.doantotnghiep.Customer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.doantotnghiep.Adapter.ProductAdapter
import com.example.doantotnghiep.Helper.Constanst
import com.example.doantotnghiep.Helper.CustomProgressBar
import com.example.doantotnghiep.IClickItem
import com.example.doantotnghiep.Model.Category
import com.example.doantotnghiep.Model.Product
import com.example.doantotnghiep.ViewModel.ProductViewModel
import com.example.doantotnghiep.databinding.ActivityListProductBinding
import es.dmoral.toasty.Toasty

class ListProductForCustomerActivity : AppCompatActivity() , IClickItem{
    lateinit var viewBinding : ActivityListProductBinding
    lateinit var adapterP : ProductAdapter
    lateinit var cateProduct : Category
    lateinit var viewModelProduct : ProductViewModel
    lateinit var loadingProduct : CustomProgressBar
    var listProduct = mutableListOf<Product>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadingProduct = CustomProgressBar(this)
        viewBinding = ActivityListProductBinding.inflate(LayoutInflater.from(this))
        setContentView(viewBinding.root)

        val bundle = intent.extras
        supportActionBar?.apply {
            title = "List Production"
            setDisplayHomeAsUpEnabled(true)
        }
        if(bundle != null  ) {
            cateProduct = bundle.getSerializable("object") as Category
        }else {
            cateProduct = CustomerActivity.mCategory
        }
        viewBinding.loadingProduct.visibility = View.VISIBLE
        adapterP = ProductAdapter(this, this)
        viewModelProduct = ViewModelProvider(this)[ProductViewModel::class.java]

        viewModelProduct.responseLiveData.observe(this, Observer {
            it?.apply {
                listProduct.clear()
                for (p in it ) {
                    if (p.idCategory.equals(cateProduct.id) ) {
                        listProduct.add(p)
                    }
                }
                if (listProduct.size > 0) {
                    adapterP.differ.submitList(listProduct)
                    adapterP.notifyDataSetChanged()
                    viewBinding.layoutShimmer.stopShimmer()
                    viewBinding.layoutShimmer.visibility = View.GONE
                }else {
                    viewBinding.labelData.text = "Not Data"
                }
                viewBinding.loadingProduct.visibility = View.INVISIBLE
            }
        })

        viewBinding.recyclerProduct?.apply {
            adapter = adapterP
            layoutManager = LinearLayoutManager(this@ListProductForCustomerActivity,
                LinearLayoutManager.VERTICAL, false)
            hasFixedSize()
            addItemDecoration(DividerItemDecoration(this@ListProductForCustomerActivity,
                DividerItemDecoration.VERTICAL))
        }
        viewBinding.layoutShimmer.startShimmer()
    }

    override fun getPosition(index: Int) {

        Toasty.info(this,"pos = $index",Toasty.LENGTH_SHORT).show()
        val intent = Intent(this, DetailProductActivity::class.java)
        val bundle = Bundle()
        bundle.putSerializable("product", listProduct.get(index))
        intent.putExtras(bundle)
        startActivity(intent)
    }
}