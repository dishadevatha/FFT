package com.project.fft

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore

class vendor_mainscreen : AppCompatActivity() {

    private lateinit var vendorImageView: ImageView
    private lateinit var vendorNameTextView: TextView
    private lateinit var activeOrdersButton: Button
    private lateinit var completedOrdersButton: Button
    private lateinit var changeMenuButton: Button

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vendor_mainscreen)

        // Initialize UI elements
        vendorImageView = findViewById(R.id.imageView4)
        vendorNameTextView = findViewById(R.id.vendorName)
        activeOrdersButton = findViewById(R.id.activeOrdersButton)
        completedOrdersButton = findViewById(R.id.completedOrdersButton)
        changeMenuButton = findViewById(R.id.changeMenuButton)

        // Get the vendor's name and image URL from the intent
        val vendorName = intent.getStringExtra("vendorName")
        val vendorImage = intent.getStringExtra("vendorImage")

        // Set the vendor name in the TextView
        vendorNameTextView.text = vendorName

        // Load the vendor's image using Glide (ensure you have added the Glide dependency in your build.gradle file)
        if (!vendorImage.isNullOrEmpty()) {
            Glide.with(this)
                .load(vendorImage)
                .into(vendorImageView)
        }

        // Set onClickListeners for the buttons
        activeOrdersButton.setOnClickListener {
            val intent = Intent(this, activeOrders::class.java)
            intent.putExtra("vendorName", vendorName)
            startActivity(intent)
        }

        completedOrdersButton.setOnClickListener {
            val intent = Intent(this, completedOrders::class.java)
            startActivity(intent)
        }

        changeMenuButton.setOnClickListener {
            val intent = Intent(this, VendorMenuActivity::class.java)
            startActivity(intent)
        }
    }

}
