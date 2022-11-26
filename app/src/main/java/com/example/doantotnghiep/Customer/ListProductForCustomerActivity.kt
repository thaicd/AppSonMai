package com.example.doantotnghiep.Customer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.doantotnghiep.Adapter.ProductAdapter
import com.example.doantotnghiep.Helper.Constanst
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
    var listProduct = mutableListOf<Product>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityListProductBinding.inflate(LayoutInflater.from(this))
        setContentView(viewBinding.root)
        Log.d(Constanst.log
            , "category ")
        val bundle = intent.extras

        if(bundle != null  ) {

            cateProduct = bundle.getSerializable("object") as Category
            Log.d(Constanst.log
                , "cate :${cateProduct} ")
        }else {
            Log.d(Constanst.log
                , "not cate  ")
        }

        adapterP = ProductAdapter(this, this)
        viewModelProduct = ViewModelProvider(this)[ProductViewModel::class.java]

        viewModelProduct.responseLiveData.observe(this, Observer {
            it?.apply {

                listProduct.clear()
                for (p in it ) {
                    Log.d(Constanst.log
                        , "after :${p.id} - ${cateProduct.id}")
                    if (p.idCategory.equals(cateProduct.id) ) {
                        listProduct.add(p)
                    }
                }
                Log.d(Constanst.log
                    , "list after :${listProduct} ")
                adapterP.differ.submitList(listProduct)
                adapterP.notifyDataSetChanged()
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