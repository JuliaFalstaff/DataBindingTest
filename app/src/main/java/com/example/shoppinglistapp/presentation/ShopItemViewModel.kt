package com.example.shoppinglistapp.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoppinglistapp.data.ShopListRepositoryImpl
import com.example.shoppinglistapp.domain.AddShopItemUseCase
import com.example.shoppinglistapp.domain.EditShopItemUseCase
import com.example.shoppinglistapp.domain.GetShopItemUseCase
import com.example.shoppinglistapp.domain.ShopItem
import java.lang.Exception

class ShopItemViewModel: ViewModel() {

    private val repository = ShopListRepositoryImpl
    private val editShopItemUseCase = EditShopItemUseCase(repository)
    private val getShopItemUseCase = GetShopItemUseCase(repository)
    private val addShopItemUseCase = AddShopItemUseCase(repository)

    private val _shouldCloseActivity = MutableLiveData<Unit>()
    val shouldCloseActivity: LiveData<Unit>
    get() = _shouldCloseActivity

    private val _shopItem = MutableLiveData<ShopItem>()
    val shopItem: LiveData<ShopItem>
    get() = _shopItem

    private val _errorName = MutableLiveData<Boolean>()
    val errorInputName: LiveData<Boolean>
    get() = _errorName

    private val _errorCount = MutableLiveData<Boolean>()
    val errorInputCount: LiveData<Boolean>
        get() = _errorCount


    fun getShopItem(shopItemId: Int) {
        val item = getShopItemUseCase.getShopItem(shopItemId)
        _shopItem.value = item
    }

    fun addShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val fieldValid = validateInput(name, count)
        if (fieldValid) {
            addShopItemUseCase.addShopItem(ShopItem(name, count, true))
           finishWork()
        }
    }

    fun editShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val fieldValid = validateInput(name, count)
        if (fieldValid) {
            _shopItem.value?.let {
                val item = it.copy(name = name, count = count)
                editShopItemUseCase.editShopItem(item)
                finishWork()
            }
        }
    }

    private fun parseName(inputName: String?): String {
        return inputName?.trim() ?: ""
    }

    private fun parseCount(inputCount: String?): Int {
        return try {
            inputCount?.trim()?.toInt() ?: 0
        } catch (e: Exception) {
            0
        }
    }

    private fun validateInput(name: String, count: Int): Boolean {
        var result  = true
        if (name.isBlank()) {
            _errorName.value = true
            result = false
        }
        if (count <= 0) {
           _errorCount.value = true
            result = false
        }
        return result
    }

     fun resetErrorName() {
        _errorName.value = false
    }

     fun resetErrorCount() {
        _errorCount.value = false
    }

    private fun finishWork() {
        _shouldCloseActivity.value = Unit
    }
 }