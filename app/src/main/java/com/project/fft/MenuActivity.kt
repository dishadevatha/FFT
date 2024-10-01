package com.project.fft

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

data class MenuItem(
    val itemName: String = "",
    val image: String = "",
    val description: String = "",
    val price: Double = 0.0
)

class MenuActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var menuRecyclerView: RecyclerView
    private lateinit var menuAdapter: MenuAdapter
    private val menuItems = mutableListOf<MenuItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        // Get vendor's menu collection name from Intent
        val menuCollection = intent.getStringExtra("menuCollection")

        menuRecyclerView = findViewById(R.id.menuRecyclerView)
        menuRecyclerView.layoutManager = LinearLayoutManager(this)

        menuAdapter = MenuAdapter(menuItems) { menuItem ->
            // Handle the + button click to add to the cart
            Toast.makeText(this, "${menuItem.itemName} added to cart", Toast.LENGTH_SHORT).show()
            // You can add logic to update a cart data structure here
        }

        menuRecyclerView.adapter = menuAdapter

        // Fetch menu items from Firestore
        if (menuCollection != null) {
            fetchMenuItems(menuCollection)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchMenuItems(menuCollection: String) {
        db.collection(menuCollection)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val item = document.toObject(MenuItem::class.java)
                    menuItems.add(item)
                }
                // Notify adapter that data has changed
                menuAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e("MenuActivity", "Error fetching menu items", exception)
                Toast.makeText(this, "Error loading menu", Toast.LENGTH_SHORT).show()
            }
    }
}
// Test