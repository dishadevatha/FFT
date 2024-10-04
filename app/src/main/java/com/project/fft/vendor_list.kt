package com.project.fft

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class vendor_list : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var vendorRecyclerView: RecyclerView
    private val vendors = mutableListOf<Vendor>()
    private lateinit var viewCartButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.vendor_list)

        // Initialize the RecyclerView
        vendorRecyclerView = findViewById(R.id.vendorRecyclerView)
        vendorRecyclerView.layoutManager = LinearLayoutManager(this)

        // Fetch and display vendor data
        readData()

        viewCartButton=findViewById(R.id.viewCartButtonVendor)
        viewCartButton.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }
    }

    private fun readData() {
        db.collection("Vendor")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val name = document.getString("name") ?: ""
                    val image = document.getString("image") ?: ""
                    if (name.isNotEmpty() && image.isNotEmpty()) {
                        vendors.add(Vendor(name, image))
                    }
                }

                // Set adapter for RecyclerView
                vendorRecyclerView.adapter = VendorAdapter(vendors) { vendor ->
                    // Handle item click
                    //Toast.makeText(this, "Clicked on ${vendor.name}", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MenuActivity::class.java)

                    intent.putExtra("menuCollection", "${vendor.name} Menu")
                    intent.putExtra("vendorName", vendor.name)
                    startActivity(intent)
                }
            }
            .addOnFailureListener { exception ->
                Log.w("Firestore", "Error getting documents.", exception)
            }
    }
}
