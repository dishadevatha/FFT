package com.project.fft

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class completedOrders : AppCompatActivity() {

    private lateinit var ordersRecyclerView: RecyclerView
    private lateinit var ordersAdapter: CompletedOrdersAdapter
    private val db = FirebaseFirestore.getInstance()
    private val completedOrders = mutableListOf<CompletedOrder>()
    private var vendorName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_completed_orders)
        val sp = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        vendorName = sp.getString("vendorName",null).toString()
        Log.e("Completed_VendorName", "Vendor Name: $vendorName")

        ordersRecyclerView = findViewById(R.id.ordersRecyclerView)
        ordersRecyclerView.layoutManager = LinearLayoutManager(this)
        ordersAdapter = CompletedOrdersAdapter(completedOrders) { orderId ->
            deleteCompletedOrder(orderId)
        }
        ordersRecyclerView.adapter = ordersAdapter

        // Fetch completed orders
        fetchCompletedOrders()
    }

    // Fetch Completed Orders from Firestore filtered by vendor name
    private fun fetchCompletedOrders() {
        db.collection("$vendorName Completed Orders")
            .get()
            .addOnSuccessListener { documents: QuerySnapshot ->
                completedOrders.clear()
                for (document in documents) {
                    val order = document.toObject(CompletedOrder::class.java)
                    order.id = document.id  // Capture the document ID
                    completedOrders.add(order)
                }
                ordersAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e("Completed_", "Error fetching completed orders", exception)
                Toast.makeText(this, "Failed to load completed orders", Toast.LENGTH_SHORT).show()
            }
    }
    // Function to delete the completed order from Firestore
    private fun deleteCompletedOrder(orderId: String) {
        db.collection("${vendorName} Completed Orders").document(orderId)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Order Deleted", Toast.LENGTH_SHORT).show()
                fetchCompletedOrders() // Refresh after deleting the order
            }
            .addOnFailureListener { exception ->
                Log.e("completedOrders", "Error deleting completed order", exception)
                Toast.makeText(this, "Failed to delete the order", Toast.LENGTH_SHORT).show()
            }
    }
}
