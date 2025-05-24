package com.example.foodpalapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val startButton: Button = findViewById(R.id.buttonStart)
        startButton.setOnClickListener {
            // Aksi ketika tombol "Start using" diklik
            val intent = Intent(this, MainActivity::class.java) // Ganti MainActivity dengan activity tujuan Anda
            startActivity(intent)
            finish() // Tutup SplashActivity agar tidak kembali ke sini saat tombol "kembali" ditekan
        }

        // Anda bisa menambahkan delay di sini jika ini benar-benar splash screen
        // Contoh:
        /*
        android.os.Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000) // Delay selama 3 detik
        */
    }
}