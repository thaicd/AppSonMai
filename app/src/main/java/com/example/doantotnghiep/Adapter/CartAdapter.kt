package com.example.doantotnghiep.Adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.doantotnghiep.Customer.Fragment.CartFragment.Companion.flag
import com.example.doantotnghiep.InterfaceProcess.IClickItem
import com.example.doantotnghiep.Model.Cart
import com.example.doantotnghiep.R
import com.example.doantotnghiep.databinding.ItemCartBinding
import com.example.doantotnghiep.interface_item.ItemChange
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.travijuu.numberpicker.library.Enums.ActionEnum
import com.travijuu.numberpicker.library.Interface.ValueChangedListener
import es.dmoral.toasty.Toasty

class CartAdapter (var listCart : MutableList<Cart>, var mContext: android.content.Context, val listener : IClickItem, var itemListener : ItemChange) : RecyclerView.Adapter<CartAdapter.CartVH>() {

    var uid = FirebaseAuth.getInstance().currentUser!!.uid
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartVH {
        val binding =
            ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartVH(binding)
    }

    override fun getItemCount(): Int {
        Log.d("SIZE : ","${listCart.size}")
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
//            txtValueNumber.text = item.numberChoice.toString()
            btnPick.value = item.numberChoice
            btnPick.valueChangedListener = object :ValueChangedListener{
                override fun valueChanged(value: Int, action: ActionEnum?) {
                    if (action == ActionEnum.INCREMENT) {
                        FirebaseDatabase.getInstance().getReference("Products/${item.prod!!.idShop}/${item.prod!!.id}").child("number")
                            .addListenerForSingleValueEvent(object  : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot != null) {
                                        val product = snapshot.getValue(Long::class.java)
                                        product?.apply {
                                            Log.d("increse ", " ${this} ")

                                            if (this <= 0){

                                                Toasty.error(mContext,"This item is empty",Toasty.LENGTH_SHORT).show()
                                            }else {
                                                val txtValueNumber = btnPick.value
                                                Log.d("INCREAMENT ", "txtValueNumber = ${txtValueNumber}")
                                                var tempNumber = txtValueNumber.toInt()
                                                var tempPrice  = itemPrice.text.toString().toInt()+ item.prod!!.price!!.toInt()
                                                var numberProduct = this - 1
                                                if (numberProduct <= 0 ) {
                                                    btnPick.setActionEnabled(ActionEnum.INCREMENT, false)
                                                }else {
                                                    btnPick.setActionEnabled(ActionEnum.INCREMENT, true)
                                                }
                                                itemPrice.text =  tempPrice.toString()
                                                itemListener.changeItem(tempPrice.toString())
                                                btnPick.value = txtValueNumber
                                                FirebaseDatabase.getInstance().getReference("Products/${item.prod!!.idShop}/${item.prod!!.id}").child("number")
                                                    .setValue(numberProduct)
                                                FirebaseDatabase.getInstance().getReference("Carts/${uid}/${item.prod!!.id}")
                                                    .child("numberChoice").setValue(tempNumber)
                                                FirebaseDatabase.getInstance().getReference("Carts/${uid}/${item.prod!!.id}")
                                                    .child("totalPrice").setValue(tempPrice)

                                            }

                                        }
                                    }
                                }
                                override fun onCancelled(error: DatabaseError) {
                                }
                            })
                        itemListener.changeItem("+${item.prod!!.price}")
                    }else if(action == ActionEnum.DECREMENT) {
                        var txtValueNumber = btnPick.value
                        var tempNumber = txtValueNumber.toInt()
                        if(txtValueNumber == 0) {
                            flag = 1;
                            Log.d("txtValueNumber", "${txtValueNumber} ")
                            FirebaseDatabase.getInstance().getReference("Carts/${uid}/${item.prod!!.id}").removeValue()
                            listCart.remove(item)
                            notifyDataSetChanged()

                            Toasty.success(mContext, "Delete Item is successful!", Toasty.LENGTH_SHORT).show()
                            FirebaseDatabase.getInstance().getReference("Products/${item.prod!!.idShop}/${item.prod!!.id}").child("number")
                                .addListenerForSingleValueEvent(object  : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if (snapshot != null) {
                                            val product = snapshot.getValue(Long::class.java)
                                            product?.apply {
                                                val numberTemp = this + 1
                                                FirebaseDatabase.getInstance().getReference("Products/${item.prod!!.idShop}/${item.prod!!.id}").child("number")
                                                    .setValue(numberTemp)
                                            }
                                        }
                                    }
                                    override fun onCancelled(error: DatabaseError) {
                                    }
                                })

                        }else{
                            var tempPrice  = itemPrice.text.toString().toInt() - item.prod!!.price!!.toInt()
                            itemPrice.text =  tempPrice.toString()
                            btnPick.value = tempNumber
//                            txtValueNumber.text =
                            FirebaseDatabase.getInstance().getReference("Carts/${uid}/${item.prod!!.id}")
                                .child("numberChoice").setValue(tempNumber)
                            FirebaseDatabase.getInstance().getReference("Carts/${uid}/${item.prod!!.id}")
                                .child("totalPrice").setValue(tempPrice)
                            FirebaseDatabase.getInstance().getReference("Products/${item.prod!!.idShop}/${item.prod!!.id}").child("number")
                                .addListenerForSingleValueEvent(object  : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if (snapshot != null) {
                                            val product = snapshot.getValue(Long::class.java)
                                            product?.apply {
                                                val numberTemp = this + 1
                                                FirebaseDatabase.getInstance().getReference("Products/${item.prod!!.idShop}/${item.prod!!.id}").child("number")
                                                    .setValue(numberTemp)
                                            }
                                        }
                                    }
                                    override fun onCancelled(error: DatabaseError) {
                                    }
                                })
                        }
                        itemListener.changeItem("-${item.prod!!.price}")
                    }
                }

            }
            btnDeleteItem.setOnClickListener {

                itemListener.changeItem( "-"+itemPrice.text.toString().trim())
                FirebaseDatabase.getInstance().getReference("Carts/${uid}/${item.prod!!.id}").removeValue()
                Toasty.success(mContext, "Delete Item is successful!", Toasty.LENGTH_SHORT).show()
                FirebaseDatabase.getInstance().getReference("Products/${item.prod!!.idShop}/${item.prod!!.id}").child("number")
                    .addListenerForSingleValueEvent(object  : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot != null) {
                                val product = snapshot.getValue(Long::class.java)
                                product?.apply {
                                    val numberTemp = this + btnPick.value.toInt()
                                    FirebaseDatabase.getInstance().getReference("Products/${item.prod!!.idShop}/${item.prod!!.id}").child("number")
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