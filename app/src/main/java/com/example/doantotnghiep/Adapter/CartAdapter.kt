package com.example.doantotnghiep.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.doantotnghiep.IClickItem
import com.example.doantotnghiep.Model.Cart
import com.example.doantotnghiep.databinding.ItemCartBinding
import com.squareup.picasso.Picasso

class CartAdapter (var mContext: android.content.Context, val listener : IClickItem) : RecyclerView.Adapter<CartAdapter.CartVH>() {

    private val differCallback = object : DiffUtil.ItemCallback<Cart>() {
        override fun areItemsTheSame(oldItem: Cart, newItem: Cart): Boolean {
            return oldItem.prod?.id == newItem.prod?.id
        }

        override fun areContentsTheSame(oldItem: Cart, newItem: Cart): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartVH {
        val binding =
            ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartVH(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: CartVH, position: Int) {

        val item = differ.currentList[position]
        holder.binding.apply {

            // TODO clean logic

            itemName.text = item.prod?.nameProduct
            itemPrice.text = "Price: " + item.totalPrice.toString()
            itemNumber.text = "Number : " + item.numberChoice.toString()
//            Picasso.with(mContext).load(item.).into(itemProduct)

            layoutItem.setOnClickListener {
                listener.getPosition(holder.bindingAdapterPosition)
            }

        }

    }

    inner class CartVH(val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root)
}