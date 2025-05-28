package com.example.foodpalapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CartViewModel : ViewModel() {
    private val _cartItems = MutableLiveData<MutableList<CartItem>>()
    val cartItems: LiveData<MutableList<CartItem>> = _cartItems

    init {
        _cartItems.value = mutableListOf()
    }

    fun addItem(item: CartItem) {
        val currentItems = _cartItems.value ?: mutableListOf()
        val existingItem = currentItems.find { it.id == item.id }
        if (existingItem != null) {
            existingItem.quantity += item.quantity
        } else {
            currentItems.add(item)
        }
        _cartItems.value = currentItems
    }

    fun removeItem(itemId: String) {
        val currentItems = _cartItems.value ?: mutableListOf()
        currentItems.removeAll { it.id == itemId }
        _cartItems.value = currentItems
    }

    fun updateQuantity(itemId: String, quantity: Int) {
        val currentItems = _cartItems.value ?: mutableListOf()
        val itemToUpdate = currentItems.find { it.id == itemId }
        itemToUpdate?.quantity = quantity
        _cartItems.value = currentItems
    }

    fun clearCart() {
        _cartItems.value = mutableListOf()
    }
}