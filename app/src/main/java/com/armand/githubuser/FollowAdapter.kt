package com.armand.githubuser

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.armand.githubuser.databinding.ItemsUserBinding
import com.bumptech.glide.Glide

class FollowAdapter : RecyclerView.Adapter<FollowAdapter.ListViewHolder>() {

    private val listUser = ArrayList<ResponseFollow>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(data: ArrayList<ResponseFollow>) {
        val diffResult = DiffUtil.calculateDiff(UserDiffCallback(listUser, data))
        listUser.clear()
        listUser.addAll(data)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemsUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    override fun getItemCount(): Int {
        return listUser.size
    }

    inner class ListViewHolder(private val binding: ItemsUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: ResponseFollow) {
            with(binding) {
                tvAccount.text = user.htmlUrl
                tvUsername.text = user.login

                Glide.with(itemView.context)
                    .load(user.avatarUrl)
                    .skipMemoryCache(true)
                    .into(imgAvatar)

                itemView.setOnClickListener { onItemClickCallback.onItemClicked(user) }
            }
        }
    }

    fun interface OnItemClickCallback {
        fun onItemClicked(data: ResponseFollow)
    }

    private class UserDiffCallback(private val oldList: List<ResponseFollow>, private val newList: List<ResponseFollow>) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition].login == newList[newItemPosition].login

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition] == newList[newItemPosition]
    }
}
