package com.example.shoppinglistapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglistapp.databinding.ItemShopDisabledBinding
import com.example.shoppinglistapp.databinding.ItemShopEnabledBinding
import com.example.shoppinglistapp.domain.ShopItem

class ShopListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var shopList = listOf<ShopItem>()
        set(value) {
            val callback = ShopListDiffCallback(shopList, value)
            val diffResult = DiffUtil.calculateDiff(callback)
            diffResult.dispatchUpdatesTo(this)
            field = value
        }

    var onLongItemClickListener: ((ShopItem)-> Unit)? = null
    var onClickListener: ((ShopItem)-> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ENABLED -> ShopViewHolderEnabled(
                ItemShopEnabledBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
            DISABLED -> ShopViewHolderDisabled(
                ItemShopDisabledBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
            else -> throw RuntimeException(VIEWHOLDER_TYPE_ERROR)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = shopList[position]
        when (holder.itemViewType) {
            ENABLED -> {
                holder as ShopViewHolderEnabled
                holder.bind(item)
            }
            DISABLED -> {
                holder as ShopViewHolderDisabled
                holder.bind(item)
            }
        }
    }

    override fun getItemCount() = shopList.size

    override fun getItemViewType(position: Int): Int {
        val item = shopList[position]
        return if (item.enabled) {
            ENABLED
        } else {
            DISABLED
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
    }

    inner class ShopViewHolderEnabled(private val binding: ItemShopEnabledBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ShopItem) {
            binding.textViewCount.text = item.name
            binding.textViewCount.text = item.count.toString()
            itemView.setOnLongClickListener {
                onLongItemClickListener?.invoke(item)
                true
            }
            itemView.setOnClickListener {
                onClickListener?.invoke(item)
            }
        }
    }

    inner class ShopViewHolderDisabled(private val binding: ItemShopDisabledBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ShopItem) {
            binding.textViewCount.text = item.name
            binding.textViewCount.text = item.count.toString()
            itemView.setOnLongClickListener {
                onLongItemClickListener?.invoke(item)
                true
            }
            itemView.setOnClickListener {
                onClickListener?.invoke(item)
            }
        }
    }

    interface OnLongItemClickListener {
        fun onLingClick(shopItem: ShopItem)
    }


    companion object {
        const val ENABLED = 100
        const val DISABLED = 101
        private const val VIEWHOLDER_TYPE_ERROR = "Unknown type of viewholder"
    }
}