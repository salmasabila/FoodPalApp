package com.example.foodpalapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth

class LoginPhoneFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login_phone, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        val emailEditText = view.findViewById<EditText>(R.id.editTextEmailLogin)
        val passwordEditText = view.findViewById<EditText>(R.id.editTextPasswordLogin)
        val loginButton = view.findViewById<Button>(R.id.buttonLogInPhone)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            // Login berhasil, navigasi ke halaman home
                            Toast.makeText(requireContext(), "Login successful", Toast.LENGTH_SHORT).show()
                            findNavController().navigate(R.id.action_loginPhoneFragment_to_homeFragment)
                        } else {
// Jika login gagal, tampilkan pesan error ke pengguna
                            Toast.makeText(requireContext(), "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(requireContext(), "Please fill email and password", Toast.LENGTH_SHORT).show()
            }
        }

        // Anda bisa menambahkan logika untuk tombol "Continue with Google/Apple/Facebook" di sini
    }
}