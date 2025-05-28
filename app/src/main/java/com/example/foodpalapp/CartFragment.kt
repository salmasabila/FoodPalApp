package com.example.foodpalapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodpalapp.databinding.FragmentCartBinding
import java.text.NumberFormat
import java.util.Locale

class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private lateinit var cartAdapter: CartItemAdapter
    private val cartViewModel: CartViewModel by activityViewModels()
    private val cartItems: MutableList<CartItem> = mutableListOf() // Daftar lokal untuk adapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cartAdapter = CartItemAdapter(cartItems) {
            updateTotal() // Panggil updateTotal setiap kali jumlah item berubah
        }

        binding.recyclerViewCartItems.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cartAdapter
        }

        // Observe LiveData dari ViewModel
        cartViewModel.cartItems.observe(viewLifecycleOwner) { items ->
            cartItems.clear()
            cartItems.addAll(items)
            cartAdapter.notifyDataSetChanged()
            updateTotal()
            binding.textViewEmptyCart.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE
            binding.recyclerViewCartItems.visibility = if (items.isNotEmpty()) View.VISIBLE else View.GONE
        }

        binding.buttonCheckout.setOnClickListener {
            // Implementasikan logika checkout di sini
        }

        // Hitung total awal (meskipun LiveData akan segera memperbarui)
        updateTotal()
    }

    private fun updateTotal() {
        var subtotal = 0.0
        for (item in cartItems) {
            subtotal += item.price * item.quantity
        }

        val deliveryFee = 0.0 // Atur biaya pengiriman sesuai kebutuhan
        val total = subtotal + deliveryFee

        val currencyFormat = NumberFormat.getCurrencyInstance(Locale("en", "US")) // Format ke mata uang USD
        binding.textViewSubtotal.text = currencyFormat.format(subtotal)
        binding.textViewDeliveryFee.text = "Free" // Atau format biaya pengiriman jika tidak gratis
        binding.textViewTotal.text = currencyFormat.format(total)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}