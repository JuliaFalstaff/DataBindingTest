package com.example.shoppinglistapp.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglistapp.databinding.ActivityMainBinding
import com.example.shoppinglistapp.presentation.adapter.ShopListAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: ShopListAdapter
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setRV()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        viewModel.shopList.observe(this, Observer {
            adapter.shopList = it

        })
    }

    private fun setRV() {
        adapter = ShopListAdapter()
        binding.recyclerViewShopList.adapter = adapter
        binding.recyclerViewShopList.recycledViewPool.setMaxRecycledViews(ShopListAdapter.ENABLED, 15)
        binding.recyclerViewShopList.recycledViewPool.setMaxRecycledViews(ShopListAdapter.DISABLED, 15)
        setAdapterClickListeners()
        setSwipeListener()
    }

    private fun setSwipeListener() {
        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = adapter.shopList[viewHolder.adapterPosition]
                viewModel.deleteSopItem(item)
            }
        }

        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewShopList)
    }

    private fun setAdapterClickListeners() {
        adapter.onLongItemClickListener = {
            viewModel.changeEnableState(it)
        }
        adapter.onClickListener = {
            Log.d("TAG", "listener")
        }
    }
}