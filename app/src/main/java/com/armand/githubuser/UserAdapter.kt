package com.armand.githubuser

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.armand.githubuser.databinding.ItemsUserBinding
import com.bumptech.glide.Glide

class UserAdapter : RecyclerView.Adapter<UserAdapter.ListViewHolder>() {

    private val userList = mutableListOf<Items>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(data: List<Items>) {
        val diffResult = DiffUtil.calculateDiff(DiffCallback(userList, data))
        userList.clear()
        userList.addAll(data)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemsUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(userList[position])
    }

    override fun getItemCount(): Int = userList.size

    inner class ListViewHolder(private val binding: ItemsUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: Items) {
            with(binding) {
                tvAccount.text = user.htmlUrl
                tvUsername.text = user.login

                Glide.with(root)
                    .load(user.avatarUrl)
                    .skipMemoryCache(true)
                    .into(imgAvatar)

                root.setOnClickListener { onItemClickCallback.onItemClicked(user) }
            }
        }
    }

    fun interface OnItemClickCallback {
        fun onItemClicked(data: Items)
    }

    class DiffCallback(private val oldList: List<Items>,private val newList: List<Items>) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition].login == newList[newItemPosition].login
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition] == newList[newItemPosition]
    }
}
