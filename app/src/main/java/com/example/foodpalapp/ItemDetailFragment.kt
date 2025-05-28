package com.example.foodpalapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.foodpalapp.databinding.FragmentItemDetailBinding

class ItemDetailFragment : Fragment() {

    private var _binding: FragmentItemDetailBinding? = null
    private val binding get() = _binding!!

    private var itemId: String? = null
    private var itemName: String = ""
    private var itemPrice: Double = 0.0
    private var quantity: Int = 1

    private val cartViewModel: CartViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentItemDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            itemId = it.getString("itemId")
            // Dapatkan detail item berdasarkan itemId (ganti dengan implementasi Anda)
            // Ini hanya contoh data statis
            when (itemId) {
                "unicorn_popcorn_id" -> {
                    itemName = "Unicorn Popcorn"
                    itemPrice = 4.00
                    Glide.with(requireContext())
                        .load(R.drawable.unicorn_popcorn) // Ganti dengan URL atau resource ID gambar yang benar
                        .into(binding.imageViewItem)
                    binding.textViewItemName.text = itemName
                    binding.textViewItemPrice.text = "$${itemPrice}"
                    binding.textViewDescription.text = getString(R.string.item_description_placeholder)
                    binding.textViewIngredients.text = getString(R.string.ingredients_description)
                }
            }
        }

        binding.buttonAddToCart.setOnClickListener {
            val cartItem = CartItem(itemId!!, itemName, itemPrice, quantity)
            cartViewModel.addItem(cartItem)
            Toast.makeText(requireContext(), "Added to cart", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_itemDetailFragment_to_cartFragment)
        }

        // Implementasi penambahan jumlah (jika diperlukan)
        // Pastikan Anda telah menambahkan elemen-elemen ini di fragment_item_detail.xml
        binding.buttonIncreaseQuantity.setOnClickListener {
            quantity++
            binding.textViewQuantity.text = quantity.toString()
        }
        binding.buttonDecreaseQuantity.setOnClickListener {
            if (quantity > 1) {
                quantity--
                binding.textViewQuantity.text = quantity.toString()
            }
        }
        binding.textViewQuantity.text = quantity.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}