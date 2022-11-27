package com.example.doantotnghiep.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.doantotnghiep.IClickItem
import com.example.doantotnghiep.Model.Cart
import com.example.doantotnghiep.R
import com.example.doantotnghiep.Repository.Repository
import com.example.doantotnghiep.databinding.ItemCartBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class CartAdapter (var mContext: android.content.Context, val listener : IClickItem) : RecyclerView.Adapter<CartAdapter.CartVH>() {

    var uid = FirebaseAuth.getInstance().currentUser!!.uid
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
            txtValueNumber.text = item.numberChoice.toString()
            btnIncrease.setOnClickListener {
                var tempNumber = txtValueNumber.text.toString().toInt() + 1
                var tempPrice  = item.totalPrice + item.prod!!.price!!.toInt()
                itemPrice.text = "Price: " + tempPrice.toString()
                txtValueNumber.text = tempNumber.toString()
                FirebaseDatabase.getInstance().getReference("Orders/${uid}/${item.idOrder}/${item.prod!!.id}")
                    .child("numberOptions").setValue(tempNumber)
                FirebaseDatabase.getInstance().getReference("Orders/${uid}/${item.idOrder}/${item.prod!!.id}")
                    .child("totalPrice").setValue(tempPrice)


                FirebaseDatabase.getInstance().getReference("Carts/${uid}/${item.prod!!.id}")
                    .child("numberChoice").setValue(tempNumber)
                FirebaseDatabase.getInstance().getReference("Carts/${uid}/${item.prod!!.id}")
                    .child("totalPrice").setValue(tempPrice)

            }
            btnDecrease.setOnClickListener {
                var tempNumber = txtValueNumber.text.toString().toInt() - 1
                var tempPrice  = item.totalPrice - item.prod!!.price!!.toInt()
                itemPrice.text = "Price: " + tempPrice.toString()
                txtValueNumber.text = tempNumber.toString()
                FirebaseDatabase.getInstance().getReference("Orders/${uid}/${item.idOrder}/${item.prod!!.id}")
                    .child("numberOptions").setValue(tempNumber)
                FirebaseDatabase.getInstance().getReference("Orders/${uid}/${item.idOrder}/${item.prod!!.id}")
                    .child("totalPrice").setValue(tempPrice)

                FirebaseDatabase.getInstance().getReference("Carts/${uid}/${item.prod!!.id}")
                    .child("numberChoice").setValue(tempNumber)
                FirebaseDatabase.getInstance().getReference("Carts/${uid} FirebaseDatabase.getInstance().getReference(\"Orders/${uid}/${item.idOrder}/${item.prod!!.id}\")/${item.prod!!.id}")
                    .child("totalPrice").setValue(tempPrice)
            }
            btnDeleteItem.setOnClickListener {
                FirebaseDatabase.getInstance().getReference("Orders/${uid}/${item.idOrder}/${item.prod!!.id}").removeValue()
                FirebaseDatabase.getInstance().getReference("Carts/${uid}/${item.prod!!.id}").removeValue()
                notifyDataSetChanged()
            }
//            Picasso.with(mContext).load(item.).into(itemProduct)
            Picasso.with(mContext).load(item.prod!!.imgUrl).error(R.drawable.ic_baseline_attach_money_24).into(this.itemProduct)
            layoutItem.setOnClickListener {
                listener.getPosition(holder.bindingAdapterPosition)
            }

        }

    }

    inner class CartVH(val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root)
}