package com.project.fft

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class activeOrders : AppCompatActivity() {

    private lateinit var ordersRecyclerView: RecyclerView
    private lateinit var ordersAdapter: OrdersAdapter
    private val db = FirebaseFirestore.getInstance()
    private val activeOrders = mutableListOf<ActiveOrder>()
    private val handler = Handler(Looper.getMainLooper())
    private var vendorName: String = "Mingos"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_active_orders)

        // Get the vendor name from the intentvendorName = intent.getStringExtra("vendorName").toString()

        ordersRecyclerView = findViewById(R.id.ordersRecyclerView)
        ordersRecyclerView.layoutManager = LinearLayoutManager(this)
        ordersAdapter = OrdersAdapter(activeOrders) { orderId ->
            completeOrder(orderId)
        }
        ordersRecyclerView.adapter = ordersAdapter

        // Fetch orders initially
        fetchActiveOrders()

        // refresh orders every 10 seconds
        handler.postDelayed(object : Runnable {
            override fun run() {
                fetchActiveOrders()
                handler.postDelayed(this, 10000)
            }
        }, 10000)
    }

    // Fetch Active Orders from Firestore filtered by vendor name
    @SuppressLint("NotifyDataSetChanged")
    private fun fetchActiveOrders() {
        db.collection("Active Orders")
            .whereEqualTo("vendor", vendorName)
            .get()
            .addOnSuccessListener { documents: QuerySnapshot ->
                activeOrders.clear()
                for (document in documents) {
                    val order = document.toObject(ActiveOrder::class.java)
                    order.id = document.id  // Capture the document ID
                    activeOrders.add(order)
                }
                ordersAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e("activeOrders", "Error fetching active orders", exception)
                Toast.makeText(this, "Failed to load orders", Toast.LENGTH_SHORT).show()
            }
    }

    // Function to complete the order (delete it from Firestore)
    private fun completeOrder(orderId: String) {
        db.collection("Active Orders").document(orderId)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Order Completed", Toast.LENGTH_SHORT).show()
                fetchActiveOrders() // Refresh after deleting the order
            }
            .addOnFailureListener { exception ->
                Log.e("activeOrders", "Error deleting order", exception)
                Toast.makeText(this, "Failed to complete the order", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null) // Stop periodic task when activity is destroyed
    }
}
