package com.example.doantotnghiep.Adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.doantotnghiep.Model.OrderDetails
import com.example.doantotnghiep.R
import com.example.doantotnghiep.databinding.ItemDetailOrderBinding
import com.squareup.picasso.Picasso

class OrderDetailAdapter(var listOrder : MutableList<OrderDetails>, var mContext : Context) : RecyclerView.Adapter<OrderDetailAdapter.DetailViewHolder>(){
    inner class DetailViewHolder(var binding : ItemDetailOrderBinding):RecyclerView.ViewHolder(binding.root){
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        var view = ItemDetailOrderBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return DetailViewHolder(view)
    }

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        val orderDetails = listOrder[position]
        if (orderDetails == null) return
        holder.binding?.apply {
            Picasso.with(mContext).load(orderDetails.product!!.imgUrl).error(R.drawable.banan).into(this.imgProduct)
            this.itemName.text = orderDetails.product!!.nameProduct
            this.itemPrice.text = orderDetails.totalPrice.toString()+" VND"
            this.itemPrice.setTextColor(Color.RED)
            this.itemNumber.text = "Ordered: "+orderDetails.numberChoice.toString()+""
        }
    }

    override fun getItemCount(): Int {
        return listOrder.size
    }
}