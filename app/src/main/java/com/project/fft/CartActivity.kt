package com.project.fft

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

@Suppress("DEPRECATION")
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
                // Proceed to Payment Page
                val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
                val editor = sharedPreferences.edit()


                val formattedItems = formatCartItems(CartManager.getCartItems())
                val vendor = cartItemsFromIntent?.get(0)?.vendor
                editor.putString("items", formattedItems)
                editor.putString("vendor", vendor.toString())
                editor.putString("amount", CartManager.getTotalPrice().toString())
                editor.apply()

                val intent = Intent(this, PaymentActivity::class.java)
                var totalPrice = CartManager.getTotalPrice().toInt()
                totalPrice *= 100
                val finalPrice = totalPrice.toString()
                intent.putExtra("totalPrice", finalPrice) // Pass total price
                CartManager.clearCart()
                cartAdapter.notifyDataSetChanged()
                updateTotalPrice()
                startActivity(intent)
                finish()
            }
        }
    }


    @SuppressLint("SetTextI18n", "DefaultLocale")
    private fun updateTotalPrice() {
        val totalPrice = CartManager.getTotalPrice()
        totalPriceTextView.text = "Total: ₹${String.format("%.2f", totalPrice)}"
    }


    private fun formatCartItems(cartItems: List<CartItem>): String {
        val formattedItems = StringBuilder()
        for (cartItem in cartItems) {
            formattedItems.append("${cartItem.menuItem.itemName} x${cartItem.quantity}\n")
        }
        return formattedItems.toString().trim() // Trim the trailing newline
    }
}
