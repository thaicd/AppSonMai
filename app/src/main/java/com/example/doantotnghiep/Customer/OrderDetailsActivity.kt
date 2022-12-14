package com.example.doantotnghiep.Customer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.doantotnghiep.Adapter.OrderDetailAdapter
import com.example.doantotnghiep.Model.Order
import com.example.doantotnghiep.Model.OrderDetails
import com.example.doantotnghiep.R
import com.example.doantotnghiep.Repository.ShareReference
import com.example.doantotnghiep.ViewModel.OrderViewModel
import com.example.doantotnghiep.databinding.ActivityOrderDetailsBinding

class OrderDetailsActivity : AppCompatActivity() {
    lateinit var viewBinding : ActivityOrderDetailsBinding
    lateinit var detailsAdapter : OrderDetailAdapter
    lateinit var viewModelOrderDetails: OrderViewModel
    var listOrders = mutableListOf<OrderDetails>()
    lateinit var order : Order
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelOrderDetails = ViewModelProvider(this)[OrderViewModel::class.java]
//        setContentView(R.layout.activity_order_details)
        detailsAdapter = OrderDetailAdapter(listOrders, this)
        val data = intent.extras
        data?.apply {
            order = this.getSerializable("order") as Order
        }
        var user = ShareReference.getUser()
        order?.apply {
            viewModelOrderDetails.getListOrdersDetail(user.id!!,order.idOrder!!).observe(this@OrderDetailsActivity,
                Observer {
                    if (it != null && it.size > 0){
                        listOrders.addAll(it)
                        detailsAdapter.notifyDataSetChanged()
                    }
                })
        }
        viewBinding = ActivityOrderDetailsBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        viewBinding.recyclerviewOrderDetail?.apply {
            hasFixedSize()
            adapter = detailsAdapter
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        }
    }
}