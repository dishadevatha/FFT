package com.project.fft

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CartAdapter(
    private val cartItems: List<CartItem>,
    private val updateTotalPriceCallback: () -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemNameTextView: TextView = view.findViewById(R.id.cartItemName)
        val itemPriceTextView: TextView = view.findViewById(R.id.cartItemPrice)
        val itemQuantityTextView: TextView = view.findViewById(R.id.cartItemQuantity)
        val itemImageView: ImageView = view.findViewById(R.id.cartItemImage)
        val increaseQuantityButton: ImageButton = view.findViewById(R.id.increaseQuantityButton)
        val decreaseQuantityButton: ImageButton = view.findViewById(R.id.decreaseQuantityButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false)
        return CartViewHolder(view)
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = cartItems[position]
        val menuItem = cartItem.menuItem

        holder.itemNameTextView.text = menuItem.itemName
        holder.itemPriceTextView.text = "₹${menuItem.price * cartItem.quantity}"
        holder.itemQuantityTextView.text = cartItem.quantity.toString()

        // Load the item image using Glide
        Glide.with(holder.itemImageView.context)
            .load(menuItem.image)
            .into(holder.itemImageView)

        // Handle increase quantity button
        holder.increaseQuantityButton.setOnClickListener {
            CartManager.updateItemQuantity(cartItem, cartItem.quantity + 1)
            notifyItemChanged(position)
            updateTotalPriceCallback()
        }

        // Handle decrease quantity button
        holder.decreaseQuantityButton.setOnClickListener {
            if (cartItem.quantity > 1) {
                CartManager.updateItemQuantity(cartItem, cartItem.quantity - 1)
            } else {
                CartManager.updateItemQuantity(cartItem, 0) // Remove if quantity is 0
                notifyItemRemoved(position)
            }
            notifyDataSetChanged() // Refresh the adapter
            updateTotalPriceCallback()
        }
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }
}
