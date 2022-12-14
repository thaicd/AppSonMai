package com.example.doantotnghiep.Admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.doantotnghiep.Adapter.OrdersAdapter
import com.example.doantotnghiep.InterfaceProcess.IClickItem
import com.example.doantotnghiep.Model.Order
import com.example.doantotnghiep.Repository.ShareReference
import com.example.doantotnghiep.ViewModel.OrderViewModel
import com.example.doantotnghiep.databinding.ActivityOrderSoldAdminBinding

class OrderSoldAdminActivity : AppCompatActivity(), IClickItem {
    lateinit var viewBinding : ActivityOrderSoldAdminBinding
    lateinit var orderAdapter : OrdersAdapter
    lateinit var viewModelOrder : OrderViewModel
    var mData = ShareReference.getUser()
    var mListOrders : MutableList<Order> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityOrderSoldAdminBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        viewBinding.toolbar.setTitle("List Order Sold")
        setSupportActionBar(viewBinding.toolbar)
        supportActionBar?.apply {
            this.setDisplayShowHomeEnabled(true)
            this.setDisplayHomeAsUpEnabled(true);
        }
        orderAdapter = OrdersAdapter(mListOrders,this,this)
        viewModelOrder = ViewModelProvider(this)[OrderViewModel::class.java]
        viewBinding.progressLoading.visibility = View.VISIBLE
        viewModelOrder.getListOrderUsers().observe(this, Observer {
            it?.apply {
                mListOrders.clear()
                if (it.size == 0) {
                    viewBinding.progressLoading.visibility = View.INVISIBLE
                    viewBinding.labelData.visibility = View.VISIBLE

                } else {
                    for (idUser in it) {
                        viewModelOrder.getListOrders(idUser).observe(this@OrderSoldAdminActivity,
                            Observer {
                                it?.apply {
                                    for (data in it ) {
                                        if (data.idShop == mData.id && data.statusOrder == 4 ) {// Completed
                                            mListOrders.add(data)
                                        }
                                    }
                                    orderAdapter.notifyDataSetChanged()
                                    if (mListOrders.size == 0) {
                                        viewBinding.labelData.visibility = View.VISIBLE
                                    }else{
                                        viewBinding.labelData.visibility = View.GONE
                                    }
                                }
                            })
                    }
                    //Log.d("mListOrders", "${mListOrders.size} ")

                    viewBinding.progressLoading.visibility = View.INVISIBLE

                }
            }
            viewBinding.layoutshimmer.stopShimmer()
            viewBinding.layoutshimmer.visibility = View.GONE
        })
        viewBinding.recyclerOrderDetail?.apply {
            adapter = orderAdapter
            layoutManager = LinearLayoutManager(this@OrderSoldAdminActivity,
                LinearLayoutManager.VERTICAL,false)
            hasFixedSize()
        }
    }

    override fun getPosition(index: Int) {
        val order = mListOrders[index]
        val intent = Intent(this, DetailOrderAdminActivity::class.java)
        val bundle = Bundle()
        bundle.putSerializable("order", order)
        bundle.putInt("is_confirm", 9999)
        intent.putExtras(bundle)
        startActivity(intent)
    }
}