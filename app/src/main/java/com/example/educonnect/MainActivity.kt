package com.example.educonnect

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Start LoginActivity immediately on app startup
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}