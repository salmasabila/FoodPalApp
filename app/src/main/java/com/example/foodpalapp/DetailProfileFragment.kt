package com.example.foodpalapp

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class DetailProfileFragment : Fragment() {

    private lateinit var profilePictureUpload: ImageView
    private lateinit var etFirstName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etEmail: EditText

    private lateinit var imagePickerLauncher: ActivityResultLauncher<String>

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private var uploadedImageUrl: String? = null
    private var selectedImageUri: Uri? = null

    private var oldFirstName: String? = null
    private var oldLastName: String? = null
    private var oldEmail: String? = null
    private var oldImageUrl: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_detail_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Inisialisasi view
        etFirstName = view.findViewById(R.id.etFirstName)
        etLastName = view.findViewById(R.id.etLastName)
        etEmail = view.findViewById(R.id.etEmail)
        profilePictureUpload = view.findViewById(R.id.profilePictureUpload)

        loadUserProfile()

        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                selectedImageUri = it
                uploadImageToServer(it)
            }
        }

        profilePictureUpload.setOnClickListener {
            imagePickerLauncher.launch("image/*")
        }
    }

    private fun loadUserProfile() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid

            firestore.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        oldFirstName = document.getString("nama depan")
                        oldLastName = document.getString("nama belakang")
                        oldEmail = document.getString("email")
                        oldImageUrl = document.getString("imageUrl")

                        val imageUrl = document.getString("imageUrl")

                        if (!imageUrl.isNullOrEmpty()) {
                            Glide.with(requireContext())
                                .load(imageUrl)
                                .circleCrop()
                                .placeholder(R.drawable.ic_profile_placeholder)
                                .into(profilePictureUpload)
                        }

                        etFirstName.setText(oldFirstName)
                        etLastName.setText(oldLastName)
                        etEmail.setText(oldEmail)
                        etEmail.setText(currentUser.email)
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Gagal mengambil data profil", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun updateProfile(onComplete: () -> Unit) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            Toast.makeText(requireContext(), "User tidak ditemukan, silakan login kembali", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = currentUser.uid
        val firstName = etFirstName.text.toString().trim()
        val lastName = etLastName.text.toString().trim()
        val email = currentUser.email ?: ""
        val imageUrl = uploadedImageUrl ?: oldImageUrl.orEmpty()

        saveUserData(userId, firstName, lastName, email, imageUrl, onComplete)
    }

    private fun saveUserData(
        userId: String,
        newFirstName: String,
        newLastName: String,
        newEmail: String,
        newImageUrl: String,
        onComplete: () -> Unit
    ) {
        val updates = mutableMapOf<String, Any>()

        if (newFirstName != oldFirstName) updates["nama depan"] = newFirstName
        if (newLastName != oldLastName) updates["nama belakang"] = newLastName
        if (newEmail != oldEmail) updates["email"] = newEmail
        if (newImageUrl != oldImageUrl) updates["imageUrl"] = newImageUrl

        if (updates.isNotEmpty()) {
            firestore.collection("users").document(userId)
                .set(updates, SetOptions.merge())
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Profilmu berhasil diperbarui!", Toast.LENGTH_SHORT).show()
                    oldFirstName = newFirstName
                    oldLastName = newLastName
                    oldEmail = newEmail
                    oldImageUrl = newImageUrl
                    onComplete()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Gagal memperbarui profil", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(requireContext(), "Profilmu tetap sama seperti sebelumnya.", Toast.LENGTH_SHORT).show()
            onComplete()
        }
    }

    private fun uploadImageToServer(uri: Uri) {
        val file = uriToFile(uri, requireContext())
        if (file == null) {
            Toast.makeText(requireContext(), "Gagal mendapatkan file dari gambar", Toast.LENGTH_SHORT).show()
            return
        }

        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("image", file.name, requestFile)
        val description = "Foto Komplain".toRequestBody("text/plain".toMediaTypeOrNull())

        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        apiService.uploadImage(body, description).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val raw = response.body()?.string()
                    Toast.makeText(requireContext(), "Upload berhasil", Toast.LENGTH_SHORT).show()

                    val prefix = "File berhasil di-upload: "
                    if (raw != null && raw.startsWith(prefix)) {
                        val fileName = raw.substringAfter(prefix).trim()
                        val serverUrl = RetrofitClient.getUploadsUrl()
                        uploadedImageUrl = "$serverUrl$fileName"

                        if (!uploadedImageUrl.isNullOrEmpty()) {
                            Glide.with(requireContext())
                                .load(uploadedImageUrl)
                                .circleCrop()
                                .placeholder(R.drawable.ic_profile_placeholder)
                                .into(profilePictureUpload)
                        }
                    } else {
                        Toast.makeText(requireContext(), "Format respon tidak dikenali", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val error = response.errorBody()?.string()
                    Toast.makeText(requireContext(), "Upload gagal: $error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(requireContext(), "Gagal upload: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun uriToFile(uri: Uri, context: Context): File? {
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null
        val tempFile = File.createTempFile("temp_image", ".jpg", context.cacheDir)
        tempFile.outputStream().use { fileOut ->
            inputStream.copyTo(fileOut)
        }
        return tempFile
    }
}
