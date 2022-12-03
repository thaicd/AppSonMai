package com.example.doantotnghiep.Adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.doantotnghiep.IClickItem
import com.example.doantotnghiep.Model.Cart
import com.example.doantotnghiep.Model.Product
import com.example.doantotnghiep.R
import com.example.doantotnghiep.Repository.Repository
import com.example.doantotnghiep.databinding.ItemCartBinding
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.tasks.await

class CartAdapter (var listCart : MutableList<Cart> , var mContext: android.content.Context, val listener : IClickItem) : RecyclerView.Adapter<CartAdapter.CartVH>() {

    var uid = FirebaseAuth.getInstance().currentUser!!.uid
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartVH {
        val binding =
            ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartVH(binding)
    }

    override fun getItemCount(): Int {
        return listCart.size
    }

    override fun onBindViewHolder(holder: CartVH, position: Int) {

        val item = listCart[position]
        val shimmer = Shimmer.ColorHighlightBuilder()
            .setBaseColor(Color.parseColor("#F7F7F7"))
            .setBaseAlpha(1F)
            .setHighlightColor(Color.parseColor("#E3E3E3"))
            .setHighlightAlpha(1F)
            .setDropoff(50F)
            .build()

        val shimmerDrawable = ShimmerDrawable()
        shimmerDrawable.setShimmer(shimmer)
        holder.binding.apply {
            itemName.text = item.prod?.nameProduct
            itemPrice.text = item.totalPrice.toString()
            txtValueNumber.text = item.numberChoice.toString()
            btnIncrease.setOnClickListener {
                FirebaseDatabase.getInstance().getReference("Products/${item.prod!!.id}").child("number")
                    .addListenerForSingleValueEvent(object  : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot != null) {
                                val product = snapshot.getValue(Long::class.java)
                                product?.apply {
                                    var tempNumber = txtValueNumber.text.toString().toInt() + 1
                                    var tempPrice  = itemPrice.text.toString().toInt()+ item.prod!!.price!!.toInt()
                                    if (this <= 0){
                                        Toasty.error(mContext,"This item is empty",Toasty.LENGTH_SHORT).show()
                                    }else {
                                        var numberProduct = this - 1
                                        itemPrice.text =  tempPrice.toString()
                                        txtValueNumber.text = tempNumber.toString()
                                        FirebaseDatabase.getInstance().getReference("Orders/${uid}/${item.idOrder}/${item.prod!!.id}")
                                            .child("numberOptions").setValue(tempNumber)
                                        FirebaseDatabase.getInstance().getReference("Orders/${uid}/${item.idOrder}/${item.prod!!.id}")
                                            .child("totalPrice").setValue(tempPrice)
                                        FirebaseDatabase.getInstance().getReference("Carts/${uid}/${item.prod!!.id}")
                                            .child("numberChoice").setValue(tempNumber)
                                        FirebaseDatabase.getInstance().getReference("Carts/${uid}/${item.prod!!.id}")
                                            .child("totalPrice").setValue(tempPrice)
                                        FirebaseDatabase.getInstance().getReference("Products/${item.prod!!.id}").child("number")
                                            .setValue(numberProduct)
                                    }

                                }
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {
                        }
                    })


            }
            btnDecrease.setOnClickListener {
                var tempNumber = txtValueNumber.text.toString().toInt() - 1
                if(tempNumber == 0) {
                    FirebaseDatabase.getInstance().getReference("Orders/${uid}/${item.idOrder}/${item.prod!!.id}").removeValue()
                    FirebaseDatabase.getInstance().getReference("Carts/${uid}/${item.prod!!.id}").removeValue()
                    Toasty.success(mContext, "Delete Item is successful!", Toasty.LENGTH_SHORT).show()
                    FirebaseDatabase.getInstance().getReference("Products/${item.prod!!.id}").child("number")
                        .addListenerForSingleValueEvent(object  : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot != null) {
                                    val product = snapshot.getValue(Long::class.java)
                                    product?.apply {
                                        val numberTemp = this + txtValueNumber.text.toString().toInt()
                                        FirebaseDatabase.getInstance().getReference("Products/${item.prod!!.id}").child("number")
                                            .setValue(numberTemp)
                                    }
                                }
                            }
                            override fun onCancelled(error: DatabaseError) {
                            }
                        })
                    listCart.remove(item)
                    notifyDataSetChanged()
                }else{
                    var tempPrice  = itemPrice.text.toString().toInt() - item.prod!!.price!!.toInt()
                    itemPrice.text =  tempPrice.toString()
                    txtValueNumber.text = tempNumber.toString()
                    FirebaseDatabase.getInstance().getReference("Orders/${uid}/${item.idOrder}/${item.prod!!.id}")
                        .child("numberOptions").setValue(tempNumber)
                    FirebaseDatabase.getInstance().getReference("Orders/${uid}/${item.idOrder}/${item.prod!!.id}")
                        .child("totalPrice").setValue(tempPrice)

                    FirebaseDatabase.getInstance().getReference("Carts/${uid}/${item.prod!!.id}")
                        .child("numberChoice").setValue(tempNumber)
                    FirebaseDatabase.getInstance().getReference("Carts/${uid}/${item.prod!!.id}")
                        .child("totalPrice").setValue(tempPrice)
                    FirebaseDatabase.getInstance().getReference("Products/${item.prod!!.id}").child("number")
                        .addListenerForSingleValueEvent(object  : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot != null) {
                                    val product = snapshot.getValue(Long::class.java)
                                    product?.apply {
                                        val numberTemp = this + 1
                                        FirebaseDatabase.getInstance().getReference("Products/${item.prod!!.id}").child("number")
                                            .setValue(numberTemp)
                                    }
                                }
                            }
                            override fun onCancelled(error: DatabaseError) {
                            }
                        })
                }
            }
            btnDeleteItem.setOnClickListener {
                FirebaseDatabase.getInstance().getReference("Orders/${uid}/${item.idOrder}/${item.prod!!.id}").removeValue()
                FirebaseDatabase.getInstance().getReference("Carts/${uid}/${item.prod!!.id}").removeValue()
                Toasty.success(mContext, "Delete Item is successful!", Toasty.LENGTH_SHORT).show()
                FirebaseDatabase.getInstance().getReference("Products/${item.prod!!.id}").child("number")
                    .addListenerForSingleValueEvent(object  : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot != null) {
                                val product = snapshot.getValue(Long::class.java)
                                product?.apply {
                                    val numberTemp = this + txtValueNumber.text.toString().toInt()
                                    FirebaseDatabase.getInstance().getReference("Products/${item.prod!!.id}").child("number")
                                        .setValue(numberTemp)
                                }
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {
                        }
                    })

                listCart.remove(item)
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