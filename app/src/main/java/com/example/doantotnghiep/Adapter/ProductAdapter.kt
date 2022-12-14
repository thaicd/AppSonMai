package com.example.doantotnghiep.Adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.doantotnghiep.InterfaceProcess.IClickItem
import com.example.doantotnghiep.Model.Product
import com.example.doantotnghiep.databinding.ItemProductBinding
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable

import com.squareup.picasso.Picasso

class ProductAdapter(var mListProduct : MutableList<Product> ,var mContext:Context, var listener : IClickItem) : RecyclerView.Adapter<ProductAdapter.ProductsVH>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsVH {
        val binding =
            ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductsVH(binding)
    }

    override fun getItemCount(): Int {
        if (mListProduct.size == 0) return 0
        if (mListProduct == null ) return  0
        return mListProduct.size
    }

    override fun onBindViewHolder(holder: ProductsVH, position: Int) {

        val item = mListProduct[position]
        if (item.number == 0 ) return
        val shimmer = Shimmer.ColorHighlightBuilder()
            .setBaseColor(Color.parseColor("#F7F7F7"))
            .setBaseAlpha(1F)
            .setHighlightColor(Color.parseColor("#E3E3E3"))
            .setHighlightAlpha(1F)
            .build()
        val shimmerData = ShimmerDrawable()
        shimmerData.setShimmer(shimmer)
        holder.binding.apply {

            // TODO clean logic

            itemName.text = item.nameProduct
            itemPrice.text = item.price.toInt().toString()+" VND"

            itemStarNumber.text = item.rate.toString()
            Picasso.with(mContext).load(item.imgUrl).into(itemProduct)

            layoutItem.setOnClickListener {
                listener.getPosition(holder.bindingAdapterPosition)
            }
        }

    }

    inner class ProductsVH(val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root)
}