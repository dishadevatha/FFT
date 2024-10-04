package com.project.fft

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
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

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        cartRecyclerView = findViewById(R.id.cartRecyclerView)
        totalPriceTextView = findViewById(R.id.totalPrice)
        checkoutButton = findViewById(R.id.checkoutButton)

        cartRecyclerView.layoutManager = LinearLayoutManager(this)

        // Retrieve the cart items from the intent
        val cartItemsFromIntent = intent.getParcelableArrayListExtra<MenuItem>("cartItems")
        Log.e("cartItems", "Received cart items: $cartItemsFromIntent")
        if (cartItemsFromIntent != null) {
            for (item in cartItemsFromIntent) {
                CartManager.addItemToCart(item)
            }
        }

        // Initialize the adapter with the updated cart items
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

    // Function to update the total price in the cart
    @SuppressLint("SetTextI18n", "DefaultLocale")
    private fun updateTotalPrice() {
        val totalPrice = CartManager.getTotalPrice()
        totalPriceTextView.text = "Total: ₹${String.format("%.2f", totalPrice)}"
    }
}
