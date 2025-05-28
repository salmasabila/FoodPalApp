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
import com.google.firebase.database.FirebaseDatabase

class SignInFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        view.findViewById<Button>(R.id.buttonSignUp)?.setOnClickListener {
            val email = view.findViewById<EditText>(R.id.editTextEmailSignIn).text.toString()
            val password = view.findViewById<EditText>(R.id.editTextPasswordSignIn).text.toString()
            val fullName = view.findViewById<EditText>(R.id.editTextFullNameSignIn).text.toString()
            val phoneNumber = view.findViewById<EditText>(R.id.editTextPhoneSignIn).text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && fullName.isNotEmpty() && phoneNumber.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            auth.currentUser?.let { user ->
                                saveUserDataToFirebase(user.uid, fullName, phoneNumber, email)
                            }
                        } else {
                            Toast.makeText(requireContext(), "Sign up failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        view.findViewById<Button>(R.id.buttonLogInFromSignIn)?.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_loginPhoneFragment)
        }
    }

    private fun saveUserDataToFirebase(userId: String, fullName: String, phoneNumber: String, email: String) {
        val userMap = hashMapOf(
            "fullName" to fullName,
            "phoneNumber" to phoneNumber,
            "email" to email
        )

        database.getReference("users") // Nama node utama di database
            .child(userId) // Gunakan UID pengguna sebagai key
            .setValue(userMap)
            .addOnSuccessListener {
                // Sign up berhasil, navigasi ke halaman berikutnya
                Toast.makeText(
                    requireContext(),
                    "Sign up successful",
                    Toast.LENGTH_SHORT
                ).show()
                findNavController().navigate(R.id.action_signInFragment_to_homeFragment)
            }
            .addOnFailureListener { e ->
                // Gagal menyimpan data ke Realtime Database
                Toast.makeText(
                    requireContext(),
                    "Failed to save user data: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}