package com.example.foodpalapp

    import android.os.Bundle
    import androidx.fragment.app.Fragment
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.Toast // Untuk debugging jika perlu
    import android.util.Log // Untuk debugging jika perlu
    import androidx.navigation.fragment.findNavController
    import com.example.foodpalapp.databinding.FragmentProfileBinding // Impor kelas ViewBinding Anda

    class ProfileFragment : Fragment() {

        // Deklarasi ViewBinding
        private var _binding: FragmentProfileBinding? = null
        private val binding get() = _binding!! // Properti ini hanya valid antara onCreateView dan onDestroyView.

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            _binding = FragmentProfileBinding.inflate(inflater, container, false)
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            // ... (kode listener lain yang mungkin sudah ada di ProfileFragment) ...

            // --- TAMBAHKAN LISTENER UNTUK editProfileIcon ---
            binding.editProfileIcon.setOnClickListener {
                try {
                    // Pastikan aksi ini sudah didefinisikan di nav_graph.xml
                    // dan mengarah dari profileFragment ke detailProfileActivity (Activity)
                    findNavController().navigate(R.id.action_profileFragment_to_detailProfileActivity)
                } catch (e: IllegalArgumentException) {
                    // Error ini terjadi jika ID aksi tidak ditemukan.
                    Toast.makeText(context, "Aksi navigasi ke Detail Profil tidak ditemukan.", Toast.LENGTH_LONG).show()
                    Log.e("ProfileFragment", "Navigasi gagal: Aksi tidak ditemukan", e)
                } catch (e: Exception) {
                    // Tangkap error umum lainnya
                    Toast.makeText(context, "Gagal navigasi ke Detail Profil: ${e.message}", Toast.LENGTH_LONG).show()
                    Log.e("ProfileFragment", "Navigasi ke Detail Profil gagal", e)
                }
            }
        }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null // Penting untuk menghindari memory leak
        }
    }