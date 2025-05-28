package com.example.foodpalapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodpalapp.databinding.CartItemLayoutBinding

class CartItemAdapter(
    private val cartItems: MutableList<CartItem>,
    private val onItemChanged: () -> Unit
) : RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder>() {

    inner class CartItemViewHolder(binding: CartItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        val imageViewItem: ImageView = binding.imageViewItem
        val textViewItemName: TextView = binding.textViewItemName
        val textViewItemPrice: TextView = binding.textViewItemPrice
        val textViewQuantity: TextView = binding.textViewQuantity
        val buttonDecreaseQuantity: Button = binding.buttonDecreaseQuantity
        val buttonIncreaseQuantity: Button = binding.buttonIncreaseQuantity
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        val binding = CartItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        val currentItem = cartItems[position]

        // Set item details using data from currentItem
        holder.textViewItemName.text = currentItem.name
        holder.textViewItemPrice.text = "$${currentItem.price}"
        holder.textViewQuantity.text = currentItem.quantity.toString()
        // Load image using an image loading library like Glide or Picasso

        holder.buttonIncreaseQuantity.setOnClickListener {
            currentItem.quantity++
            holder.textViewQuantity.text = currentItem.quantity.toString()
            onItemChanged() // Notify the fragment to update the total
        }

        holder.buttonDecreaseQuantity.setOnClickListener {
            if (currentItem.quantity > 1) {
                currentItem.quantity--
                holder.textViewQuantity.text = currentItem.quantity.toString()
                onItemChanged() // Notify the fragment to update the total
            }
        }
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }
}