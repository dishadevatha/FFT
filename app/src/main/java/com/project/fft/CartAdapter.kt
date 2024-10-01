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
    private val onQuantityChange: () -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemNameTextView: TextView = itemView.findViewById(R.id.itemName)
        val itemPriceTextView: TextView = itemView.findViewById(R.id.itemPrice)
        val itemQuantityTextView: TextView = itemView.findViewById(R.id.itemQuantity)
        val itemImageView: ImageView = itemView.findViewById(R.id.itemImage)
        val increaseButton: ImageButton = itemView.findViewById(R.id.increaseQuantity)
        val decreaseButton: ImageButton = itemView.findViewById(R.id.decreaseQuantity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false)
        return CartViewHolder(view)
    }

    @SuppressLint("SetTextI18n", "DefaultLocale")
    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = cartItems[position]
        val menuItem = cartItem.menuItem

        holder.itemNameTextView.text = menuItem.itemName
        holder.itemPriceTextView.text = "₹${String.format("%.2f", menuItem.price * cartItem.quantity)}"
        holder.itemQuantityTextView.text = cartItem.quantity.toString()

        Glide.with(holder.itemView.context)
            .load(menuItem.image)
            .into(holder.itemImageView)

        // Increase quantity
        holder.increaseButton.setOnClickListener {
            CartManager.updateItemQuantity(cartItem, cartItem.quantity + 1)
            notifyItemChanged(position)
            onQuantityChange()
        }

        // Decrease quantity
        holder.decreaseButton.setOnClickListener {
            if (cartItem.quantity > 1) {
                CartManager.updateItemQuantity(cartItem, cartItem.quantity - 1)
                notifyItemChanged(position)
                onQuantityChange()
            }
        }
    }

    override fun getItemCount(): Int = cartItems.size
}
