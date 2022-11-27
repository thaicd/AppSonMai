package com.example.doantotnghiep.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.doantotnghiep.IClickItem
import com.example.doantotnghiep.Model.MyFavoriteProduct
import com.example.doantotnghiep.databinding.ItemCartBinding
import com.squareup.picasso.Picasso

class FavoriteAdapter (var mContext: android.content.Context, val listener : IClickItem) : RecyclerView.Adapter<FavoriteAdapter.FavoriteVH>() {

    private val differCallback = object : DiffUtil.ItemCallback<MyFavoriteProduct>() {
        override fun areItemsTheSame(oldItem: MyFavoriteProduct, newItem: MyFavoriteProduct): Boolean {
            return oldItem.prod?.id == newItem.prod?.id
        }

        override fun areContentsTheSame(oldItem: MyFavoriteProduct, newItem: MyFavoriteProduct): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteVH {
        val binding =
            ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteVH(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: FavoriteVH, position: Int) {

        val item = differ.currentList[position]
        holder.binding.apply {

            // TODO clean logic

            itemName.text = item.prod?.nameProduct
            itemPrice.text = "Price: " + item.prod?.price.toString()
            txtValueNumber.text = "Number : " + item.prod?.number.toString()
            Picasso.with(mContext).load(item.prod?.imgUrl).into(itemProduct)

            layoutItem.setOnClickListener {
                listener.getPosition(holder.bindingAdapterPosition)
            }

        }

    }

    inner class FavoriteVH(val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root)
}