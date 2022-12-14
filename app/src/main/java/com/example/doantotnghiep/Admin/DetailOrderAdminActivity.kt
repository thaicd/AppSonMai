package com.example.doantotnghiep.Admin

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.CompoundButton
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.doantotnghiep.Adapter.OrderDetailAdapter
import com.example.doantotnghiep.Model.Order
import com.example.doantotnghiep.Model.OrderDetails
import com.example.doantotnghiep.Repository.ShareReference
import com.example.doantotnghiep.ViewModel.OrderViewModel
import com.example.doantotnghiep.databinding.ActivityDetailOrderAdminBinding
import com.example.doantotnghiep.databinding.CustomDialogOptionsBinding
import es.dmoral.toasty.Toasty

class DetailOrderAdminActivity : AppCompatActivity() {
    lateinit var viewBinding : ActivityDetailOrderAdminBinding
    lateinit var detailsAdapter : OrderDetailAdapter
    lateinit var viewModelOrderDetails: OrderViewModel
    lateinit var order : Order
    var is_confirm = -1
    var listOrders = mutableListOf<OrderDetails>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_detail_order_admin)
        viewBinding = ActivityDetailOrderAdminBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        viewModelOrderDetails = ViewModelProvider(this)[OrderViewModel::class.java]
//        setContentView(R.layout.activity_order_details)
        detailsAdapter = OrderDetailAdapter(listOrders, this)
        val data = intent.extras
        data?.apply {
            order = this.getSerializable("order") as Order
            is_confirm = this.getInt("is_confirm",0)
        }
        if (is_confirm == 9999) {
            viewBinding.btnConfirm.visibility = View.GONE
        }
        viewBinding.toolbar.setTitle("Detail Order")
        setSupportActionBar(viewBinding.toolbar)
        supportActionBar?.apply {
            this.setDisplayShowHomeEnabled(true)
            this.setDisplayHomeAsUpEnabled(true);
        }
        order?.apply {
            viewModelOrderDetails.getListOrdersDetail(order.idUser!!,order.idOrder!!).observe(this@DetailOrderAdminActivity,
                Observer {
                    if (it != null && it.size > 0){
                        listOrders.addAll(it)
                        detailsAdapter.notifyDataSetChanged()
                    }
                })
        }
        viewModelOrderDetails.liveDataOrder.observe(this, Observer {
            if (it == true) {
                Toasty.warning(this,"Updated status order is successful !", Toasty.LENGTH_SHORT).show()
            }else {
                Toasty.warning(this,"Updated status order is failure !", Toasty.LENGTH_SHORT).show()

            }
        })
        viewBinding.btnConfirm.setOnClickListener {
            openDialog()
        }
        viewBinding.orderAdminDetail?.apply {
            hasFixedSize()
            adapter = detailsAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        }
    }
    fun openDialog(){
        val dialog = Dialog(this)
        val binding = CustomDialogOptionsBinding.inflate(layoutInflater)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(binding.root)

        var window = dialog.window
        window?.apply {
            this.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
            this.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val attWindow = this.attributes
            attWindow.gravity = Gravity.BOTTOM
            this.attributes = attWindow

            dialog.setCancelable(true)
            binding.radioComplete.setOnCheckedChangeListener(object :CompoundButton.OnCheckedChangeListener{
                override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                    Log.d( "onCheckedChanged: ", "[radioComplete] ${isChecked}")
                    if (isChecked) {
                        viewModelOrderDetails.updateStatusOrder(order.idUser!!,order.idOrder!!,4);
                        dialog.dismiss()
                        startActivity(Intent(this@DetailOrderAdminActivity, OrderProductAdminActivity::class.java))
                        finish()
                    }
                }
            })
            binding.radioDelivery.setOnCheckedChangeListener(object :CompoundButton.OnCheckedChangeListener{
                override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
//                    Log.d( "onCheckedChanged: ", "[radioDelivery] ${isChecked}")
                    if (isChecked) {
                        viewModelOrderDetails.updateStatusOrder(order.idUser!!,order.idOrder!!,3);
                        dialog.dismiss()
                    }
                }
            })
            binding.radioWaiting.setOnCheckedChangeListener(object :CompoundButton.OnCheckedChangeListener{
                override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
//                    Log.d( "onCheckedChanged: ", "[radioWaiting] ${isChecked}")
                    if (isChecked) {
                        viewModelOrderDetails.updateStatusOrder(order.idUser!!,order.idOrder!!,2);
                        dialog.dismiss()
                    }
                }
            })

            dialog.show()
        }
    }
}