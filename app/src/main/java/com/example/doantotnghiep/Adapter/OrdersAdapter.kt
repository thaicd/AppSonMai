package com.example.doantotnghiep.Adapter

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
import com.squareup.picasso.Picasso

class OrdersAdapter (var mContext: android.content.Context, val listener : IClickItem) : RecyclerView.Adapter<OrdersAdapter.OrderViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.idOrder == newItem.idOrder
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding =
            ItemOrdersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {

        val item = differ.currentList[position]
        holder.binding.apply {
            item?.apply {
                itemName.text = this.listProduct!!.nameProduct
                itemPrice.text = "Price: "+this.totalPrice.toString()
                itemNumber.text ="Number : "+this.numberOptions.toString()
                Picasso.with(mContext).load(this.listProduct!!.imgUrl).into(holder.binding.imgProduct)
            }
            // TODO clean logic
            layoutItem.setOnClickListener {
                listener.getPosition(holder.bindingAdapterPosition)
            }
        }

    }

    inner class OrderViewHolder(val binding: ItemOrdersBinding) : RecyclerView.ViewHolder(binding.root)
}