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
    private var vendorName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_active_orders)

        vendorName = intent.getStringExtra("vendorName").toString()
        Log.e("VendorName", "Vendor Name: $vendorName")
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

    // Function to complete the order (move it from 'Active Orders' to '{vendorName} Completed Orders')
    private fun completeOrder(orderId: String) {
        val activeOrderRef = db.collection("Active Orders").document(orderId)

        // Retrieve the order from 'Active Orders' before deleting it
        activeOrderRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                val completedOrder = document.toObject(ActiveOrder::class.java)

                // Set the vendorName completed orders collection
                val completedOrdersRef = db.collection("${vendorName} Completed Orders")

                // Add the order to the vendor's completed orders collection
                if (completedOrder != null) {
                    completedOrdersRef.add(completedOrder)
                        .addOnSuccessListener {
                            // Once added to completed orders, delete from active orders
                            activeOrderRef.delete()
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Order Completed", Toast.LENGTH_SHORT).show()
                                    fetchActiveOrders() // Refresh after deleting the order
                                }
                                .addOnFailureListener { exception ->
                                    Log.e("activeOrders", "Error deleting order", exception)
                                    Toast.makeText(this, "Failed to complete the order", Toast.LENGTH_SHORT).show()
                                }
                        }
                        .addOnFailureListener { exception ->
                            Log.e("activeOrders", "Error adding order to completed orders", exception)
                            Toast.makeText(this, "Failed to move the order to completed", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }.addOnFailureListener { exception ->
            Log.e("activeOrders", "Error retrieving order", exception)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null) // Stop periodic task when activity is destroyed
    }
}
