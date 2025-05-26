package com.example.foodpalapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.navigation.fragment.findNavController

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<LinearLayout>(R.id.linearLayoutFastFoodCategory)?.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_fastFoodFragment)
        }

        view.findViewById<LinearLayout>(R.id.linearLayoutBreakfastCategory)?.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_breakfastFragment)
        }

        view.findViewById<LinearLayout>(R.id.linearLayoutDrinksCategory)?.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_drinksFragment)
        }

        // ... (Kode lain untuk HomeFragment) ...
    }
}