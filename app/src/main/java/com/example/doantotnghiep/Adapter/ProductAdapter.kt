package com.example.doantotnghiep.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.doantotnghiep.IClickItem
import com.example.doantotnghiep.Model.Product
import com.example.doantotnghiep.Model.User
import com.example.doantotnghiep.databinding.ItemProductBinding
import com.example.doantotnghiep.databinding.ItemUserBinding
import com.google.firebase.database.core.Context
import com.squareup.picasso.Picasso

class ProductAdapter(var mContext: android.content.Context, val listener : IClickItem) : RecyclerView.Adapter<ProductAdapter.ProductsVH>() {

    private val differCallback = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsVH {
        val binding =
            ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductsVH(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ProductsVH, position: Int) {

        val item = differ.currentList[position]
        holder.binding.apply {

            // TODO clean logic

            itemName.text = item.nameProduct
            itemPrice.text = "Price: "+item.price.toString()
            itemNumber.text ="Number : "+item.number.toString()
            Picasso.with(mContext).load(item.imgUrl).into(itemProduct)

            layoutItem.setOnClickListener {
                listener.getPosition(holder.bindingAdapterPosition)
            }
            itemDelete.setOnClickListener{_->
                listener.getPosition(holder.adapterPosition)
            }

        }

    }

    inner class ProductsVH(val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root)
}