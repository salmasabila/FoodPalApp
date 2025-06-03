package com.example.foodpalapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast // Import Toast
import android.util.Log // Import Log
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

        // --- TAMBAHKAN ATAU MODIFIKASI BAGIAN INI UNTUK ITEM PROFIL ---
        val profileNavItem = view.findViewById<LinearLayout>(R.id.nav_item_profile) // Gunakan ID yang tadi ditambahkan di XML
        profileNavItem?.setOnClickListener {
            try {
                // Pastikan action_homeFragment_to_profileFragment sudah ada di nav_graph.xml
                findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
            } catch (e: IllegalArgumentException) {
                // Error ini terjadi jika ID aksi tidak ditemukan di NavController saat ini.
                // Cek apakah Anda berada di fragment yang benar dan ID aksi sudah benar di nav_graph.xml
                Toast.makeText(context, "Aksi navigasi ke Profil tidak ditemukan.", Toast.LENGTH_LONG).show()
                Log.e("HomeFragment", "Navigasi gagal: Aksi tidak ditemukan", e)
            } catch (e: Exception) {
                // Tangkap error umum lainnya
                Toast.makeText(context, "Gagal navigasi ke Profil: ${e.message}", Toast.LENGTH_LONG).show()
                Log.e("HomeFragment", "Navigasi ke Profil gagal", e)
            }
        }

        // ... (Kode lain untuk HomeFragment) ...
    }
}