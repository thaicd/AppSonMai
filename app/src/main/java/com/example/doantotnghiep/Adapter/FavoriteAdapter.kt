package com.example.doantotnghiep.Adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.doantotnghiep.IClickItem
import com.example.doantotnghiep.Model.MyFavoriteProduct
import com.example.doantotnghiep.databinding.ItemCartBinding
import com.example.doantotnghiep.databinding.ItemFavoriteBinding
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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
            ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteVH(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: FavoriteVH, position: Int) {

        val item = differ.currentList[position]

        val skimmer = Shimmer.ColorHighlightBuilder()
            .setBaseColor(Color.parseColor("#F3F3F3"))
            .setBaseAlpha(1F)
            .setHighlightColor(Color.parseColor("#E7E7E7"))
            .setHighlightAlpha(1F)
            .setDropoff(50F)
            .build()
        val shimmerDrawable = ShimmerDrawable()
        shimmerDrawable.setShimmer(skimmer)


        holder.binding.apply {

            // TODO clean logic

            itemName.text = item.prod?.nameProduct
            itemPrice.text = "Price: " + item.prod?.price.toString()

            FirebaseDatabase.getInstance().getReference("Products/${item.prod!!.id}").child("number")
                .addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot != null) {
                            val number = snapshot.getValue(Long::class.java)
                            itemNumber.text = "Number : " + number.toString()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })
            Picasso.with(mContext).load(item.prod?.imgUrl).into(itemProduct)

            layoutItem.setOnClickListener {
                listener.getPosition(holder.bindingAdapterPosition)
            }

        }

    }

    inner class FavoriteVH(val binding: ItemFavoriteBinding) : RecyclerView.ViewHolder(binding.root)
}