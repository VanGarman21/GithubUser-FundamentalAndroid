package com.armand.githubuser

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.armand.githubuser.databinding.ItemsUserBinding
import com.bumptech.glide.Glide


class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.ListViewHolder>() {

    private val listFavorite = ArrayList<ResponseDetailUser>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(data: ArrayList<ResponseDetailUser>) {
        val diffCallback = DiffUtilCallback(listFavorite, data)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        listFavorite.clear()
        listFavorite.addAll(data)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemsUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listFavorite[position])
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listFavorite[position])
        }
    }


    override fun getItemCount(): Int {
        return listFavorite.size
    }

    inner class ListViewHolder(private val binding: ItemsUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: ResponseDetailUser) {
            binding.apply {
                tvAccount.text = user.htmlUrl
                tvUsername.text = user.login
                Glide.with(root)
                    .load(user.avatarUrl)
                    .skipMemoryCache(true)
                    .into(imgAvatar)
            }
        }
    }

    fun interface OnItemClickCallback {
        fun onItemClicked(user: ResponseDetailUser)
    }

    class DiffUtilCallback(
        private val oldList: List<ResponseDetailUser>,
        private val newList: List<ResponseDetailUser>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

}