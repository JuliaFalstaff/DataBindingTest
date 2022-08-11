package com.example.shoppinglistapp.presentation

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglistapp.R
import com.example.shoppinglistapp.databinding.ActivityMainBinding
import com.example.shoppinglistapp.presentation.adapter.ShopListAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: ShopListAdapter
    lateinit var binding: ActivityMainBinding
    private var shopItemContainer: FragmentContainerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        shopItemContainer = binding.shopItemContainer
        setRV()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        viewModel.shopList.observe(this, Observer {
            adapter.submitList(it)
        })
    }

    private fun setRV() {
        adapter = ShopListAdapter()
        binding.recyclerViewShopList.adapter = adapter
        binding.recyclerViewShopList.recycledViewPool.setMaxRecycledViews(ShopListAdapter.ENABLED, 15)
        binding.recyclerViewShopList.recycledViewPool.setMaxRecycledViews(ShopListAdapter.DISABLED, 15)
        setAdapterClickListeners()
        setSwipeListener()
        addNewItem()
    }

    private fun isPaneMode(): Boolean {
          return shopItemContainer == null
    }

    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
                .replace(R.id.shop_item_container, fragment)
                .addToBackStack(null)
                .commit()
    }

    private fun addNewItem() {
            binding.fabAddItem.setOnClickListener {
                if (isPaneMode()) {
                    val intent = ShopItemActivity.newIntentAddItem(this)
                    startActivity(intent)
                } else {
                    launchFragment(ShopItemFragment.newInstanceAddItem())
                }
            }
    }

    private fun setSwipeListener() {
        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = adapter.currentList[viewHolder.adapterPosition]
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
            if (isPaneMode()) {
                val intent = ShopItemActivity.newIntentEditItem(this, it.id)
                startActivity(intent)
            } else {
                launchFragment(ShopItemFragment.newInstanceEditItem(it.id))
            }
        }
    }
}