package com.example.shoppinglistapp.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.shoppinglistapp.domain.ShopItem

class ShopListCallback(
        private val oldList: List<ShopItem>,
        private val newList: List<ShopItem>
        ) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem == newItem
    }
}