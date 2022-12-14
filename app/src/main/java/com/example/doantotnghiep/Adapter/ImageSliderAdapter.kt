package com.example.doantotnghiep.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.doantotnghiep.R
import com.example.doantotnghiep.databinding.ItemImageBinding
import com.squareup.picasso.Picasso

class ImageSliderAdapter(var listImage : MutableList<Int>, var mContext : Context) : RecyclerView.Adapter<ImageSliderAdapter.ImageViewHolder>()  {


    inner class ImageViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.img_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image,parent,false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
//        TODO("Not yet implemented")
        holder.imageView?.apply {
            this.setImageResource(listImage.get(position))
        }
    }

    override fun getItemCount(): Int {
//        TODO("Not yet implemented")
        return listImage.size
    }
}