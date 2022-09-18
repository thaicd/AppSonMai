package com.example.doantotnghiep.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.doantotnghiep.Model.User
import com.example.doantotnghiep.databinding.ItemUserBinding

class UserAdapter : RecyclerView.Adapter<UserAdapter.NewsVH>() {

    private val differCallback = object : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsVH {
        val binding =
            ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsVH(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: NewsVH, position: Int) {

        val item = differ.currentList[position]
        holder.binding.apply {

            // TODO clean logic

                itemArticleTitle.text = item.name
                itemPostDescription.text = item.address
                itemPostAuthor.text = item.phone


            // on item click
            holder.itemView.setOnClickListener {
                onItemClickListener?.let { it(item) }
            }
        }
    }

    inner class NewsVH(val binding: ItemUserBinding ) : RecyclerView.ViewHolder(binding.root)

    // on item click listener
    private var onItemClickListener: ((User) -> Unit)? = null
    fun setOnItemClickListener(listener: (User) -> Unit) {
        onItemClickListener = listener
    }
}