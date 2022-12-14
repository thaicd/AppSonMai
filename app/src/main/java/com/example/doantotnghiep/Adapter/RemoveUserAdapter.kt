package com.example.doantotnghiep.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.doantotnghiep.InterfaceProcess.IClickUser
import com.example.doantotnghiep.Model.User
import com.example.doantotnghiep.databinding.ItemRemoveShopBinding

class RemoveUserAdapter (public var mListUser : MutableList<User> , var mListener : IClickUser) : RecyclerView.Adapter<RemoveUserAdapter.UserViewHolder>(){
    inner class UserViewHolder(var binding: ItemRemoveShopBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = ItemRemoveShopBinding.inflate(LayoutInflater.from(parent.context), parent,false);
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val item = mListUser.get(position)
        if (item == null) return
        holder.binding.itemName.text = item.name
        holder.binding.itemPhone.text = item.phone
        holder.binding.itemAddress.text = item.address
        holder.binding.btnDeletePerson.setOnClickListener {
            mListener.getPosition(item)
        }
    }

    override fun getItemCount(): Int {
       return mListUser.size
    }
}