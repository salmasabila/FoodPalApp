package com.example.foodpalapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.navigation.fragment.findNavController
import com.example.foodpalapp.databinding.FragmentFastFoodBinding

class FastFoodFragment : Fragment() {

    private var _binding: FragmentFastFoodBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFastFoodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set OnClickListener untuk setiap item makanan cepat saji
        binding.cardViewClassicCheeseBurger.setOnClickListener {
            navigateToItemDetail("classic_cheese_burger_id") // Ganti dengan ID unik item Anda
        }

        binding.cardViewGrandpasHotDog.setOnClickListener {
            navigateToItemDetail("grandpas_hot_dog_id") // Ganti dengan ID unik item Anda
        }

        binding.cardViewVeryFrenchFries.setOnClickListener {
            navigateToItemDetail("very_french_fries_id") // Ganti dengan ID unik item Anda
        }

        binding.cardViewUnicornPopcorn.setOnClickListener {
            navigateToItemDetail("unicorn_popcorn_id") // Ganti dengan ID unik item Anda
        }

        // ... (Tambahkan OnClickListener untuk item lainnya jika ada) ...

        // Set OnClickListener untuk tombol Cart di bottom navigation
        binding.linearLayoutCartButton.setOnClickListener {
            findNavController().navigate(R.id.action_fastFoodFragment_to_cartFragment) // Navigasi ke CartFragment
        }
    }

    private fun navigateToItemDetail(itemId: String) {
        val bundle = Bundle().apply {
            putString("itemId", itemId)
        }
        findNavController().navigate(R.id.action_fastFoodFragment_to_itemDetailFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}