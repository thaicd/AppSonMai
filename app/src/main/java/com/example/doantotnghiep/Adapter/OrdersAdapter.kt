package com.example.doantotnghiep.Adapter

import android.app.Activity
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.doantotnghiep.IClickItem
import com.example.doantotnghiep.Model.Order
import com.example.doantotnghiep.Model.Product
import com.example.doantotnghiep.databinding.ItemOrdersBinding
import com.example.doantotnghiep.databinding.ItemProductBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class OrdersAdapter ( var listOrders: MutableList<Order>, var mContext: android.content.Context, val listener : IClickItem) : RecyclerView.Adapter<OrdersAdapter.OrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding =
            ItemOrdersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun getItemCount(): Int {
        Log.d( "getItemCount: ",listOrders.size.toString())
        return listOrders.size
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {

        val item = listOrders[position]
        Log.d("ITEM", item.toString())
        holder.binding.apply {
            Log.d( "onBindViewHolder: ",position.toString())
            item?.apply {
                Log.d( "onBindViewHolder1: ",position.toString())
                itemId.text = this.idOrder.toString()
                itemPrice.text = "Price: "+this.totalPrice.toString()
                itemNumber.text ="Number : "+this.numberOptions.toString()
                if (item.statusOrder == 1) {
                    itemStatusOrder.text = "Processing"
                    itemStatusOrder.setBackgroundColor(Color.parseColor("#FFFFEB3B"))
                }
            }
            // TODO clean logic
            layoutItem.setOnClickListener {
                listener.getPosition(holder.bindingAdapterPosition)
            }
        }

    }

    inner class OrderViewHolder(val binding: ItemOrdersBinding) : RecyclerView.ViewHolder(binding.root)
}