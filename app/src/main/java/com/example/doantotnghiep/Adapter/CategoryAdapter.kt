package com.example.doantotnghiep.Adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.doantotnghiep.IClickItem
import com.example.doantotnghiep.Model.Category
import com.example.doantotnghiep.databinding.ItemCategoryBinding
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable

class CategoryAdapter (val mListCategory : MutableList<Category>,
                       val mContext : Context,
                       val listener : IClickItem

                       ) : RecyclerView.Adapter<CategoryAdapter.CategoryVH>(){

    inner class CategoryVH(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cate : Category) {
            binding.itemName.text = cate.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryVH {
        val view = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryVH(view)
    }

    override fun onBindViewHolder(holder: CategoryVH, position: Int) {
        val cate = mListCategory.get(position)
        val shimmer = Shimmer.ColorHighlightBuilder()
            .setBaseColor(Color.parseColor("#F7F7F7"))
            .setBaseAlpha(1F)
            .setHighlightColor(Color.parseColor("#E3E3E3"))
            .setHighlightAlpha(1F)
            .setDropoff(50F)
            .build()
        val shimmerDrawable = ShimmerDrawable()
        shimmerDrawable.setShimmer(shimmer)
        if (cate == null ) return
        holder.binding?.apply {
            itemName.text = cate.name

        }
        holder.itemView.setOnClickListener { _ ->
            listener.getPosition(holder.adapterPosition)
        }


    }

    override fun getItemCount(): Int {
       return mListCategory.size
    }
}