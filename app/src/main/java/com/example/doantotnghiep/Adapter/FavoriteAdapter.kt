package com.example.doantotnghiep.Adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.doantotnghiep.InterfaceProcess.IClickItem
import com.example.doantotnghiep.Model.MyFavoriteProduct
import com.example.doantotnghiep.Model.Product
import com.example.doantotnghiep.databinding.ItemFavoriteBinding

import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class FavoriteAdapter (var mListFavorite : MutableList<MyFavoriteProduct> ,var mContext: android.content.Context, val listener : IClickItem) : RecyclerView.Adapter<FavoriteAdapter.FavoriteVH>() {


    override fun getItemCount(): Int {
        return mListFavorite.size
    }

    override fun onBindViewHolder(holder: FavoriteVH, position: Int) {

        val item = mListFavorite[position]

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

            FirebaseDatabase.getInstance().getReference("Products/${item.prod!!.idShop}/${item.prod!!.id}")
                .addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot != null) {
                            val prod = snapshot.getValue(Product::class.java)
                            prod?.apply {
                                if(this.number != 0) {
                                    itemName.text = this.nameProduct
                                    itemPrice.text = this.price.toLong().toString()+" VND"
                                    itemStarNumber.text = this.rate.toString()
                                }
                            }
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteVH {
        val view = ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return FavoriteVH(view)
    }
}