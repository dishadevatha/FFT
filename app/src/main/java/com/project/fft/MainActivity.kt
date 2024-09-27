package com.project.fft

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    // variables-
    private lateinit var orderButton: Button
    private lateinit var vendorButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        orderButton = findViewById(R.id.order_button)
        vendorButton=findViewById(R.id.vendor_button)

        orderButton.setOnClickListener {
            val intent = Intent(this, vendor_list::class.java)
            startActivity(intent)
        }
        vendorButton.setOnClickListener {
            val intent = Intent(this, vendor_login::class.java)
            startActivity(intent)
        }
    }
}