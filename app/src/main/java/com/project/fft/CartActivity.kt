package com.project.fft

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class CartActivity : AppCompatActivity() {

    private lateinit var cartRecyclerView: RecyclerView
    private lateinit var totalPriceTextView: TextView
    private lateinit var checkoutButton: MaterialButton
    private lateinit var cartAdapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        cartRecyclerView = findViewById(R.id.cartRecyclerView)
        totalPriceTextView = findViewById(R.id.totalPrice)
        checkoutButton = findViewById(R.id.checkoutButton)

        cartRecyclerView.layoutManager = LinearLayoutManager(this)
        cartAdapter = CartAdapter(CartManager.getCartItems(), ::updateTotalPrice)
        cartRecyclerView.adapter = cartAdapter

        updateTotalPrice()

        checkoutButton.setOnClickListener {
            if (CartManager.getCartItems().isEmpty()) {
                Toast.makeText(this, "Your cart is empty!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Proceeding to Checkout", Toast.LENGTH_SHORT).show()
                CartManager.clearCart()
                cartAdapter.notifyDataSetChanged()
                updateTotalPrice()
            }
        }
    }

    private fun updateTotalPrice() {
        val totalPrice = CartManager.getTotalPrice()
        totalPriceTextView.text = "Total: ₹${String.format("%.2f", totalPrice)}"
    }
}
