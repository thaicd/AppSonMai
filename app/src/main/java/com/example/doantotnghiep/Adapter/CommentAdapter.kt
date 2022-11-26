package com.example.doantotnghiep.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.doantotnghiep.Model.Comment
import com.example.doantotnghiep.databinding.ItemCommentBinding
import com.google.firebase.database.core.Context

class CommentAdapter(var listComment : List<Comment>) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    inner class CommentViewHolder(val binding: ItemCommentBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = listComment[position]
        if (comment == null ) return
        holder.binding?.apply {
            itemNameUser.text = comment.nameUser
            itemMessageComment.text = comment.message
            itemTimeComment.text = comment.timeStamp
        }
    }

    override fun getItemCount(): Int {
        return listComment.size
    }
}